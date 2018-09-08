package com.laika.laika_yellow_book;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DbHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "Calving.db";
    public static final String TABLE_NAME = "Calving_Table";

    public static final String COL1 = "CowNum";
    public static final String COL2 = "DueCalveDate";
    public static final String COL3 = "SireOfCalf";
    public static final String COL4 = "CalfBW";
    public static final String COL5 = "CalvingDate";
    public static final String COL6 = "CalvingDiff";
    public static final String COL7 = "Condition";
    public static final String COL8 = "Sex";
    public static final String COL9 = "Fate";
    public static final String COL10 = "CalfID";
    public static final String COL11 = "Remarks";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+COL1+" INTEGER, "+COL2+" DATE, "+COL3+" INTEGER, "+COL4+" DOUBLE, "+COL5+" DATE, "+COL6+" TEXT, "+COL7+" TEXT, "+COL8+" TEXT, "+COL9+" TEXT, "+COL10+" INTEGER, "+COL11+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(int CowNum, Date DueCalveDate, int SireOfCalf, double CalfBW, Date CalvingDate, String CalvingDiff, String Condition, String Sex, String Fate, int CalfIndentNo, String Remarks) {
        if(CowNum<0)return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = addValues(CowNum,DueCalveDate,SireOfCalf,CalfBW,CalvingDate,CalvingDiff,Condition,Sex,Fate,CalfIndentNo,Remarks);
        long result = db.insert(TABLE_NAME, null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateData(String id, int CowNum, Date DueCalveDate, int SireOfCalf, double CalfBW, Date CalvingDate, String CalvingDiff, String Condition, String Sex, String Fate, int CalfIndentNo, String Remarks) {
        if(CowNum<0)return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = addValues(CowNum,DueCalveDate,SireOfCalf,CalfBW,CalvingDate,CalvingDiff,Condition,Sex,Fate,CalfIndentNo,Remarks);
        long result = db.update(TABLE_NAME,contentValues,"ID = ?",new String[] {id});
        if(result == -1)
            return false;
        else
            return true;
    }
    private ContentValues addValues(int CowNum, Date DueCalveDate, int SireOfCalf, double CalfBW, Date CalvingDate, String CalvingDiff, String Condition, String Sex, String Fate, int CalfIndentNo, String Remarks) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues contentValues = new ContentValues(11);

        contentValues.put(COL1,CowNum);
        if(DueCalveDate!=null){
            contentValues.put(COL2,dateFormat.format(DueCalveDate));
        }
        else contentValues.putNull(COL2);
        if(SireOfCalf!=-1) {
            contentValues.put(COL3, SireOfCalf);
        }
        else contentValues.putNull(COL3);
        if(CalfBW!=-1){
            contentValues.put(COL4, CalfBW);
        }
        else contentValues.putNull(COL4);
        if(CalvingDate!=null){
            contentValues.put(COL5, dateFormat.format(CalvingDate));
        }
        else contentValues.putNull(COL5);
        if(CalvingDiff!=null) {
            contentValues.put(COL6, CalvingDiff);
        }
        else contentValues.putNull(COL6);
        if(Condition!=null){
            contentValues.put(COL7, Condition);
        }
        else contentValues.putNull(COL7);
        if(Sex!=null) {
            contentValues.put(COL8, Sex);
        }
        else contentValues.putNull(COL8);
        if(Fate!=null){
            contentValues.put(COL9, Fate);
        }
        else contentValues.putNull(COL9);
        if(CalfIndentNo!=-1){
            contentValues.put(COL10, CalfIndentNo);
        }
        else contentValues.putNull(COL10);
        if(Remarks!=null){
            contentValues.put(COL11, Remarks);
        }
        else contentValues.putNull(COL11);
        return contentValues;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

    public Cursor getDataByID(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME + " WHERE CowNum = ?",new String[] {id});
        return res;
    }


}
