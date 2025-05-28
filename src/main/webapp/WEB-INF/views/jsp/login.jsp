<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>독터 로그인</title>
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

        .container {
            width: 100%;
            max-width: 400px;
            margin: 20px;
            background: #fff;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
        }

        .logo-section {
            text-align: center;
            margin-bottom: 32px;
        }

        .logo-section h1 {
            font-size: 28px;
            color: var(--primary-color);
            margin: 0;
        }

        .logo-section p {
            color: #868e96;
            margin-top: 8px;
        }

        .form-group {
            margin-bottom: 24px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: var(--text-color);
            font-size: 14px;
        }

        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 12px 16px;
            border: 1px solid var(--border-color);
            border-radius: 8px;
            font-size: 16px;
            transition: all 0.3s ease;
            box-sizing: border-box;
        }

        input[type="text"]:focus,
        input[type="password"]:focus {
            border-color: var(--primary-color);
            outline: none;
            box-shadow: 0 0 0 3px rgba(255, 107, 107, 0.1);
        }

        .submit-btn {
            width: 100%;
            padding: 16px;
            background-color: var(--primary-color);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s ease;
            margin-bottom: 16px;
        }

        .submit-btn:hover {
            background-color: #ff5252;
        }

        .links {
            text-align: center;
            margin-top: 24px;
            padding-top: 16px;
            border-top: 1px solid var(--border-color);
        }

        .links a {
            color: #868e96;
            text-decoration: none;
            font-size: 14px;
            margin: 0 8px;
            transition: color 0.3s ease;
        }

        .links a:hover {
            color: var(--primary-color);
        }

        .error-message {
            color: #fa5252;
            text-align: center;
            margin-bottom: 16px;
            font-size: 14px;
        }

        .remember-me {
            display: flex;
            align-items: center;
            margin-bottom: 24px;
        }

        .remember-me input[type="checkbox"] {
            margin-right: 8px;
            accent-color: var(--primary-color);
        }

        @media (max-width: 480px) {
            .container {
                margin: 0;
                border-radius: 0;
                box-shadow: none;
            }
        }
    </style>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
</head>
<body>
    <div class="container">
        <div class="logo-section">
            <h1>🐾 Dogtor</h1>
            <p>반려동물의 건강한 삶을 위한 첫걸음</p>
        </div>

        <form action="/member/login" method="post" id="loginForm">
            <c:if test="${param.error != null}">
                <div class="error-message">
                    ${param.error}
                </div>
            </c:if>

            <div class="form-group">
                <label for="loginId">아이디</label>
                <input type="text" id="loginId" name="loginId" required placeholder="아이디를 입력해주세요"/>
            </div>

            <div class="form-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" required placeholder="비밀번호를 입력해주세요"/>
            </div>

            <div class="remember-me">
                <input type="checkbox" id="remember" name="remember"/>
                <label for="remember">로그인 상태 유지</label>
            </div>

            <button type="submit" class="submit-btn">로그인</button>

            <div class="links">
                <a href="/member/signup">회원가입</a>
                <span>|</span>
                <a href="/member/find-id">아이디 찾기</a>
                <span>|</span>
                <a href="/member/find-password">비밀번호 찾기</a>
            </div>
        </form>
    </div>

    <script>
        document.getElementById('loginForm').addEventListener('submit', function(e) {
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