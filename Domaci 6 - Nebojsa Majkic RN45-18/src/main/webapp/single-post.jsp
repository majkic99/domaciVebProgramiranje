<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="post" scope="request" class="rs.raf.demo.models.Quote"/>
<%@ taglib prefix="n" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <%@ include file="styles.jsp" %>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container" >
    <h3 class="text-center"> <%= post.getTitle() %>
    </h3>
    <h8>Post <%= post.getDate() %>
    </br>

		<figure class="text-center">
			<blockquote class="blockquote">
				<p><%= post.getText() %></p>
			</blockquote>
			<figcaption class="blockquote-footer">
				<%=post.getAuthor()%>
			</figcaption>
		</figure>

		<% if (post.getComments() != null) {%>
    <h2>Comments</h2>

    <n:forEach var="comment" items="<%=post.getComments()%>">
        Author: <n:out value="${comment.getName()}"/></b>
        Comment: <n:out value="${comment.getComment()}"/></b>
        <hr>
    </n:forEach>

    <% } %>

    <h3>New Comment</h3>
    <br>

    <form method="POST" action="<%=application.getContextPath()%>/posts/<%= post.getId() %>">
        <div class="form-group">
            <label for="name">Name</label>
            <input type="text" class="form-control" id="name" name="name" required="true">
        </div>
        <div class="form-group">
            <label for="comment">Comment</label>
            <textarea type="text" rows="4" cols="50" class="form-control" id="comment" name="comment" required="true"></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Comment</button>
    </form>
    <br>
</div>


</body>
</html>

