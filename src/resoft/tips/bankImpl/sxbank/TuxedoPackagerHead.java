package resoft.tips.bankImpl.sxbank;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public class TuxedoPackagerHead {
	
	//ϵͳ��Ϣͷ
	private static final Log logger = LogFactory.getLog(TuxedoPackagerHead.class);
	public String getSysMsgHead(){
		String SCAD = "    "; //Ŀ���ַ 4
		String DNAD = "    "; //Դ��ַ 4
		String RSVB = " "; // ϵͳ����λ 1 
		String MGEF = " "; //��Ϣ������־ 1
		String MGSQ = "  "; //�������  2
		String IFCK = " "; //У���־ 1
		String CKVL = "        "; //У��ֵ 8
		
		
		
		return StToChar(SCAD + DNAD + RSVB + MGEF + MGSQ + IFCK + CKVL);
	}
	private String StToChar(String oristr)
	{
		String returnstr="";
		char c = (char)0;
		for(int i=0;i<oristr.length();i++)
		{
			String s=oristr.substring(i,i+1);
			if(s.equals(" "))
				returnstr=returnstr+c;
			else
				returnstr=returnstr+s;
			
		}
		logger.info("����ͷ���ȷֱ�Ϊ"+returnstr.length());
		return returnstr;
	}
	//���׹���ͷ
	public String getTransHead(){
		String WSNO = "p0000"; //�����ն˺� 5
		String CTNO = "    "; //���д��� 4
		String SBNO = "0101"; //�������� 4
		String USID = "01019920";//���׹�Ա 8

		return StToChar(WSNO + CTNO + SBNO + USID);
	}
	
	//��������ͷ
	public String getTransDateHead(int flag){
		String tranCode = null;
		if(flag == 1)					//1:ʵʱ���� 2:�������� 3:���˽���
			tranCode = "7645";
		else if(flag==2)
			tranCode="7867";
		else if(flag==3)
			tranCode="7629";
		String TRCD = tranCode; //���״��� 4 
		String TRSD = "    "; //�������� 4
	    String TRMD = " "; //����ģʽ 1
	    String TRSQ = "  "; //������� 2
	    String TRLN = "  "; //�����װ�����2
	    String OFF1 = "  "; //ϵͳƫ��1 2λ
	    String OFF2 = "  "; //ϵͳƫ��2 2λ
	    String AUUS = "        "; //��Ȩ��Ա 8
	    String AUPS = "        ";//��Ȩ���� 8
		
	    return StToChar(TRCD + TRSD + TRMD + TRSQ + TRLN + OFF1 + OFF2 + AUUS + AUPS);
	}
	
	public String getPackagerHead(int flag){
		
		return getSysMsgHead() + getTransHead() + getTransDateHead(flag);
	}
}
