<%@ page language="java" contentType="text/html; charset=GB2312"
	pageEncoding="UTF-8"%>

<%@ page import="resoft.common.domain.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>无标题文档</title>
<link href="../stylesheets/common.css" rel="stylesheet" type="text/css" />
		<link  href="../stylesheets/calendar.css" rel="stylesheet" type="text/css" >
		<script language="javascript" type="text/javascript" src="../javascripts/browserSniffer.js"></script>
		<script language="javascript" type="text/javascript" src="../javascripts/Calendar.js"></script>
</head>
<script>
function checkingData(){
	if(document.all("newPwd").value != document.all("confirmPwd").value){
		alert("新密码和确认密码不相同");
		return;
	}
	if(document.all("newPwd").value.length <6){
		alert("长度不得少于6位");
		return;
	}
	var errorMsg = "必须包含";
	var newPwd = document.all("newPwd").value;

	document.all("modifyPwdForm").submit();
}
function SearchDemo(str,strategy)  
   {  
   var r=str.search(strategy); // 查找字符串。  
   return(r); // 返回 Boolean 结果。  
   }
function whenClose() {
	window.opener.document.location.reload();
}
</script>
<body>
   <form action="../action/ChangePasswd.action" method="post" name="modifyPwdForm">
 <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left" valign="top"  style="background:url(../images/right_bg.gif) bottom repeat-x"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="43" style="background:url(../images/title.gif) left no-repeat" class="title_zi">修改登录密码 </td>
      </tr>
    </table>
      <table width="75%" border="0" cellpadding="0" cellspacing="0" class="list" style="margin-left:33px;margin-top:34px">
        <tr>
          <th >原始密码</th>
            <td  align="left">
                <input name="oldPwd" type="password" id="oldPwd" maxlength="32" >
            </td>
          <td  align="left">*&nbsp;</td>
        </tr>

        <tr>
          <th>新 密 码</th>
          <td align="left">
                     <input name="newPwd" type="password" id="newPwd" maxlength="32">
                    </td>
          <td>*&nbsp;</td>
        </tr>
		        <tr>
          <th>确 认 密 码</th>
          <td align="left">
	      <input name="confirmPwd" type="password" id="confirmPwd"  maxlength="32">
    
      </td>
          <td>*&nbsp;</td>
        </tr>
      </table>
    <p>
      <input type="submit" name="Submit3" value=" 确 定 " class="submit" style="margin-left:34px" >
    </p></td>
  </tr>
</table>
</form>
</body>
</html>

