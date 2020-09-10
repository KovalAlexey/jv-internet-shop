<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>All orders</title>
</head>
<body>
<h1>All orders</h1>
<table border="1">
    <tr>
        <th>Order ID</th>
        <th>User ID</th>
        <th>Products</th>
        <th>Action</th>
    </tr>
    <c:forEach var="order" items="${orders}">
        <tr>
            <td>
                <c:out value="${order.id}"/>
            </td>
            <td>
                <c:out value="${order.userId}"/>
            </td>
            <td>
                <c:out value="${order.products}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/users/order/delete?id=${order.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
