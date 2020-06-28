package resoft.tips.chqsh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>���˻�ת��ȡ��</p>
 * Author: fanchengwei
 */
public class IN_DATAInit {
	private static final Log logger = LogFactory.getLog(IN_DATAInit.class);
	/**
	 * ���ż�3���ո񹹳ɴ˱��ĵĿ���
	 * I_CARDNO = PayAcct + "   ";
	 */
	//private static final String I_CARDNO = PayAcct;
	private static final String I_CARDNO = "   ";                     //����     19��16λ����+3���ո�
	private static final String I_CARDPWD = "        ";               //������   8���ո�
	private static final String I_SEQ_NO = "99    ";                  //˳���   6��99+4���ո�
	private static final String I_AC_NO = "               ";          //�ʺ�     15���ո�
	//private static final String I_DEP_AMT = "";                     //���׽�� 19����
	private static final String I_TRN_TYP = "91";                     //�������� 2��91��
	private static final String I_TRN_CCY = "01";                     //���ױ��� 2��01��
	//private static final String I_TRN_INF = "00009998010112230171";   //������Ϣ 20��00009998010100008791��
	/**
	 * ����ֶΣ����ڱ���˰���ֶ�
	 */
	private static final String ABS = "                              "; //ժҪ   30��30���ո�
	
	private static final String IN_DATAdata2 = I_TRN_TYP + I_TRN_CCY;
	private static String data1 = "";
	private static String data2 = "";
	private static int count = 0;
	
	/**
	 * @param PayAcct	����˰���ʺ�
	 * @param TraAmt	���ۿ���
	 * @param TL_TD		��������Ա
	 * @param BankPayAcct	�������ʺ�
	 * @return			�����ؿ��˻�����
	 */
	//yangyuanxu update
	public static String getIN_DATA(String PayAcct,String TraAmt,String TaxTypeName,String TL_TD,String BankPayAcct,String PayeeBankNo)
	{
		/**
		 * ��ʼ������ժҪ
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
