<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/webwork" prefix="ww" %>
<html xmlns="http://www.w3.org/1999/xhtml" >
<head id="Head1"><meta http-equiv="Pragma" content="no-cache" /><title>
        
        ??ˉ?±?    
        
    </title>
    <style type="text/css">.calendarControl{behavior: url(../JS/Calendar.htc)}</style>
    <link id="cssFile" type="text/css" rel="stylesheet" href="../Skins/Default/Css/common.css" />
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
                <span id="lblTitle">′?????ˉ?±?span>
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
                        <td width="200">??ˉ??:2000  </td>
                        <td width="200">±??т???ˉ:20</td>
                        <td width="200">δ′|m??ˉ:20</td>
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
                            <th>??ˉ?a????</th>
                            <td> <input name="fromDate" type="text"/>  </td>
                            <th>??ˉ??1???</th>
                            <td> <input name="toDate" type="text"/>  </td>
                        </tr>
                        <tr>
                            <th><div align="right">??????o</div></th>
                            <td><input type="text" name="cnt"/>
                            <th><div align="right">??￡o</div></th>
                            <td><select name="status">
                                    <option value="5" selected>?2?</option>
                                    <option value="0">δ′|m</option>
                                    <option value="1">μ?μ/option>
                                    <option value="2">°?}</option>
                                    <option value="3">?3?ion>
                            </select>                                </td>
                        </tr>
                        
                        <tr>
                            <th><div align="right">??￡o</div></th>
                            <td><input name="orderBy" type="radio" />??<input name="ctl06$Bcdname" type="radio"  />?μ? </td>
                            <th><div align="right">??ˉ????/div></th>
                            <td><input name="custNo" type="text" id="custNo" /> </td>
                        </tr>
                    </table>
                    
                    
                </td>
                <td style="width:30%" align="left">
                    <input type="submit" name="btnQuery" value="2硑ˉ" id="btnQuery" class="input_button" />
                    <input type="submit" name="btnCancel" value="? л" onclick="resetAll(this.form);" id="btnCancel" class="input_button" />
                </td>
            </tr>
        </table>
    
    
     <table id="tblTools" width="100%" border="0" class="toolbar">
            <tr>
                <td width="9%" align="left">          
                    
                    
                    <input type="button" name="btnDes" value="т???ˉ" onClick="location.href='ToAddAlert.action'" id="btnDes" class="input_button" />
                    
                    
                </td>
                <td width="91%" align="left">          
                    
                    
                    <input type="button" name="btnDes" value="?3? onClick="location.href='RemoveAlert.action'" id="btnDes" class="input_button" />
                    
                    
                </td>
            </tr>
        </table> 
    
    <div id="divList">
        <div>
            <table class="list" cellspacing="0" rules="all" border="1" id="gdvList" style="border-collapse:collapse;">
                <tr class="rowheader">
                    <th align="center" scope="col" style="width:60px;"><div align="center">? ?</div></th>
                    <th align="center" scope="col" style="width:100px;"><div align="center">??ˉ?oü/div></th>
                    <th align="center" scope="col" style="width:100px;"><div align="center">??ˉ·??div></th>
                    <th align="center" scope="col" style="width:150px;"><div align="center">??ˉ?3?/div></th>
                    <th align="center" scope="col" style="width:60px;"><div align="center">??ˉ????iv></th>
                    <th align="center" scope="col" style="width:60px;"><div align="center">??ˉμ??</div></th>
                    <th align="center" scope="col" style="width:60px;"><div align="center">??ˉ·??</div></th>
                    <th align="center" scope="col" style="width:80px;"><div align="center">???????</div></th>
                    <th align="center" scope="col" style="width:80px;"><div align="center">??????/div></th>
                    <th align="center" scope="col" style="width:80px;"><div align="center">2?ǚ</div></th>
                    <th align="center" scope="col" style="width:60px;"><div align="center">??ˉ??</div></th>
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
            <td id="tdDataCount" align="right" style="color:blank;">122?</td>
            
            <td id="tdPageTips" style="WIDTH: 120px;" align="center" nowrap="nowrap">μ±?1?￡?121?</td>
            
            <td style="WIDTH: 30px;" align="center" nowrap>
                <a id="lkbFirstPage" disabled="disabled">??</a>
            </td>
            <td style="WIDTH: 30px;" align="center" nowrap>
                <a id="lkbPreviousPage" disabled="disabled">??</a>
            </td>
            <td style="WIDTH: 30px;" align="center" nowrap>
                <a id="lkbNextPage" href="javascript:__doPostBack('lkbNextPage','')">??</a>
            </td>
            <td style="WIDTH: 30px" align="center" nowrap>
                <a id="lkbLastPage" href="javascript:__doPostBack('lkbLastPage','')">??</a>
            </td>
            <td id="tdJumpText" style="WIDTH: 50px;" align="right" nowrap="nowrap">???:</td>
            
            <td align="left" style="WIDTH: 20px"><input name="txtPageIndex" type="text" id="txtPageIndex" class="input_text" style="height:20px;width:20px;" /></td>
            <td id="tdPageText" style="WIDTH: 10px" nowrap="nowrap">?</td>
            
            <td  style="WIDTH: 40px;" nowrap>
                <input type="submit" name="lkbGotoPage" value="? ?" onclick="regur=/^[0-9]*[1-9][0-9]*$/;txt=document.getElementById('txtPageIndex'); if(!regur.test(txt.value)){alert('?????μ??);txt.value='';return false;} else {if(txt.value>296){alert('???1￡-296μ??￡?');txt.value='';return false;}};" id="lkbGotoPage" class="input_button" style="width:40px;" /> 
            </td>
        </tr>
    </table>
    <table id="tblSheets" width="100%" border="0" cellpadding="0" cellspacing="0" class="sheet">
        <tr>
            <td width="20px" class="space"></td>
            <td nowrap="nowrap"><a id="btnSheet0" href="AlertList.action?alerttype=all">????ˉ</a></td>
            <td class="on" nowrap="nowrap"><span>′?????ˉ</span></td>
            <td nowrap="nowrap"><a id="btnSheet1" href="AlertList.action?alerttype=doubtAlert">??????ˉ</a></td>
            <td nowrap="nowrap"><a id="btnSheet2" href="AlertList.action?alerttype=manual">?1¤·???ˉ</a></td>
            <td width="90%" class="space"></td> 
        </tr>
    </table>
    <div>
        
        <input type="hidden" name="__EVENTVALIDATION" id="__EVENTVALIDATION" value="/wEWFQKN4eDXDALehrivAgL97q3rAQKFyMWBDAKKp+9vAoun728CiKfvbwKJp+9vAo6n728Cj6fvbwKMp+9vAp2n728CkqfvbwKKp69sAu+OvL8FApD0z+sFAoyTgdgPArbhq80NAouTpcYFAouT8cYFAvPDy48P18IQkcgs2JcTGK4GFMVk7u+n4AA=" />
</div></form>
</body>
</html>