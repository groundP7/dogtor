<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Dogtor - 반려동물 건강 파트너</title>
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
            align-items: center;
        }

        .nav-menu a {
            color: var(--text-color);
            text-decoration: none;
            font-weight: 500;
            transition: color 0.3s;
        }

        .nav-menu a:hover {
            color: var(--primary-color);
        }

        .category-menu {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0.5rem 1rem;
            display: flex;
            gap: 2rem;
            border-top: 1px solid var(--border-color);
        }

        .category-menu a {
            color: var(--text-color);
            text-decoration: none;
            font-size: 14px;
            transition: color 0.3s;
        }

        .category-menu a:hover {
            color: var(--primary-color);
        }

        .main-content {
            margin-top: 120px;
            padding: 20px;
            max-width: 1200px;
            margin-left: auto;
            margin-right: auto;
        }

        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }

        .product-card {
            background: white;
            border-radius: 8px;
            padding: 15px;
            transition: transform 0.3s;
            cursor: pointer;
        }

        .product-card:hover {
            transform: translateY(-5px);
        }

        .product-image {
            width: 100%;
            aspect-ratio: 1;
            object-fit: cover;
            border-radius: 4px;
            margin-bottom: 10px;
        }

        .product-brand {
            color: #666;
            font-size: 12px;
            margin-bottom: 4px;
        }

        .product-name {
            font-size: 14px;
            margin-bottom: 8px;
            line-height: 1.4;
        }

        .product-price {
            font-weight: bold;
            color: var(--primary-color);
        }

        .section-title {
            font-size: 24px;
            margin: 40px 0 20px;
            color: var(--text-color);
        }

        @media (max-width: 768px) {
            .category-menu {
                overflow-x: auto;
                -webkit-overflow-scrolling: touch;
            }
            
            .product-grid {
                grid-template-columns: repeat(2, 1fr);
            }
        }

        /* AI 상담 버튼 스타일 추가 */
        .ai-consult {
            color: var(--primary-color) !important;
            font-weight: bold !important;
        }
    </style>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
</head>
<body>
    <header class="header">
        <div class="top-header">
            <a href="/" class="logo">🐾 Dogtor</a>
            <nav class="nav-menu">
                <c:choose>
                    <c:when test="${not empty sessionScope.memberId}">
                        <a href="/shop">핫딜</a>
                        <a href="/member/cart">장바구니</a>
                        <a href="/member/profile">${sessionScope.memberName}님</a>
                        <a href="/member/logout">로그아웃</a>
                    </c:when>
                    <c:otherwise>
                        <a href="/member/login">로그인</a>
                        <a href="/member/signup">회원가입</a>
                    </c:otherwise>
                </c:choose>
            </nav>
        </div>
        <div class="category-menu">
            <a href="/category/food">사료</a>
            <a href="/category/snack">간식</a>
            <a href="/chatbot" class="ai-consult">🤖 AI 상담</a>
        </div>
    </header>

    <main class="main-content">
        <h2 class="section-title">추천 상품</h2>
        <div class="product-grid">
            <div class="product-card">
                <img src="/images/product1.jpg" alt="사료1" class="product-image">
                <div class="product-brand">로얄캐닌</div>
                <div class="product-name">미니 어덜트 1.5kg</div>
                <div class="product-price">32,000원</div>
            </div>
            <div class="product-card">
                <img src="/images/product2.jpg" alt="간식1" class="product-image">
                <div class="product-brand">ANF</div>
                <div class="product-name">6Free 플러스 연어 1.8kg</div>
                <div class="product-price">28,000원</div>
            </div>
        </div>
    </main>
</body>
</html>