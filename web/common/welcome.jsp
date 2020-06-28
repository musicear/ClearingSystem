<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>
welcome
</title>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">

<script language="VBScript">
	dim hkey_root,hkey_path,hkey_key
	hkey_root="HKEY_CURRENT_USER"
	hkey_path="\Software\Microsoft\Internet Explorer\PageSetup"
	'//设置网页打印的页眉页脚为空
	function pagesetup_null()
		on error resume next
		Set RegWsh = CreateObject("WScript.Shell")
		hkey_key="\header" 
		RegWsh.RegWrite hkey_root+hkey_path+hkey_key,""
		hkey_key="\footer"
		RegWsh.RegWrite hkey_root+hkey_path+hkey_key,""
	end function
	'//设置网页打印的页眉页脚为默认值
	function pagesetup_default()
		on error resume next
		Set RegWsh = CreateObject("WScript.Shell")
		hkey_key="\header" 
		RegWsh.RegWrite hkey_root+hkey_path+hkey_key,"&w&b页码，&p/&P"
		hkey_key="\footer"
		RegWsh.RegWrite hkey_root+hkey_path+hkey_key,"&u&b&d"
	end function
</script>
</head>
<body onload="pagesetup_null()" style="background:url(../images/welcome.jpg) no-repeat center center">

</body>
</html>
