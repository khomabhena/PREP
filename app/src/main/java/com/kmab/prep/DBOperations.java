package com.kmab.prep;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOperations extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1; //8
    private static final String DB_NAME = "wiwie.db";
    //private String localUid;

    Context context;

    DBOperations(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_12);
        db.execSQL(QUERY_13);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Expenses.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Questions.TABLE_NAME);

        onCreate(db);
    }

    Cursor getQuestion(SQLiteDatabase db) {
        Cursor cursor;
        String[] projections = {
                DBContract.Questions.ID,
                DBContract.Questions.KEY,
                DBContract.Questions.PARENT_KEY,
                DBContract.Questions.NAME,
                DBContract.Questions.LINK,
                DBContract.Questions.QUESTION
        };
// + DBContract.Event.LOCAL_UID + "='" + localUid + "'"
        cursor = db.query(
                true,
                DBContract.Questions.TABLE_NAME, projections,
                null,
                null,
                DBContract.Questions.ID,
                null,
                DBContract.Questions.ID + " DESC",
                null
        );

        return cursor;
    }



    private static final String QUERY_12 = "CREATE TABLE "+ DBContract.Expenses.TABLE_NAME +"("+
            DBContract.Expenses.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Expenses.LOCAL_UID + " TEXT, " +
            DBContract.Expenses.PAYMENT_TYPE + " TEXT, " +
            DBContract.Expenses.FROM + " TEXT, " +
            DBContract.Expenses.KEY + " TEXT, " +
            DBContract.Expenses.AMOUNT + " TEXT, " +
            DBContract.Expenses.NAME + " TEXT, " +
            DBContract.Expenses.DESC + " TEXT, " +
            DBContract.Expenses.TAG + " TEXT, " +
            DBContract.Expenses.DATE + " TEXT, " +
            DBContract.Expenses.PHONE + " TEXT, " +
            DBContract.Expenses.DATE_TIMESTAMP + " TEXT, " +
            DBContract.Expenses.PAID + " TEXT, " +
            DBContract.Expenses.INCOME + " TEXT, " +
            DBContract.Expenses.BY + " TEXT, " +
            DBContract.Expenses.VIA + " TEXT, " +
            DBContract.Expenses.COl_15 + " TEXT, " +
            DBContract.Expenses.COl_16 + " TEXT, " +
            DBContract.Expenses.COl_17 + " TEXT, " +
            DBContract.Expenses.COl_18 + " TEXT, " +
            DBContract.Expenses.COl_19 + " TEXT, " +
            DBContract.Expenses.COl_20 +" TEXT);";

    private static final String QUERY_13 = "CREATE TABLE "+ DBContract.Questions.TABLE_NAME +"("+
            DBContract.Questions.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Questions.LOCAL_UID + " TEXT, " +
            DBContract.Questions.KEY + " TEXT, " +
            DBContract.Questions.PARENT_KEY + " TEXT, " +
            DBContract.Questions.NAME + " TEXT, " +
            DBContract.Questions.LINK + " TEXT, " +
            DBContract.Questions.QUESTION + " TEXT, " +
            DBContract.Questions.COl_6 + " TEXT, " +
            DBContract.Questions.COl_7 + " TEXT, " +
            DBContract.Questions.COl_8 + " TEXT, " +
            DBContract.Questions.COl_9 + " TEXT, " +
            DBContract.Questions.COl_10 + " TEXT, " +
            DBContract.Questions.COl_11 + " TEXT, " +
            DBContract.Questions.COl_12 + " TEXT, " +
            DBContract.Questions.COl_13 + " TEXT, " +
            DBContract.Questions.COl_14 + " TEXT, " +
            DBContract.Questions.COl_15 + " TEXT, " +
            DBContract.Questions.COl_16 + " TEXT, " +
            DBContract.Questions.COl_17 + " TEXT, " +
            DBContract.Questions.COl_18 + " TEXT, " +
            DBContract.Questions.COl_19 + " TEXT, " +
            DBContract.Questions.COl_20 +" TEXT);";


}