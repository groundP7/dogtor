<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>독터 회원가입</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f4f8fb;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 480px;
            margin: 50px auto;
            background: #fff;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
        }

        h2 {
            text-align: center;
            margin-bottom: 24px;
            color: #333;
        }

        label {
            display: block;
            margin: 10px 0 6px;
            font-weight: bold;
            color: #555;
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
        }

        input[type="checkbox"] {
            margin-right: 5px;
        }

        .btn {
            width: 100%;
            padding: 12px;
            margin-top: 20px;
            border: none;
            border-radius: 6px;
            background-color: #4caf50;
            color: white;
            font-size: 16px;
            cursor: pointer;
        }

        .btn:hover {
            background-color: #45a049;
        }

        .postcode-group {
            display: flex;
            gap: 8px;
            margin-bottom: 8px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>🐶 Dogtor 회원가입</h2>
    <form action="/member/signup" method="post">
        <label>아이디</label>
        <input type="text" name="loginId" required/>

        <label>비밀번호</label>
        <input type="password" name="password" required/>

        <label>이름</label>
        <input type="text" name="name" required/>

        <label>전화번호</label>
        <input type="text" name="phoneNumber" required/>

        <label>우편번호</label>
        <div class="postcode-group">
            <input type="text" id="sample6_postcode" name="postalCode" placeholder="우편번호" required/>
            <input type="button" onclick="sample6_execDaumPostcode()" value="주소 찾기"/>
        </div>

        <label>주소</label>
        <input type="text" id="sample6_address" name="address" placeholder="주소" required/>

        <label>상세주소</label>
        <input type="text" id="sample6_detailAddress" name="detailAddress" placeholder="상세주소" required/>

        <label>참고항목</label>
        <input type="text" id="sample6_extraAddress" disabled placeholder="참고항목"/>

        <label>
            <input type="checkbox" name="smsAgree" value="true"/> SMS 수신 동의
        </label>

        <button type="submit" class="btn">회원가입</button>
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

                if (data.userSelectedType === 'R') {
                    if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                        extraAddr += data.bname;
                    }
                    if (data.buildingName !== '' && data.apartment === 'Y') {
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    if (extraAddr !== '') {
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    document.getElementById("sample6_extraAddress").value = extraAddr;
                } else {
                    document.getElementById("sample6_extraAddress").value = '';
                }

                document.getElementById('sample6_postcode').value = data.zonecode;
                document.getElementById("sample6_address").value = addr;
                document.getElementById("sample6_detailAddress").focus();
            }
        }).open();
    }
</script>
</body>
</html>