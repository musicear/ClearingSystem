<%@ page contentType="text/html; charset=gb2312" %>
<html>
    <head><title>
            µ«¬º
    </title><link id="cssFile" rel="stylesheet" type="text/css" href="stylesheets/login.css" /></head>
    <script language="javascript">

    function init ( )
    {
       form1.userCode.focus();
       return true;
    }
    function enter() {
            if (event.keyCode == 13) {
                var elementName = document.activeElement.name;

                if (elementName == "userCode") {
                    form1.password.focus();
                }
                if (elementName == "password") {
                    form1.submit();
                }
            }
        }
    </script>
    <body onLoad="return init();">
    <body>
	        <%
        session.removeAttribute("user");
        %>
        <form method="POST" action="action/auth.action" name="form1"> 
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td bgcolor="#FFFFFF">&nbsp;</td>
  </tr>
  <tr>
    <td height="592" background="images/bj.gif">
	
	<table width="606" height="348" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td background="images/tupian.jpg"><div id="zi">”√ªß’ ∫≈£∫
	  <input type="text" name="userCode" id="userCode" onKeyPress="javascript:enter();" size="15">&nbsp;&nbsp;&nbsp; ‰»Î√‹¬Î£∫<input type="password" name="password" id="password" size="15" onKeyPress="javascript:enter();">&nbsp;&nbsp;
	  </div>
	  <div id="position"><input name="submit" type="image" src="images/anniu.gif" border="0" onclick="javascript:document.form1.submit();"/>
	  </div>
	  </td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td bgcolor="#72a4c5">&nbsp;</td>
  </tr>
</table>
</form>
</body>
</html>
