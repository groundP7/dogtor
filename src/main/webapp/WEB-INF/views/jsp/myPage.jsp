<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Dogtor - 내 정보 수정</title>
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

        .profile-container {
            background: white;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 600px;
            margin: 20px;
        }

        .profile-header {
            text-align: center;
            margin-bottom: 32px;
        }

        .profile-header h1 {
            font-size: 28px;
            color: var(--primary-color);
            margin: 0;
        }

        .profile-header p {
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

        .form-group input[readonly] {
            background-color: #e9ecef;
            cursor: not-allowed;
        }

        .save-button {
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

        .save-button:hover {
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

        .success-message {
            background-color: #d3f9d8;
            color: #2b8a3e;
            padding: 16px;
            border-radius: 8px;
            margin-bottom: 16px;
            text-align: center;
        }

        @media (max-width: 480px) {
            .profile-container {
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

    <div class="profile-container">
        <div class="profile-header">
            <h1>내 정보 수정</h1>
            <p>회원 정보를 수정할 수 있습니다.</p>
        </div>

        <form action="/member/update-profile" method="post">
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    <c:out value="${errorMessage}" />
                </div>
            </c:if>
            
            <c:if test="${not empty successMessage}">
                <div class="success-message">
                    <c:out value="${successMessage}" />
                </div>
            </c:if>

            <div class="form-group">
                <label for="loginId">아이디</label>
                <input type="text" id="loginId" name="loginId" value="${member.loginId}" readonly>
            </div>

            <div class="form-group">
                <label for="name">이름</label>
                <input type="text" id="name" name="name" value="${member.name}" required>
            </div>

            <div class="form-group">
                <label for="phoneNumber">전화번호</label>
                <input type="tel" id="phoneNumber" name="phoneNumber" value="${member.phoneNumber}" required>
            </div>

            <div class="form-group">
                <label for="postalCode">우편번호</label>
                <div style="display: flex; gap: 8px;">
                    <input type="text" id="postalCode" name="postalCode" value="${memberAddress.postalCode}" readonly required style="flex: 1;">
                    <button type="button" onclick="searchAddress()" style="padding: 0 16px; background-color: var(--secondary-color); color: white; border: none; border-radius: 8px; cursor: pointer;">주소 검색</button>
                </div>
            </div>

            <div class="form-group">
                <label for="address">주소</label>
                <input type="text" id="address" name="address" value="${memberAddress.address}" readonly required>
            </div>

            <div class="form-group">
                <label for="detailAddress">상세주소</label>
                <input type="text" id="detailAddress" name="detailAddress" value="${memberAddress.detailAddress}" required>
            </div>

            <div class="form-group">
                <label for="currentPassword">현재 비밀번호</label>
                <input type="password" id="currentPassword" name="currentPassword" placeholder="현재 비밀번호를 입력해주세요">
            </div>

            <div class="form-group">
                <label for="newPassword">새 비밀번호 (선택사항)</label>
                <input type="password" id="newPassword" name="newPassword" placeholder="변경을 원하시면 입력해주세요">
            </div>

            <div class="form-group">
                <label for="confirmPassword">새 비밀번호 확인</label>
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="새 비밀번호를 다시 입력해주세요">
            </div>

            <button type="submit" class="save-button">정보 수정</button>
        </form>
    </div>

    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script>
        function searchAddress() {
            new daum.Postcode({
                oncomplete: function(data) {
                    document.getElementById('postalCode').value = data.zonecode;
                    document.getElementById('address').value = data.address;
                    document.getElementById('detailAddress').focus();
                }
            }).open();
        }

        document.querySelector('form').addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const currentPassword = document.getElementById('currentPassword').value;
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            const postalCode = document.getElementById('postalCode').value;
            const address = document.getElementById('address').value;
            const detailAddress = document.getElementById('detailAddress').value;

            if (!currentPassword) {
                alert('현재 비밀번호를 입력해주세요.');
                return;
            }

            if (!postalCode || !address || !detailAddress) {
                alert('주소를 모두 입력해주세요.');
                return;
            }

            if (newPassword) {
                if (newPassword.length < 8 || newPassword.length > 20) {
                    alert('새 비밀번호는 8~20자 사이여야 합니다.');
                    return;
                }

                if (!/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/.test(newPassword)) {
                    alert('새 비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.');
                    return;
                }

                if (newPassword !== confirmPassword) {
                    alert('새 비밀번호가 일치하지 않습니다.');
                    return;
                }
            }

            try {
                const formData = new FormData(this);
                const response = await fetch('/member/update-profile', {
                    method: 'POST',
                    body: formData
                });

                if (response.ok) {
                    const result = await response.json();
                    if (result.success) {
                        alert('회원 정보가 성공적으로 수정되었습니다.');
                        window.location.href = '/';
                    } else {
                        alert(result.message || '회원 정보 수정에 실패했습니다.');
                    }
                } else {
                    const error = await response.json();
                    alert(error.message || '회원 정보 수정에 실패했습니다.');
                }
            } catch (error) {
                console.error('Error:', error);
                alert('오류가 발생했습니다. 다시 시도해주세요.');
            }
        });
    </script>
</body>
</html> 