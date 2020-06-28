
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="resoft.common.domain.*" %>
<%@ taglib uri="/webwork" prefix="ww"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>无标题文档</title>
<link href="../stylesheets/common.css" rel="stylesheet" type="text/css" />
		<link  href="../stylesheets/calendar.css" rel="stylesheet" type="text/css" >
		<script language="javascript" type="text/javascript" src="../javascripts/browserSniffer.js"></script>
		
</head>

<script language=javascript>
    function check() {
      
        if (form1.content.value == "") {
            alert("请输入内容");
            return false;
        }
       
        return true;
    }
</script>

<body>
 <form name="form1" method="post" action="../action/SendFreeMessage.action"  onsubmit="return(check())";>
 <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left" valign="top"  style="background:url(../images/right_bg.gif) bottom repeat-x"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="43" style="background:url(../images/title.gif) left no-repeat" class="title_zi">发送自由格式报文</td>
      </tr>
    </table>
      <table width="80%" border="0" cellpadding="0" cellspacing="0" class="list" style="margin-left:33px;margin-top:34px">
        <tr>
          <th width="30%">接收机构</th>
		  
		 
			<td align="left">				
            <select name="srcNodeCode">
			 
				 <ww:iterator  value="NodeMngList">
                  <option   value="<ww:property value="key"/>"><ww:property value="value"/></option>
				 </ww:iterator>
           </select>
		   </td>
        </tr>

		<tr>
          <th width="30%">内容</th>
		 <td><textarea name="content" cols="36" rows="4" id="REMARK" value=""></textarea></td>
        </tr>

      
      </table>
    <p>
      <input type="submit"  value=" 确 定 " class="submit" style="margin-left:34px" >
      <input type="button"  value=" 取 消 " class="submit" onClick="javascript:self.location.href='../common/welcome.jsp'" >
    </p></td>
  </tr>
</table>
</form>
</body>
</html>
