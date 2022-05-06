package com.example.wellplants.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;

public class DataBaseHelper extends SQLiteOpenHelper {
    private Context mycontext;

    //private String DB_PATH = mycontext.getApplicationContext().getPackageName()+"/databases/";
    private static String DB_NAME = "plants_v1.sqlite"; //the extension may be .sqlite or .db
    public SQLiteDatabase myDataBase;
     /*private String DB_PATH = "/data/data/"
                             + mycontext.getApplicationContext().getPackageName()
                             + "/databases/";*/

    public DataBaseHelper(Context context) throws IOException {
        super(context, DB_NAME, null, 1);
        this.mycontext = context;
        boolean dbexist = checkdatabase();
        if (dbexist) {
            System.out.println("Database exists");
            opendatabase();
        } else {
            System.out.println("Database doesn't exist");
        }

    }

    private boolean checkdatabase() {
        //SQLiteDatabase checkdb = null;
        boolean checkdb = false;
        try {
            String myPath = DB_NAME;
            File dbfile = new File(myPath);
            //checkdb = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            System.out.println("Database doesn't exist");
        }

        return checkdb;
    }

    public void opendatabase() throws SQLException {
        //Open the database
        String mypath = DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);

    }


    public synchronized void close() {
        if (myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Plants (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "PLANT_NAME TEXT, " +
                "HUMIDITY INTEGER, " +
                "TEMPERATURE INTEGER, " +
                "ILLUMINATION INTEGER," +
                "PLANT_IMG BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}