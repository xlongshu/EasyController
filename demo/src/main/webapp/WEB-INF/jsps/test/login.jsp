<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <title>登录</title>
</head>
<body>
<form action="login" method="post">
    name:<input type="text" name="username" value="username"/>
    <br/>
    password:<input type="password" name="password" value="password"/>
    <br/>
    <input type="submit" value="登录"/>
</form>
</body>
</html>
