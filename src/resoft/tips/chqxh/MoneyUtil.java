package resoft.tips.chqxh;
/**
 * <p>���ת��</p>
 * User: liwei
 * Date: 2007-11-06
 * Time: 13:21:00
 */
public class MoneyUtil {
    public static String[] chineseDigits = new String[] { "��", "Ҽ", "��", "��", "��", "��", "½", "��", "��", "��"};
    public static String amountToChinese(double amount) {
        if(amount > 99999999999999.99 || amount < -99999999999999.99)
            throw new IllegalArgumentException("����ֵ��������Χ (-99999999999999.99 �� 99999999999999.99)��");
        boolean negative = false;
        if(amount < 0) {
            negative = true;
            amount = amount * (-1);
        }
        long temp = Math.round(amount * 100);
        int numFen = (int)(temp % 10); // ��
        temp = temp / 10;
        int numJiao = (int)(temp % 10); //��
        temp = temp / 10;
        //temp Ŀǰ�ǽ�����������

        int[] parts = new int[20]; // ���е�Ԫ���ǰ�ԭ������������ַָ�Ϊֵ�� 0~9999 ֮������ĸ�������
        int numParts = 0; // ��¼��ԭ������������ַָ�Ϊ�˼������֣�ÿ���ֶ��� 0~9999 ֮�䣩
        for(int i=0; ; i++) {
            if(temp ==0)
                break;
            int part = (int)(temp % 10000);
            parts[i] = part;
            numParts ++;
            temp = temp / 10000;
        }
        boolean beforeWanIsZero = true; // ��־��������һ���ǲ��� 0
        String chineseStr = "";
        for(int i=0; i<numParts; i++) {
            String partChinese = partTranslate(parts[i]);
            if(i % 2 == 0) {
                if("".equals(partChinese))
                    beforeWanIsZero = true;
                else
                    beforeWanIsZero = false;
            }
            if(i != 0) {
                if(i % 2 == 0)
                    chineseStr = "��" + chineseStr;
                else {
                    if("".equals(partChinese) && !beforeWanIsZero)   // ������򡱶�Ӧ�� part Ϊ 0������������һ����Ϊ 0���򲻼ӡ��򡱣����ӡ��㡱
                        chineseStr = "��" + chineseStr;
                    else {
                        if(parts[i-1] < 1000 && parts[i-1] > 0) // ���"��"�Ĳ��ֲ�Ϊ 0, ��"��"ǰ��Ĳ���С�� 1000 ���� 0�� �������Ӧ�ø����㡱
                            chineseStr = "��" + chineseStr;
                        chineseStr = "��" + chineseStr;
                    }
                }
            }
            chineseStr = partChinese + chineseStr;
        }

        if("".equals(chineseStr))  // ��������Ϊ 0, ����Ϊ"��Ԫ"
            chineseStr = chineseDigits[0];
        else if(negative) // �������ֲ�Ϊ 0, ����ԭ���Ϊ����
            chineseStr = "��" + chineseStr;

        chineseStr = chineseStr + "Ԫ";

        if(numFen == 0 && numJiao == 0) {
            chineseStr = chineseStr + "��";
        }
        else if(numFen == 0) { // 0 �֣�������Ϊ 0
            chineseStr = chineseStr + chineseDigits[numJiao] + "��";
        }
        else { // ���֡�����Ϊ 0
            if(numJiao == 0)
                chineseStr = chineseStr + "��" + chineseDigits[numFen] + "��";
            else
                chineseStr = chineseStr + chineseDigits[numJiao] + "��" + chineseDigits[numFen] + "��";
        }

        return chineseStr;

    }

    /**
     * ��һ�� 0~9999 ֮�������ת��Ϊ���ֵ��ַ���������� 0 �򷵻� ""
     * @param amountPart
     * @return
     */
    private static String partTranslate(int amountPart) {

        if(amountPart < 0 || amountPart > 10000) {
            throw new IllegalArgumentException("���������Ǵ��ڵ��� 0��С�� 10000 ��������");
        }
        String[] units = new String[] {"", "ʰ", "��", "Ǫ"};
        int temp = amountPart;
        String amountStr = new Integer(amountPart).toString();
        int amountStrLength = amountStr.length();
        boolean lastIsZero = true; //�ڴӵ�λ����λѭ��ʱ����¼��һλ�����ǲ��� 0
        String chineseStr = "";
        for(int i=0; i<amountStrLength; i++) {
            if(temp == 0)  // ��λ��������
                break;
            int digit = temp % 10;
            if(digit == 0) { // ȡ��������Ϊ 0
                if(!lastIsZero)  //ǰһ�����ֲ��� 0�����ڵ�ǰ���ִ�ǰ�ӡ��㡱��;
                    chineseStr = "��" + chineseStr;
                lastIsZero = true;
            }
            else { // ȡ�������ֲ��� 0
                chineseStr = chineseDigits[digit] + units[i] + chineseStr;
                lastIsZero = false;
            }
            temp = temp / 10;
        }
        return chineseStr;
    }

    public static void main(String[] args) {

        if(args.length == 0) {
            System.out.println("ת����ʾ��");
            System.out.println("-------------------------");
            System.out.println("25000000000005.999: " + amountToChinese(25000000000005.999));
            System.out.println("45689263.626: " + amountToChinese(45689263.626));
            System.out.println("0.69457: " + amountToChinese(0.69457));
            System.out.println("253.0: " + amountToChinese(253.0));
            System.out.println("0: " + amountToChinese(0));
            System.out.println("-------------------------");
            System.out.println("�������÷���");
            System.out.println("    ��ʾ��        java MoneyUtil");
            System.out.println("    ת��ָ�����֣�java MoneyUtil [Ҫת��������]");
            //System.out.println(Long.MAX_VALUE);
            //System.out.println(Long.MIN_VALUE);
        }else {
            System.out.println("ת�������");
            System.out.println(args[0] + ": " + amountToChinese(Double.parseDouble(args[0])));
        }

    }

}

