package resoft.tips.chqsh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>֧Ʊ�˻�ת��ȡ��(INPT)</p>
 * Author: fanchengwei
 */
public class INPTInit {   
	private static final Log logger = LogFactory.getLog(INPTInit.class);
	//private static final String OutAC_NO = "";                        //�ʺ�          15����
	private static final String CCY = "01";                             //���ױ���      2��01��
	//private static final String AMT = "";                             //���׽��      18����
	private static final String TranCCY = "01";                         //���ɴ�Ʊ����  2��01��
	private static final String TranACSel = "91";                       //��������      2��91��
	private static final String TranMsg = "00009998010112230171";       //������Ϣ      20��0000+15λֵ+1��
	private static final String Voucher = "00";                         //ƾ֤����      2(00)
	private static final String VouchNo = "000000000";                  //ƾ֤��        9��000000000��
	/**
	 * ժҪ�ֶΣ����ڱ���˰���ֶ�
	 */
	private static final String ABS = "                              "; //ժҪ   30��30���ո�
	private static final String CDWriDate = "        ";                 //֧Ʊǩ������  8(8���ո�
	private static final String Passwd = "          ";                  //����          10��10���ո�
	private static String data1 = "";
	private static String data2 = "";
	private static int count = 0;
	private static final String Inptdata21 = TranCCY + TranACSel;
	private static final String Inptdata22 = Voucher + VouchNo;
//											+ ABS + CDWriDate + Passwd;
	/**
	 * ע������				��֧Ʊ�˻����͵Ľ����18λ�أ���������Ŀ��ͻ��ڵĶ���19λ�أ�
	 * @param PayAcct		����˰���ʺ�
	 * @param TraAmt		�����
	 * @param TaxTypeName	��˰������
	 * @param TL_TD			��������Ա
	 * @param BankPayAcct	�������ʺ�
	 * @return				��֧Ʊ�˻�����
	 */
	public static String getINPT(String PayAcct,String TraAmt,String TaxTypeName,String TL_TD,String BankPayAcct,String PayeeBankNo)
	{
		/**
		 * ��ʼ������ժҪ��˰�����ƣ�
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










