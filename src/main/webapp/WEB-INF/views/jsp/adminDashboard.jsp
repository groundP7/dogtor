<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dogtor Admin - 관리자 대시보드</title>
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
        }

        .dashboard-header {
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin-bottom: 30px;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .stat-card {
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .stat-card h3 {
            color: var(--secondary-color);
            margin-bottom: 10px;
        }

        .stat-card p {
            font-size: 24px;
            font-weight: bold;
        }

        .product-list {
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .product-list h2 {
            margin-bottom: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .btn-add {
            background-color: var(--success-color);
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            display: flex;
            align-items: center;
        }

        .btn-add i {
            margin-right: 5px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid var(--border-color);
        }

        th {
            background-color: #f8f9fa;
            font-weight: 500;
        }

        .product-actions {
            display: flex;
            gap: 10px;
        }

        .btn-edit, .btn-delete {
            padding: 5px 10px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            font-size: 12px;
        }

        .btn-edit {
            background-color: var(--warning-color);
            color: var(--text-color);
        }

        .btn-delete {
            background-color: var(--accent-color);
            color: white;
        }

        .product-image {
            width: 50px;
            height: 50px;
            object-fit: cover;
            border-radius: 5px;
        }

        @media (max-width: 768px) {
            .sidebar {
                width: 70px;
                padding: 10px;
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
            <div class="nav-item active">
                <i class="fas fa-home"></i>
                <span>대시보드</span>
            </div>
            <div class="nav-item">
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
            <div class="nav-item" onclick="location.href='/admin/logout'">
                <i class="fas fa-sign-out-alt"></i>
                <span>로그아웃</span>
            </div>
        </nav>
    </div>

    <div class="main-content">
        <div class="dashboard-header">
            <h1>관리자 대시보드</h1>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <h3>총 제품 수</h3>
                <p>${totalProducts}</p>
            </div>
            <div class="stat-card">
                <h3>사료 제품</h3>
                <p>${foodProducts}</p>
            </div>
            <div class="stat-card">
                <h3>영양제 제품</h3>
                <p>${supplementProducts}</p>
            </div>
            <div class="stat-card">
                <h3>품절 임박</h3>
                <p>${lowStockProducts}</p>
            </div>
        </div>

        <div class="product-list">
            <h2>
                제품 목록
                <button class="btn-add" onclick="location.href='/admin/products/new'">
                    <i class="fas fa-plus"></i> 새 제품 등록
                </button>
            </h2>
            <table>
                <thead>
                    <tr>
                        <th>이미지</th>
                        <th>제품명</th>
                        <th>카테고리</th>
                        <th>가격</th>
                        <th>재고</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${products}" var="product">
                        <tr>
                            <td>
                                <img src="${product.imageUrl}" alt="${product.name}" class="product-image">
                            </td>
                            <td>${product.name}</td>
                            <td>${product.category == 'FOOD' ? '사료' : '영양제'}</td>
                            <td>${product.price}원</td>
                            <td>${product.stock}개</td>
                            <td class="product-actions">
                                <button class="btn-edit" onclick="location.href='/admin/products/edit/${product.id}'">
                                    수정
                                </button>
                                <button class="btn-delete" onclick="deleteProduct(${product.id})">
                                    삭제
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <script>
        function deleteProduct(productId) {
            if (confirm('정말로 이 제품을 삭제하시겠습니까?')) {
                fetch(`/admin/products/${productId}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('제품이 삭제되었습니다.');
                        location.reload();
                    } else {
                        alert(data.message || '제품 삭제에 실패했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('오류가 발생했습니다. 다시 시도해주세요.');
                });
            }
        }
    </script>
</body>
</html> 