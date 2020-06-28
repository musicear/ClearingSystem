package resoft.tips.web.action;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TaxSubject implements Serializable{
  private String detailNo;
  private String taxSubjectCode;
  private String taxNumber;
  private String taxAmt;
  private String factTaxAmt;
  public TaxSubject() {
  }
  public String getDetailNo() {
    return detailNo;
  }
  public void setDetailNo(String detailNo) {
    this.detailNo = detailNo;
  }
  public String getTaxSubjectCode() {
    return taxSubjectCode;
  }
  public void setTaxSubjectCode(String taxSubjectCode) {
    this.taxSubjectCode = taxSubjectCode;
  }
  public String getTaxNumber() {
    return taxNumber;
  }
  public void setTaxNumber(String taxNumber) {
    this.taxNumber = taxNumber;
  }
  public String getTaxAmt() {
    return taxAmt;
  }
  public void setTaxAmt(String taxAmt) {
    this.taxAmt = taxAmt;
  }
  public String getFactTaxAmt() {
    return factTaxAmt;
  }
  public void setFactTaxAmt(String factTaxAmt) {
    this.factTaxAmt = factTaxAmt;
  }
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }
}
