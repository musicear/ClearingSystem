package resoft.tips.chqsh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>卡账户转帐取款</p>
 * Author: fanchengwei
 */
public class IN_DATAInit {
	private static final Log logger = LogFactory.getLog(IN_DATAInit.class);
	/**
	 * 卡号加3个空格构成此报文的卡号
	 * I_CARDNO = PayAcct + "   ";
	 */
	//private static final String I_CARDNO = PayAcct;
	private static final String I_CARDNO = "   ";                     //卡号     19（16位卡号+3个空格）
	private static final String I_CARDPWD = "        ";               //卡密码   8（空格）
	private static final String I_SEQ_NO = "99    ";                  //顺序号   6（99+4个空格）
	private static final String I_AC_NO = "               ";          //帐号     15（空格）
	//private static final String I_DEP_AMT = "";                     //交易金额 19（）
	private static final String I_TRN_TYP = "91";                     //过渡类型 2（91）
	private static final String I_TRN_CCY = "01";                     //交易币种 2（01）
	//private static final String I_TRN_INF = "00009998010112230171";   //过渡信息 20（00009998010100008791）
	/**
	 * 后加字段，用于保存税种字段
	 */
	private static final String ABS = "                              "; //摘要   30（30个空格）
	
	private static final String IN_DATAdata2 = I_TRN_TYP + I_TRN_CCY;
	private static String data1 = "";
	private static String data2 = "";
	private static int count = 0;
	
	/**
	 * @param PayAcct	：纳税人帐号
	 * @param TraAmt	：扣款金额
	 * @param TL_TD		：操作柜员
	 * @param BankPayAcct	：贷方帐号
	 * @return			：返回卡账户报文
	 */
	//yangyuanxu update
	public static String getIN_DATA(String PayAcct,String TraAmt,String TaxTypeName,String TL_TD,String BankPayAcct,String PayeeBankNo)
	{
		/**
		 * 初始化金额和摘要
		 */
		TraAmt = TuxedoPackHeadInit.AMTFormat.substring(0, 19-TraAmt.length()) + TraAmt;
		
		String I_TRN_INF = "0000"+BankPayAcct+"1";
		String CMT = PayeeBankNo;
		CMT = CMT.trim();
		for(int i=CMT.length();i<40;i++){
			CMT = CMT + " ";
		}
		String IN_DATAGROUP2 = "0000"+PayAcct+"1";
		/**
		count = TaxTypeName.getBytes().length;
		if(count >= 30)
		{
			TaxTypeName = TaxTypeName.substring(0, 14);
		}
		TaxTypeName = ABS.substring(0, 30-count) + TaxTypeName;	
		*/	
		count = TaxTypeName.getBytes().length;
		logger.info("TaxTypeName length "+TaxTypeName.getBytes().length);
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
		data1 = PayAcct + I_CARDNO + I_CARDPWD + I_SEQ_NO + I_AC_NO + TraAmt + IN_DATAdata2 + I_TRN_INF + TaxTypeName;
		count = data1.getBytes().length;
		for(int i=0;i<(250-count);i++)
		{
			data1 += " ";
		}
		data2 = TuxedoData2Init.data2Head1 + BankPayAcct + TuxedoData2Init.data2Head2 + TraAmt + TuxedoData2Init.data2forIN_DATA + IN_DATAGROUP2;
		count = data2.length();
		for(int i=0;i<(251-count);i++)
		{
			data2 += " ";
		}
		return TuxedoPackHeadInit.getHead("0161", TL_TD) + data1 + data2;
	}
	
}
