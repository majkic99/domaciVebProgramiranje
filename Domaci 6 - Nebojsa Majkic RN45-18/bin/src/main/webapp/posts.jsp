<%@ page import="java.util.List" %>
<%@ page import="rs.raf.demo.models.Quote" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Posts</title>
    <%@ include file="styles.jsp" %>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <h1 class="naslov">All Posts</h1>
    <br>

    <% List<Quote> quotes = (List<Quote>) request.getAttribute("posts"); %>

    <div class="container">
        <% for (Quote q : quotes) { %>
        <br>
        <h4><%=q.getTitle()%>
        </h4>
        <a href="<%=application.getContextPath()%>/posts/<%= q.getId() %>">Opsirnije...
        </a>
        <br>

        <% } %>
        <br>
        <a href="new-post.jsp" class="btn btn-primary btn-sm active" role="button" aria-pressed="true">New Post</a>
    </div>
</div>

</body>
</html>

