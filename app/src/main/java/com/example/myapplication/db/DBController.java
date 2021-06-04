package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.Note;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DBController extends SQLiteOpenHelper {
    private static final String tableName = "notes_table";
    private static final String id = "ID";
    private static final String note_id = "noteId";
    private static final String databaseName = "dbNotes";
    private static final int versioncode = 1;
    private static final String content = "content";
    private static final String header = "header";
    private static final String persistInfo = "persistInfo";
    private static final String plainContent = "plainContent";
    private static final String secondary = "secondary";
    private static final String imageResource = "imageResource";
    private static final String tags = "tags";
    private static final String type = "type";
    private static final String modifyDate = "modifyDate";

    public DBController(Context context) {
        super(context, databaseName, null, versioncode);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        createTable(database);
    }

    private void createTable(SQLiteDatabase database) {
        String query;

        query = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + id + " integer primary key, "
                + content + " text, " + header + " text, " + persistInfo + " text, "
                + note_id + " integer, " + imageResource + " text, " + type + " text, "
                + plainContent + " text, " + secondary + " text, " + modifyDate + " text, " + tags + " text )";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old,
                          int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS " + tableName;
        database.execSQL(query);
        onCreate(database);
    }

    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            createTable(database);

            Cursor cursor = database.rawQuery("SELECT * FROM " + tableName, null);
            if (cursor.moveToFirst()) {
                do {
                    int noteIdV = cursor.getInt(cursor.getColumnIndex(note_id));
                    String contentV = cursor.getString(cursor.getColumnIndex(content));
                    String headerV = cursor.getString(cursor.getColumnIndex(header));
                    String persistInfoV = cursor.getString(cursor.getColumnIndex(persistInfo));
                    String plainContentV = cursor.getString(cursor.getColumnIndex(plainContent));
                    String secondaryV = cursor.getString(cursor.getColumnIndex(secondary));
                    String tagsV = cursor.getString(cursor.getColumnIndex(tags));
                    String typeV = cursor.getString(cursor.getColumnIndex(type));
                    String modifyDateV = cursor.getString(cursor.getColumnIndex(modifyDate));
                    int imageResourceV = cursor.getInt(cursor.getColumnIndex(imageResource));

                    Note note = new Note();

                    note.setId(noteIdV);
                    note.setContent(contentV);
                    note.setHeader(headerV);
                    note.setPersistInfoDisplay(persistInfoV);
                    note.setTags(plainContentV);
                    note.setSecondary(secondaryV);
                    note.setTags(tagsV);
                    note.setType(typeV);
                    note.setModifyDate(Timestamp.valueOf(modifyDateV));
                    note.setImageResource(imageResourceV);

                    notes.add(note);
                } while (cursor.moveToNext());
            }

            cursor.close();
            database.close();
        } catch (Exception e) {
            Log.d("UPDATE_NOTE", "getNotes: " + e.getMessage());
        }
        return notes;
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, note_id + "=" + note.getId(), null);
    }

    public boolean addNote(Note note) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(note_id, note.getId());
            cv.put(imageResource, note.getImageResource());
            cv.put(content, note.getContent());
            cv.put(header, note.getHeader());
            cv.put(persistInfo, note.getPersistInfoDisplay());
            cv.put(modifyDate, note.getModifyDate().toString());
            cv.put(plainContent, note.getPlainContent());
            cv.put(secondary, note.getSecondary());
            cv.put(tags, note.getTags());
            cv.put(type, note.getType());

            db.insert(tableName, null, cv);
            db.close();

            return true;
        } catch (Exception ex) {
            return false;
        }

    }
}