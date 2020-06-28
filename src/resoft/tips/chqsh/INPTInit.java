package resoft.tips.chqsh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>支票账户转帐取款(INPT)</p>
 * Author: fanchengwei
 */
public class INPTInit {   
	private static final Log logger = LogFactory.getLog(INPTInit.class);
	//private static final String OutAC_NO = "";                        //帐号          15（）
	private static final String CCY = "01";                             //交易币种      2（01）
	//private static final String AMT = "";                             //交易金额      18（）
	private static final String TranCCY = "01";                         //过渡传票币种  2（01）
	private static final String TranACSel = "91";                       //过渡类型      2（91）
	private static final String TranMsg = "00009998010112230171";       //过渡信息      20（0000+15位值+1）
	private static final String Voucher = "00";                         //凭证种类      2(00)
	private static final String VouchNo = "000000000";                  //凭证号        9（000000000）
	/**
	 * 摘要字段，用于保存税种字段
	 */
	private static final String ABS = "                              "; //摘要   30（30个空格）
	private static final String CDWriDate = "        ";                 //支票签发日期  8(8个空格）
	private static final String Passwd = "          ";                  //密码          10（10个空格）
	private static String data1 = "";
	private static String data2 = "";
	private static int count = 0;
	private static final String Inptdata21 = TranCCY + TranACSel;
	private static final String Inptdata22 = Voucher + VouchNo;
//											+ ABS + CDWriDate + Passwd;
	/**
	 * 注意事项				：支票账户发送的金额是18位地！！（其余的卡和活期的都是19位地）
	 * @param PayAcct		：纳税人帐号
	 * @param TraAmt		：金额
	 * @param TaxTypeName	：税种名称
	 * @param TL_TD			：操作柜员
	 * @param BankPayAcct	：贷方帐号
	 * @return				：支票账户报文
	 */
	public static String getINPT(String PayAcct,String TraAmt,String TaxTypeName,String TL_TD,String BankPayAcct,String PayeeBankNo)
	{
		/**
		 * 初始化金额和摘要（税种名称）
		 */
		TraAmt = TuxedoPackHeadInit.AMTFormat.substring(0, 18-TraAmt.length()) + TraAmt;
		
		/*
		if(count >= 30)
		{
			TaxTypeName = TaxTypeName.substring(0, 14);
		}
		*/
		String TranMsg = "0000"+BankPayAcct+"1";
		
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
		logger.info("TaxTypeName length "+TaxTypeName.getBytes().length);
		TaxTypeName = TaxTypeName + PayeeBankNo;
		count = TaxTypeName.getBytes().length;
		logger.info("TaxTypeName length "+TaxTypeName.getBytes().length);
		TaxTypeName = ABS.substring(0, 30-count) + TaxTypeName;
		logger.info("TaxTypeName length "+TaxTypeName.getBytes().length);
		data1 = PayAcct+ CCY + TraAmt + Inptdata21 + TranMsg + Inptdata22 + TaxTypeName + CDWriDate + Passwd;
		count = data1.getBytes().length;
		for(int i=0;i<(250-count);i++)
		{
			data1 += " ";
		}
		data2 = TuxedoData2Init.data2Head1 + BankPayAcct + TuxedoData2Init.data2Head2 + " " + TraAmt + TuxedoData2Init.data2forIandINPT + PayAcct +TuxedoData2Init.GROUP2;
		count = data2.length();
		for(int i=0;i<(251-count);i++)
		{
			data2 += " ";
		}
		return TuxedoPackHeadInit.getHead("0152", TL_TD) + data1 + data2;
	}
}










