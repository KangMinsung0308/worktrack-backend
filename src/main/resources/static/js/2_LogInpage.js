document.addEventListener("DOMContentLoaded", () => {
    const saveBtn = document.getElementById("saveData");
    const emailInput = document.getElementById("emailInput");
    const passwordInput = document.getElementById("passwordInput");

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

    // Toast 메시지 함수
    function showToast(message) {
        const toast = document.createElement('div');
        toast.className = 'toast';
        toast.textContent = message;
        document.body.appendChild(toast);

        setTimeout(() => {
            toast.classList.add('hide');
            setTimeout(() => {
                document.body.removeChild(toast);
            }, 300);
        }, 2000);
    }

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
            const res = await fetch("http://localhost:8080/worktrack/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username: email, password: password })
            });

            const result = await res.json();

            if (result.success) {
                window.location.href = result.redirectUrl;
            } else {
                showToast(result.message);
            }
        } catch (err) {
            showToast("로그인 오류: " + err);
        }
    }
});
