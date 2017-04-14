package kr.co.moon.aeds;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "db.db", null,1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // db.execSQL("CREATE TABLE math_temp (col_1 TEXT, col_2 TEXT, col_3 TEXT, col_4 TEXT, col_5 TEXT, col_6 TEXT, col_7 TEXT, col_8 TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        onCreate(db);
    }

}
