package com.laika.laika_yellow_book;

import java.util.Date;

//new cow entry has int values at -1
public class DataLine {
    private DataLine dataLine;
    int cowNum;
    Date dueCalveDate;
    int sireOfCalf;
    double calfBW;
    Date calvingDate;
    String calvingDiff;
    String condition;
    String sex;
    String fate;
    int calfIndentNo;
    String remarks;

    public DataLine() {
        dataLine = new DataLine(0, new Date(), 0, 0, new Date(), "", "", "", "", 0,"");
    }

    public DataLine(int cowNum, Date dueCalveDate, int sireOfCalf, double calfBW, Date calvingDate, String calvingDiff, String condition, String sex, String fate, int calfIndentNo, String remarks) {
        this.cowNum = cowNum;
        this.dueCalveDate = dueCalveDate;
        this.sireOfCalf = sireOfCalf;
        this.calfBW = calfBW;
        this.calvingDate = calvingDate;
        this.calvingDiff = calvingDiff;
        this.condition = condition;
        this.sex = sex;
        this.fate = fate;
        this.calfIndentNo = calfIndentNo;
        this.remarks = remarks;
    }
}
