package com.example.sharara.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.sharara.Model.ResultScore;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "sharara";
    private static final int DB_VERSION = 5;
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_ITEMS = "items";
    private static final String CATEGORY_ID = "category_id";
    private static final String CATEGORY_NAME = "category_name";
    private static final String LEVEL = "level";
    private static final String _ITEM_ID = "_item_id";
    private static final String SCORE = "score";
    private static final String SCORE_LEN = "score_length";
    private static final String ITEM_NAME = "levels";




    public Database(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String categories = "CREATE TABLE " + TABLE_CATEGORY +
                " (" + CATEGORY_ID + " INTEGER, " +
                CATEGORY_NAME + " TEXT, " +
                LEVEL + " INTEGER, " +
                SCORE + " INTEGER, " +
                SCORE_LEN + " INTEGER);";

//        String items = "CREATE TABLE " + TABLE_ITEMS +
//                " (" + _ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                LEVEL + " INTEGER, " +
//                ITEM_NAME + " TEXT);";
//        Log.d("mina", "d");

        db.execSQL(categories);
       // db.execSQL(items);
        Log.d("mina", "d1");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    SQLiteDatabase db = this.getWritableDatabase();
    //insert to categories
    public boolean insert(ResultScore resultScore) throws SQLiteException {
        ContentValues content = new ContentValues();
        content.put(Database.CATEGORY_ID, resultScore.getCategory_id());
        content.put(Database.CATEGORY_NAME, resultScore.getCategory_name());
        content.put(Database.LEVEL, resultScore.getLevel());
        content.put(Database.SCORE, resultScore.getScore());
        content.put(Database.SCORE_LEN, resultScore.getScore_len());
        long result = db.insert(TABLE_CATEGORY, null, content);
        Log.d("mina", "d1");

        db.close();
        return result != -1;
    }

    public ArrayList<ResultScore> getAllResults() {
        ArrayList<ResultScore> resultScores = new ArrayList<>();
        if (db != null) {
            Cursor cursor = db.rawQuery(" select * from " + TABLE_CATEGORY, null);
            while (cursor.moveToNext()) {

                int cat_id = cursor.getInt(cursor.getColumnIndex(Database.CATEGORY_ID));
                String cat_name = cursor.getString(cursor.getColumnIndex(Database.CATEGORY_NAME));
                int level = cursor.getInt(cursor.getColumnIndex(Database.LEVEL));
                int score = cursor.getInt(cursor.getColumnIndex(Database.SCORE));
                int score_len = cursor.getInt(cursor.getColumnIndex(Database.SCORE_LEN));

                resultScores.add(new ResultScore(cat_id,cat_name,level,score,score_len));

                Log.d("mina", "d2");
            }

            cursor.close();

            return resultScores;
        } else {
            Toast.makeText(context, "Data is Null;)", Toast.LENGTH_SHORT).show();
            return null;

        }
    }

    public int getLastScore() {
        int _id = 0;
        db = this.getReadableDatabase();
        Cursor cursor = db.query(Database.TABLE_CATEGORY, new String[] {Database.SCORE}, null, null, null, null, null);

        if (cursor.moveToLast()) {
            _id = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return _id;
    }
//    public boolean update(Store store) throws SQLiteException {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues content = new ContentValues();
//        content.put(Database.NAME, store.getCategoryName());
//        content.put(Database.UNIT, store.getUnit());
//        content.put(Database.GROUP, store.getGroup());
//        content.put(Database.UNIT_PRICE, store.getUnitPrice());
//        content.put(Database.TOTAL_PRICE, store.getTotalPrice());
//        content.put(Database.TOTAL_ALL, store.getTotalAll());
//        content.put(Database.TIMES_TAMP, store.getTimesTamp());
//        String args[] = {String.valueOf(store.getId())};
//        int result = db.update(TABLE_STORE, content, "_id = ?",args);
//        return result > 0;
//    }
//
//    //delete
//    public boolean delete(Store store) throws SQLiteException {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues content = new ContentValues();
//        String args[] ={String.valueOf(store.getId())};
//        int result = db.delete(TABLE_STORE, "_id=?", args);
//        return result > 0;
//    }


//    public ArrayList<ResultScore> getCustomerScore(int cat_id,int level) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        ArrayList<ResultScore> orderItems = new ArrayList<>();
//        if (db != null) {
//            Cursor c = db.rawQuery("select docid as _id, recipeID from " +
//                            TABLE_CATEGORY + " where " + CATEGORY_ID + " = ? AND " + LEVEL + " = ?",
//                    new String[] { cat_id, level});
//
//            while (cursor.moveToNext()) {
//                int cat = cursor.getInt(cursor.getColumnIndex(Database.CATEGORY_ID));
//                int lev = cursor.getInt(cursor.getColumnIndex(Database.LEVEL));
//                String score = cursor.getString(cursor.getColumnIndex(Database.SCORE));
//
//
//                orderItems.add(new ResultScore(cat, lev, score));
//               // Log.d("minaMagid2", ","+idOrderItem+","+idOrder+","+name+","+Quantity+ "," +price);
//
//            }
//            cursor.close();
//            return orderItems;
//        } else {
//            Toast.makeText(context, "Data is Null;)", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//    }



}
