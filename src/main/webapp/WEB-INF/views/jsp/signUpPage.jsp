<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>독터 회원가입</title>
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
        }

        .container {
            width: 100%;
            max-width: 480px;
            margin: 40px auto;
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

        .postcode-group {
            display: flex;
            gap: 8px;
            margin-bottom: 12px;
        }

        .postcode-group input {
            flex: 1;
        }

        .address-search-btn {
            padding: 12px 20px;
            background-color: var(--secondary-color);
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 500;
            transition: background-color 0.3s ease;
        }

        .address-search-btn:hover {
            background-color: #45b7af;
        }

        .checkbox-group {
            display: flex;
            align-items: center;
            gap: 8px;
            margin: 24px 0;
        }

        .checkbox-group input[type="checkbox"] {
            width: 18px;
            height: 18px;
            accent-color: var(--primary-color);
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
        }

        .submit-btn:hover {
            background-color: #ff5252;
        }

        .error-message {
            color: #fa5252;
            font-size: 14px;
            margin-top: 4px;
            display: none;
        }

        @media (max-width: 520px) {
            .container {
                margin: 0;
                border-radius: 0;
                padding: 24px;
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

    <form action="/member/signup" method="post" id="signupForm">
        <div class="form-group">
            <label for="loginId">아이디</label>
            <input type="text" id="loginId" name="loginId" required placeholder="아이디를 입력해주세요"/>
            <div class="error-message" id="loginIdError"></div>
        </div>

        <div class="form-group">
            <label for="password">비밀번호</label>
            <input type="password" id="password" name="password" required placeholder="8-20자 영문, 숫자, 특수문자"/>
            <div class="error-message" id="passwordError"></div>
        </div>

        <div class="form-group">
            <label for="name">이름</label>
            <input type="text" id="name" name="name" required placeholder="이름을 입력해주세요"/>
        </div>

        <div class="form-group">
            <label for="phoneNumber">전화번호</label>
            <input type="text" id="phoneNumber" name="phoneNumber" required placeholder="'-' 없이 입력해주세요"/>
            <div class="error-message" id="phoneError"></div>
        </div>

        <div class="form-group">
            <label>주소</label>
            <div class="postcode-group">
                <input type="text" id="sample6_postcode" name="postalCode" placeholder="우편번호" required readonly/>
                <button type="button" class="address-search-btn" onclick="sample6_execDaumPostcode()">주소 찾기</button>
            </div>
            <input type="text" id="sample6_address" name="address" placeholder="기본주소" required readonly/>
            <input type="text" id="sample6_detailAddress" name="detailAddress" placeholder="상세주소를 입력해주세요" required/>
        </div>

        <div class="checkbox-group">
            <input type="checkbox" id="smsAgree" name="smsAgree" value="true"/>
            <label for="smsAgree">SMS 수신에 동의합니다</label>
        </div>

        <button type="submit" class="submit-btn">회원가입</button>
    </form>
</div>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
    function sample6_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                var addr = '';
                var extraAddr = '';

                if (data.userSelectedType === 'R') {
                    addr = data.roadAddress;
                } else {
                    addr = data.jibunAddress;
                }

                document.getElementById('sample6_postcode').value = data.zonecode;
                document.getElementById("sample6_address").value = addr;
                document.getElementById("sample6_detailAddress").focus();
            }
        }).open();
    }

    // Form validation
    document.getElementById('signupForm').addEventListener('submit', function(e) {
        let isValid = true;

        // Password validation
        const password = document.getElementById('password').value;
        const passwordError = document.getElementById('passwordError');
        if (password.length < 8 || password.length > 20 ||
            !/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[$`~!@$!%*#^?&\(\)\-_=+])/.test(password)) {
            passwordError.textContent = '비밀번호는 8-20자의 영문, 숫자, 특수문자를 포함해야 합니다.';
            passwordError.style.display = 'block';
            isValid = false;
        } else {
            passwordError.style.display = 'none';
        }

        // Phone number validation
        const phone = document.getElementById('phoneNumber').value;
        const phoneError = document.getElementById('phoneError');
        if (!/^01[016789]\d{7,8}$/.test(phone)) {
            phoneError.textContent = '올바른 휴대폰 번호를 입력해주세요.';
            phoneError.style.display = 'block';
            isValid = false;
        } else {
            phoneError.style.display = 'none';
        }

        if (!isValid) {
            e.preventDefault();
        }
    });
</script>
</body>
</html>