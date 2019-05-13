package com.jjshop.utils.dbutils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jjshop.utils.StringUtil;

/**
 * 作者：张国庆
 * 时间：2018/8/9
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add persons
     */
    public boolean insert(String citysjson) {
        if(StringUtil.isEmpty(citysjson) || null == db){
            return false;
        }
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //添加城市数据
        cValue.put("citysjson",citysjson);
        // 插入数据库
        db.insert(DBHelper.TABLE_NAME,null, cValue);
        return true;
    }


    /**
     * query all persons, return list
     * @return List<Person>
     */
    public boolean update(String citysjson) {
        if(StringUtil.isEmpty(citysjson) || null == db){
            return false;
        }
        //实例化内容值
        ContentValues values = new ContentValues();
        values.put("citysjson", citysjson);
        //修改
        db.update(DBHelper.TABLE_NAME, values, null, null);
        return true;
    }

    /**
     * query all persons, return cursor
     * @return    Cursor
     */
    public String query() {
        if(null == db){
            return "";
        }
        String citysjson = "";
        //查询获得游标
        Cursor cursor = db.query(DBHelper.TABLE_NAME, new String[]{"citysjson"},null,null,null,null,null);

        //判断游标是否为空
        if(cursor.moveToFirst()) {
            citysjson = cursor.getString(cursor.getColumnIndex("citysjson"));
        }
        return citysjson;
    }

    /**
     * close database
     */
    public void closeDB() {
        if(null != db && db.isOpen()){
            db.close();
        }

    }
}
