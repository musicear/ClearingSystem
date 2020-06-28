<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="resoft.common.domain.*" %>
<%@ taglib uri="/webwork" prefix="ww"%>
<html>
<head>
 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>实时扣税汇总查询</title>
<link href="../stylesheets/common.css" rel="stylesheet" type="text/css" />
<link  href="../stylesheets/calendar.css" rel="stylesheet" type="text/css" >
<script language="javascript" type="text/javascript" src="../javascripts/browserSniffer.js"></script>
<script language="javascript" type="text/javascript" src="../javascripts/Calendar.js"></script>
</head>
<body>
<form name="form1" method="post" >

 <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left" valign="top"  style="background:url(../images/right_bg.gif) bottom repeat-x"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="43" style="background:url(../images/title.gif) left no-repeat" class="title_zi">实时扣税汇总查询
		</td>
      </tr>
  </table>

      <table width="80%" border="0" cellpadding="0" cellspacing="0" class="list" style="margin-left:33px;margin-top:34px">
        <tr>
         <td align="left" width="50%">交易总比数：<ww:property value="total_Num" /></td>
        </tr> 
        
        <tr>
         <td align="left" width="50%"><ww:property value="money_list" />
        </tr> 
       </table>
      </td>
     </tr>
     
      </table>
	  </td>
  </tr>
</table>
</form>
</body>
</html>