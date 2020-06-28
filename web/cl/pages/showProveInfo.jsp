
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="resoft.common.domain.*" %>
<%@ taglib uri="/webwork" prefix="ww"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>无标题文档</title>
<link href="../stylesheets/common.css" rel="stylesheet" type="text/css" />
</head>

<body>
 <form name="frm" method="post"  action="../action/DelProveInfo.action" >
 <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left" valign="top"  style="background:url(../images/right_bg.gif) bottom repeat-x"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="43" style="background:url(../images/title.gif) left no-repeat" class="title_zi">协议删除</td>
      </tr>
    </table>
      <table width="80%" border="0" cellpadding="0" cellspacing="0" class="list" style="margin-left:33px;margin-top:34px">
	    <tr>
          <th>付款单位账号</th>
          <td align="left"><input type="text" readonly name="AcctNo" value="<ww:property  value="AcctNo" />"></td>
          <td align="center">&nbsp;</td>
        </tr>
	    <tr>
          <th>纳税人编码</th>
          <td align="left"><input type="text" readonly name="TaxPayCode" value="<ww:property  value="TaxPayCode" />"></td>
          <td align="center">&nbsp;</td>
        </tr>
	    <tr>
          <th>征收机关名称</th>
          <td align="left"><input type="text" readonly name="TaxOrgName" value="<ww:property  value="TaxOrgName" />"></td>
          <td align="center">&nbsp;</td>
        </tr>
		<tr>
          <th>协议书号</th>
          <td align="left"><input type="text" readonly name="ProtocolNo" value="<ww:property  value="ProtocolNo" />"> </td>
          <td align="center">&nbsp;</td>
        </tr>
		 <tr>
          <th>付款单位名称</th>
          <td align="left"><input type="text" readonly  name="PayAcctName" value="<ww:property  value="PayAcctName" />"></td>
          <td align="center">&nbsp;</td>
        </tr>

      
      </table>
	     <p>
      <input type="submit" name="Submit" value=" 删除 " class="submit" style="margin-left:34px" >
    </p> </td>
  </tr>
</table>
</form>
</body>
</html>
