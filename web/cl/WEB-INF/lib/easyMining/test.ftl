<html>
<body>
hello,${user.toString()}
<table>
<#list books as book>
<tr>
<#assign bookName = book.getName()>
<td>${bookName}</td>
</tr>
</#list>
</table>
</body>
</html>