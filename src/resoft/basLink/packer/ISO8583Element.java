package resoft.basLink.packer;

/**
 * function: 8583格式中的元素
 * User: albert lee
 * Date: 2005-10-27
 * Time: 21:29:04
 */
public class ISO8583Element {
    public static final ISO8583Element ReservedElement = new ISO8583Element("NoUse",0,"C",0);

    public static final String DataType_Char = "C";//字符型数据类型
    public static final String DataType_Number = "N";//数值型数据类型
    public static final String DataType_DateTime = "D";//日期型


    public ISO8583Element(String name,int flag,String dataType,int length) {
        this.name = name;
        this.flag = flag;
        this.dataType = dataType;
        this.length = length;
    }
    public String getName() {
        return name;
    }
    public int getFlag() {
        return flag;
    }
    public String getDataType() {
        return dataType;
    }
    public int getLength() {
        return length;
    }
    private String name;//域名称
    private int flag;//0:定长;2:2位变长；3:3位变长
    private String dataType;//数据类型 N:数值型;C:字符型;D:日期型
    private int length;//定长域的长度
}
