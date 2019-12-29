package com.kuroh4su.lab2mt;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

class DataBaseHelper {
    private SQLiteDatabase db = null;
    private Cursor query = null;

    boolean createDB(Context con) {
        try {
            db = con.openOrCreateDatabase("inf.db", MODE_PRIVATE, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS data (nick TEXT, skill INTEGER, hours INTEGER)");
            db.close();
            return true;
        } catch (SQLException e) {
            Log.getStackTraceString(e);
            db.close();
            return false;
        }
    }

    ArrayList<InformationList.Player> getAllData(ArrayList<InformationList.Player> mdPlayersList, Context con) {
        try {
            db = con.openOrCreateDatabase("inf.db", MODE_PRIVATE, null);
            query = db.rawQuery("SELECT * FROM data;", null);
            if (query.moveToFirst()) {
                do {
                    mdPlayersList.add(new InformationList.Player(query.getString(0),
                            query.getInt(1), query.getInt(2)));
                }
                while (query.moveToNext());
            }
            query.close();
            db.close();
        } catch (SQLException e) {
            Log.getStackTraceString(e);
            query.close();
            db.close();
        }
        return mdPlayersList;
    }

    ArrayList<InformationList.Player> updateData(ArrayList<InformationList.Player> mPlayersList, Context con) {
        try {
            db = con.openOrCreateDatabase("inf.db", MODE_PRIVATE, null);
            query = db.rawQuery("SELECT * FROM data;", null);
            if (query.moveToLast()) {
                mPlayersList.add(new InformationList.Player(query.getString(0),
                        query.getInt(1), query.getInt(2)));
            }
            query.close();
            db.close();
        } catch (SQLException e) {
            Log.getStackTraceString(e);
            query.close();
            db.close();
        }
        return mPlayersList;
    }

    void deleteData(Context con) {
        try {
            db = con.openOrCreateDatabase("inf.db", MODE_PRIVATE, null);
            db.execSQL("DELETE FROM data");
            db.close();
        } catch (SQLException e) {
            Log.getStackTraceString(e);
            query.close();
            db.close();
        }
    }

    boolean addElement(String nickname, int skill, int hours, Context con) {
        try {
            db = con.openOrCreateDatabase("inf.db", MODE_PRIVATE, null);
            db.execSQL("INSERT INTO data VALUES (\"" + nickname + "\"," + skill + "," + hours + ")");
            db.close();
            return true;
        } catch (SQLException e) {
            Log.getStackTraceString(e);
            db.close();
            return false;
        }
    }
}