<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dogtor - ${product.name}</title>
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

        .product-detail {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 40px;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .product-image {
            width: 100%;
            aspect-ratio: 1;
            object-fit: cover;
            border-radius: 10px;
        }

        .product-info {
            display: flex;
            flex-direction: column;
        }

        .product-category {
            color: var(--primary-color);
            font-size: 14px;
            font-weight: 500;
            margin-bottom: 10px;
        }

        .product-name {
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 20px;
        }

        .product-price {
            font-size: 24px;
            font-weight: 700;
            color: var(--primary-color);
            margin-bottom: 20px;
        }

        .product-description {
            font-size: 16px;
            line-height: 1.6;
            color: #666;
            margin-bottom: 30px;
        }

        .stock-info {
            font-size: 14px;
            color: #666;
            margin-bottom: 20px;
        }

        .quantity-selector {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 20px;
        }

        .quantity-selector button {
            width: 30px;
            height: 30px;
            border: 1px solid var(--border-color);
            background: white;
            border-radius: 5px;
            cursor: pointer;
        }

        .quantity-selector input {
            width: 60px;
            height: 30px;
            text-align: center;
            border: 1px solid var(--border-color);
            border-radius: 5px;
        }

        .action-buttons {
            display: flex;
            gap: 10px;
            margin-top: auto;
        }

        .btn {
            padding: 15px 30px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .btn-cart {
            background-color: var(--secondary-color);
            color: white;
        }

        .btn-buy {
            background-color: var(--primary-color);
            color: white;
        }

        .btn:hover {
            opacity: 0.9;
        }

        @media (max-width: 768px) {
            .product-detail {
                grid-template-columns: 1fr;
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
        <div class="product-detail">
            <img src="${product.imageUrl}" alt="${product.name}" class="product-image">
            <div class="product-info">
                <div class="product-category">${product.category == 'FOOD' ? '사료' : '영양제'}</div>
                <h1 class="product-name">${product.name}</h1>
                <div class="product-price">${product.price}원</div>
                <p class="product-description">${product.description}</p>
                <div class="stock-info">재고: ${product.stock}개</div>
                <div class="quantity-selector">
                    <button onclick="decreaseQuantity()">-</button>
                    <input type="number" id="quantity" value="1" min="1" max="${product.stock}">
                    <button onclick="increaseQuantity()">+</button>
                </div>
                <div class="action-buttons">
                    <button class="btn btn-cart" onclick="addToCart(${product.id})">장바구니</button>
                    <button class="btn btn-buy" onclick="buyNow(${product.id})">바로 구매</button>
                </div>
            </div>
        </div>
    </main>

    <script>
        function decreaseQuantity() {
            const input = document.getElementById('quantity');
            const currentValue = parseInt(input.value);
            if (currentValue > 1) {
                input.value = currentValue - 1;
            }
        }

        function increaseQuantity() {
            const input = document.getElementById('quantity');
            const currentValue = parseInt(input.value);
            const maxStock = ${product.stock};
            if (currentValue < maxStock) {
                input.value = currentValue + 1;
            }
        }

        function addToCart(productId) {
            const quantity = document.getElementById('quantity').value;
            
            fetch('/cart/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    productId: productId,
                    quantity: quantity
                })
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    if (confirm('장바구니에 추가되었습니다. 장바구니로 이동하시겠습니까?')) {
                        window.location.href = '/member/cart';
                    }
                } else {
                    alert(data.message || '장바구니 추가 실패');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('장바구니 추가 중 오류가 발생했습니다.');
            });
        }

        function buyNow(productId) {
            const quantity = document.getElementById('quantity').value;
            window.location.href = `/order/direct?productId=${productId}&quantity=${quantity}`;
        }
    </script>
</body>
</html> 