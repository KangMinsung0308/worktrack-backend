document.addEventListener("DOMContentLoaded", () => {
    const saveBtn = document.getElementById("saveData");
    const emailInput = document.getElementById("emailInput");
    const passwordInput = document.getElementById("passwordInput");
    const go_to_signup_page = document.getElementById("go_signup_page");

    // 로그인 버튼 클릭 이벤트
    saveBtn.addEventListener("click", handleLogin);

    // Enter 키 지원
    emailInput.addEventListener("keypress", function(e) {
        if (e.key === "Enter") {
            passwordInput.focus();
        }
    });

    passwordInput.addEventListener("keypress", function(e) {
        if (e.key === "Enter") {
            handleLogin();
        }
    });

    //  회원가입 이동
    go_to_signup_page.addEventListener("click", () => {
        window.location.href = URL_CREATE_ACOUNT;
    });

    // 실제 로그인 처리 함수
    async function handleLogin() {
        const email = emailInput.value;
        const password = passwordInput.value;

        if (!email || !password) {
            showToast("이메일과 비밀번호를 입력해주세요");
            return;
        }

        showToast("로그인 중...");

        try {
            const res = await fetch(API_URL_LOGIN, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username: email, password: password })
            });

            const result = await res.json();

            if (result.success) {
                window.location.href = "/";
            } else {
                showToast(result.message);
            }
        } catch (err) {
            showToast("로그인 오류: " + err);
        }
    }
});
