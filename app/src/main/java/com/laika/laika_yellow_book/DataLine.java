package com.laika.laika_yellow_book;

import java.util.Date;

//new cow entry has int values at -1
class DataFields {
    int cowNum =-1;
    Date dueCalveDate;
    int sireOfCalf =-1;
    double calfBW =-1;
    Date calvingDate;
    String calvingDiff;
    String condition;
    String sex;
    String fate;
    int calfIndentNo =-1;
    String remarks;

    public void DataFields() {
        //default
    }

    public void setCowNum(int cowNum) {
        this.cowNum = cowNum;
    }

    public void setDueCalveDate(Date dueCalveDate) {
        this.dueCalveDate = dueCalveDate;
    }

    public void setSireOfCalf(int sireOfCalf) {
        this.sireOfCalf = sireOfCalf;
    }

    public void setCalfBW(double calfBW) {
        this.calfBW = calfBW;
    }

    public void setCalvingDate(Date calvingDate) {
        this.calvingDate = calvingDate;
    }

    public void setCalvingDiff(String calvingDiff) {
        this.calvingDiff = calvingDiff;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setFate(String fate) {
        this.fate = fate;
    }

    public void setCalfIndentNo(int calfIndentNo) {
        this.calfIndentNo = calfIndentNo;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getCowNum() {
        return cowNum;
    }

    public Date getDueCalveDate() {
        return dueCalveDate;
    }

    public int getSireOfCalf() {
        return sireOfCalf;
    }

    public double getCalfBW() {
        return calfBW;
    }

    public Date getCalvingDate() {
        return calvingDate;
    }

    public String getCalvingDiff() {
        return calvingDiff;
    }

    public String getCondition() {
        return condition;
    }

    public String getSex() {
        return sex;
    }

    public String getFate() {
        return fate;
    }

    public int getCalfIndentNo() {
        return calfIndentNo;
    }

    public String getRemarks() {
        return remarks;
    }
}
