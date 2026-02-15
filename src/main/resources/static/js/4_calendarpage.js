// =========================
// State
// =========================
const now = new Date();

let currentYear = now.getFullYear();
let currentMonth = now.getMonth() + 1; // 0-based 주의
let selectedDate = null;

let workData = {};
let rawWorkData = {};

let isPanelVisible = true;
let isDropdownOpen = false;
let activeNav = "calendar";

let worktype ;

// =========================
// Utils
// =========================
function pad(n) {
  return String(n).padStart(2, "0");
}

function dateKey(y, m, d) {
  return `${y}-${pad(m)}-${pad(d)}`;
}

function formatHours(hours) {
  const h = Math.floor(hours);
  const m = Math.round((hours - h) * 60);
  return `${h}H${m ? " " + m + "M" : ""}`;
}

function updateDateTitles() {
  if (selectedDate === null) return;

  const dateObj = new Date(currentYear, currentMonth - 1, selectedDate);
  const dayNames = ["일", "월", "화", "수", "목", "금", "토"];
  const dayName = dayNames[dateObj.getDay()];

  const text = `${currentMonth}.${pad(selectedDate)} (${dayName})`;

  const panelTitle = document.getElementById("panelDateTitle");
  const editTitle = document.getElementById("editDateTitle");

  if (panelTitle) panelTitle.textContent = text;
  if (editTitle) editTitle.textContent = text;
}

// =========================
// Initialize
// =========================
function init() {
  funcWorkDate(currentYear, currentMonth);
  populateMonthDropdown();
  renderCalendar();
}

// =========================
// API
// =========================
function funcWorkDate(year, month) {
  const date = `${year}-${pad(month)}-01`;

  fetch(`${API_URL}/${date}`)
    .then((res) => {
      if (!res.ok) throw new Error("서버 오류");
      return res.json();
    })
    .then((data) => {
      workData = {};
      rawWorkData = {};

      data.forEach((item) => {
        const key = item.workDate;

        // 기존 rawWorkData 유지
        rawWorkData[key] = item;

        // workData를 객체 구조로 변경
        workData[key] = {
          workType: item.workType ? parseInt(item.workType, 10) : 0,
          totalHours: item.totalHours ?? 0,
          startTime: item.startTime ?? null,
          endTime: item.endTime ?? null,
          totalHours: item.totalHours ?? null,
          yasumiTime: item.yasumiTime ?? null,
          memo: item.bikou ?? "",
        };
      });

      renderCalendar();
      updatePanel(); // ✅ 클릭 이후에만 의미 있음
    })
    .catch(console.error);
}


// =========================
// Month Dropdown
// =========================
function populateMonthDropdown() {
  const dropdown = document.getElementById("monthDropdown");
  dropdown.innerHTML = "";

  const baseDate = new Date(currentYear, currentMonth - 1, 1);

  const RANGE = 4; // 앞뒤 2년씩
  const start = new Date(baseDate);
  start.setFullYear(start.getFullYear() - RANGE);

  const end = new Date(baseDate);
  end.setFullYear(end.getFullYear() + RANGE);

  const iter = new Date(start);

  while (iter <= end) {
    const y = iter.getFullYear();
    const m = iter.getMonth() + 1;

    const btn = document.createElement("button");
    btn.className = "month-dropdown-item";
    btn.textContent = `${y}년 ${m}월`;

    if (y === currentYear && m === currentMonth) {
      btn.classList.add("selected");
    }

    btn.onclick = () => handleMonthSelect(y, m);
    dropdown.appendChild(btn);

    iter.setMonth(iter.getMonth() + 1);
  }
}

function toggleDropdown() {
  isDropdownOpen = !isDropdownOpen;

  const dropdown = document.getElementById("monthDropdown");
  const backdrop = document.getElementById("dropdownBackdrop");

  dropdown.classList.toggle("visible", isDropdownOpen);
  backdrop.classList.toggle("visible", isDropdownOpen);

  // ✅ 열릴 때 현재 월로 스크롤 이동
  if (isDropdownOpen) {
    requestAnimationFrame(() => {
      const selected = dropdown.querySelector(".month-dropdown-item.selected");
      if (selected) {
        selected.scrollIntoView({
          block: "center",
        });
      }
    });
  }
}

function closeDropdown() {
  isDropdownOpen = false;
  document.getElementById("monthDropdown").classList.remove("visible");
  document.getElementById("dropdownBackdrop").classList.remove("visible");
}

function handleMonthSelect(year, month) {
  currentYear = year;
  currentMonth = month;
  selectedDate = null; 
  isPanelVisible = false;

  closeDropdown();
  populateMonthDropdown();
  funcWorkDate(year, month);
  hidePanel();
}

// =========================
// Calendar Render
// =========================
function renderCalendar() {
  const grid = document.getElementById("calendarGrid");
  grid.innerHTML = "";

  const firstDay = new Date(currentYear, currentMonth - 1, 1).getDay();
  const daysInMonth = new Date(currentYear, currentMonth, 0).getDate();
  const prevMonthDays = new Date(currentYear, currentMonth - 1, 0).getDate();

  // 이전 달
  for (let i = firstDay - 1; i >= 0; i--) {
    const cell = document.createElement("div");
    cell.className = "calendar-day disabled";
    cell.innerHTML = `<div class="day-number" style="color:#bdbdbd">${prevMonthDays - i}</div>`;
    grid.appendChild(cell);
  }

  // 현재 달
  for (let day = 1; day <= daysInMonth; day++) {
    const cell = document.createElement("div");
    const dow = new Date(currentYear, currentMonth - 1, day).getDay();
    const key = dateKey(currentYear, currentMonth, day);

    cell.className = "calendar-day";
    if (dow === 0) cell.classList.add("sunday");
    if (dow === 6) cell.classList.add("saturday");
    if (day === selectedDate) cell.classList.add("selected"); 

    cell.innerHTML = `<div class="day-number">${day}</div>`;


      const dayData = workData[key]; // 객체 가져오기

    if (dayData) {
      const badge = document.createElement("div");
      const isVacation = dayData.workType === 1 || dayData.workType === 3;
      badge.className = isVacation ? "vacation-badge" : "work-badge";
      badge.textContent = isVacation ? "휴가" : formatHours(dayData.totalHours);
      cell.appendChild(badge);
    }
    cell.onclick = () => handleDateClick(day);
    grid.appendChild(cell);
  

  document.getElementById("monthText").textContent =
    `${currentYear}년 ${currentMonth}월`;
}
}

// =========================
// Panel
// =========================
function handleDateClick(day) {
  selectedDate = day; // 
  isPanelVisible = true;
  renderCalendar();
  updatePanel();
  updateDateTitles(); // 
  showPanel();
}

function updatePanel() {
  if (selectedDate === null) return;

  const key = dateKey(currentYear, currentMonth, selectedDate);
  const dayData = workData[key];

  const panelWorkHoursElem = document.getElementById("panelWorkHours");
  const infoGrid = document.querySelector(".info-grid");

  if (!dayData) {
    panelWorkHoursElem.style.display = "none";
    infoGrid.innerHTML = `<div class="empty-state">입력하신 출퇴근 기록이 없습니다.</div>`;
    infoGrid.style.display = "block";
    infoGrid.style.gridTemplateColumns = "1fr";
    return;
  }

  // grid 컬럼 설정 초기화
  infoGrid.style.display = "grid";
  infoGrid.style.gridTemplateColumns = "1fr 1fr";

  const isVacation = dayData.workType === 1 || dayData.workType === 3;

  // 패널 근무시간 / 휴가 표시
  panelWorkHoursElem.style.display = "inline-block";
  panelWorkHoursElem.textContent = isVacation ? (dayData.workType === 1 ? "휴가" : "대휴") : formatHours(dayData.totalHours);
  panelWorkHoursElem.className = isVacation
    ? "panel-work-hours vacation"
    : "panel-work-hours work";

  // info-grid 내용
  if (isVacation) {
    // 휴가/대휴일 경우: 메모만 표시
    infoGrid.innerHTML = `
      <div class="info-item full-width">
        <div class="info-label">메모</div>
        <div class="info-value" id="memoValue">${dayData.memo || (dayData.workType === 1 ? "휴가 사용" : "대휴 사용")}</div>
      </div>
    `;
    infoGrid.style.gridTemplateColumns = "1fr";
  } else {
    // 일반 근무: 모든 정보 표시
    infoGrid.innerHTML = `
      <div class="info-item">
        <div class="info-label">근무 시간</div>
        <div class="info-value">${dayData.startTime.slice(11, 16)+" ~ "+dayData.endTime.slice(11,16)}</div>
      </div>
      <div class="info-item">
        <div class="info-label">휴게 시간</div>
        <div class="info-value">${dayData.yasumiTime?? "0"}분</div>
      </div>
      <div class="info-item full-width">
        <div class="info-label">메모</div>
        <div class="info-value">${dayData.memo}</div>
      </div>
    `;
    infoGrid.style.gridTemplateColumns = "1fr 1fr";
  }
}


function showPanel() {
  document.getElementById("detailsPanel").classList.add("visible");
}

function hidePanel() {
  document.getElementById("detailsPanel").classList.remove("visible");
}

function closePanel() {
  isPanelVisible = false;
  selectedDate = null;
  hidePanel();
  renderCalendar();
}

// =========================
// 몰라이거 머임
// =========================
function handlePrevDate() {
  if (selectedDate === null) return;
  selectedDate--;
  if (selectedDate < 1) return;
  renderCalendar();
  updatePanel();
  updateDateTitles();
}

function handleNextDate() {
  const lastDay = new Date(currentYear, currentMonth, 0).getDate();
  if (selectedDate === null) return;
  selectedDate++;
  if (selectedDate > lastDay) return;
  renderCalendar();
  updatePanel();
  updateDateTitles();
}

// =========================
// Vacation Type Handler (추가)
// =========================
function handleVacationTypeChange() {
  const vacationType = document.getElementById("vacationType")?.value;

  const startTimeGroup = document.getElementById("startTimeGroup");
  const endTimeGroup = document.getElementById("endTimeGroup");
  const breakTimeGroup = document.getElementById("breakTimeGroup");

  if (!startTimeGroup || !endTimeGroup || !breakTimeGroup) return;

  const isVacation = vacationType === "1" || vacationType === "3"; // 휴가 또는 대휴

  startTimeGroup.style.display = isVacation ? "none" : "block";
  endTimeGroup.style.display = isVacation ? "none" : "block";
  breakTimeGroup.style.display = isVacation ? "none" : "block";
}

// =========================
// Edit / Save (그대로)
// =========================
function handleEdit() {
  const key = dateKey(currentYear, currentMonth, selectedDate);
  const data = rawWorkData[key];

  updateDateTitles(); // ✅ 추가
  if (!data) {
    // 초기값 세팅
    document.getElementById("startTime").value = "";
    document.getElementById("endTime").value = "";
    document.getElementById("breakTime").value = "60";
    document.getElementById("memo").value = "";
    document.getElementById("vacationType").value = "0";
    handleVacationTypeChange();
    document.getElementById("mainView").classList.add("hidden");
    document.getElementById("editView").classList.add("active");
    hidePanel();
    return;
  }

  // 기존 값 세팅
  document.getElementById("breakTime").value = data.yasumiTime ?? "60";
  document.getElementById("startTime").value = data.startTime?.substring(11, 16) ?? "";
  document.getElementById("endTime").value =data.endTime?.substring(11, 16) ?? "";
  document.getElementById("memo").value = data.memo ?? "";

  // ✅ 휴가 타입 반영
  document.getElementById("vacationType").value = data.vacationType ?? "";
  handleVacationTypeChange();

  document.getElementById("mainView").classList.add("hidden");
  document.getElementById("editView").classList.add("active");
  hidePanel();
}

function handleSave() {
  const key = dateKey(currentYear, currentMonth, selectedDate);

  const startTimeValue = document.getElementById("startTime").value; // "09:00"
  const endTimeValue = document.getElementById("endTime").value;     // "18:00"

  const payload = {
    workDate: key,
    startTime: startTimeValue ? `${key}T${startTimeValue}:00` : null,
    endTime: endTimeValue ? `${key}T${endTimeValue}:00` : null,
    yasumiTime: document.getElementById("breakTime").value,
    bikou: document.getElementById("memo").value,
    workType: document.getElementById("vacationType").value || 0,
  };

  console.log("FINAL PAYLOAD", payload); // ⭐ 꼭 확인

fetch(API_URL_PUT_WORKTIME, {
  method: "PUT",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify(payload),
})
.then(async (response) => {
  const data = await response.json(); // ⭐ JSON 파싱

  if (!response.ok) {
    // 서버에서 내려준 실패 메시지
    throw new Error(data.message);
  }

  return data;
})
.then((data) => {
  // 성공
  if (data.success) {
    showToast("근무 정보가 성공적으로 저장되었습니다.");
  }
  funcWorkDate(currentYear, currentMonth);
  handleBackFromEdit();
})
.catch((error) => {
  // 실패
  showToast(error.message);
});
}


function handleBackFromEdit() {
  document.getElementById("mainView").classList.remove("hidden");
  document.getElementById("editView").classList.remove("active");
  showPanel();
}

// =========================
// Init
// =========================
window.addEventListener("DOMContentLoaded", init);

window.toggleDropdown = toggleDropdown;
window.closePanel = closePanel;
window.handleEdit = handleEdit;
window.handleSave = handleSave;

window.handlePrevDate = handlePrevDate;
window.handleNextDate = handleNextDate;
window.handleVacationTypeChange = handleVacationTypeChange;
