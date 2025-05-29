<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Dogtor - 비밀번호 재설정</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        :root {
            --primary-color: #FF6B6B;
            --secondary-color: #4ECDC4;
            --background-color: #f8f9fa;
            --text-color: #343a40;
            --border-color: #dee2e6;
        }

        body {
            font-family: 'Noto Sans KR', sans-serif;
            background-color: var(--background-color);
            margin: 0;
            padding: 0;
            color: var(--text-color);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        .header {
            background-color: white;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            position: fixed;
            width: 100%;
            top: 0;
            z-index: 1000;
        }

        .top-header {
            max-width: 1200px;
            margin: 0 auto;
            padding: 1rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            font-size: 24px;
            color: var(--primary-color);
            text-decoration: none;
            font-weight: bold;
        }

        .login-container {
            background: white;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            margin: 20px;
        }

        .login-header {
            text-align: center;
            margin-bottom: 32px;
        }

        .login-header h1 {
            font-size: 28px;
            color: var(--primary-color);
            margin: 0;
        }

        .login-header p {
            color: #868e96;
            margin-top: 8px;
        }

        .form-group {
            margin-bottom: 24px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-size: 14px;
            font-weight: 500;
        }

        .form-group input {
            width: 100%;
            padding: 12px 16px;
            border: 1px solid var(--border-color);
            border-radius: 8px;
            font-size: 16px;
            transition: all 0.3s;
            box-sizing: border-box;
        }

        .form-group input:focus {
            border-color: var(--primary-color);
            outline: none;
            box-shadow: 0 0 0 3px rgba(255, 107, 107, 0.1);
        }

        .login-button {
            width: 100%;
            padding: 16px;
            background-color: var(--primary-color);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s;
            margin-bottom: 16px;
        }

        .login-button:hover {
            background-color: #ff5252;
        }

        .error-message {
            background-color: #ffe3e3;
            color: #fa5252;
            padding: 16px;
            border-radius: 8px;
            margin-bottom: 16px;
            text-align: center;
        }

        @media (max-width: 480px) {
            .login-container {
                margin: 0;
                border-radius: 0;
                box-shadow: none;
            }
        }
    </style>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
</head>
<body>
    <header class="header">
        <div class="top-header">
            <a href="/" class="logo">🐾 Dogtor</a>
        </div>
    </header>

    <div class="login-container">
        <div class="login-header">
            <h1>비밀번호 재설정</h1>
            <p class="description">
                        새로운 비밀번호를 입력해주세요.<br>
                        비밀번호는 8-20자의 영문, 숫자, 특수문자를 포함해야 합니다.
                    </p>
        </div>

        <form action="/member/reset-password" method="post">
            <input type="hidden" name="loginId" value="${loginId}">
            <input type="hidden" name="phoneNumber" value="${phoneNumber}">
            
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    <c:out value="${errorMessage}" />
                </div>
            </c:if>

            <div class="form-group">
                <label for="newPassword">새 비밀번호</label>
                <input type="password" id="newPassword" name="newPassword" required 
                       placeholder="새 비밀번호를 입력해주세요">
            </div>

            <div class="form-group">
                <label for="confirmPassword">비밀번호 확인</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required 
                       placeholder="비밀번호를 다시 입력해주세요">
            </div>

            <button type="submit" class="login-button">비밀번호 변경</button>

            <div style="text-align: center; margin-top: 16px;">
                <a href="/member/login" style="color: #868e96; text-decoration: none;">로그인으로 돌아가기</a>
            </div>
        </form>
    </div>

    <script>
        document.querySelector('form').addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            if (!newPassword || !confirmPassword) {
                alert('새 비밀번호와 비밀번호 확인을 모두 입력해주세요.');
                return;
            }

            if (newPassword !== confirmPassword) {
                alert('비밀번호가 일치하지 않습니다.');
                return;
            }

            if (newPassword.length < 8 || newPassword.length > 20) {
                alert('비밀번호는 8~20자 사이여야 합니다.');
                return;
            }

            if (!/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/.test(newPassword)) {
                alert('비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.');
                return;
            }

            try {
                const formData = new FormData(this);
                const response = await fetch('/member/reset-password', {
                    method: 'POST',
                    body: formData,
                    headers: {
                        'Accept': 'application/json'
                    }
                });

                if (response.ok) {
                    alert('비밀번호가 성공적으로 변경되었습니다.');
                    sessionStorage.setItem('passwordResetSuccess', 'true');
                    window.location.href = '/member/login';
                } else {
                    const data = await response.json().catch(() => null);
                    if (data && data.message) {
                        alert(data.message);
                    } else {
                        alert('비밀번호 변경에 실패했습니다. 다시 시도해주세요.');
                    }
                }
            } catch (error) {
                console.error('Error:', error);
                alert('오류가 발생했습니다. 다시 시도해주세요.');
            }
        });
    </script>
</body>
</html>