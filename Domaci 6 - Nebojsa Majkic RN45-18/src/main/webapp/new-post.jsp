<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>New Post</title>
    <%@ include file="styles.jsp" %>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <h1>New Post</h1>
    <br>

    <form method="POST" action="<%=application.getContextPath() + "/new-post"%>">
        <div class="form-group">
            <label for="author">Author</label>
            <input type="text" class="form-control" id="author" name="author" required="true">
        </div>
        <div class="form-group">
            <label for="title">Title</label>
            <input type="text" class="form-control" id="title" name="title" required="true">
        </div>
        <div class="form-group">
            <label for="text">Text</label>
            <textarea type="text" rows="4" cols="30" class="form-control" id="text" name="text" required="true"></textarea>
        </div>

        <button type="submit" class="btn btn-primary">Save Post</button>
    </form>
    <br>
</div>

</body>
</html>
