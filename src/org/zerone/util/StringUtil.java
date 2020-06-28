// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StringUtil.java

package org.zerone.util;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class StringUtil
{

    private StringUtil()
    {
    }

    public static String convertNumber2Chinese(double amount)
    {
        String chineseDigit[] = {
            "\u96F6", "\u58F9", "\u8D30", "\u53C1", "\u8086", "\u4F0D", "\u9646", "\u67D2", "\u634C", "\u7396"
        };
        String hzUnit[] = {
            "", "\u62FE", "\u4F70", "\u4EDF"
        };
        String hzWanUnit[] = {
            "\u4E07", "\u4EBF", "\u5146"
        };
        long intPart = (long)amount;
        String result = "";
        int multiple = 0;
        int wan = 0;
        int continuousZero = 0;
        int remainder;
        for(; intPart > 0L; intPart = (intPart - (long)remainder) / 10L)
        {
            remainder = (int)(intPart % 10L);
            boolean needAddMyriadUnit = multiple == 0 && wan > 0;
            if(needAddMyriadUnit)
                result = hzWanUnit[wan - 1] + result;
            if(remainder > 0)
            {
                result = chineseDigit[remainder] + hzUnit[multiple] + result;
                continuousZero = 0;
            } else
            {
                boolean needAddZero = continuousZero == 0 && multiple > 0 || continuousZero == 0 && multiple > 0 && wan > 0 && !result.equals("");
                if(needAddZero)
                    result = "\u96F6" + result;
                continuousZero++;
            }
            if(++multiple == 4)
            {
                wan++;
                multiple = 0;
            }
        }

        if(!result.equals(""))
            result = result + "\u5143";
        int fraction = (int)Math.round((amount - (double)(long)amount) * 100D);
        if(!result.equals("") && fraction == 0)
            result = result + "\u6574";
        if(fraction > 0)
        {
            int n = fraction / 10;
            if(n > 0)
                result = result + chineseDigit[n] + "\u89D2";
            n = fraction % 10;
            if(!result.equals("") && n == 0)
                result = result + "\u6574";
            if(n > 0)
            {
                if(fraction < 10)
                    result = result + "\u96F6";
                result = result + chineseDigit[n] + "\u5206";
            }
        }
        return result;
    }

    public static Map parseMap(String str)
    {
        Map map = new HashMap();
        if(str == null || str.equals(""))
            return map;
        StringTokenizer st = new StringTokenizer(str, ";");
        do
        {
            if(!st.hasMoreTokens())
                break;
            String token = st.nextToken();
            int pos = token.indexOf(":");
            if(pos > 0)
            {
                String key = token.substring(0, pos);
                String value = token.substring(pos + 1, token.length());
                map.put(key, value);
            }
        } while(true);
        return map;
    }
	
	public static Object isNullThen(Object first, Object second)
    {
        return first != null && !first.equals("") ? first : second;
    }
}
