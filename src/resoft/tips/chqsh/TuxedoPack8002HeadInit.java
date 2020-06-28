package resoft.tips.chqsh;

public class TuxedoPack8002HeadInit {

	/*
	 * ǰ̨���������ģ�
	 * Tuxedo����ͷ+Ӧ����+������(�Ǹ�����)+������Դ +�ն˺� +�������к�+��Ա��+�������� 
	 * +��Ȩ����+����1+����2+����1����+����2����+����1���������豸+����2���������豸
	 * +��Ȩԭ����+��һ�ʽ�����־��+������+DATA1Length+DATA2Length
	 */
	private static final String TuxeHead = "TIPS3001";            //����C�ж��Ƿ�������TIPS������
	private static final String TuxeSelecHead = "TIPS3002";       //����C�ж��Ƿ�������TIPS������
	
	private static final String HEAD = "0000010000";              //Tuxedo����ͷ      10��0000010000��
	private static final String AP_CODE = "01";                   //Ӧ����             2��01��
	
	//public static final String TR_CODE = "";                    //�����룬�Ǹ�����    4
	private static final String INPTTR_CODE = "0152";             //֧Ʊת����         4��0152��
	private static final String ITR_CODE = "0160";                //�����˻�ת�ڲ��˻�  4��0160��
	private static final String IN_DATATR_CODE = "0161";          //��ת����ȡ��        4(0161)
	private static final String UNDOTR_CODE = "0399";             //���ʽ���            4��0399��
	private static final String SQL_CODE = "0384";                //��ѯ����            4��0384��
	
	private static final String TR_FROM = "TIPS";                 //������Դ            4��TIPS��
	private static final String TM_NO = "880";                    //�ն˺�              4��8001��
	private static final String TM_SEQ_NO = "0001";               //�������к�          4��0001��
	private static final String TL_TD = "880";                    //��Ա��              4��8001��
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
	
	public static final String AMTFormat = "                   "; //���׽���ʽ 
	
	public static final String INPTHead = TuxeHead + HEAD + AP_CODE + INPTTR_CODE 
											+ TR_FROM + TM_NO + TM_SEQ_NO + TL_TD + REQ_TYPE 
											+ AUTH_LVL + SUP1_ID + SUP2_ID + SUP1_PSWD 
											+ SUP2_PSWD + SUP1_DEV + SUP2_DEV + AUTH_RESN 
											+ JRN_NO + CHK_CODE + DATA1Length + DATA2Length;
	public static final String IHead = TuxeHead + HEAD + AP_CODE + ITR_CODE
											+ TR_FROM + TM_NO + TM_SEQ_NO + TL_TD + REQ_TYPE 
											+ AUTH_LVL + SUP1_ID + SUP2_ID + SUP1_PSWD 
											+ SUP2_PSWD + SUP1_DEV + SUP2_DEV + AUTH_RESN 
											+ JRN_NO + CHK_CODE + DATA1Length + DATA2Length;
	public static final String IN_DATAHead = TuxeHead + HEAD + AP_CODE + IN_DATATR_CODE
											+ TR_FROM + TM_NO + TM_SEQ_NO + TL_TD + REQ_TYPE 
											+ AUTH_LVL + SUP1_ID + SUP2_ID + SUP1_PSWD 
											+ SUP2_PSWD + SUP1_DEV + SUP2_DEV + AUTH_RESN 
											+ JRN_NO + CHK_CODE + DATA1Length + DATA2Length;
	public static final String UNDOHead = TuxeHead + HEAD + AP_CODE + UNDOTR_CODE
											+ TR_FROM + TM_NO + TM_SEQ_NO + TL_TD + REQ_TYPE 
											+ AUTH_LVL + SUP1_ID + SUP2_ID + SUP1_PSWD 
											+ SUP2_PSWD + SUP1_DEV + SUP2_DEV + AUTH_RESN 
											+ JRN_NO + CHK_CODE + "019" + "000";
	public static final String SQLHead = TuxeSelecHead + HEAD + AP_CODE + SQL_CODE
											+ TR_FROM + TM_NO + TM_SEQ_NO + TL_TD + REQ_TYPE 
											+ AUTH_LVL + SUP1_ID + SUP2_ID + SUP1_PSWD 
											+ SUP2_PSWD + SUP1_DEV + SUP2_DEV + AUTH_RESN 
											+ JRN_NO + CHK_CODE + "020" + "480";
	private static final String FirstHead = TuxeHead + HEAD + AP_CODE;
	private static final String SecondHead = REQ_TYPE + AUTH_LVL + SUP1_ID + SUP2_ID + SUP1_PSWD
											+ SUP2_PSWD + SUP1_DEV + SUP2_DEV + AUTH_RESN
											+ JRN_NO + CHK_CODE ;	
	public static String getHead(String id,String type)
	{
		String Type_Code = type + "_CODE";
		return "" + FirstHead + Type_Code + TR_FROM + TM_NO + id + TM_SEQ_NO + TL_TD + SecondHead ;
	}
}
