<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Dogtor - 아이디 찾기</title>
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

        .find-container {
            background: white;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            margin: 20px;
        }

        .find-header {
            text-align: center;
            margin-bottom: 32px;
        }

        .find-header h1 {
            font-size: 28px;
            color: var(--primary-color);
            margin: 0;
        }

        .find-header p {
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

        .submit-button {
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

        .submit-button:hover {
            background-color: #ff5252;
        }

        .result-message {
            text-align: center;
            margin-top: 16px;
            padding: 16px;
            border-radius: 8px;
            font-size: 14px;
        }

        .success-message {
            background-color: #d3f9d8;
            color: #2b8a3e;
        }

        .error-message {
            background-color: #ffe3e3;
            color: #fa5252;
        }

        @media (max-width: 480px) {
            .find-container {
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

    <div class="find-container">
        <div class="find-header">
            <h1>아이디 찾기</h1>
            <p>회원가입 시 입력한 이름과 전화번호를 입력해주세요.</p>
        </div>

        <form action="/member/find-id" method="post">
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    <c:out value="${errorMessage}" />
                </div>
            </c:if>

            <c:if test="${not empty foundId}">
                <div class="success-message">
                    찾은 아이디: <c:out value="${foundId}" />
                </div>
            </c:if>

            <div class="form-group">
                <label for="name">이름</label>
                <input type="text" id="name" name="name" required placeholder="이름을 입력해주세요">
            </div>

            <div class="form-group">
                <label for="phoneNumber">전화번호</label>
                <input type="tel" id="phoneNumber" name="phoneNumber" required placeholder="전화번호를 입력해주세요">
            </div>

            <button type="submit" class="submit-button">아이디 찾기</button>

            <div style="text-align: center; margin-top: 16px;">
                <a href="/member/login" style="color: #868e96; text-decoration: none;">로그인으로 돌아가기</a>
            </div>
        </form>
    </div>
</body>
</html>