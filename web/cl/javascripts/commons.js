document.ondblclick = function docondblclick() {
   window.event.returnValue = false;
   return false;   
}

function openSelect(rcvID,codetype){
	var nodercv =document.getElementById(rcvID);
	var surl="../action/SelectCode.action?codetype="+codetype;
	nodercv.value=window.showModalDialog(surl);
}

function openVoucher(cubeCode,transNo,sheetId) {
	//alert(transNo);
	var url = "../servlet/ShowVoucher?cubeCode=" + cubeCode + "&&transNo=" + transNo + "&&sheetId=" + sheetId;
	open(url,"","resize=0,top=10,left=10,height=450,width=800,status=no");

}
function formatNumber(number,pattern)
{
    var str			= number.toString();
    var strInt;
    var strFloat;
    var formatInt;
    var formatFloat;
    if(/\./g.test(pattern))
    {
        formatInt		= pattern.split('.')[0];
        formatFloat		= pattern.split('.')[1];
    }
    else
    {
        formatInt		= pattern;
        formatFloat		= null;
    }

    if(/\./g.test(str))
    {
        if(formatFloat!=null)
        {
            var tempFloat	= Math.round(parseFloat('0.'+str.split('.')[1])*Math.pow(10,formatFloat.length))/Math.pow(10,formatFloat.length);
            strInt		= (Math.floor(number)+Math.floor(tempFloat)).toString();
            strFloat	= /\./g.test(tempFloat.toString())?tempFloat.toString().split('.')[1]:'0';
        }
        else
        {
            strInt		= Math.round(number).toString();
            strFloat	= '0';
        }
    }
    else
    {
        strInt		= str;
        strFloat	= '0';
    }
    if(formatInt!=null)
    {
        var outputInt	= '';
        var zero		= formatInt.match(/0*$/)[0].length;
        var comma		= null;
        if(/,/g.test(formatInt))
        {
            comma		= formatInt.match(/,[^,]*/)[0].length-1;
        }
        var newReg		= new RegExp('(\\d{'+comma+'})','g');

        if(strInt.length<zero)
        {
            outputInt		= new Array(zero+1).join('0')+strInt;
            outputInt		= outputInt.substr(outputInt.length-zero,zero)
        }
        else
        {
            outputInt		= strInt;
        }

        outputInt			= outputInt.substr(0,outputInt.length%comma)+outputInt.substring(outputInt.length%comma).replace(newReg,(comma!=null?',':'')+'$1')
        outputInt			= outputInt.replace(/^,/,'');

        strInt	= outputInt;
    }

    if(formatFloat!=null)
    {
        var outputFloat	= '';
        var zero		= formatFloat.match(/^0*/)[0].length;

        if(strFloat.length<zero)
        {
            outputFloat		= strFloat+new Array(zero+1).join('0');
            //outputFloat		= outputFloat.substring(0,formatFloat.length);
            var outputFloat1	= outputFloat.substring(0,zero);
            var outputFloat2	= outputFloat.substring(zero,formatFloat.length);
            outputFloat		= outputFloat1+outputFloat2.replace(/0*$/,'');
        }
        else
        {
            outputFloat		= strFloat.substring(0,formatFloat.length);
        }

        strFloat	= outputFloat;
    }
    else
    {
        if(pattern!='' || (pattern=='' && strFloat=='0'))
        {
            strFloat	= '';
        }
    }

    return strInt+(strFloat==''?'':'.'+strFloat);
}

var submitted = false;
function submitForm(theForm) {
    if(submitted==false) {
        //theForm.submit();
        submitted = true;
        return true;
    } else {
        return false;
    }
}

