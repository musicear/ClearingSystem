
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
        <td height="43" style="background:url(../images/title.gif) left no-repeat" class="title_zi">银行端缴款查询 </td>
      </tr>
    </table>
      <table width="75%" border="0" cellpadding="0" cellspacing="0" class="list" style="margin-left:33px;margin-top:34px">
        

        <tr>
          <th>流水号</th>
          <td align="left">
                        <input name="column1" type="hidden" value="001">
                        <input name="operator1" type="hidden" value="equals" >
                        <input name="value1" type="text" id="Id" maxlength="18" >
                    </td>
          <td align="center">&nbsp;</td>
        </tr> 
        
       <tr>
          <th width="30%">征收机关</th>
		 <input type="hidden" name="column2" value="002">
       <input name="operator2" type="hidden" value="equals" >
			<td align="left">				
            <select name="value2" >
			 <option value="">全部</option>
				 <ww:iterator  value="TaxOrgList">
                  <option value="<ww:property value="value"/>"><ww:property value="value"/></option>
				 </ww:iterator>
           </select>
		   </td>	
          <td>&nbsp;</td>
        </tr>

        
        
        <tr>
          <th>票税号码</th>
          <td align="left">
                        <input name="column3" type="hidden" value="008">
                        <input name="operator3" type="hidden" value="equals" >
                        <input name="value3" type="text" id="Id" maxlength="20" >
                    </td>
          <td align="center">&nbsp;</td>
        </tr> 
        
        
        <tr>
          <th>Tips工作日期</th>
          <td align="left">
	      <input name="column4" type="hidden" value="003">
          <input name="operator4" type="hidden" value="equals" >
	      <input name="value4" type="text"  value="" readonly>
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
        </tr>
        
        
        <tr>
          <th>付款人账号</th>
          <td align="left">
                        <input name="column5" type="hidden" value="004">
                        <input name="operator5" type="hidden" value="equals" >
                        <input name="value5" type="text" id="Id" maxlength="18" >
                    </td>
          <td align="center">&nbsp;</td>
        </tr> 
        
        
        <tr>
          <th>开户行行号</th>
          <td align="left">
                        <input name="column6" type="hidden" value="005">
                        <input name="operator6" type="hidden" value="equals" >
                        <input name="value6" type="text" id="Id" maxlength="18" >
                    </td>
          <td align="center">&nbsp;</td>
        </tr> 
        
        <tr>
          <th>扣款结果</th>
          <td align="left">
                        <input name="column7" type="hidden" value="011">
                        <input name="operator7" type="hidden" value="equals" >
                        <input name="value7" type="text" id="Id" maxlength="18" >
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
