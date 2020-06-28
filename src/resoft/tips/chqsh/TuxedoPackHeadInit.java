package resoft.tips.chqsh;

public class TuxedoPackHeadInit {

	/**
	 * ǰ̨���������ģ�
	 * Tuxedo����ͷ+Ӧ����+������(�Ǹ�����)+������Դ +�ն˺� +�������к�+��Ա��+�������� 
	 * +��Ȩ����+����1+����2+����1����+����2����+����1���������豸+����2���������豸
	 * +��Ȩԭ����+��һ�ʽ�����־��+������+DATA1Length+DATA2Length
	 */
	private static final String TuxeHead = "TIPS3001";            //����C�ж��Ƿ�������TIPS������
	private static final String TuxeSelecHead = "TIPS3002";       //����C�ж��Ƿ�������TIPS������
	
	private static final String HEAD = "0000010000";              //Tuxedo����ͷ      10��0000010000��
	private static final String AP_CODE = "01";                   //Ӧ����             2��01��
	
	/**
	 * public static final String TR_CODE = "";                    //�����룬�Ǹ�����    4
	 * private static final String INPTTR_CODE = "0152";             //֧Ʊת����         4��0152��
	 * private static final String ITR_CODE = "0160";                //�����˻�ת�ڲ��˻�  4��0160��
	 * private static final String IN_DATATR_CODE = "0161";          //��ת����ȡ��        4(0161)
	 */
	private static final String UNDOTR_CODE = "0399";             //���ʽ���            4��0399��
	private static final String SQL_CODE = "0384";                //��ѯ����            4��0384��
	private static final String CHKPWSQL_CODE = "3018";			  //����У�齻��        4��3018��
	private static final String CHKTELLERPWSQL_CODE = "0377";	  //��Ա����У�齻��    4��3077��
	
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
	
	public static final String AMTFormat = "                   "; //���׽���ʽ 
	private static final String FirstHead = HEAD + AP_CODE;
	private static final String SecondHead = REQ_TYPE + AUTH_LVL + SUP1_ID + SUP2_ID + SUP1_PSWD
											+ SUP2_PSWD + SUP1_DEV + SUP2_DEV + AUTH_RESN
											+ JRN_NO + CHK_CODE ;	
	public TuxedoPackHeadInit(){
		
	}
	/**
	 * ���˱���ͷ
	 * @param TR_CODE	��������---֧Ʊת����(0152)�������˻�ת�ڲ��˻�(0160)����ת����ȡ��(0161)
	 * @param TL_TD		��������Ա��
	 * @return			�����˱���ͷ
	 */
	public static String getHead(String TR_CODE,String TL_TD)
	{
		return TuxeHead + FirstHead + TR_CODE + TR_FROM + TL_TD + TM_SEQ_NO + TL_TD + SecondHead + DATA1Length + DATA2Length;
	}
	/**
	 * ���ʱ���ͷ
	 * @param TL_TD		��������Ա��
	 * @return			�����ʱ���ͷ
	 */
	public static String getUNDOHead(String TL_TD)
	{
		return TuxeHead + FirstHead + UNDOTR_CODE + TR_FROM + TL_TD + TM_SEQ_NO + TL_TD + SecondHead + "019000";
	}
	/**
	 * ��ѯ����ͷ
	 * @param TL_TD		��������Ա��
	 * @return			�����ʱ���ͷ
	 */
	public static String getSQLHead(String TL_TD)
	{
		return TuxeSelecHead + FirstHead + SQL_CODE + TR_FROM + TL_TD + TM_SEQ_NO + TL_TD + SecondHead + "020480";
	}
	/**
	 * У����˰������
	 * @return
	 */
	public static String checkPassWordHead()
	{
		return TuxeHead + FirstHead + CHKPWSQL_CODE + TR_FROM + "8801" + TM_SEQ_NO + "8801" + SecondHead + DATA1Length + DATA2Length;
	}
	/**
	 * У���Ա����
	 * @param SUP1_ID
	 * @param SUP1_PSWD
	 * @return
	 */
	/**
	public static String checkPassWordHead(String SUP1_ID,String SUP1_PSWD,String SUP2_ID)
	{
		return TuxeHead + FirstHead + CHKTELLERPWSQL_CODE + TR_FROM + "8801" + TM_SEQ_NO + "8801" + REQ_TYPE + AUTH_LVL + SUP1_ID
				+ SUP2_ID + SUP1_PSWD + SUP2_PSWD + SUP1_DEV + SUP2_DEV + AUTH_RESN + JRN_NO + CHK_CODE + DATA1Length + DATA2Length;
	}
	*/
	public static String checkPassWordHead(String SUP1_ID,String SUP1_PSWD,String TM_NO)
	{
		return TuxeHead + FirstHead + CHKTELLERPWSQL_CODE + TR_FROM + TM_NO + TM_SEQ_NO + TM_NO + REQ_TYPE + AUTH_LVL + SUP1_ID
		+ SUP2_ID + SUP1_PSWD + SUP2_PSWD + "1" + SUP2_DEV + AUTH_RESN + JRN_NO + CHK_CODE + DATA1Length + DATA2Length;
	}
}
