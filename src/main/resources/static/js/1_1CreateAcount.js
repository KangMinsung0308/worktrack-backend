const emailInput = document.getElementById("emailInput");
const Button_Post_signUp_Email = document.getElementById("Button_Post_signUp_Email");
const googleBtn = document.getElementById("googleBtn");
const appleBtn = document.getElementById("appleBtn");


    // [계속] 버튼 클릭 이벤트
    Button_Post_signUp_Email.addEventListener("click", handleCreateAcount);

    // 이메일 등록처리 (유효 이메일 체크)
    async function handleCreateAcount() {
        const email = emailInput.value;

        try {
            const res = await fetch(API_URL_SIGNUP_EMAIL, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ userEmail: email })
            });

            const result = await res.json();

            if (result.success) {
                window.location.href = URL_CREATE_PASSWORD
            } else {
                showToast(result.message);
            }
        } catch (err) {
            showToast(`${MESSAGE_CLIENT_ERR}` + err);
        }
    }

// Enable/disable continue button based on email input
emailInput.addEventListener("input", (e) => {
  if (e.target.value.trim()) {
    Button_Post_signUp_Email.disabled = false;
  } else {
    Button_Post_signUp_Email.disabled = true;
  }
});

// Continue button click
Button_Post_signUp_Email.addEventListener("click", () => {
  const email = emailInput.value.trim();
  if (email) {
    showToast(`이메일로 계속하기: ${email}`);
  }
});

// Google button click
googleBtn.addEventListener("click", () => {
  showToast("Google 계정으로 로그인 중...");
});

// Apple button click
appleBtn.addEventListener("click", () => {
  showToast("Apple 계정으로 로그인 중...");
});

// Enter key support
emailInput.addEventListener("keypress", (e) => {
  if (e.key === "Enter" && !continueBtn.disabled) {
    continueBtn.click();
  }
});
