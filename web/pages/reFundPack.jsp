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
		<script language="javascript" type="text/javascript" src="../javascripts/Calendar.js"></script>
</head>

<body>
 <form name="form1" method="post" action="../dofile/ProcessCondition.do">
 <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left" valign="top"  style="background:url(../images/right_bg.gif) bottom repeat-x"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="43" style="background:url(../images/title.gif) left no-repeat" class="title_zi">退款申请包 </td>
      </tr>
    </table>
      <table width="75%" border="0" cellpadding="0" cellspacing="0" class="list" style="margin-left:33px;margin-top:34px">
        
        <tr>
          <th>包流水号</th>
          <td align="left">
                        <input name="column2" type="hidden" value="002">
                        <input name="operator2" type="hidden" value="equals" >
                        <input name="value2" type="text" id="Id" maxlength="18" >
                    </td>
          <td align="center">&nbsp;</td>
        </tr>
		        <tr>
          <th>委托日期</th>
          <td align="left">
	      <input name="column3" type="hidden" value="001">
          <input name="operator3" type="hidden" value="greaterorequals" >
	      <input name="value3" type="text"  value="" readonly>
            <script language="JavaScript" type="text/javascript">
            <!--
            /**
            * Example callback function
            */
            function entrustDate_callback(date, month, year)
            {
                if (String(month).length == 1) {
                    month = '0' + month;
                }
                if (String(month).length == 2) {
                    month = '' + month;
                }
                
                if (String(date).length == 1) {
                    date = '0' + date;
                }
                if (String(date).length == 2) {
                    date = '' + date;
                }
                document.forms['form1'].value3.value = year +  month +  date;               
            }
			dynCalendar_setImagesPath("../images/");
            calendar1 = new dynCalendar('calendar1', 'entrustDate_callback');
            
            calendar1.setMonthCombo(true);
            calendar1.setYearCombo(true);
            //-->
            </script>
    
      </td>
          <td>&nbsp;</td>
          
           <td align="left">
	      <input name="column8" type="hidden" value="001">
          <input name="operator8" type="hidden" value="lessorequals" >
	      <input name="value8" type="text"  value="" readonly>
            <script language="JavaScript" type="text/javascript">
           
            function entrustDate_callback1(date, month, year)
            {
                if (String(month).length == 1) {
                    month = '0' + month;
                }
                if (String(month).length == 2) {
                    month = '' + month;
                }
                
                if (String(date).length == 1) {
                    date = '0' + date;
                }
                if (String(date).length == 2) {
                    date = '' + date;
                }
                document.forms['form1'].value8.value = year +  month +  date;               
            }
			dynCalendar_setImagesPath("../images/");
            calendar2 = new dynCalendar('calendar2', 'entrustDate_callback1');
            
            calendar2.setMonthCombo(true);
            calendar2.setYearCombo(true);
         
            </script>
    
      </td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <th>申请状态</th>
          
                        <input name="column10" type="hidden" value="008">
                        <input name="operator10" type="hidden" value="equals" >
                        <td bgcolor="#FFFFFF">	  
                         <select name="value10">
                         <option value="">全部</option>
                  <option  value="2">已入库</option>
                  <option  value="3">已发送</option>
                  <option  value="4">已收到回执</option>
                </select></td>
          
                    
          <td align="center">&nbsp;</td>
        </tr>
      </table>
    <p>
      <input type="submit" name="Submit3" value=" 确 定 " class="submit" style="margin-left:34px" >
      <input type="button" name="Submit22" value=" 取 消 " class="submit" onClick="javascript:self.location.href='../common/welcome.jsp'" >
    </p></td>
  </tr>
</table>
</form>
</body>
</html>
