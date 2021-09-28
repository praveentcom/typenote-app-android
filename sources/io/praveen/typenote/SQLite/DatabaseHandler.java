package io.praveen.typenote.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import com.anjlab.android.iab.v3.Constants;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String DATABASE_NAME = "noteManager.db";
    private static final int DATABASE_VERSION = 5;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 5);
    }

    public void onCreate(@NonNull SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE notes(id INTEGER PRIMARY KEY,note TEXT,date TEXT,star INTEGER DEFAULT 0,title TEXT DEFAULT '');");
    }

    public void onUpgrade(@NonNull SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (i == 4) {
            sQLiteDatabase.execSQL("ALTER TABLE notes ADD COLUMN title TEXT DEFAULT ''");
        } else if (i < i2) {
            sQLiteDatabase.execSQL("ALTER TABLE notes ADD COLUMN star INTEGER DEFAULT 0");
            sQLiteDatabase.execSQL("ALTER TABLE notes ADD COLUMN title TEXT DEFAULT ''");
        }
    }

    @NonNull
    public Note getNote(int i) {
        Cursor query = getReadableDatabase().query("notes", new String[]{"id", "note", "date", "star", Constants.RESPONSE_TITLE}, "id=?", new String[]{String.valueOf(i)}, null, null, null, null);
        if (query != null) {
            query.moveToFirst();
        }
        return new Note(Integer.parseInt(query.getString(0)), query.getString(1), query.getString(2), query.getInt(3), query.getString(4));
    }

    @NonNull
    public List<Note> getAllNotes() {
        ArrayList arrayList = new ArrayList();
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT  * FROM notes", null);
        if (rawQuery.moveToFirst()) {
            do {
                Note note = new Note();
                note.setID(Integer.parseInt(rawQuery.getString(0)));
                note.setNote(rawQuery.getString(1));
                note.setDate(rawQuery.getString(2));
                note.setStar(rawQuery.getInt(3));
                note.setTitle(rawQuery.getString(4));
                arrayList.add(note);
            } while (rawQuery.moveToNext());
        }
        return arrayList;
    }

    public int getNotesCount() {
        Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  * FROM notes", null);
        rawQuery.close();
        return rawQuery.getCount();
    }

    public void updateNote(@NonNull Note note) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note", note.getNote());
        contentValues.put("date", note.getDate());
        contentValues.put("star", Integer.valueOf(note.getStar()));
        contentValues.put(Constants.RESPONSE_TITLE, note.getTitle());
        writableDatabase.update("notes", contentValues, "id = ?", new String[]{String.valueOf(note.getID())});
    }

    public void deleteNote(@NonNull Note note) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete("notes", "id = ?", new String[]{String.valueOf(note.getID())});
        writableDatabase.close();
    }

    public void addNote(@NonNull Note note) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note", note.getNote());
        contentValues.put("date", note.getDate());
        contentValues.put("star", Integer.valueOf(note.getStar()));
        contentValues.put(Constants.RESPONSE_TITLE, note.getTitle());
        writableDatabase.insert("notes", null, contentValues);
        writableDatabase.close();
    }
}
