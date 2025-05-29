<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Dogtor - 비밀번호 찾기</title>
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
            <h1>비밀번호 찾기</h1>
            <p>회원가입 시 입력한 아이디와 전화번호를 입력해주세요.</p>
        </div>

        <form action="/member/find-password" method="post">
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    <c:out value="${errorMessage}" />
                </div>
            </c:if>

            <div class="form-group">
                <label for="loginId">아이디</label>
                <input type="text" id="loginId" name="loginId" required placeholder="아이디를 입력해주세요">
            </div>

            <div class="form-group">
                <label for="phoneNumber">전화번호</label>
                <input type="tel" id="phoneNumber" name="phoneNumber" required placeholder="전화번호를 입력해주세요">
            </div>

            <button type="submit" class="login-button">다음</button>

            <div style="text-align: center; margin-top: 16px;">
                <a href="/member/login" style="color: #868e96; text-decoration: none;">로그인으로 돌아가기</a>
            </div>
        </form>
    </div>

    <script>
        document.querySelector('form').addEventListener('submit', function(e) {
            const loginId = document.getElementById('loginId').value;
            const phoneNumber = document.getElementById('phoneNumber').value;

            if (!loginId || !phoneNumber) {
                e.preventDefault();
                alert('아이디와 전화번호를 모두 입력해주세요.');
            }
        });
    </script>
</body>
</html>