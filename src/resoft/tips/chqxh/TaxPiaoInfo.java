package resoft.tips.chqxh;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.tips.util.DateTimeUtil;
/**
 * <p>˰Ʊ��Ϣ</p>
 * Author: liwei
 * Date: 2007-10-22
 * Time: 18:06:06
 */
public class TaxPiaoInfo {
	private static final Log logger = LogFactory.getLog(TaxPiaoInfo.class);
	String lineBytesCount="104";
	private String[] deatilNames={"TaxPayCode","TaxPayName","HandOrgName","PayAcct","PayOpBkName","TaxOrgName","PayeeOrgName","TraAmt","TraNo","TraAmtStr","TaxVodNo"};
	private String[] deatilNamesFormats={lineBytesCount,lineBytesCount,lineBytesCount,lineBytesCount,lineBytesCount,lineBytesCount,lineBytesCount,"58","46","58","46"};
	
	private String sql="";
	public String piaoInfo="",taxTypeInfo="",lineStartFormat="";
	public static String printTeller="";
	public static int taxVodTypeCount=15,startNullRows=3;	
	
	public Map packTags=new HashMap();
	
	public TaxPiaoInfo(){
		//���ױ���ͷ
		packTags.put("TaxPayCode", "��˰��ȫ�Ƽ���˰��ʶ��ţ�");					//��˰�˱���			13*2,104
		packTags.put("TaxPayName","");											//��˰������ 			104
		packTags.put("HandOrgName","������ȫ�ƣ�");								//����������			6*2,104  		
		packTags.put("PayAcct","�������˺ţ�");									//�������ʺ�			6*2,104			  		
		packTags.put("PayOpBkName","�����˿������У�");							//�����˿�������		8*2,104
		packTags.put("TaxOrgName","���ջ������ƣ�");								//���ջ�������		7*2,104		  					  		
		packTags.put("PayeeOrgName","�տ����(����)���ƣ�");						//�տ��������		10*2,104	
		
		packTags.put("TraAmt","Сд(�ϼ�)����");								//Сд���			8*2+16,22*2				  		
		packTags.put("TraNo","�ɿ��齻����ˮ�ţ�");								//���׺�				9*2,30*2	  			
		
		packTags.put("TraAmtStr", "��д(�ϼ�)��");							//��д���			8*2+16,38*2	
		packTags.put("TaxVodNo","˰Ʊ���룺");									//˰Ʊ����			5*2+18,28
										
	}		
	
	public String initTaxPiaoInfo(Map params) throws Exception {
		String taxVodType=(String)params.get("TaxVodType");
		if (taxVodType.equals("1")) {
			sql="select * from VoucherTaxType where bizCode='1' and taxOrgCode='"+(String)params.get("taxOrgCode")+"' and entrustDate='"+(String)params.get("entrustDate")+"' and traNo='"+(String)params.get("traNo")+"' ";
		}else if(taxVodType.equals("2")){
			sql="select * from VoucherTaxType where bizCode='2' and taxOrgCode='"+(String)params.get("taxOrgCode")+"' and entrustDate='"+(String)params.get("entrustDate")+"' and packNo='"+(String)params.get("packNo")+"' and traNo='"+(String)params.get("traNo")+"' ";
		}
		//logger.info("˰����Ϣ��ѯ��"+sql);
		
		packTags.put("TaxPayCode", (String)packTags.get("TaxPayCode")+(String)params.get("taxPayCode"));
		packTags.put("TaxPayName", (String)packTags.get("TaxPayName")+(String)params.get("taxPayName"));
		packTags.put("HandOrgName", (String)packTags.get("HandOrgName")+(String)params.get("handOrgName"));
		packTags.put("PayAcct", (String)packTags.get("PayAcct")+(String)params.get("payAcct"));
		packTags.put("TraAmt", (String)packTags.get("TraAmt")+(String)params.get("traAmt"));
		packTags.put("TraNo", (String)packTags.get("TraNo")+(String)params.get("TraNo"));
		packTags.put("TaxVodNo", (String)packTags.get("TaxVodNo")+(String)params.get("taxVouNo"));
		packTags.put("TraAmtStr",(String)packTags.get("TraAmtStr")+MoneyUtil.amountToChinese(Double.parseDouble((String)params.get("traAmt"))));
					
		//��ѯ˰Ʊ˰����ϸ��Ϣ
		List queryList=QueryUtil.queryRowSet(sql);		
		if (queryList.size()>0) {
			//��ӡƱ����
			for (int i=0;i<startNullRows;i++) {
				piaoInfo+="\n";
			}					
			//�п�ʼ��ӡ��ʽ
			lineStartFormat=ACEPackUtil.leftStrFormat("10", ""," ");
			//��ӡת������
			piaoInfo+=lineStartFormat+ACEPackUtil.leftStrFormat("9", ""," ")+(String)params.get("bankTraDate")+"\n";						
			//��ӡƱͨ����Ϣ
			for(int i=0;i<deatilNames.length;i++){
				//����ӡ����ʼ��ʽ
				if(deatilNames[i].equals("TraAmt")||deatilNames[i].equals("TraAmtStr")){
					piaoInfo+=lineStartFormat+ACEPackUtil.leftStrFormat(deatilNamesFormats[i],(String)packTags.get(deatilNames[i])," ");
					continue;
				}else {//��ӡ����ʼ��ʽ
					piaoInfo+=lineStartFormat+ACEPackUtil.leftStrFormat(deatilNamesFormats[i],(String)packTags.get(deatilNames[i])," ");
					piaoInfo+="\n";
				}
			}
			piaoInfo+=lineStartFormat+ACEPackUtil.leftStrFormat("46","˰(��)������"," ")+ACEPackUtil.leftStrFormat("12","����ʱ��"," ")+ACEPackUtil.leftStrFormat("8",""," ")+ACEPackUtil.leftStrFormat("38","ʵ�ɽ��"," ");
			piaoInfo+="\n";
			//��ӡ˰����ϸ��Ϣ
			for (int i=0;i<queryList.size();i++) {				
				Map row=(Map)queryList.get(i);
				//��ӡ�п�ʼ��ʽ
				taxTypeInfo+=lineStartFormat;
				//˰�� 60
				taxTypeInfo+=ACEPackUtil.leftStrFormat("40",(String)row.get("TaxTypeName")," ");
				//�������� 18
				taxTypeInfo+=ACEPackUtil.leftStrFormat("9"," "+(String)row.get("TaxStartDate")," ");
				taxTypeInfo+="-";
				taxTypeInfo+=ACEPackUtil.leftStrFormat("8",(String)row.get("TaxEndDate")," ");
				//8���ո�
				taxTypeInfo+=ACEPackUtil.leftStrFormat("8",""," ");
				//ʵ�ɽ�� 18
				taxTypeInfo+=ACEPackUtil.leftStrFormat("38","��"+(String)row.get("TaxTypeAmt")," ");
				taxTypeInfo+="\n";
			}
			//ÿ��˰Ʊ��ӡ15��˰��
			for (int i=0;i<(taxVodTypeCount-queryList.size());i++) {
				taxTypeInfo+="\n";
			}
			//10+2+6+6+10+20+10+10+30   ��ӡ��ӡʱ�����
			taxTypeInfo+=lineStartFormat+ACEPackUtil.leftStrFormat("10",""," ")+"��"+ACEPackUtil.leftStrFormat("6",""+(Integer.parseInt((String)params.get("printTimes"))+1)," ")+"�δ�ӡ"+ACEPackUtil.leftStrFormat("10",""," ")+ACEPackUtil.leftStrFormat("20","����Ա��"+TaxPiaoInfo.printTeller," ")+ACEPackUtil.leftStrFormat("10",""," ")+"��ӡʱ�䣺"+ACEPackUtil.leftStrFormat("30",DateTimeUtil.getDateTimeString()," ");
			//��ӡƱβ
			for (int i=0;i<3;i++) {
				taxTypeInfo+="\n";
			}
		}
		
		return piaoInfo+taxTypeInfo;
	}	
}
