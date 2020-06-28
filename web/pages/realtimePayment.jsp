
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
        <td height="43" style="background:url(../images/title.gif) left no-repeat" class="title_zi">实时扣税查询 </td>
      </tr>
    </table>
      <table width="80%" border="0" cellpadding="0" cellspacing="0" class="list" style="margin-left:33px;margin-top:34px">
        

        <tr>
          <th>流水号</th>
          <td align="left">
                        <input name="column1" type="hidden" value="001">
                        <input name="operator1" type="hidden" value="equals" >
                        <input name="value1" type="text" id="Id" maxlength="40" >
                    </td>
          <td align="center">&nbsp;</td>
        </tr> 
        
       

        
        
        <tr>
          <th>票税号码</th>
          <td align="left">
                        <input name="column3" type="hidden" value="010">
                        <input name="operator3" type="hidden" value="equals" >
                        <input name="value3" type="text" id="Id" maxlength="40" >
                    </td>
          <td align="center">&nbsp;</td>
        </tr> 
        <tr>
          <th>是否冲正</th>
          
                        <input name="column9" type="hidden" value="011">
                        <input name="operator9" type="hidden" value="equals" >
                        <td bgcolor="#FFFFFF">	  
                         <select name="value9">
                         <option value="">全部</option>
                  <option  value="Y">已冲正</option>
                  <option  value="N">未冲正</option>
                </select></td>
          
                    
          <td align="center">&nbsp;</td>
        </tr> 
        
        
        <tr>
          <th>对账状态</th>
          
                        <input name="column10" type="hidden" value="015">
                        <input name="operator10" type="hidden" value="equals" >
                        <td bgcolor="#FFFFFF">	  
                         <select name="value10">
                         <option value="">全部</option>
                  <option  value="0">未对账</option>
                  <option  value="1">对账成功</option>
                  <option  value="3">需进行账务核对</option>
                  <option  value="4">已冲正</option>
                </select></td>
          
                    
          <td align="center">&nbsp;</td>
        </tr>
        
        <tr>
          <th>Tips工作日期</th>
          <td align="left">
	      <input name="column4" type="hidden" value="016">
          <input name="operator4" type="hidden" value="greaterorequals" >
	      <input name="value4" type="text"  value="" readonly >
            <script language="JavaScript" type="text/javascript">
           
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
                document.forms['form1'].value4.value = year +  month +  date;               
            }
			dynCalendar_setImagesPath("../images/");
            calendar1 = new dynCalendar('calendar1', 'entrustDate_callback');
            
            calendar1.setMonthCombo(true);
            calendar1.setYearCombo(true);
         
            </script>
    
      </td>
        <td>&nbsp;</td>
      <th> 至 </th>
        
       
          <td align="left">
	      <input name="column8" type="hidden" value="016">
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
          <th>开户行行号</th>
          <td align="left">
                        <input name="column6" type="hidden" value="007">
                        <input name="operator6" type="hidden" value="equals" >
                        <input name="value6" type="text" id="Id" maxlength="40" >
                    </td>
          <td align="center">&nbsp;</td>
        </tr> 
        
        <tr>
          <th>扣款结果</th>
          <td align="left">
                        <input name="column7" type="hidden" value="013">
                        <input name="operator7" type="hidden" value="equals" >
                        <input name="value7" type="text" id="Id" maxlength="40" >
                    </td>
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
