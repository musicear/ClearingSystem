package resoft.tips.chqsh;

public class TuxedoData2Init {

	private static final String OPER = "91";                      //过渡类型      2（91）
	//private static final String CRAC = "999801010000879";         //贷方帐号      15（999801010000879）
	//private static final String CRAC = "999801011223017";         //贷方帐号      15（999801010000879）
	
	private static final String VCHTYPE = "00";                   //凭证种类      2(00)
	private static final String VCHNO = "123456789";              //凭证号        9（随便9个数）
	private static final String TRCCY = "01";                     //交易币种      2（01）
	//private static final String TRAMT = "";                     //交易金额      19（）
	private static final String CMT = "                                        ";
																  //备注      能添加的内容    40（空格）
	private static final String RVSOCUDATE = "        ";          //销帐日期       8（空格）
	private static final String RVSOCUVCH = "        ";           //销帐传票号     8（空格）
	private static final String GROUP1 = "0000";
	//private static final String NO = "";                        //帐号           15（）
	//活期储蓄（I）和支票（INPT）
	public static final String GROUP2 ="1";
	//卡账户（IN_DATA）
	//private static final String IN_DATAGROUP2 = "0000               1";//过渡信息  添加借方帐号   20（0000+15位值+1）
	
	public static String data2Head1 = OPER;
	public static String data2Head2 = VCHTYPE + VCHNO + TRCCY;
	//I（活期）和INPT（支票）账户数据区2：data2Head + TRAMT + data2forIandINPT + NO + GROUP2 
	public static String data2forIandINPT = CMT + RVSOCUDATE + RVSOCUVCH + GROUP1;
	//public static String data2forIandINPT = RVSOCUDATE + RVSOCUVCH + GROUP1;
	//IN_DATA（卡）账户数据区2：data2Head + TRAMT + data2forIN_DATA
	public static String data2forIN_DATA = data2forIandINPT;
}
