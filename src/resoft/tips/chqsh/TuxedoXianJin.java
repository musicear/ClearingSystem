package resoft.tips.chqsh;

/**
 * @author fanchengwei
 * date2008-6-8
 * time����09:00:39
 */
public class TuxedoXianJin {

	/**
	 * �������е�Tuxedoϵͳ��Ϊ����ϵͳ�Ͳ�ѯϵͳ����C���ݵ�һ���ֶ��жϷ����Ǹ�ϵͳ��
	 * TIPS3001����������ϵͳ��
	 * TIPS3002��������ѯϵͳ��
	 */
	private static final String TuxeHead = "TIPS3001";            //����C�ж��Ƿ�������TIPS������
//	private static final String TuxeSelecHead = "TIPS3002";       //����C�ж��Ƿ�������TIPS������
	
	private static final String HEAD = "0000010000";              //Tuxedo����ͷ      10��0000010000��
	private static final String AP_CODE = "01";                   //Ӧ����             2��01��
	public static final String TR_CODE = "0101";                  //�����룬�Ǹ�����    4
	
	private static final String TR_FROM = "TIPS";                 //������Դ            4��TIPS��
	//private static final String TM_NO = "8801";                 //�ն˺�              4��8001��
	private static final String TM_SEQ_NO = "0001";               //�������к�          4��0001��
	//private static final String TL_TD = "8801";                   //��Ա��              4��8001��
	private static final String REQ_TYPE = "0";                   //��������            1��0��
	private static final String AUTH_LVL = "01";                  //��Ȩ����            2��01��
	private static final String SUP1_ID = "    ";                 //����1             4���ո�
	private static final String SUP2_ID = "    ";                 //����2             4���ո�
	private static final String SUP1_PSWD = "    ";               //����1����           4���ո�
	private static final String SUP2_PSWD = "    ";               //����2����           4���ո�
	private static final String SUP1_DEV = "0";                   //����1���������豸    1��0��
	private static final String SUP2_DEV = "0";                   //����2���������豸    1��0��
	private static final String AUTH_RESN = "                                        ";
	                                                              //��Ȩԭ����,         40���ո�
	private static final String JRN_NO = "       ";               //���һ�ʽ�����־��    7���ո�
	private static final String CHK_CODE = "        ";            //������               8���ո�
	private static final String DATA1Length = "250";    
	private static final String DATA2Length = "250";
	
	private static String data1 = "";
	private static String data2 = "";

	private static final String FirstHead = HEAD + AP_CODE + TR_CODE + TR_FROM ;
	private static final String SecondHead = REQ_TYPE + AUTH_LVL + SUP1_ID + SUP2_ID + SUP1_PSWD
											+ SUP2_PSWD + SUP1_DEV + SUP2_DEV + AUTH_RESN
											+ JRN_NO + CHK_CODE + DATA1Length + DATA2Length;
	
	
	public String getXianJin(String TL_TD){
		
		return TuxeHead + FirstHead + TL_TD + TM_SEQ_NO + TL_TD + SecondHead+data1+data2;
	}	

}
