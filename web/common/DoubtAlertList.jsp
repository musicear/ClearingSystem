<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/webwork" prefix="ww" %>


<html xmlns="http://www.w3.org/1999/xhtml" >
<head id="Head1"><meta http-equiv="Pragma" content="no-cache" /><title>
        
        Ԥ¾¯±랠    
        
    </title>
    <style type="text/css">.calendarControl{behavior: url(../JS/Calendar.htc)}</style>
    <link id="cssFile" type="text/css" rel="stylesheet" href="../Skins/Default/Css/common.css" />
    <link href="css/style.css" rel="stylesheet" type="text/css" />
    <script language="javascript" src="../JS/QueryList.js" type="text/javascript" charset="gb2312"></script>
    <base target="_self" />
</head>
<body  class="main">
<form name="formList" method="post" action="" id="formList">
    <div>
        <input type="hidden" name="__EVENTTARGET" id="__EVENTTARGET" value="" />
        <input type="hidden" name="__EVENTARGUMENT" id="__EVENTARGUMENT" value="" />
        <input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="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" />
    </div>
    
    <script type="text/javascript">
<!--
var theForm = document.forms['formList'];
if (!theForm) {
    theForm = document.formList;
}
function __doPostBack(eventTarget, eventArgument) {
    if (!theForm.onsubmit || (theForm.onsubmit() != false)) {
        theForm.__EVENTTARGET.value = eventTarget;
        theForm.__EVENTARGUMENT.value = eventArgument;
        theForm.submit();
    }
}
// -->
    </script>
    
    
    <table border="0" width="100%" class="title">
        <tr>
            <td>
                <span id="lblTitle">¿ʒɷ׀ᔤ¾¯±뺯span>
            </td>
            <td style="display:none">
                <input name="txtSelectedRowsIndex" type="text" id="txtSelectedRowsIndex" />
            </td>
        </tr>
    </table>
    <table id="tblQuery" width="100%" border="0" class="searchblock">
        <tr>
            <td align="left" id="tdUserControl">
                
                <table width="776" >
                    <tr >
                        <td width="200">Ԥ¾¯؜˽:2000  </td>
                        <td width="200">±¾ɕтնԤ¾¯:20</td>
                        <td width="200">δ´¦mԤ¾¯:20</td>
                    </tr>
                </table>
                
            </td>
        </tr>
    </table>
    <table id="tblQuery" width="100%" border="0" class="searchblock">
            <tr>
                <td id="tdUserControl" align="left">
                    
                    <table>
                        <tr>
                            <th>Ԥ¾¯¿ªʼʱ¼䣺</th>
                            <td> <input name="fromDate" type="text"/>  </td>
                            <th>Ԥ¾¯½ٖ¹ʱ¼䣺</th>
                            <td> <input name="toDate" type="text"/>  </td>
                        </tr>
                        <tr>
                            <th><div align="right">½»ӗ½𷮣º</div></th>
                            <td><input type="text" name="cnt"/>
                            <th><div align="right">״̬£º</div></th>
                            <td><select name="status">
                                    <option value="5" selected>ȫ²¿</option>
                                    <option value="0">δ´¦m</option>
                                    <option value="1">µ�μ/option>
                                    <option value="2">°¸}</option>
                                    <option value="3">ƅ³�ion>
                            </select>                                </td>
                        </tr>
                        
                        <tr>
                            <th><div align="right">ƅѲ£º</div></th>
                            <td><input name="orderBy" type="radio" />ʽѲ<input name="ctl06$Bcdname" type="radio"  />½µѲ </td>
                            <th><div align="right">Ԥ¾¯¶Տ󣸼/div></th>
                            <td><input name="custNo" type="text" id="custNo" /> </td>
                        </tr>
                    </table>
                    
                    
                </td>
                <td style="width:30%" align="left">
                    <input type="submit" name="btnQuery" value="²硑¯" id="btnQuery" class="input_button" />
                    <input type="submit" name="btnCancel" value="ȡ л" onclick="resetAll(this.form);" id="btnCancel" class="input_button" />
                </td>
            </tr>
        </table>
            
     <table id="tblTools" width="100%" border="0" class="toolbar">
            <tr>
                <td width="9%" align="left">          
                    
                    
                    <input type="button" name="btnDes" value="тնԤ¾¯" onClick="location.href='ToAddAlert.action'" id="btnDes" class="input_button" />
                    
                    
                </td>
                <td width="91%" align="left">          
                    
                    
                    <input type="button" name="btnDes" value="ɾ³� onClick="location.href='RemoveAlert.action'" id="btnDes" class="input_button" />
                    
                    
                </td>
            </tr>
        </table> 
    
    
    <div id="divList">
        <div>
            <table class="list" cellspacing="0" rules="all" border="1" id="gdvList" style="border-collapse:collapse;">
                <tr class="rowheader">
                    <th align="center" scope="col" style="width:60px;"><div align="center">ѡ ձ</div></th>
                    <th align="center" scope="col" style="width:100px;"><div align="center">Ԥ¾¯Ѳºü/div></th>
                    <th align="center" scope="col" style="width:100px;"><div align="center">Ԥ¾¯·׀�div></th>
                    <th align="center" scope="col" style="width:150px;"><div align="center">Ԥ¾¯Ļ³ļ/div></th>
                    <th align="center" scope="col" style="width:60px;"><div align="center">Ԥ¾¯¶Տ񺮤iv></th>
                    <th align="center" scope="col" style="width:60px;"><div align="center">Ԥ¾¯µȼ¶</div></th>
                    <th align="center" scope="col" style="width:60px;"><div align="center">Ԥ¾¯·½ʽ</div></th>
                    <th align="center" scope="col" style="width:80px;"><div align="center">½»ӗ¼Ȃ¼˽</div></th>
                    <th align="center" scope="col" style="width:80px;"><div align="center">½»ӗ½𷬼/div></th>
                    <th align="center" scope="col" style="width:80px;"><div align="center">²�ǚ</div></th>
                    <th align="center" scope="col" style="width:60px;"><div align="center">Ԥ¾¯״̬</div></th>
                </tr>
                <ww:iterator value="allAlertList" >
                    <tr class="row1" onMouseOver="itemOver(this)" onMouseOut="itemOut(this)" onClick="setSelectedRowIndex(this,false)" onDblClick="dbClick(this)">				
                            <td><div align="center"><input align="center" type="checkbox" name="allAlertList" value="<ww:property value="alertNo" />"</div></td>
                            <td><div align="center"><a href=""><ww:property value="alertno" /></a></div></td>
                            <td><ww:property value="alerttype" /></td> 
                            <td><ww:property value="" /></td>
                            <td><div align="right"><ww:property value="custno" /></div></td>
                            <td><ww:property value="" /></td>
                            <td><ww:property value="alertway" /></td>
                            <td><div align="right"><ww:property value="cnt" /></div></td>
                            <td><div align="right"><ww:property value="amt" /></div></td>
                            <td><div align="center"><ww:property value="finddate" /></div></td>
                            <td><ww:property value="status" /></td>
                        </tr>
                </ww:iterator>		
            </table>
        </div>
    </div>
    <table width="100%" border="0" class="page">
        <tr>
            <td id="tdDataCount" align="right" style="color:blank;">¹²2͵</td>
            
            <td id="tdPageTips" style="WIDTH: 120px;" align="center" nowrap="nowrap">µ±ǰ1ҳ£¬¹²1ҳ</td>
            
            <td style="WIDTH: 30px;" align="center" nowrap>
                <a id="lkbFirstPage" disabled="disabled">˗ҳ</a>
            </td>
            <td style="WIDTH: 30px;" align="center" nowrap>
                <a id="lkbPreviousPage" disabled="disabled">ʏҳ</a>
            </td>
            <td style="WIDTH: 30px;" align="center" nowrap>
                <a id="lkbNextPage" href="javascript:__doPostBack('lkbNextPage','')">Ђҳ</a>
            </td>
            <td style="WIDTH: 30px" align="center" nowrap>
                <a id="lkbLastPage" href="javascript:__doPostBack('lkbLastPage','')">ĩҳ</a>
            </td>
            <td id="tdJumpText" style="WIDTH: 50px;" align="right" nowrap="nowrap">͸תׁ:</td>
            
            <td align="left" style="WIDTH: 20px"><input name="txtPageIndex" type="text" id="txtPageIndex" class="input_text" style="height:20px;width:20px;" /></td>
            <td id="tdPageText" style="WIDTH: 10px" nowrap="nowrap">ҳ</td>
            
            <td  style="WIDTH: 40px;" nowrap>
                <input type="submit" name="lkbGotoPage" value="͸ ת" onClick="regur=/^[0-9]*[1-9][0-9]*$/;txt=document.getElementById('txtPageIndex'); if(!regur.test(txt.value)){alert('ȫˤɫֽȷµŊ�);txt.value='';return false;} else {if(txt.value>296){alert('ȫˤɫ1£­296µŕ�£¡');txt.value='';return false;}};" id="lkbGotoPage" class="input_button" style="width:40px;" /> 
            </td>
        </tr>
    </table>
    <table id="tblSheets" width="100%" border="0" cellpadding="0" cellspacing="0" class="sheet">
        <tr>
            <td width="20px" class="space"></td>
            <td nowrap="nowrap"><a id="btnSheet0" href="AlertList.action?alerttype=all">̹ԐԤ¾¯</a></td>
            <td  nowrap="nowrap"><a id="btnSheet1" href="AlertList.action?alerttype=largeAlert">´󷮷׀ᔤ¾¯</a></td>
            <td class="on" nowrap="nowrap"><span>¿ʒɷ׀ᔤ¾¯</span></td>
            <td nowrap="nowrap"><a id="btnSheet2" href="AlertList.action?alerttype=manual">Ա¹¤·׀ᔤ¾¯</a></td>
            <td width="90%" class="space"></td>
            
            
            
            
            <td width="90%" class="space"></td> 
        </tr>
    </table>         
    <div> 
        <input type="hidden" name="__EVENTVALIDATION" id="__EVENTVALIDATION" value="/wEWFQKN4eDXDALehrivAgL97q3rAQKFyMWBDAKKp+9vAoun728CiKfvbwKJp+9vAo6n728Cj6fvbwKMp+9vAp2n728CkqfvbwKKp69sAu+OvL8FApD0z+sFAoyTgdgPArbhq80NAouTpcYFAouT8cYFAvPDy48P18IQkcgs2JcTGK4GFMVk7u+n4AA=" />
</div></form>
</body>
</html>