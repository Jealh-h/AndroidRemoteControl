package com.example.javaremotecontroller.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.javaremotecontroller.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SqliteHelper extends SQLiteOpenHelper {

    Context context;

    public SqliteHelper(Context ctx, String name, int version) {
        super(ctx, name,null, version);
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        executeAssetsSQL(db, R.raw.irext_db_20220303_sqlite);
        Log.e("DB oncreate", "DB onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void executeAssetsSQL(SQLiteDatabase db, int resourceId){
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        int result = 0;
        try {
            inputStream = this.context.getResources().openRawResource(resourceId);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            while(bufferedReader.ready()){
                String insertStmt = bufferedReader.readLine();
                db.execSQL(insertStmt);
                result++;
            }

            bufferedReader.close();
        } catch (IOException e){
            Log.e("Sql File execute error", e.toString());
        }
    }
}
