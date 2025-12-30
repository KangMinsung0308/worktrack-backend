  



// Toast notification function
const toast = document.getElementById("toast"); // toast 요소를 가져오기

function showToast(message) {
  if (!message) return;

  const toast = document.getElementById("toast");
  if (!toast) return;

  if (toast.timeoutId) clearTimeout(toast.timeoutId);

  toast.innerText = message;
  toast.classList.add("show");
  toast.classList.remove("hide");

  toast.timeoutId = setTimeout(() => {
    toast.classList.remove("show");
    toast.classList.add("hide");
  }, 3000);
}

// DOMContentLoaded에서 toast 요소 체크
document.addEventListener("DOMContentLoaded", () => {
  const toast = document.getElementById("toast");
  if (!toast) {
    const newToast = document.createElement("div");
    newToast.id = "toast";
    newToast.className = "toast";
    document.body.appendChild(newToast);
  }
});
//  화면 이동 함수
document.addEventListener("DOMContentLoaded", () => {
  const navButtons = document.querySelectorAll("[data-nav]");
  // --------------------
  // 네비게이션 버튼 함수
  navButtons.forEach((button) => {
    button.addEventListener("click", function () {
      const navType = this.getAttribute("data-nav");

      // 모든 버튼의 active 클래스 제거
      navButtons.forEach((btn) => btn.classList.remove("active"));

      // 클릭한 버튼에 active 클래스 추가
      this.classList.add("active");

      // 아이콘 및 텍스트 색상 업데이트
      updateButtonStyles(navType);

      activeTab = navType;

      switch (navType) {
        case "calendar":
          window.location.href = URL_CALENDAR;
          break;

        case "profile":
          window.location.href = URL_ICHIRAN;
          break;

        case "main":
          window.location.href = MAIN_URL;
          break;
      }
    });
  });
});

// 네비게이션 버튼 스타일 업데이트 함수
function updateButtonStyles(activeNav) {
  // Calendar 버튼 스타일
  const calendarBtn = document.querySelector(".calendar-icon-button");
  const calendarIcon = calendarBtn.querySelector("svg path");
  const calendarLabel = document.getElementById("calendarLabel");

  if (activeNav === "calendar") {
    calendarIcon.style.fill = "#010101";
    calendarLabel.style.color = "#010101";
  } else {
    calendarIcon.style.fill = "#c7c7cc";
    calendarLabel.style.color = "#c7c7cc";
  }

  // Profile 버튼 스타일
  const profileBtn = document.querySelector(".profile-icon-button");
  const profileIcon = profileBtn.querySelector("svg path");
  const profileLabel = document.getElementById("profileLabel");

  if (activeNav === "profile") {
    profileIcon.style.fill = "#010101";
    profileLabel.style.color = "#010101";
  } else {
    profileIcon.style.fill = "#c7c7cc";
    profileLabel.style.color = "#c7c7cc";
  }

  // Main 버튼 스타일
  const mainCircle = document.getElementById("mainCircle");
  const mainLabel = document.getElementById("mainLabel");

  if (activeNav === "main") {
    mainCircle.style.fill = "#010101";
    mainLabel.style.color = "white";
  } else {
    mainCircle.style.fill = "#5a5a5a";
    mainLabel.style.color = "#c7c7cc";
  }
}
document.addEventListener("DOMContentLoaded", () => {
  const path = window.location.pathname;

  let currentNav = "main"; // 기본값

  if (path.includes("calendar")) {
    currentNav = "calendar";
  } else if (path.includes("profile") || path.includes("ichiran")) {
    currentNav = "profile";
  }

  // 버튼 active 복구
  const navButtons = document.querySelectorAll("[data-nav]");
  navButtons.forEach((btn) => {
    if (btn.getAttribute("data-nav") === currentNav) {
      btn.classList.add("active");
    } else {
      btn.classList.remove("active");
    }
  });

  // 아이콘 / 색상 복구
  updateButtonStyles(currentNav);
});
