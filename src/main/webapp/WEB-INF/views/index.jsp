<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Internet-shop</title>
</head>
<body>
<h1>Hello Mates!</h1>
<h2>Today is: ${time}</h2>
<a href="${pageContext.request.contextPath}/products">Catalog</a>
<a href="${pageContext.request.contextPath}/shopping-cart/products">User cart</a>
<a href="${pageContext.request.contextPath}/orders/by-user">User orders</a>
<a href="${pageContext.request.contextPath}/logout">Logout</a>
<p></p>
<a href="${pageContext.request.contextPath}/products/admin">Admin catalog</a>
<a href="${pageContext.request.contextPath}/orders/admin">Admin orders</a>
<a href="${pageContext.request.contextPath}/users/all">All users</a>
<a href="${pageContext.request.contextPath}/injectData">Inject data to DB</a>
</body>
</html>
