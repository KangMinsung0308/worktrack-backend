// =====================
// DOM elements
// =====================
const passwordInput = document.getElementById("passwordInput");
const submitButton = document.getElementById("submitButton");
const errorMessage = document.getElementById("errorMessage");
const modalOverlay = document.getElementById("modalOverlay");
const modalCloseButton = document.getElementById("modalCloseButton");


// =====================
// Event bindings
// =====================
submitButton.addEventListener("click", handleCreatePassword);

passwordInput.addEventListener("input", () => {
  hideError();
  updateButtonState();
});

passwordInput.addEventListener("keypress", (e) => {
  if (e.key === "Enter" && submitButton.classList.contains("active")) {
    submitButton.click();
  }
});

modalCloseButton.addEventListener("click", hideSuccessModal);

modalOverlay.addEventListener("click", (e) => {
  if (e.target === modalOverlay) {
    hideSuccessModal();
  }
});


// =====================
// 서버 통신 
// =====================
async function handleCreatePassword() {
  if (!submitButton.classList.contains("active")) return;

  const password = passwordInput.value;

  // 비밀번호 검증
  const validationError = validatePassword(password);
  if (validationError) {
    showError(validationError);
    return;
  }

  hideError();

  try {
    const res = await fetch(API_URL_SIGNUP_PASSWORD, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ password }) 
    });

    const result = await res.json();

    // 서버 결과 기준 처리
    if (result.success) {
      showSuccessModal();
      return;

    } else {
      showError(result.message);
    }

  } catch (err) {
    showError(MESSAGE_CLIENT_ERR);
    console.error(err);
  }
}


// =====================
// Validation
// =====================
function validatePassword(password) {
  if (password.length < 8) {
    return "비밀번호는 최소 8자 이상이어야 합니다.";
  }
  if (!/[A-Z]/.test(password)) {
    return "비밀번호에 대문자가 포함되어야 합니다.";
  }
  if (!/[a-z]/.test(password)) {
    return "비밀번호에 소문자가 포함되어야 합니다.";
  }
  if (!/[0-9]/.test(password)) {
    return "비밀번호에 숫자가 포함되어야 합니다.";
  }
  if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
    return "비밀번호에 특수문자가 포함되어야 합니다.";
  }
  return null;
}


// =====================
// UI helpers
// =====================
function showError(message) {
  errorMessage.textContent = message;
  errorMessage.classList.add("show");
  passwordInput.classList.add("error");
}

function hideError() {
  errorMessage.classList.remove("show");
  passwordInput.classList.remove("error");
}

function updateButtonState() {
  if (passwordInput.value.length > 0) {
    submitButton.classList.add("active");
  } else {
    submitButton.classList.remove("active");
  }
}

function showSuccessModal() {
  modalOverlay.classList.add("show");
}

function hideSuccessModal() {
  modalOverlay.classList.remove("show");
  passwordInput.value = "";
  updateButtonState();
      window.location.href = URL_LOGIN;
}


// =====================
// init
// =====================
updateButtonState();
