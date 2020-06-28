package resoft.tips.chqsh;

public class TuxedoPack8002HeadInit {

	/*
	 * 前台交易请求报文：
	 * Tuxedo报文头+应用码+交易码(是个变量)+交易来源 +终端号 +报文序列号+柜员号+请求类型 
	 * +授权级别+主管1+主管2+主管1密码+主管2密码+主管1密码输入设备+主管2密码输入设备
	 * +授权原因码+后一笔交易日志号+鉴定码+DATA1Length+DATA2Length
	 */
	private static final String TuxeHead = "TIPS3001";            //用于C判断是否是来自TIPS的请求
	private static final String TuxeSelecHead = "TIPS3002";       //用于C判断是否是来自TIPS的请求
	
	private static final String HEAD = "0000010000";              //Tuxedo报文头      10（0000010000）
	private static final String AP_CODE = "01";                   //应用码             2（01）
	
	//public static final String TR_CODE = "";                    //交易码，是个变量    4
	private static final String INPTTR_CODE = "0152";             //支票转过渡         4（0152）
	private static final String ITR_CODE = "0160";                //活期账户转内部账户  4（0160）
	private static final String IN_DATATR_CODE = "0161";          //卡转过渡取款        4(0161)
	private static final String UNDOTR_CODE = "0399";             //摸帐交易            4（0399）
	private static final String SQL_CODE = "0384";                //查询交易            4（0384）
	
	private static final String TR_FROM = "TIPS";                 //交易来源            4（TIPS）
	private static final String TM_NO = "880";                    //终端号              4（8001）
	private static final String TM_SEQ_NO = "0001";               //报文序列号          4（0001）
	private static final String TL_TD = "880";                    //柜员号              4（8001）
	private static final String REQ_TYPE = "0";                   //请求类型            1（0）
	private static final String AUTH_LVL = "01";                  //授权级别            2（01）
	private static final String SUP1_ID = "    ";                 //主管1             4（空格）
	private static final String SUP2_ID = "    ";                 //主管2             4（空格）
	private static final String SUP1_PSWD = "    ";               //主管1密码           4（空格）
	private static final String SUP2_PSWD = "    ";               //主管2密码           4（空格）
	private static final String SUP1_DEV = "0";                   //主管1密码输入设备    1（0）
	private static final String SUP2_DEV = "0";                   //主管2密码输入设备    1（0）
	private static final String AUTH_RESN = "                                        ";
	                                                              //授权原因码,         40（空格）
	private static final String JRN_NO = "       ";               //最后一笔交易日志号    7（空格）
	private static final String CHK_CODE = "        ";            //鉴定码               8（空格）
	private static final String DATA1Length = "250";    
	private static final String DATA2Length = "250";
	
	public static final String AMTFormat = "                   "; //交易金额格式 
	
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
