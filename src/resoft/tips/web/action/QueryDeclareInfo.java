package resoft.tips.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Message;
import resoft.common.web.AbstractAction;
import resoft.tips.util.TransCommUtil;

/**
 * <p/>
 * �����걨��ѯ
 * ����T2300�����걨��Ϣ
 * </p>
 * Author: zhuchangwu Date: 2007-8-21 Time: 15:19:58
 */
public class QueryDeclareInfo extends AbstractAction {
    private static final Log logger = LogFactory.getLog(QueryDeclareInfo.class);

    public String execute() {
        //�����걨��ѯ��������ϵͳ����T2300����
        Message msg = new Message();
        msg.setValue("TaxPayCode", taxPayCode);
        msg.setValue("TaxOrgCode", taxOrgCode);
        msg.setValue("OuterLevyNo", outerLevyNo);
        Message returnMsg;
        try {
            returnMsg = TransCommUtil.send("T2300", msg);
        } catch (Exception e) {
            setMessage("���Ӻ�̨����ϵͳʧ��");
            logger.error("���Ӻ�̨����ϵͳʧ��", e);
            return ERROR;
        }
        if (!"Y".equals(returnMsg.getValue("Result"))) {
            setMessage("�걨��ѯʧ�ܡ��޴��걨��Ϣ");
            return ERROR;
        }
        //���ݲ�ѯ�걨�Ľ������ֱ���걨
        int DetailNum = Integer.parseInt(returnMsg.getValue("DetailNum"));//�ܼ�¼����
        Message msg2100 = new Message();
        msg2100.setValue("TaxPayCode", taxPayCode);
        msg2100.setValue("TaxOrgCode", taxOrgCode);
        msg2100.setValue("PayAcct", "99999999");
        msg2100.setValue("HandOrgName", "saffdasf");
        msg2100.setValue("LevyState", "1");
        msg2100.setValue("OuterLevyNo", outerLevyNo);
        msg2100.setValue("DetailNum", String.valueOf(DetailNum));
        msg2100.setValue("TraAmt", returnMsg.getValue("TraAmt"));
        for (int j = 0; j < DetailNum; j++) {
            msg2100.setValue("TaxTypeName" + j, returnMsg.getValue("TaxTypeName" + String.valueOf(j)));
            msg2100.setValue("TaxTypeCode" + j, returnMsg.getValue("TaxTypeCode" + String.valueOf(j)));
            msg2100.setValue("TaxStartDate" + j, returnMsg.getValue("TaxStartDate" + String.valueOf(j)));
            msg2100.setValue("TaxEndDate" + j, returnMsg.getValue("TaxEndDate" + String.valueOf(j)));
            msg2100.setValue("TaxType" + j, returnMsg.getValue("TaxType" + String.valueOf(j)));
            msg2100.setValue("TaxSubjectCode" + j, returnMsg.getValue("TaxSubjectCode" + String.valueOf(j)));
            msg2100.setValue("TaxNumber" + j, returnMsg.getValue("TaxNumber" + String.valueOf(j)));
            msg2100.setValue("TaxAmt" + j, returnMsg.getValue("TaxAmt" + String.valueOf(j)));
            msg2100.setValue("FactTaxAmt" + j, returnMsg.getValue("FactTaxAmt" + String.valueOf(j)));
        }
        Message returnMsg2100;
        try {
            returnMsg2100 = TransCommUtil.send("T2100", msg2100);
        } catch (Exception e) {
            setMessage("���Ӻ�̨����ϵͳʧ��");
            logger.error("���Ӻ�̨����ϵͳʧ��", e);
            return ERROR;
        }
        //��ʾ�걨�����ҳ����
        if ("Y".equals(returnMsg2100.getValue("Result"))) {
            setMessage("�걨ʧ�ܡ�" + returnMsg2100.getValue("AddWord"));
            return ERROR;
        }
        handOrgName = returnMsg2100.getValue("HandOrgName");//�ɿλ����˰Ʊ ԭ�ӽ��׻�û��ʵ��
        taxVouNo = returnMsg2100.getValue("TaxVouNo");//˰Ʊ ԭ�ӽ��׻�û��ʵ��
        traNo = returnMsg2100.getValue("traNoNodePath");//��ˮ��
        entrustDate = returnMsg2100.getValue("entrustNodePath");//ί������
        HashMap typeMap = new HashMap();
        for (int i = 1; i <= DetailNum; i++) {
            String taxTypeCode = returnMsg2100.getValue("TaxTypeCode" + String.valueOf(i));
            if (!typeMap.containsValue(taxTypeCode)) {
                DeclareVO declareVO = new DeclareVO();
                if (i != 1)
                    //declareVO.setTaxSubjectList(subList);//����˰�ֵ�˰Ŀ���϶���
                    typeMap.put("TaxTypeCode", taxTypeCode);

                List subList = new ArrayList();
                declareVO.setTaxTypeCode(taxTypeCode);
                declareVO.setTaxTypeName(returnMsg2100.getValue("TaxTypeName" + String.valueOf(i)));
                declareVO.setTaxStartDate(returnMsg2100.getValue("TaxStartDate" + String.valueOf(i)));
                declareVO.setTaxEndDate(returnMsg2100.getValue("TaxEndDate" + String.valueOf(i)));
                declareVO.setTaxType(returnMsg2100.getValue("TaxType" + String.valueOf(i)));
                typeList.add(declareVO);
            }
            TaxSubject subVo = new TaxSubject();
            subVo.setTaxSubjectCode(returnMsg2100.getValue("TaxSubjectCode" + String.valueOf(i)));
            subVo.setTaxNumber(returnMsg2100.getValue("TaxNumber" + String.valueOf(i)));
            subVo.setTaxAmt(returnMsg2100.getValue("TaxAmt" + String.valueOf(i)));
            subVo.setFactTaxAmt(returnMsg2100.getValue("FactTaxAmt" + String.valueOf(i)));
            //subList.add(subVo);
//            if (i == DetailNum)
//                declareVO.setTaxSubjectList(subList);


        }
        return SUCCESS;

    }

    public String getTaxPayCode() {
        return taxPayCode;
    }

    public void setTaxPayCode(String taxPayCode) {
        this.taxPayCode = taxPayCode;
    }

    public String getTaxOrgCode() {
        return taxOrgCode;
    }

    public void setTaxOrgCode(String taxOrgCode) {
        this.taxOrgCode = taxOrgCode;
    }

    public String getOuterLevyNo() {
        return taxPayCode;
    }

    public void setOuterLevyNo(String outerLevyNo) {
        this.outerLevyNo = outerLevyNo;
    }

    public List getTypeList() {
        return typeList;
    }

    public String getTaxVouNo() {
        return taxVouNo;
    }

    public String getHandOrgName() {
        return handOrgName;
    }

    public void setHandOrgName(String handOrgName) {
        this.handOrgName = handOrgName;
    }

    private String taxPayCode;
    private String taxOrgCode;
    private String outerLevyNo;
    private List typeList = new ArrayList();
    private String taxVouNo;
    private String traNo;
    private String entrustDate;
    private String handOrgName;
}
