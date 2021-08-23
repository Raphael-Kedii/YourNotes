package com.boldcomp.yournotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.boldcomp.yournotes.Notes;

import java.util.ArrayList;

public class SqliteDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "yournote";
    private static final String TABLE_NOTES = "notes";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "notestitle";
    private static final String COLUMN_CONTENT = "notescontent";

    public SqliteDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT," + COLUMN_CONTENT + " TEXT" + ")";
        db.execSQL(CREATE_NOTES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }
    public ArrayList<Notes> listNotes(){
        String sql = "select * from " + TABLE_NOTES;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Notes> storeNotes = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String n_title = cursor.getString(1);
                String n_content = cursor.getString(2);
                storeNotes.add(new Notes(id, n_title, n_content));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeNotes;
    }
    public void addNotes (Notes notes){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, notes.getN_title());
        values.put(COLUMN_CONTENT, notes.getN_content());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NOTES, null, values);

    }
    public void updateNotes(Notes notes){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, notes.getN_title());
        values.put(COLUMN_CONTENT, notes.getN_content());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_NOTES, values, COLUMN_ID + "  = ?", new String[] { String.valueOf(notes.getId())});
    }
    public Notes findNotes(String title){
        String query = "Select * FROM " + TABLE_NOTES + " WHERE " + COLUMN_NAME + " = " + "n_title";
        SQLiteDatabase db = this.getWritableDatabase();
        Notes notes = null;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(0));
            String notesTitle = cursor.getString(1);
            String notesContent = cursor.getString(2);
            notes = new Notes(id, notesTitle, notesContent);
        }
        cursor.close();
        return notes;
    }
    public void  deleteNotes(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COLUMN_ID +" =?", new String[]{String.valueOf(id)});
    }
}
