<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <title>Test</title>
</head>
<body>
<div class="container">
    <h1>TestController</h1>
    <p>loginUser: ${loginUser}</p>
    <a href="${base}/test/userList">用户列表</a>
    <hr/>
    <ul>
        <li><a href="${base}/test/redirect" target="_blank">redirect</a></li>
        <li><a href="${base}/test/forward" target="_blank">forward</a></li>
        <li><a href="${base}/test/text" target="_blank">text</a></li>
        <li><a href="${base}/test/js" target="_blank">js</a></li>
        <li><a href="${base}/test/json" target="_blank">json</a></li>
        <li><a href="${base}/test/json2" target="_blank">json2</a></li>
        <li><a href="${base}/test/file" target="_blank">file</a></li>
        <li><a href="${base}/test/file2" target="_blank">file2</a></li>
        <li><a href="${base}/test/freemarker" target="_blank">freemarker</a></li>
        <li><a href="${base}/test/velocity" target="_blank">velocity</a></li>
    </ul>
</div>
<hr/>
</body>
</html>
