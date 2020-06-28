
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
        if (frm.PayAcct.value == "") {
            alert("账号不能为空！");
            document.frm.PayAcct.focus();
            return false;
        }
        if (frm.TaxVouNo.value == "") {
            alert("票税号码不能为空！");
            document.frm.TaxVouNo.focus();
            return false;
        }
        if (frm.TaxPayCode.value == "") {
            alert("纳税人编号不能为空！");
            document.frm.TaxPayCode.focus();
            return false;
        }
    }
    
    
</script>

<body>
 <form name="frm" method="post"  action="../action/ShowVoucherInfo.action" onsubmit="return(go());">
 <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left" valign="top"  style="background:url(../images/right_bg.gif) bottom repeat-x"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="43" style="background:url(../images/title.gif) left no-repeat" class="title_zi">付款打印回单 </td>
      </tr>
    </table>
      <table width="75%" border="0" cellpadding="0" cellspacing="0" class="list" style="margin-left:33px;margin-top:34px">
      
      	 <tr>
          <th>账号</th>
          <td align="left">
	      <input name="PayAcct" type="text" id="PayAcct" value="" maxlength="30">
   			</td>
          <td>&nbsp;</td>
        </tr>
        
        <tr>
          <th>票税号码</th>
          <td align="left">
	      <input name="TaxVouNo" type="text" id="TaxVouNo" value="" maxlength="30">
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

        
		
		 
		 
      </table>
    <p>
      <input type="submit"  value=" 确 定 " class="submit" style="margin-left:34px" >
	    <input type="button"  value=" 返 回 " class="submit" onClick="javascript:self.location.href='../common/welcome.jsp'" >
    </p></td>
  </tr>
</table>
</form>
</body>
</html>
