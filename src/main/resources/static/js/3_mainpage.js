document.addEventListener("DOMContentLoaded", () => {
  // DOM 요소
  const dateButton = document.getElementById("dateButton");
  const dateText = document.getElementById("dateText");
  const datePicker = document.getElementById("datePicker");
  const dateInput = document.getElementById("dateInput");
  const startTimeInput = document.getElementById("startTime");
  const endTimeInput = document.getElementById("endTime");
  const toggleButton = document.getElementById("toggleButton");
  const toggleBg = document.getElementById("toggleBg");
  const toggleCircle = document.getElementById("toggleCircle");
  const submitButton = document.getElementById("submitButton");
  const calendarIcon = document.getElementById("calendarIcon");
  const peopleIcon = document.getElementById("peopleIcon");
  const calendarPath = document.getElementById("calendarPath");
  const peoplePath = document.getElementById("peoplePath");

  const apiUrl = "http://localhost:8080/worktrack/record";

  // 상태 변수
  let isHalfDay = false;
  let isDatePickerOpen = false;
  let activeTab = "calendar";

  // --------------------
  // 초기 날짜 세팅 (오늘)
  const now = new Date();
  const yyyy = now.getFullYear();
  const mm = String(now.getMonth() + 1).padStart(2, "0");
  const dd = String(now.getDate()).padStart(2, "0");
  const days = ["일", "월", "화", "수", "목", "금", "토"];
  const dayOfWeek = days[now.getDay()];

  const formattedDisplayDate = `${mm}.${dd}(${dayOfWeek})`; // 화면용
  const formattedInputDate = `${yyyy}-${mm}-${dd}`; // input 값용

  dateText.textContent = formattedDisplayDate;
  dateInput.value = formattedInputDate;

  // --------------------
  // Date picker toggle
  dateButton.addEventListener("click", () => {
    isDatePickerOpen = !isDatePickerOpen;
    datePicker.classList.toggle("active", isDatePickerOpen);
  });

  // Date input change - 화면 표시용 & fetch용
  dateInput.addEventListener("change", (e) => {
    const date = new Date(e.target.value);
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    const dayOfWeek = days[date.getDay()];
    dateText.textContent = `${month}.${day}(${dayOfWeek})`;
    isDatePickerOpen = false;
    datePicker.classList.remove("active");
  });

  // Half day toggle
  toggleButton.addEventListener("click", () => {
    isHalfDay = !isHalfDay;
    toggleBg.classList.toggle("active", isHalfDay);
    toggleCircle.classList.toggle("active", isHalfDay);
  });

  // --------------------
  // Submit button - 서버 전송
  submitButton.addEventListener("click", async () => {
    const date = dateInput.value; // yyyy-MM-dd
    const startTimeStr = startTimeInput.value; // HH:mm
    const endTimeStr = endTimeInput.value; // HH:mm

    if (!date || !startTimeStr || !endTimeStr) {
      alert("날짜와 출근/퇴근 시간을 모두 입력하세요.");
      return;
    }

    // LocalDateTime ISO 형식
    const startDateTime = `${date}T${startTimeStr}:00`;
    const endDateTime = `${date}T${endTimeStr}:00`;

    const payload = {
      workDate: date,
      startTime: startDateTime,
      endTime: endDateTime,
      isHalfDay,
    };

    try {
      const res = await fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      const result = await res.json();

      if (result.success) {
        showToast(
          `근무 정보가 추가되었습니다!<br>출근: ${startDateTime.split("T")[1].substring(0, 5)}<br>퇴근: ${endDateTime.split("T")[1].substring(0, 5)}<br>반차: ${isHalfDay ? "사용" : "미사용"}`
        );
      } else {
        alert(`등록 실패: ${result.message}`);
      }
    } catch (err) {
      alert("등록 오류: " + err);
    }
  });

  // --------------------
  // Navigation tabs
  calendarIcon.addEventListener("click", () => {
    activeTab = "calendar";
    calendarPath.setAttribute("fill", "#000");
    peoplePath.setAttribute("fill", "#2A343D");
  });
  peopleIcon.addEventListener("click", () => {
    activeTab = "people";
    calendarPath.setAttribute("fill", "#2A343D");
    peoplePath.setAttribute("fill", "#000");

    window.location.href = "/worktrack/ichiran";
  });

  // Close date picker when clicking outside
  document.addEventListener("click", (e) => {
    if (!dateButton.contains(e.target) && !datePicker.contains(e.target)) {
      isDatePickerOpen = false;
      datePicker.classList.remove("active");
    }
  });

  // Toast 메시지 함수
  function showToast(message) {
    const toast = document.createElement("div");
    toast.className = "toast";
    toast.innerHTML = message
    document.body.appendChild(toast);

    setTimeout(() => {
      toast.classList.add("hide");
      setTimeout(() => {
        document.body.removeChild(toast);
      }, 300);
    }, 2000);
  }
});
