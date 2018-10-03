package com.laika.laika_yellow_book;

public class CowSearched {

    public int cowNum, sire, calfID, rowID;


    public CowSearched(int cowNum, int sire, int calfID, int rowID)
    {
        this.setCowNum(cowNum);
        this.setSire(sire);
        this.setCalfID(calfID);
        this.setRowID(rowID);
    }


    public int getCowNum() {
        return cowNum;
    }

    public void setCowNum(int cowNum) {
        this.cowNum = cowNum;
    }

    public int getSire() {
        return sire;
    }

    public void setSire(int sire) {
        this.sire = sire;
    }

    public int getCalfID() {
        return calfID;
    }

    public void setCalfID(int calfID) {
        this.calfID = calfID;
    }

    public int getRowID() { return rowID; }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }
}
