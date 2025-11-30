    <script>
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

        function handleLogin() {
            const email = document.getElementById('emailInput').value;
            const password = document.getElementById('passwordInput').value;
            
            if (!email || !password) {
                showToast('이메일과 비밀번호를 입력해주세요');
                return;
            }
            
            showToast('로그인 중...');
        }

        function handleGoogleLogin() {
            showToast('Google 계정으로 로그인 중...');
        }

        function handleAppleLogin() {
            showToast('Apple 계정으로 로그인 중...');
        }

        // Enter key support
        document.getElementById('emailInput').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                document.getElementById('passwordInput').focus();
            }
        });

        document.getElementById('passwordInput').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                handleLogin();
            }
        });
    </script>
