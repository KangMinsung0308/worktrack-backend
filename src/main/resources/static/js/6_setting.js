// 화면 전환 함수
function showScreen(screenId) {
  document.querySelectorAll(".screen").forEach((screen) => {
    screen.classList.remove("active");
  });
  document.getElementById(screenId).classList.add("active");
}

function showSettings() {
  showScreen("settings-screen");
}

function showProfile() {
  showScreen("profile-screen");
}

function showLanguage() {
  showScreen("language-screen");
}

function showConnectedAccounts() {
  showScreen("accounts-screen");
}

// 알림 토글
function toggleNotification() {
  const toggle = document.getElementById("notification-toggle");
  if (toggle.classList.contains("on")) {
    toggle.classList.remove("on");
    toggle.classList.add("off");
  } else {
    toggle.classList.remove("off");
    toggle.classList.add("on");
  }
}

// 근무시간 설정
function showWorkHours() {
  window.location.href = URL_STANDARD_TIME;
}

// 언어 선택
let selectedLanguage = "ko";
function selectLanguage(lang) {
  selectedLanguage = lang;
  document.querySelectorAll(".language-check").forEach((check) => {
    check.style.display = "none";
  });
  document.getElementById("check-" + lang).style.display = "block";
}

// 프로필 저장
function saveProfile() {
  const userName = document.getElementById("user-name").value;
  alert("프로필이 저장되었습니다.");
}

// 로그아웃
function logout() {
  if (confirm("로그아웃 하시겠습니까?")) {
    fetch(`${API_URL_LOGOUT}`, { method: "POST" })
      .then((res) => res.json())
      .then((data) => {
        if (data.success) {
          window.location.href = URL_LOGIN;
        } else {
          console.log("로그아웃 실패");
        }
      })
      .catch((err) => console.error("요청 오류:", err));
    alert("로그아웃 되었습니다.");
  }
}

// 프로필 업데이트
function updateProfile() {
  const userName = document.getElementById("user-name").value;
  const dept = document.getElementById("user-dept").value;

  const payload = {
    name: userName,
    dept: dept,
  };

  fetch(API_URL_UPDATE_PROFILE, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  }).then(async (response) => {
    const data = await response.json(); // ⭐ JSON 파싱

    if (!response.ok) {
      // 서버에서 내려준 실패 메시지
      throw new Error(data.message);
    }
    return saveProfile();
  });
}

// Google 계정 토글 (미구현)
let googleConnected = true;
function toggleGoogle() {
  const button = document.getElementById("google-button");
  const email = document.getElementById("google-email");

  if (googleConnected) {
    button.classList.remove("disconnect");
    button.classList.add("connect");
    button.textContent = "연결하기";
    email.style.display = "none";
  } else {
    button.classList.remove("connect");
    button.classList.add("disconnect");
    button.textContent = "연결 해제";
    email.style.display = "block";
    email.textContent = "user@gmail.com";
  }
  googleConnected = !googleConnected;
}

// Apple 계정 토글 (미구현)
let appleConnected = false;
function toggleApple() {
  const button = document.getElementById("apple-button");
  const email = document.getElementById("apple-email");

  if (appleConnected) {
    button.classList.remove("disconnect");
    button.classList.add("connect");
    button.textContent = "연결하기";
    email.style.display = "none";
  } else {
    button.classList.remove("connect");
    button.classList.add("disconnect");
    button.textContent = "연결 해제";
    email.style.display = "block";
    email.textContent = "user@icloud.com";
  }
  appleConnected = !appleConnected;
}

// 뒤로 가기 버튼
function goBack() {
  const currentScreen = document.querySelector(".screen.active");
  if (currentScreen.id === "profile-screen") {
    showSettings();
  } else if (currentScreen.id === "language-screen") {
    showSettings();
  } else if (currentScreen.id === "accounts-screen") {
    showProfile();
  } else {
    // 메인 화면으로 이동
    window.location.href = URL_DASH_BOARD;
  }
}
