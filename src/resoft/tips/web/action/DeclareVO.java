package resoft.tips.web.action;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeclareVO implements Serializable{
  private String taxStartDate;
  private String taxEndDate;
  private String taxType;
  private String taxTypeName;
  private String taxTypeCode;
  private List taxSubjectList=new ArrayList();
  public DeclareVO() {
  }
  public String getTaxStartDate() {
    return taxStartDate;
  }
  public void setTaxStartDate(String taxStartDate) {
    this.taxStartDate = taxStartDate;
  }
  public String getTaxEndDate() {
    return taxEndDate;
  }
  public void setTaxEndDate(String taxEndDate) {
    this.taxEndDate = taxEndDate;
  }
  public String getTaxType() {
    return taxType;
  }
  public void setTaxType(String taxType) {
    this.taxType = taxType;
  }
  public String getTaxTypeName() {
    return taxTypeName;
  }
  public void setTaxTypeName(String taxTypeName) {
    this.taxTypeName = taxTypeName;
  }
  public String getTaxTypeCode() {
    return taxTypeCode;
  }
  public void setTaxTypeCode(String taxTypeCode) {
    this.taxTypeCode = taxTypeCode;
  }
  public List getTaxSubjectList() {
    return taxSubjectList;
  }
  public void setTaxSubjectList(List taxSubjectList) {
    this.taxSubjectList = taxSubjectList;
  }
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }
}
