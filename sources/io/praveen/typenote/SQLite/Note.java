package io.praveen.typenote.SQLite;

public class Note {
    private String _date;
    private int _id;
    private String _note;
    private int _star;
    private String _title;

    public Note() {
    }

    public Note(int i, String str, String str2, int i2, String str3) {
        this._id = i;
        this._note = str;
        this._date = str2;
        this._star = i2;
        this._title = str3;
    }

    public Note(String str, String str2, int i, String str3) {
        this._note = str;
        this._date = str2;
        this._star = i;
        this._title = str3;
    }

    public int getID() {
        return this._id;
    }

    public void setID(int i) {
        this._id = i;
    }

    public String getNote() {
        return this._note;
    }

    public void setNote(String str) {
        this._note = str;
    }

    public String getDate() {
        return this._date;
    }

    public void setDate(String str) {
        this._date = str;
    }

    public int getStar() {
        return this._star;
    }

    public void setStar(int i) {
        this._star = i;
    }

    public String getTitle() {
        return this._title;
    }

    public void setTitle(String str) {
        this._title = str;
    }
}
