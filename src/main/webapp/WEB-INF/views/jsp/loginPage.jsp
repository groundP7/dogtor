<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Dogtor - 로그인</title>
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

        .login-links {
            text-align: center;
            margin-top: 24px;
            padding-top: 16px;
            border-top: 1px solid var(--border-color);
        }

        .login-links a {
            color: #868e96;
            text-decoration: none;
            font-size: 14px;
            margin: 0 8px;
            transition: color 0.3s;
        }

        .login-links a:hover {
            color: var(--primary-color);
        }

        .error-message {
            color: #fa5252;
            text-align: center;
            margin-bottom: 16px;
            font-size: 14px;
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
            <h1>로그인</h1>
            <p>반려동물의 건강한 삶을 위한 첫걸음</p>
        </div>

        <form action="/member/login" method="post">
            <c:if test="${param.error != null}">
                <div class="error-message">
                    ${param.error}
                </div>
            </c:if>

            <div class="form-group">
                <label for="loginId">아이디</label>
                <input type="text" id="loginId" name="loginId" required placeholder="아이디를 입력해주세요">
            </div>

            <div class="form-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" required placeholder="비밀번호를 입력해주세요">
            </div>

            <button type="submit" class="login-button">로그인</button>

            <div class="login-links">
                <a href="/member/signup">회원가입</a>
                <span>|</span>
                <a href="/member/find-id">아이디 찾기</a>
                <span>|</span>
                <a href="/member/find-password">비밀번호 찾기</a>
            </div>
        </form>
    </div>

    <script>
        document.querySelector('form').addEventListener('submit', function(e) {
            const loginId = document.getElementById('loginId').value;
            const password = document.getElementById('password').value;

            if (!loginId || !password) {
                e.preventDefault();
                alert('아이디와 비밀번호를 모두 입력해주세요.');
            }
        });
    </script>
</body>
</html>