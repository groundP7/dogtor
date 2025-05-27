<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>회원가입 완료 - Dogtor</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        :root {
            --primary-color: #FF6B6B;
            --background-color: #f8f9fa;
            --text-color: #343a40;
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

        .container {
            width: 100%;
            max-width: 480px;
            margin: 20px;
            background: #fff;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
            text-align: center;
        }

        .success-icon {
            font-size: 64px;
            margin-bottom: 24px;
        }

        h1 {
            color: var(--primary-color);
            margin-bottom: 16px;
        }

        p {
            color: #666;
            line-height: 1.6;
            margin-bottom: 24px;
        }

        .home-button {
            display: inline-block;
            padding: 12px 24px;
            background-color: var(--primary-color);
            color: white;
            text-decoration: none;
            border-radius: 8px;
            transition: background-color 0.3s ease;
        }

        .home-button:hover {
            background-color: #ff5252;
        }
    </style>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
</head>
<body>
    <div class="container">
        <div class="success-icon">🎉</div>
        <h1>회원가입 완료!</h1>
        <p>
            Dogtor의 회원이 되신 것을 환영합니다.<br>
            반려동물과 함께하는 건강한 삶이 시작됩니다.
        </p>
        <a href="/" class="home-button">홈으로 이동</a>
    </div>
</body>
</html>