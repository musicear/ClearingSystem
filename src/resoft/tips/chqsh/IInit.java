package resoft.tips.chqsh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>�������ת��ȡ��</p>
 * Author: fanchengwei
 */
public class IInit {
	private static final Log logger = LogFactory.getLog(IInit.class);
	//private static final String AC_NO = "";                      //�ʺ�        15����
	private static final String VCHTYP = "00";                     //ƾ֤����     2��00��
	private static final String PBNO = "         ";                //ƾ֤��       9���ո�
	private static final String DBK_AMT = "                   ";   //�������     19���ո�
	private static final String WDC_MODE = "1100";                 //֧�ط�ʽ     4��1100��
	private static final String PASWRD = "        ";               //�û�����     8���ո�
	private static final String ID_NO = "                  ";      //֤������     18���ո�
	private static final String SLCOD = "      ";                  //ӡ������     6���ո�
	private static final String TRN_TYPE = "91";                   //��������     2��91��
	//private static final String TRN_INF = "00009998010100008791";  //������Ϣ     20��0000+15λ�����ʺ�+1��
	//private static final String TRN_INF = "00009998010112230171";  //������Ϣ     20��0000+15λ�����ʺ�+1��
	//private static final Stirng AMT = "";                        //���         19����
	private static final String ADD_COD = "   ";                   //֤����������  3���ո�
	private static final String ID_TYPE = " ";                     //֤������      1���ո�
	private static final String CCY = "01";                        //���ױ���      2��01��
	private static final String TRN_CCY = "01";                    //���ɴ�Ʊ����  2��01��
	/**
	 * ����ֶΣ����ڱ���˰���ֶ�
	 */
//	private static final String ABS = "                              "; //ժҪ   30��30���ո�
	private static final String Idata1 =  VCHTYP + PBNO + DBK_AMT + WDC_MODE + PASWRD 
											+ ID_NO + SLCOD + TRN_TYPE;
	private static final String Idata2 = ADD_COD + ID_TYPE + CCY + TRN_CCY;
	private static String data1 = "";
	private static String data2 = "";
	private static int count = 0;
	
	/**
	 * �������ת��ȡ�������
	 * @param PayAcct		����˰���ʺ�
	 * @param TraAmt		����˰���
	 * @param TaxTypeName	��˰������
	 * @param TL_TD			��������Ա
	 * @param BankPayAcct	�������ʺ�
	 * @return				������ת�˱���
	 */
	public static String getI(String PayAcct,String TraAmt,String TaxTypeName,String TL_TD,String BankPayAcct,String PayeeBankNo)
	{
		/**
		 * ��ʼ������ժҪ
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
