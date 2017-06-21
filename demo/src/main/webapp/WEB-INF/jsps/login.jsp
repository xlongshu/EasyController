<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <title>登录</title>
</head>
<body>
<div class="container">
    <br/>
    <p style="color: #FF0000">${msg}</p>
    <form action="${base}/test/login" method="post">
        <div class="form-group">
            <label for="username">用户名:</label>
            <input type="text" class="form-control" id="username" name="username" placeholder="Username"/>
        </div>
        <div class="form-group">
            <label for="password">密码:</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="Password">
        </div>
        <div class="form-group">
            <label for="captcha">验证码:</label>
            <input type="text" class="form-control" id="captcha" name="captcha" placeholder="captcha">
            <img src="${base}/captcha"/>
        </div>
        <button type="submit" class="btn btn-default">登录</button>
    </form>
</div>
</body>
</html>
