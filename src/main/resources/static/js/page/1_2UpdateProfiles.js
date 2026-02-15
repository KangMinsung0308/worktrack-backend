// =====================
// DOM elements
// =====================
const nameInput = document.getElementById("nameInput");
const deptInput = document.getElementById("deptInput");
const submitButton = document.getElementById("submitButton");

const nameError = document.getElementById("nameError");
const deptError = document.getElementById("deptError");

// =====================
// Event bindings
// =====================
submitButton.addEventListener("click", handleAddWorkPlace);

nameInput.addEventListener("input", () => {
  hideNameError();
  updateButtonState();
});

deptInput.addEventListener("input", () => {
  hideDeptError();
  updateButtonState();
});

// =====================
// 서버 통신
// =====================
async function handleAddWorkPlace() {
  if (!submitButton.classList.contains("active")) return;

  const name = nameInput.value.trim();
  const dept = deptInput.value.trim();

  const nameValidationError = validateName(name);
  const deptValidationError = validateDept(dept);

  if (nameValidationError) {
    showNameError(nameValidationError);
    return;
  }

  if (deptValidationError) {
    showDeptError(deptValidationError);
    return;
  }

  try {
    const res = await fetch(API_URL_UPDATE_PROFILES, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name, dept }),
    });

    const result = await res.json();

    if (result.success) {
      window.location.href = MAIN_URL;
    } else {
      showDeptError(result.message);
    }
  } catch (err) {
    showDeptError(MESSAGE_CLIENT_ERR);
    console.error(err);
  }
}

// =====================
// UI helpers
// =====================
function showNameError(message) {
  nameError.textContent = message;
  nameError.classList.add("show");
  nameInput.classList.add("error");
}

function hideNameError() {
  nameError.classList.remove("show");
  nameInput.classList.remove("error");
}

function showDeptError(message) {
  deptError.textContent = message;
  deptError.classList.add("show");
  deptInput.classList.add("error");
}

function hideDeptError() {
  deptError.classList.remove("show");
  deptInput.classList.remove("error");
}

function updateButtonState() {
  if (nameInput.value.trim().length > 0 && deptInput.value.trim().length > 0) {
    submitButton.classList.add("active");
  } else {
    submitButton.classList.remove("active");
  }
}

// =====================
// Validation
// =====================
function validateName(name) {
  if (name.length < 1) return "이름을 입력해주세요.";
  if (name.length > 30) return "이름은 최대 30자까지 입력할 수 있습니다.";
  if (/[!@#$%^&*(),.?":{}|<>]/.test(name)) {
    return "이름에는 특수문자를 사용할 수 없습니다.";
  }
  return null;
}

function validateDept(dept) {
  if (dept.length < 1) return "근무지를 한 글자 이상 입력해주세요.";
  if (dept.length > 50) return "근무지는 최대 50자까지 입력할 수 있습니다.";
  if (/[!@#$%^&*(),.?":{}|<>]/.test(dept)) {
    return "근무지에는 특수문자를 사용할 수 없습니다.";
  }
  return null;
}

// =====================
// init
// =====================
updateButtonState();
