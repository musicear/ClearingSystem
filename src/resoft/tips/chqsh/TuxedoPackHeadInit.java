package resoft.tips.chqsh;

public class TuxedoPackHeadInit {

	/**
	 * 前台交易请求报文：
	 * Tuxedo报文头+应用码+交易码(是个变量)+交易来源 +终端号 +报文序列号+柜员号+请求类型 
	 * +授权级别+主管1+主管2+主管1密码+主管2密码+主管1密码输入设备+主管2密码输入设备
	 * +授权原因码+后一笔交易日志号+鉴定码+DATA1Length+DATA2Length
	 */
	private static final String TuxeHead = "TIPS3001";            //用于C判断是否是来自TIPS的请求
	private static final String TuxeSelecHead = "TIPS3002";       //用于C判断是否是来自TIPS的请求
	
	private static final String HEAD = "0000010000";              //Tuxedo报文头      10（0000010000）
	private static final String AP_CODE = "01";                   //应用码             2（01）
	
	/**
	 * public static final String TR_CODE = "";                    //交易码，是个变量    4
	 * private static final String INPTTR_CODE = "0152";             //支票转过渡         4（0152）
	 * private static final String ITR_CODE = "0160";                //活期账户转内部账户  4（0160）
	 * private static final String IN_DATATR_CODE = "0161";          //卡转过渡取款        4(0161)
	 */
	private static final String UNDOTR_CODE = "0399";             //摸帐交易            4（0399）
	private static final String SQL_CODE = "0384";                //查询交易            4（0384）
	private static final String CHKPWSQL_CODE = "3018";			  //密码校验交易        4（3018）
	private static final String CHKTELLERPWSQL_CODE = "0377";	  //柜员密码校验交易    4（3077）
	
	private static final String TR_FROM = "TIPS";                 //交易来源            4（TIPS）
	//private static final String TM_NO = "8801";                 //终端号              4（8001）
	private static final String TM_SEQ_NO = "0001";               //报文序列号          4（0001）
	//private static final String TL_TD = "8801";                   //柜员号              4（8001）
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
	private static final String FirstHead = HEAD + AP_CODE;
	private static final String SecondHead = REQ_TYPE + AUTH_LVL + SUP1_ID + SUP2_ID + SUP1_PSWD
											+ SUP2_PSWD + SUP1_DEV + SUP2_DEV + AUTH_RESN
											+ JRN_NO + CHK_CODE ;	
	public TuxedoPackHeadInit(){
		
	}
	/**
	 * 记账报文头
	 * @param TR_CODE	：交易码---支票转过渡(0152)、活期账户转内部账户(0160)、卡转过渡取款(0161)
	 * @param TL_TD		：操作柜员号
	 * @return			：记账报文头
	 */
	public static String getHead(String TR_CODE,String TL_TD)
	{
		return TuxeHead + FirstHead + TR_CODE + TR_FROM + TL_TD + TM_SEQ_NO + TL_TD + SecondHead + DATA1Length + DATA2Length;
	}
	/**
	 * 摸帐报文头
	 * @param TL_TD		：操作柜员号
	 * @return			：摸帐报文头
	 */
	public static String getUNDOHead(String TL_TD)
	{
		return TuxeHead + FirstHead + UNDOTR_CODE + TR_FROM + TL_TD + TM_SEQ_NO + TL_TD + SecondHead + "019000";
	}
	/**
	 * 查询报文头
	 * @param TL_TD		：操作柜员号
	 * @return			：摸帐报文头
	 */
	public static String getSQLHead(String TL_TD)
	{
		return TuxeSelecHead + FirstHead + SQL_CODE + TR_FROM + TL_TD + TM_SEQ_NO + TL_TD + SecondHead + "020480";
	}
	/**
	 * 校验纳税人密码
	 * @return
	 */
	public static String checkPassWordHead()
	{
		return TuxeHead + FirstHead + CHKPWSQL_CODE + TR_FROM + "8801" + TM_SEQ_NO + "8801" + SecondHead + DATA1Length + DATA2Length;
	}
	/**
	 * 校验柜员密码
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
