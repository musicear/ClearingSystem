package resoft.tips.chqsh;

public class TuxedoData2Init {

	private static final String OPER = "91";                      //��������      2��91��
	//private static final String CRAC = "999801010000879";         //�����ʺ�      15��999801010000879��
	//private static final String CRAC = "999801011223017";         //�����ʺ�      15��999801010000879��
	
	private static final String VCHTYPE = "00";                   //ƾ֤����      2(00)
	private static final String VCHNO = "123456789";              //ƾ֤��        9�����9������
	private static final String TRCCY = "01";                     //���ױ���      2��01��
	//private static final String TRAMT = "";                     //���׽��      19����
	private static final String CMT = "                                        ";
																  //��ע      ����ӵ�����    40���ո�
	private static final String RVSOCUDATE = "        ";          //��������       8���ո�
	private static final String RVSOCUVCH = "        ";           //���ʴ�Ʊ��     8���ո�
	private static final String GROUP1 = "0000";
	//private static final String NO = "";                        //�ʺ�           15����
	//���ڴ��I����֧Ʊ��INPT��
	public static final String GROUP2 ="1";
	//���˻���IN_DATA��
	//private static final String IN_DATAGROUP2 = "0000               1";//������Ϣ  ��ӽ跽�ʺ�   20��0000+15λֵ+1��
	
	public static String data2Head1 = OPER;
	public static String data2Head2 = VCHTYPE + VCHNO + TRCCY;
	//I�����ڣ���INPT��֧Ʊ���˻�������2��data2Head + TRAMT + data2forIandINPT + NO + GROUP2 
	public static String data2forIandINPT = CMT + RVSOCUDATE + RVSOCUVCH + GROUP1;
	//public static String data2forIandINPT = RVSOCUDATE + RVSOCUVCH + GROUP1;
	//IN_DATA�������˻�������2��data2Head + TRAMT + data2forIN_DATA
	public static String data2forIN_DATA = data2forIandINPT;
}
