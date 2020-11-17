package com.example.notebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.notebook.bean.NotepadBean;
import com.example.notebook.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERION);
        //获取可读写的sqliteDatabase对象
        sqLiteDatabase = this.getWritableDatabase();
    }

        //创建数据表（数据库第一次创建时调用）
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+DBUtils.DATABASE_TABLE+"("+DBUtils.NOTEPAD_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ DBUtils.NOTEPAD_CONTENT+" text,"+DBUtils.NOTEPAD_TIME+" text)");
    }

    //添加数据
    public boolean insertData(String userContent,String userTIme){
        ContentValues values = new ContentValues();
        values.put(DBUtils.NOTEPAD_CONTENT,userContent);
        values.put(DBUtils.NOTEPAD_TIME,userTIme);
        return sqLiteDatabase.insert(DBUtils.DATABASE_TABLE,null,values)>0;
    }

    //删除数据
    public boolean deleteData(String id){
        String sql = DBUtils.NOTEPAD_ID+"=?";
        String[] strings = {String.valueOf(id)};
        return sqLiteDatabase.delete(DBUtils.DATABASE_TABLE,sql,strings)>0;
    }

    //修改数据
    public boolean updateData(String id,String content,String userYear){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBUtils.NOTEPAD_CONTENT,content);
        contentValues.put(DBUtils.NOTEPAD_TIME,userYear);
        String sql = DBUtils.NOTEPAD_ID+"=?";
        String[] strings = new String[]{id};
        return sqLiteDatabase.update(DBUtils.DATABASE_TABLE,contentValues,sql,strings)>0;
    }

    //查询数据
    public List<NotepadBean> query(){
        List<NotepadBean> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(DBUtils.DATABASE_TABLE,null,null,null,null,null,DBUtils.NOTEPAD_ID+" desc");
        if(cursor!=null){
            while(cursor.moveToNext()){
                NotepadBean noteInfo = new NotepadBean();
                String id = String.valueOf(cursor.getInt(cursor.getColumnIndex(DBUtils.NOTEPAD_ID)));
                String content = cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_CONTENT));
                String time = cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_TIME));
                noteInfo.setId(id);
                noteInfo.setNotepadContent(content);
                noteInfo.setNotepadTime(time);
                list.add(noteInfo);
            }
            cursor.close();
        }
        return list;
    }





    //迭代版本时调用的方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
