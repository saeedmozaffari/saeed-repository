package mozaffari.saeed.app.internetusing.tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteCommands extends SQLiteOpenHelper {

    //Insert Commands
    //Select Commands
    //Update Commands
    //Delete Commands
    //yyyy-MM-dd HH:mm:ss ==> date insert format

    /*
    select * 
    from MyTable 
    where mydate >= Datetime('2000-01-01 00:00:00') 
    and mydate <= Datetime('2050-01-01 23:00:59')
    */

    public final String   path = "data/data/mozaffari.saeed.app.internetusing/databases/";
    public final String   Name = "internet_using";
    public SQLiteDatabase cmd;


    @SuppressLint("NewApi")
    public SQLiteCommands(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    public SQLiteCommands(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public SQLiteCommands(Context context, int version) {
        super(context, "internet_using", null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase arg0) {}


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}


    public void useable() {
        boolean checkdb = checkdb();
        if (checkdb) {

        } else {
            this.getReadableDatabase();
            try {
                copydatabase();
            }
            catch (IOException e) {}
        }
    }


    public void open() {
        cmd = SQLiteDatabase.openDatabase(path + Name, null, SQLiteDatabase.OPEN_READWRITE);
    }


    @Override
    public void close() {
        cmd.close();
    }


    public void resetTables(String table) {
        cmd.delete(table, null, null);
    }


    public boolean checkdb() {
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openDatabase(path + Name, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch (SQLException e) {}
        return db != null ? true : false;
    }


    public void copydatabase() throws IOException {
        OutputStream myOutput = new FileOutputStream(path + Name);
        byte[] buffer = new byte[1024];
        int lenght;
        InputStream myInput = G.context.getAssets().open(Name);
        while ((lenght = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, lenght);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }


    public void setVersion(int version) {
        cmd.execSQL("PRAGMA user_version = " + version);
    }


    public int getVersion() {
        int version = cmd.getVersion();
        return version;
    }


    //Select Commands

    public Cursor selectInternetUsage(String fromDate, String toDate) {
        //        select column from Table where columnDate between '2012-07-01' and '2012-07-07'
        //        Cursor cursor = cmd.rawQuery("SELECT download , upload FROM usage_details WHERE use_date_stop BETWEEN " + fromDate + " AND " + toDate, null);
        //        Cursor cursor = cmd.rawQuery("SELECT download , upload FROM usage_details WHERE use_date_stop BETWEEN Datetime('" + fromDate + "') AND Datetime('" + toDate + "') ", null);
        //        Cursor cursor = cmd.rawQuery("SELECT download , upload FROM usage_details WHERE Datetime('use_date_stop') >= Datetime('" + fromDate + "') AND Datetime('use_date_stop') <= Datetime('" + toDate + "') ", null);
        //        Cursor cursor = cmd.rawQuery("SELECT download , upload FROM usage_details WHERE use_date_stop BETWEEN strftime('%s', '" + fromDate + "') AND strftime('%s', '" + toDate + "')", null);
        Cursor cursor = cmd.rawQuery("SELECT download , upload FROM usage_details WHERE use_date_stop >= " + fromDate + " AND use_date_stop <= " + toDate, null);
        return cursor;
    }


    public Cursor selectBeforWifiDetails() {
        Cursor cursor = cmd.rawQuery("SELECT * FROM wifi_details", null);
        return cursor;
    }


    public Cursor selectLastUsageDetails() {
        Cursor cursor = cmd.rawQuery("SELECT * FROM usage_details ORDER BY id DESC LIMIT 1 ", null);
        return cursor;
    }


    public Cursor selectMostUseInOneConnetion() {
        Cursor cursor = cmd.rawQuery("SELECT download,upload FROM usage_details ORDER BY download DESC LIMIT 1 ", null);
        return cursor;
    }


    public Cursor selectDurations() {
        Cursor cursor = cmd.rawQuery("SELECT duration FROM usage_details", null);
        return cursor;
    }


    public Cursor selectAllUse() {
        Cursor cursor = cmd.rawQuery("SELECT * FROM all_uses", null);
        return cursor;
    }


    public int selectAllUseCount(String table) {
        Cursor cursor = cmd.rawQuery("SELECT * FROM " + table, null);
        int count = cursor.getCount();
        return count;
    }


    public int selectCountWithClause(String table, String clause) {
        Cursor mCount = cmd.rawQuery("SELECT COUNT(*) FROM " + table + " where " + clause, null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        return count;
    }


    public int selectCountWithoutClause(String table) {
        Cursor mCount = cmd.rawQuery("SELECT COUNT(*) FROM " + table, null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        return count;
    }


    public boolean isExist(String table, String clause) {
        Cursor mCount = cmd.rawQuery("SELECT COUNT(*) FROM " + table + " where " + clause, null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor select(String what, String table) {
        Cursor cursor = cmd.rawQuery("SELECT " + what + " FROM " + table, null);
        return cursor;
    }


    public Cursor select(String what, String table, String cluase) {
        Cursor cursor = cmd.rawQuery("SELECT " + what + " FROM " + table + " WHERE " + cluase, null);
        return cursor;
    }


    //Delete Commands

    public void deleteUserWords(int id) {
        cmd.delete("user_words", " id = '" + id + "'", null);
    }


    //Insert Commands

    public void insertAllUses(int download, int upload) {
        ContentValues values = new ContentValues();
        values.put("download", download);
        values.put("upload", upload);
        cmd.insert("all_uses", null, values);
    }


    public void insertUseDetails(String wifiName, int download, int upload, String dateStart, String dateStop, long duration) {
        ContentValues values = new ContentValues();
        values.put("wifi_name", wifiName);
        values.put("download", download);
        values.put("upload", upload);
        values.put("use_date_start", dateStart);
        values.put("use_date_stop", dateStop);
        values.put("duration", duration);
        cmd.insert("usage_details", null, values);
    }


    public void insretWifiDetails(String wifiName, int download, int upload) {
        ContentValues values = new ContentValues();
        values.put("wifi_name", wifiName);
        values.put("download", download);
        values.put("upload", upload);
        cmd.insert("wifi_details", null, values);
    }


    //Update Commands

    public void updateWifiDetails(String wifiName, int download, int upload) {
        ContentValues values = new ContentValues();
        values.put("download", download);
        values.put("upload", upload);
        cmd.update("wifi_details", values, " wifi_name = '" + wifiName + "'", null);
    }


    public void updateAllUses(int id, int download, int upload) {
        ContentValues values = new ContentValues();
        values.put("download", download);
        values.put("upload", upload);
        cmd.update("all_uses", values, "id = '" + id + "'", null);
    }

}
