package resoft.tips.chqsh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>储蓄活期转帐取款</p>
 * Author: fanchengwei
 */
public class IInit {
	private static final Log logger = LogFactory.getLog(IInit.class);
	//private static final String AC_NO = "";                      //帐号        15（）
	private static final String VCHTYP = "00";                     //凭证种类     2（00）
	private static final String PBNO = "         ";                //凭证号       9（空格）
	private static final String DBK_AMT = "                   ";   //存折余额     19（空格）
	private static final String WDC_MODE = "1100";                 //支控方式     4（1100）
	private static final String PASWRD = "        ";               //用户密码     8（空格）
	private static final String ID_NO = "                  ";      //证件号码     18（空格）
	private static final String SLCOD = "      ";                  //印鉴号码     6（空格）
	private static final String TRN_TYPE = "91";                   //过渡类型     2（91）
	//private static final String TRN_INF = "00009998010100008791";  //过渡信息     20（0000+15位贷方帐号+1）
	//private static final String TRN_INF = "00009998010112230171";  //过渡信息     20（0000+15位贷方帐号+1）
	//private static final Stirng AMT = "";                        //金额         19（）
	private static final String ADD_COD = "   ";                   //证件地区编码  3（空格）
	private static final String ID_TYPE = " ";                     //证件类型      1（空格）
	private static final String CCY = "01";                        //交易币种      2（01）
	private static final String TRN_CCY = "01";                    //过渡传票币种  2（01）
	/**
	 * 后加字段，用于保存税种字段
	 */
//	private static final String ABS = "                              "; //摘要   30（30个空格）
	private static final String Idata1 =  VCHTYP + PBNO + DBK_AMT + WDC_MODE + PASWRD 
											+ ID_NO + SLCOD + TRN_TYPE;
	private static final String Idata2 = ADD_COD + ID_TYPE + CCY + TRN_CCY;
	private static String data1 = "";
	private static String data2 = "";
	private static int count = 0;
	
	/**
	 * 储蓄活期转帐取款——报文
	 * @param PayAcct		：纳税人帐号
	 * @param TraAmt		：缴税金额
	 * @param TaxTypeName	：税种名称
	 * @param TL_TD			：操作柜员
	 * @param BankPayAcct	：贷方帐号
	 * @return				：活期转账报文
	 */
	public static String getI(String PayAcct,String TraAmt,String TaxTypeName,String TL_TD,String BankPayAcct,String PayeeBankNo)
	{
		/**
		 * 初始化金额和摘要
		 */
		TraAmt = TuxedoPackHeadInit.AMTFormat.substring(0, 19-TraAmt.length()) + TraAmt;
		String TRN_INF = "0000"+BankPayAcct+"1";
		String CMT = PayeeBankNo;
		CMT = CMT.trim();
		for(int i=CMT.length();i<40;i++){
			CMT = CMT + " ";
		}
		/**
		count = TaxTypeName.getBytes().length;
		if(count >= 30)
		{
			TaxTypeName = TaxTypeName.substring(0, 14);
		}
		TaxTypeName = ABS.substring(0, 30-count) + TaxTypeName;
		*/
		
		count = TaxTypeName.getBytes().length;
		if(count > 14)
		{
			TaxTypeName = TaxTypeName.substring(0, 7);
			
		}else{
			for(int i=count;i<14;i++){
				TaxTypeName = TaxTypeName +" ";
			}
		}
		count = TaxTypeName.getBytes().length;
		if(count < 14)
		{
			for(int i=count;i<14;i++){
				TaxTypeName = TaxTypeName +" ";
			}
		}
		TaxTypeName = TaxTypeName + PayeeBankNo;
		logger.info("TaxTypeName length "+TaxTypeName.getBytes().length);
		data1 = PayAcct + Idata1 + TRN_INF + TraAmt + Idata2 + TaxTypeName;
		count = data1.getBytes().length;
		for(int i=0;i<(250-count);i++)
		{
			data1 += " ";
		} 
		data2 = TuxedoData2Init.data2Head1 + BankPayAcct + TuxedoData2Init.data2Head2 + TraAmt + TuxedoData2Init.data2forIandINPT+ PayAcct + TuxedoData2Init.GROUP2;
		count = data2.length();
		for(int i=0;i<(251-count);i++)
		{
			data2 += " ";
		}
		return TuxedoPackHeadInit.getHead("0160", TL_TD) + data1 + data2;
	}
}
