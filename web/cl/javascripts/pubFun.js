<!--
function either( obj1,obj2,msg)
{
	var flag1 = false;
	var flag2 = false;
	if ( obj1.value != "" ) 
	{  
	   if ( !isNumber(obj1,"请输入数字") ) return false;
	   flag1 = true;
	}
	if ( obj2.value != "" ) 
	{  
	   if ( !isNumber(obj2,"请输入数字") ) return false;
	   flag2 = true;
	}
	if ( flag1 == flag2 ) 
	{
           alert( msg);
           return false;
        }
	return true;
			
	
} 
function isNull( obj,msg )
{
	switch( isType( obj ) ){
		case 1:
			if (  obj.value == "" ){
				alert( msg);
				return true;
			}
			return false;
		case 2:
			for ( i = 0; i < obj.length; i++ ){
				if( obj[i].checked == true ) return false;
			}
			alert( msg);	
			return true;
		case 3:
			for ( i = 0; i < obj.length; i++ ){
				if( obj[i].checked == true ) return false;
			}
			alert( msg);	
			return true;
		case 4:
			if( obj.checked == true ) return false;
			alert( msg );	
			return true;
		case 5:
			if( obj.checked == true ) return false;
			alert( msg );	
			return true;
		case 6:
		        if (  obj.value == "" ){
				alert( msg);
				return true;
			}
			return false;
	}
	alert( "没有控件被判断是否为空，请检查页面！" );
	return true; 
} 
function isType( obj )
{
	var tag = 0;
	if( obj.length > 0 ){
		type = obj[0].type;
		if( type == "radio" ) tag = 2;
		if( type == "checkbox" ) tag = 3;
	}else{
		type =obj.type;
		if( type == "text" ) tag = 1;
		if( type == "radio" ) tag = 4;
		if( type == "checkbox" ) tag = 5;
		if( type == "password" ) tag = 6;
	}
	return tag;
}
function isNumber( obj,msg )
{ 
	if( isNull( obj ) ) return false;
 
	var l = obj.value.length; 
	var count=0; 
 
	for(var i=0; i < l; i++){ 
		var digit = obj.value.charAt(i);
		if( digit == "." ){ 
			count++; 
			if(count>1){
				alert( msg);
				return false;
			} 
		}else	
		if( (digit != "," && digit < "0") || (digit != "," && digit > "9") ){
			alert( msg);
			return false; 
		}
	} 
	return true;
}
function isAlpha( obj,msg ) 
{ 
	if( isNull( obj ) ) return false;
	for (var i=0; i<obj.value.length; i++) 
	{ 
		var Char = obj.value.charAt(i); 
		if ((Char < "a" || Char > "z") && (Char < "A" || Char > "Z") ){
			alert( msg);
			return false; 
		}
	} 
    return true; 
}
function isContains(strSource,strFind,msg)
{
	if (strSource.indexOf(strFind)== -1) 
	{
		alert( msg);
		return false;
	}
	return true;
}
function compValue( obj1, obj2 ) 
{ 
	var tag = obj1.value - obj2.value;
	if( tag == 0 )
		return 0;
	else	if( tag < 0 )
			return -1;
		else
			return 1;
}
function compNumberValue( obj1, obj2 ) 
{ 

	var	re=/,/gi
	f1 = parseFloat(obj1.value.replace(re,""));	
	f2 = parseFloat(obj2.value.replace(re,""));
	var tag = f1 - f2;
	if( tag == 0 )
		return 0;
	else	if( tag < 0 )
			return -1;
		else
			return 1;
}
function initDate( y, m, d ) 
{ 
	var now = new Date();
	var year = now.getYear();
	year = year.toString();

	var month = now.getMonth() + 1;
	if( month < 10 )
		month = "0" + month;
	var day = now.getDate();
	if( day < 10 )
		day = "0" + day;
	initOptionChecked(y,year);
	initOptionChecked(m,month);
	initOptionChecked(d,day);
}
function consDate( y, m, d )
{ 
	var str = "";
	//var year = getChoiceValue(y);
	//var month = getChoiceValue(m);
	//var day = getChoiceValue(d);
	str = y.value + m.value + d.value;
	
	return	str;
}

function chkBookDate(y,m,d ) 
{ 
	/*
	var now = new Date();
	var nowhours=now.getHours();
	var nowminutes=now.getMinutes();
	var nowseconds=now.getSeconds();

	var year = y.value;
	var month = m.value;
	var day = d.value;

	//var year = getChoiceValue(document.eservice.year);
	//var month = getChoiceValue(document.eservice.month);
	//var day = getChoiceValue(document.eservice.day);
	
	var bookDate = new Date( year, month-1, day,nowhours,nowminutes,nowseconds+1 );

	var nowtime = now.getTime();
	var booktime = bookDate.getTime();

	if( now > bookDate )
	{
		alert( "预约日期应该大于今天" );	
		return false;
	}
	if( ( booktime - nowtime ) > 10*24*60*60*1000 )
	{
		alert( "预约日期应该小于10天" );	
		return false;
	}
	*/
	return true;
}

function setAllCheckBox ( obj, val )
{
	if( obj.length > 0 ){
		for ( i = 0; i < obj.length; i++ )
			obj[i].checked = val;
	}else{
		obj.checked = val;
	}
}
function getChoiceValue(obj) 
{   
	for (var i = 0; i < obj.length; i++) 
	{      
		if (obj.options[i].selected == true) 
			return obj.options[i].value; 
	}  
	return null;
}

function getChoiceText(obj) 
{   
	for (var i = 0; i < obj.length; i++) 
	{      
		if (obj.options[i].selected == true) 
			return obj.options[i].text; 
	}  
	return null;
}
function initOptionChecked(obj,value)
{
	for (var i = 0; i < obj.length; i++) 
	{      
		if (obj.options[i].value == value) 
		{
			obj.options[i].selected=true;
			break;
		}
	}
}
function gback() 
{   
	//var tx = document.eservice.txcode.value;
	//document.eservice.txcode.value = tx.substring(0,4)+"00";
	//document.eservice.submit();
	var tx = document.eservice.txcode.value;
	if (tx.substring(4,6)== "01")
	{
		document.eservice.txcode.value = "001000";
		document.eservice.submit();
		return true;
	}
	history.back();
	return true;
}
//special page
function initpg( )
{
	//if (document.eservice.currPageNo.value == document.eservice.totalPageNum.value) document.eservice.next.disabled=true;
	return true;
}
function chkpgprev( )
{
	if (document.eservice.currPageNo.value == 1)
	{
		alert("当前显示页面已经为第一个查询结果页面")
		document.eservice.inqPageNo.value="";
		return false;
	}
	document.eservice.inqPageNo.value =  parseInt(document.eservice.currPageNo.value) - 1;
	document.eservice.submit();
	return true;
}
function chkpgnext( )
{
	if (document.eservice.currPageNo.value == document.eservice.totalPageNum.value)
	{
		alert("当前显示页面已经为最后查询结果页面")
		document.eservice.inqPageNo.value="";
		return false;
	}
	document.eservice.inqPageNo.value = parseInt(document.eservice.currPageNo.value) + 1;
	document.eservice.submit();
	return true;
}
function chkpginq( )
{
	if (document.eservice.totalPageNum.value == "1") 
	{
		alert("查询结果页面只有一页");
		return false;
	}
	if (isNull(document.eservice.inqPageNo,"请输入查询页次")) return false;
	if (!isNumber(document.eservice.inqPageNo,"请输入正确查询页次"))
	{
		document.eservice.inqPageNo.value="";
		return false;
	}
	if (parseInt(document.eservice.inqPageNo.value) < parseInt("1"))
	{
		alert("查询页次不能小于1")
		document.eservice.inqPageNo.value="";
		return false;
	}
	if (parseInt(document.eservice.inqPageNo.value) > parseInt(document.eservice.totalPageNum.value) )
	{
		alert("查询页次不能大于总页数")
		document.eservice.inqPageNo.value="";
		return false;
	}
	if (document.eservice.inqPageNo.value == document.eservice.currPageNo.value )
	{
		alert("当前显示页面已经是查询页次");
		document.eservice.inqPageNo.value="";
		return false;
	}
	return true;
}
 function click() 
 { 
	 if (event.button==2) 
	 { 
		 alert('对不起，我们暂时不使用鼠标右键！') 
	 } 
 } 
 document.onmousedown=click 
//-->