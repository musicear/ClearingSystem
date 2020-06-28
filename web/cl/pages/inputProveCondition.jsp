
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>无标题文档</title>
<link href="../stylesheets/common.css" rel="stylesheet" type="text/css" />
		<link  href="../stylesheets/calendar.css" rel="stylesheet" type="text/css" >
</head>
<script language=javascript>
    function go() {
        if (frm.TaxPayCode.value == "") {
            alert("纳税人编号不能为空！");
            document.frm.TaxPayCode.focus();
            return false;
        }
    }
</script>
<body>
 <form name="frm" method="post"  action="../action/ShowProveInfo.action" onsubmit="return(go());">
 <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left" valign="top"  style="background:url(../images/right_bg.gif) bottom repeat-x"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="43" style="background:url(../images/title.gif) left no-repeat" class="title_zi">协议删除 </td>
      </tr>
    </table>
      <table width="75%" border="0" cellpadding="0" cellspacing="0" class="list" style="margin-left:33px;margin-top:34px">
	<tr>
          <th>付款单位账号</th>
          <td align="left">
	      <input name="AcctNo" type="text" id="AcctNo"  maxlength="30">
    
      </td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <th >纳税人编号</th>
            <td  align="left">
                <input name="TaxPayCode" type="text" id="TaxPayCode" maxlength="30" >
            </td>
          <td  align="left">&nbsp;</td>
        </tr>
		 <tr>
          <th>协议书号</th>
          <td align="left">
	      <input name="ProtocolNo" type="text" id="ProtocolNo" maxlength="30">
    
      </td>
          <td>&nbsp;</td>
        </tr>
      </table>
    <p>
      <input type="submit" name="Submit" value=" 确 定 " class="submit" style="margin-left:34px" >
      <input type="reset" name="Submit22" value=" 重 置 " class="submit"> 
	    <input type="button" name="Submit22" value=" 返 回 " class="submit" onClick="javascript:self.location.href='../common/welcome.jsp'" >
    </p></td>
  </tr>
</table>
</form>
</body>
</html>
