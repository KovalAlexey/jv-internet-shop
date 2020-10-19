<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8"  %>
<%@include file="../header.jsp"%>
<html>
<head>
    <title>User Cart</title>
</head>
<body>
<h1>Mock user buy this stuff: </h1>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Product</th>
        <th>Price</th>
        <th>Action</th>
    </tr>
    <c:forEach var="product" items="${products}">
        <tr>
            <td>
                <c:out value="${product.id}"/>
            </td>
            <td>
                <c:out value="${product.name}"/>
            </td>
            <td>
                <c:out value="${product.price}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/shopping-cart/products/delete?productId=${product.id}">Delete</a>
            </td>

        </tr>
    </c:forEach>
</table>
<a href="${pageContext.request.contextPath}/order/complete">Complete</a>
<a href="${pageContext.request.contextPath}/">Back to main</a>
</body>
</html>
