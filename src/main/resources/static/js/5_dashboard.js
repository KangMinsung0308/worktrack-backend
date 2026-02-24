let allWorkData = [];
let currentMonthData = [];

// 서버에서 근무 데이터 가져오기
function funcWorkDate() {
  const now = new Date();
  const workDate = now.toISOString().substring(0, 10);
  const settingsIcon = document.getElementById("settingsIcon");

  fetch(`${API_URL_GET_WORKTIME}/${workDate}`)
    .then((res) => {
      if (!res.ok) {
        throw new Error("서버 응답 오류: " + res.status);
      }
      return res.json();
    })
    .then((data) => {
      console.log("응답 데이터:", data);

      // 🔥 배열 안전 체크 추가 (기존 기능 유지)
      if (!Array.isArray(data)) {
        console.error("배열이 아님:", data);
        return;
      }

      allWorkData = data;
      currentMonthData = filterCurrentMonthData(allWorkData);

      renderGraph();
      renderTable();
    })
    .catch((err) => alert("조회 오류: " + err));
}

// 현재 월 데이터 필터
function filterCurrentMonthData(data) {
  const now = new Date();
  const year = now.getFullYear();
  const month = now.getMonth() + 1;

  // 🔥 날짜 포맷 수정 (YYYY-MM)
  const currentMonthKey = `${year}-${String(month).padStart(2, "0")}`;

  return data.filter(
    (work) => work.workDate && work.workDate.startsWith(currentMonthKey),
  );
}

// 시간 파싱
function parseHours(hoursStr) {
  if (hoursStr === null || hoursStr === undefined) return 0;

  // 🔥 숫자면 그대로 반환
  if (typeof hoursStr === "number") {
    return hoursStr;
  }

  // 🔥 문자열일 경우 기존 로직 유지
  if (typeof hoursStr === "string") {
    if (hoursStr.includes("H")) {
      const parts = hoursStr.split("H");
      const hours = parseFloat(parts[0]);
      let minutes = 0;

      if (parts[1] && parts[1].includes("M")) {
        minutes = parseFloat(parts[1].replace("M", ""));
      }

      return hours + minutes / 60;
    }

    return parseFloat(hoursStr) || 0;
  }

  return 0;
}

// 월별 통계 계산
function calculateMonthlyStats(data) {
  const stats = {};

  data.forEach((work) => {
    if (!work.workDate) return;

    const monthKey = work.workDate.substring(0, 7);
    const hours = parseHours(work.totalHours);

    stats[monthKey] = (stats[monthKey] || 0) + hours;
  });

  return stats;
}

// 월 데이터 생성
function generateMonthlyData(stats) {
  const months = Object.keys(stats).sort();

  return months.map((month) => {
    const totalHours = stats[month] || 0;
    const hours = Math.floor(totalHours);
    const minutes = Math.round((totalHours - hours) * 60);

    const monthNumber = month.split("-")[1];
    const monthLabel = `${monthNumber}월`;

    return {
      month: monthLabel,
      totalMinutes: totalHours * 60,
      displayHours: minutes > 0 ? `${hours}H${minutes}M` : `${hours}H`,
    };
  });
}

// 그래프 렌더링
function renderGraph() {
  const graphContainer = document.querySelector(".graph-container");
  if (!graphContainer) return;

  graphContainer.innerHTML = "";

  const monthlyStats = calculateMonthlyStats(allWorkData);
  const monthlyData = generateMonthlyData(monthlyStats);

  if (monthlyData.length === 0) return;

  const maxHeight = Math.max(...monthlyData.map((d) => d.totalMinutes));

  const totalBars = monthlyData.length;
  const barWidth = 45;
  const barGap = 16;
  const totalWidth = totalBars * barWidth + (totalBars - 1) * barGap;
  const startLeft = (327 - totalWidth) / 2;

  monthlyData.forEach((data, index) => {
    const barHeight =
      maxHeight === 0 ? 0 : Math.round((data.totalMinutes / maxHeight) * 115);

    const leftPosition = startLeft + index * (barWidth + barGap);

    const centerIndex = Math.floor(totalBars / 2);
    const distanceFromCenter = Math.abs(index - centerIndex);
    const animationDelay = 100 + distanceFromCenter * 100;

    const wrapper = document.createElement("div");
    wrapper.className = "bar-wrapper";
    wrapper.style.left = leftPosition + "px";

    const bar = document.createElement("div");
    bar.className = "bar";
    bar.style.setProperty("--bar-height", barHeight + "px");

    const monthLabel = document.createElement("div");
    monthLabel.className = "month-label";
    monthLabel.textContent = data.month;

    const hoursLabel = document.createElement("div");
    hoursLabel.className = "hours-label";
    hoursLabel.style.bottom = 30 + barHeight + 5 + "px";
    hoursLabel.style.left = "23px";
    hoursLabel.style.transform = "translateX(-50%)";
    hoursLabel.textContent = data.displayHours;

    wrapper.appendChild(bar);
    wrapper.appendChild(monthLabel);

    if (data.totalMinutes > 0) {
      wrapper.appendChild(hoursLabel);
    }

    graphContainer.appendChild(wrapper);

    setTimeout(() => {
      bar.classList.add("animate");
    }, animationDelay);

    if (data.totalMinutes > 0) {
      setTimeout(() => {
        hoursLabel.classList.add("show");
      }, animationDelay + 800);
    }
  });
}

// 테이블 렌더링
function renderTable() {
  const tableRows = document.getElementById("tableRows");
  const tableMainBg = document.querySelector(".table-main-bg");

  if (!tableRows || !tableMainBg) return;

  tableRows.innerHTML = "";

  currentMonthData.forEach((work) => {
    const row = document.createElement("div");
    row.className = "table-row";
    row.innerHTML = `
      <div class="table-cell cell-date">${work.workDate || ""}</div>
      <div class="table-cell cell-start">${work.startTime.substring(11, 16) || ""}</div>
      <div class="table-cell cell-end">${work.endTime.substring(11, 16) || ""}</div>
      <div class="table-cell cell-total">${work.totalHours || ""}</div>
      <div class="table-cell cell-overtime">${work.overtime || "0"}</div>
    `;
    tableRows.appendChild(row);
  });

  const headerHeight = 50.317;
  const rowHeight = 45;
  const padding = 10;
  const totalHeight =
    headerHeight + currentMonthData.length * rowHeight + padding;

  tableMainBg.style.height = totalHeight + "px";
}

// 현재 날짜 표시
function updateCurrentDate() {
  const now = new Date();
  const month = String(now.getMonth() + 1).padStart(2, "0");
  const day = String(now.getDate()).padStart(2, "0");
  const weekdays = ["일", "월", "화", "수", "목", "금", "토"];
  const weekday = weekdays[now.getDay()];

  const dateElement = document.getElementById("currentDate");
  if (!dateElement) return;

  dateElement.textContent = `${month}.${day} (${weekday})`;
}

    //  
    settingsIcon.addEventListener("click", () => {
        window.location.href = URL_SETTINGS;
    });




// 초기 실행
updateCurrentDate();
funcWorkDate();
