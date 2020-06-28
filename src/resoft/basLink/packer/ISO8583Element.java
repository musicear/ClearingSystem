package resoft.basLink.packer;

/**
 * function: 8583��ʽ�е�Ԫ��
 * User: albert lee
 * Date: 2005-10-27
 * Time: 21:29:04
 */
public class ISO8583Element {
    public static final ISO8583Element ReservedElement = new ISO8583Element("NoUse",0,"C",0);

    public static final String DataType_Char = "C";//�ַ�����������
    public static final String DataType_Number = "N";//��ֵ����������
    public static final String DataType_DateTime = "D";//������


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
    private String name;//������
    private int flag;//0:����;2:2λ�䳤��3:3λ�䳤
    private String dataType;//�������� N:��ֵ��;C:�ַ���;D:������
    private int length;//������ĳ���
}
