package resoft.tips.chqsh;

/**
 * @author fanchengwei
 * date2008-6-8
 * time上午09:00:39
 */
public class TuxedoXianJin {

	/**
	 * 由于商行的Tuxedo系统分为记账系统和查询系统（由C根据第一个字段判断发往那个系统）
	 * TIPS3001：发往记账系统；
	 * TIPS3002：发往查询系统；
	 */
	private static final String TuxeHead = "TIPS3001";            //用于C判断是否是来自TIPS的请求
//	private static final String TuxeSelecHead = "TIPS3002";       //用于C判断是否是来自TIPS的请求
	
	private static final String HEAD = "0000010000";              //Tuxedo报文头      10（0000010000）
	private static final String AP_CODE = "01";                   //应用码             2（01）
	public static final String TR_CODE = "0101";                  //交易码，是个变量    4
	
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
