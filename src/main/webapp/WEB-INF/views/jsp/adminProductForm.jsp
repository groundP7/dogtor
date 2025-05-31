<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dogtor Admin - 제품 ${product == null ? '등록' : '수정'}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #3498db;
            --accent-color: #e74c3c;
            --background-color: #ecf0f1;
            --text-color: #2c3e50;
            --border-color: #bdc3c7;
            --success-color: #27ae60;
            --warning-color: #f1c40f;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Noto Sans KR', sans-serif;
            background-color: var(--background-color);
            color: var(--text-color);
            min-height: 100vh;
            display: flex;
        }

        .sidebar {
            position: fixed;
            left: 0;
            top: 0;
            width: 250px;
            height: 100vh;
            background-color: var(--primary-color);
            padding: 20px;
            color: white;
        }

        .sidebar-header {
            text-align: center;
            padding: 20px 0;
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }

        .sidebar-header h1 {
            font-size: 24px;
            margin-bottom: 10px;
        }

        .admin-info {
            font-size: 14px;
            color: rgba(255, 255, 255, 0.7);
        }

        .nav-menu {
            margin-top: 30px;
        }

        .nav-item {
            padding: 15px;
            cursor: pointer;
            border-radius: 5px;
            transition: background-color 0.3s;
            margin-bottom: 5px;
            display: flex;
            align-items: center;
        }

        .nav-item i {
            margin-right: 10px;
            width: 20px;
        }

        .nav-item:hover {
            background-color: rgba(255, 255, 255, 0.1);
        }

        .nav-item.active {
            background-color: var(--secondary-color);
        }

        .main-content {
            margin-left: 250px;
            padding: 30px;
            flex-grow: 1;
        }

        .form-container {
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            max-width: 800px;
            margin: 0 auto;
        }

        .form-header {
            margin-bottom: 30px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
        }

        .form-group input[type="text"],
        .form-group input[type="number"],
        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid var(--border-color);
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s;
        }

        .form-group input[type="text"]:focus,
        .form-group input[type="number"]:focus,
        .form-group select:focus,
        .form-group textarea:focus {
            border-color: var(--secondary-color);
            outline: none;
        }

        .form-group textarea {
            height: 150px;
            resize: vertical;
        }

        .image-preview {
            width: 200px;
            height: 200px;
            border: 2px dashed var(--border-color);
            border-radius: 5px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-top: 10px;
            position: relative;
            overflow: hidden;
        }

        .image-preview img {
            max-width: 100%;
            max-height: 100%;
            object-fit: contain;
        }

        .image-preview.empty {
            background-color: #f8f9fa;
        }

        .image-preview.empty::after {
            content: '이미지 미리보기';
            color: var(--border-color);
        }

        .btn-container {
            display: flex;
            gap: 10px;
            margin-top: 30px;
        }

        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
            transition: background-color 0.3s;
        }

        .btn-primary {
            background-color: var(--secondary-color);
            color: white;
        }

        .btn-primary:hover {
            background-color: #2980b9;
        }

        .btn-cancel {
            background-color: var(--border-color);
            color: var(--text-color);
        }

        .btn-cancel:hover {
            background-color: #95a5a6;
        }

        @media (max-width: 768px) {
            .sidebar {
                width: 70px;
            }

            .sidebar-header h1, .admin-info, .nav-item span {
                display: none;
            }

            .main-content {
                margin-left: 70px;
            }

            .nav-item {
                justify-content: center;
                padding: 15px 0;
            }

            .nav-item i {
                margin: 0;
            }

            .form-container {
                margin: 0;
                padding: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="sidebar-header">
            <h1>🐾 Dogtor Admin</h1>
            <div class="admin-info">
                관리자: ${adminName}
            </div>
        </div>
        <nav class="nav-menu">
            <div class="nav-item" onclick="location.href='/admin'">
                <i class="fas fa-home"></i>
                <span>대시보드</span>
            </div>
            <div class="nav-item active">
                <i class="fas fa-box"></i>
                <span>제품 관리</span>
            </div>
            <div class="nav-item">
                <i class="fas fa-users"></i>
                <span>회원 관리</span>
            </div>
            <div class="nav-item">
                <i class="fas fa-cog"></i>
                <span>설정</span>
            </div>
            <div class="nav-item">
                <i class="fas fa-sign-out-alt"></i>
                <span>로그아웃</span>
            </div>
        </nav>
    </div>

    <div class="main-content">
        <div class="form-container">
            <div class="form-header">
                <h1>${product == null ? '새 제품 등록' : '제품 정보 수정'}</h1>
            </div>

            <form id="productForm" method="POST" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="name">제품명</label>
                    <input type="text" id="name" name="name" value="${product.name}" required>
                </div>

                <div class="form-group">
                    <label for="category">카테고리</label>
                    <select id="category" name="category" required>
                        <option value="">카테고리 선택</option>
                        <option value="FOOD" ${product.category == 'FOOD' ? 'selected' : ''}>사료</option>
                        <option value="SUPPLEMENT" ${product.category == 'SUPPLEMENT' ? 'selected' : ''}>영양제</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="description">제품 설명</label>
                    <textarea id="description" name="description" required>${product.description}</textarea>
                </div>

                <div class="form-group">
                    <label for="price">가격</label>
                    <input type="number" id="price" name="price" value="${product.price}" min="0" required>
                </div>

                <div class="form-group">
                    <label for="stock">재고 수량</label>
                    <input type="number" id="stock" name="stock" value="${product.stock}" min="0" required>
                </div>

                <div class="form-group">
                    <label for="image">제품 이미지</label>
                    <input type="file" id="image" name="image" accept="image/*" onchange="previewImage(this)">
                    <div class="image-preview ${product.imageUrl == null ? 'empty' : ''}">
                        <c:if test="${product.imageUrl != null}">
                            <img src="${product.imageUrl}" alt="제품 이미지">
                        </c:if>
                    </div>
                </div>

                <div class="btn-container">
                    <button type="submit" class="btn btn-primary">
                        ${product == null ? '등록하기' : '수정하기'}
                    </button>
                    <button type="button" class="btn btn-cancel" onclick="history.back()">취소</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function previewImage(input) {
            const preview = document.querySelector('.image-preview');
            preview.innerHTML = '';
            preview.classList.remove('empty');

            if (input.files && input.files[0]) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    const img = document.createElement('img');
                    img.src = e.target.result;
                    preview.appendChild(img);
                }
                reader.readAsDataURL(input.files[0]);
            } else {
                preview.classList.add('empty');
            }
        }

        document.getElementById('productForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const formData = new FormData(this);
            const url = ${product == null} ? '/admin/products/new' : '/admin/products/${product.id}';
            
            try {
                const response = await fetch(url, {
                    method: 'POST',
                    body: formData
                });

                const result = await response.json();
                if (result.success) {
                    alert(${product == null} ? '제품이 등록되었습니다.' : '제품이 수정되었습니다.');
                    location.href = '/admin';
                } else {
                    alert(result.message || '오류가 발생했습니다.');
                }
            } catch (error) {
                console.error('Error:', error);
                alert('오류가 발생했습니다. 다시 시도해주세요.');
            }
        });
    </script>
</body>
</html> 