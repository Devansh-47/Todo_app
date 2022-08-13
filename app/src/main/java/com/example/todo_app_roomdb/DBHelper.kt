package com.example.todo_app_roomdb
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "userinfo_table", factory, 1) {
    override fun onCreate(db: SQLiteDatabase) {

        val query = ("CREATE TABLE " + "userinfo_table" + " ("
                + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "title" + " TEXT," +
                "description" + " TEXT," +
                "date" + " TEXT,"+
                "time" + " TEXT,"+
                "isFinished" + " TEXT"+")")

        db.execSQL(query)
    }


    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + "userinfo_table")
        onCreate(db)
    }

    fun delete(){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM userinfo_table")
    }

    fun insert(title : String,description : String,date : String,time : String ){
        val values = ContentValues()

        values.put("title", title)
        values.put("description", description)
        values.put("date", date)
        values.put("time", time)
        values.put("isFinished", "-1")

        val db = this.writableDatabase
        db.insert("userinfo_table", null, values)
        db.close()
    }

    fun getdata(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + "userinfo_table", null)
    }

    fun getdatastatus(title: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT isFinished FROM " + "userinfo_table Where title='${title}' ;", null)
    }




    fun updatefinishstatus(title: String) {
        val db = this.readableDatabase
         db.execSQL("UPDATE userinfo_table" +
                " SET isFinished=" +"1"+
                " WHERE title='"+title+"';")

    }
    fun deleteuser(title: String) {
        val db = this.readableDatabase
         db.execSQL("DELETE FROM userinfo_table WHERE title='$title';")

    }
    //DELETE FROM table_name WHERE condition;
}
