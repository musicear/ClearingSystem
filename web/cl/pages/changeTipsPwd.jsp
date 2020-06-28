
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>无标题文档</title>
<link href="../stylesheets/common.css" rel="stylesheet" type="text/css" />
		<link  href="../stylesheets/calendar.css" rel="stylesheet" type="text/css" >
		<script language="javascript" type="text/javascript" src="../javascripts/browserSniffer.js"></script>
		<script language="javascript" type="text/javascript" src="../javascripts/Calendar.js"></script>
</head>
<script language=javascript>
    function go() {
        if (frm.oldPassword.value == "") {
            alert("请输入原密码!");
            return false;
        }
        if (frm.newPassword.value == "") {
            alert("请输入新密码!");
            return false;
        }
	  else if(frm.newPassword.value.length<8)
	{
			 alert("密码长度不能小于8位!");
            return false;
		}
        if (frm.newPassword.value != frm.confirmPassword.value) {
            alert("确认密码与新密码不符!");
            return false;
        }
        return true;
    }
</script>
<body>
 <form name="frm" method="post" action="../action/ChangeTipsPassword.action" onsubmit="return(go());">
 <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left" valign="top"  style="background:url(../images/right_bg.gif) bottom repeat-x"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="43" style="background:url(../images/title.gif) left no-repeat" class="title_zi">修改登录TIPS密码 </td>
      </tr>
    </table>
      <table width="75%" border="0" cellpadding="0" cellspacing="0" class="list" style="margin-left:33px;margin-top:34px">
        <tr>
          <th >原始密码</th>
            <td  align="left">
                <input name="oldPassword" type="password" id="oldPassword" maxlength="32" >
            </td>
          <td  align="left">*&nbsp;</td>
        </tr>

        <tr>
          <th>新 密 码</th>
          <td align="left">
                     <input name="newPassword" type="password" id="newPassword" maxlength="32">
                    </td>
          <td>*&nbsp;</td>
        </tr>
		        <tr>
          <th>确 认 密 码</th>
          <td align="left">
	      <input name="confirmPassword" type="password" id="confirmPassword" value="" maxlength="32">
    
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
