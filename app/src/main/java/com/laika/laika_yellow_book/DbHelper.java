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
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (ID PRIMARY KEY AUTOINCREMENT, "+COL1+" INTEGER, "+COL2+" DATE, "+COL3+" INTEGER, "+COL4+" DOUBLE, "+COL5+" DATE, "+COL6+" TEXT, "+COL7+" TEXT, "+COL8+" TEXT, "+COL9+" TEXT, "+COL10+" INTEGER, "+COL11+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(int CowNum, Date DueCalveDate, int SireOfCalf, double CalfBW, Date CalvingDate, String CalvingDiff, String Condition, String Sex, String Fate, int CalfIndentNo, String Remarks) {
        SQLiteDatabase db = this.getWritableDatabase();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues contentValues = new ContentValues(11);
        contentValues.put(COL1,CowNum);
        contentValues.put(COL2, dateFormat.format(DueCalveDate));
        contentValues.put(COL3, SireOfCalf);
        contentValues.put(COL4, CalfBW);
        contentValues.put(COL5, dateFormat.format(CalvingDate));
        contentValues.put(COL6, CalvingDiff);
        contentValues.put(COL7, Condition);
        contentValues.put(COL8, Sex);
        contentValues.put(COL9, Fate);
        contentValues.put(COL10, CalfIndentNo);
        contentValues.put(COL11, Remarks);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateData(String id, int CowNum, Date DueCalveDate, int SireOfCalf, double CalfBW, Date CalvingDate, String CalvingDiff, String Condition, String Sex, String Fate, int CalfIndentNo, String Remarks) {
        SQLiteDatabase db = this.getWritableDatabase();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues contentValues = new ContentValues(11);
        contentValues.put(COL1,CowNum);
        contentValues.put(COL2, dateFormat.format(DueCalveDate));
        contentValues.put(COL3, SireOfCalf);
        contentValues.put(COL4, CalfBW);
        contentValues.put(COL5, dateFormat.format(CalvingDate));
        contentValues.put(COL6, CalvingDiff);
        contentValues.put(COL7, Condition);
        contentValues.put(COL8, Sex);
        contentValues.put(COL9, Fate);
        contentValues.put(COL10, CalfIndentNo);
        contentValues.put(COL11, Remarks);
        long result = db.update(TABLE_NAME,contentValues,"ID = ?",new String[] {id});
        if(result == -1)
            return false;
        else
            return true;
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
