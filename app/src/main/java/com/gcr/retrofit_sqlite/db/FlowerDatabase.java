package com.gcr.retrofit_sqlite.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gcr.retrofit_sqlite.models.FlowerResponse;
import com.gcr.retrofit_sqlite.utils.SQLScripts;

import java.util.ArrayList;
import java.util.List;

public class FlowerDatabase extends SQLiteOpenHelper {
    public FlowerDatabase(@Nullable Context context,
                          @Nullable String name,
                          @Nullable SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLScripts.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS flores");
        onCreate(db);
    }

    public void addFlower(FlowerResponse flores) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SQLScripts.CATEGORIA, flores.getCategory());
        values.put(SQLScripts.PRECIO, flores.getPrice());
        values.put(SQLScripts.INSTRUCCIONES, flores.getInstructions());
        values.put(SQLScripts.NOMBRE, flores.getName());
        values.put(SQLScripts.ID, flores.getProductId());
        try {
            db.insert(
                    SQLScripts.TABLE_NAME,
                    null,
                    values);
        }catch (Exception e){
            Log.d("DB", "addFlower: "+e.getMessage());
        }
        db.close();
    }

    public List<FlowerResponse> getDataFromDb() {
        List<FlowerResponse> flowerList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT *FROM " + SQLScripts.TABLE_NAME, null);
        FlowerResponse flower;
        while (cursor.moveToNext()) {
            Log.d("DB", "running getData: ");
            flower = new FlowerResponse();
            flower.setCategory(cursor.getString(0));
            flower.setPrice(cursor.getDouble(1));
            flower.setInstructions(cursor.getString(2));
            flower.setName(cursor.getString(3));
            flower.setProductId(cursor.getInt(4));
            flowerList.add(flower);
        }
        db.close();
        return flowerList;
    }

}
