<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dogtor - ${categoryName}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
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

        .nav-menu {
            display: flex;
            gap: 20px;
        }

        .nav-menu a {
            color: var(--text-color);
            text-decoration: none;
            font-weight: 500;
        }

        .main-content {
            max-width: 1200px;
            margin: 100px auto 0;
            padding: 20px;
        }

        .category-title {
            font-size: 28px;
            margin-bottom: 30px;
            color: var(--text-color);
        }

        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 30px;
        }

        .product-card {
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s;
        }

        .product-card:hover {
            transform: translateY(-5px);
        }

        .product-image {
            width: 100%;
            height: 200px;
            object-fit: cover;
        }

        .product-info {
            padding: 15px;
        }

        .product-name {
            font-size: 16px;
            font-weight: 500;
            margin-bottom: 8px;
        }

        .product-price {
            font-size: 18px;
            color: var(--primary-color);
            font-weight: bold;
        }

        .product-description {
            font-size: 14px;
            color: #666;
            margin-top: 8px;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }

        @media (max-width: 768px) {
            .product-grid {
                grid-template-columns: repeat(2, 1fr);
                gap: 15px;
            }
        }
    </style>
</head>
<body>
    <header class="header">
        <div class="top-header">
            <a href="/" class="logo">🐾 Dogtor</a>
            <nav class="nav-menu">
                <c:choose>
                    <c:when test="${not empty sessionScope.memberId}">
                        <a href="/member/cart">장바구니</a>
                        <a href="/member/my-profile">${sessionScope.memberName}님</a>
                        <a href="/member/logout">로그아웃</a>
                    </c:when>
                    <c:otherwise>
                        <a href="/member/login">로그인</a>
                        <a href="/member/signup">회원가입</a>
                    </c:otherwise>
                </c:choose>
            </nav>
        </div>
    </header>

    <main class="main-content">
        <h1 class="category-title">${categoryName}</h1>
        <div class="product-grid">
            <c:forEach items="${products}" var="product">
                <a href="/products/${product.id}" style="text-decoration: none; color: inherit;">
                    <div class="product-card">
                        <img src="${product.imageUrl}" alt="${product.name}" class="product-image">
                        <div class="product-info">
                            <h2 class="product-name">${product.name}</h2>
                            <p class="product-price">${product.price}원</p>
                            <p class="product-description">${product.description}</p>
                        </div>
                    </div>
                </a>
            </c:forEach>
        </div>
    </main>
</body>
</html> 