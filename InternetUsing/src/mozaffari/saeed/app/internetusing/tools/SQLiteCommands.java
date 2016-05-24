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

    public Cursor selectLastUsageDetails() {
        Cursor cursor = cmd.rawQuery("SELECT * FROM usage_details ORDER BY id DESC LIMIT 1 ", null);
        return cursor;
    }


    public Cursor selectMostUseInOneConnetion() {
        Cursor cursor = cmd.rawQuery("SELECT amount_download,amount_upload FROM usage_details ORDER BY amount_download DESC LIMIT 1 ", null);
        return cursor;
    }


    public Cursor selectDurations() {
        Cursor cursor = cmd.rawQuery("SELECT duration FROM usage_details", null);
        return cursor;
    }


    public Cursor selectAllUse() {
        Cursor cursor = cmd.rawQuery("SELECT wifi_name,amount_download,amount_upload FROM usage_details", null);
        return cursor;
    }


    //Delete Commands

    public void deleteUserWords(int id) {
        cmd.delete("user_words", " id = '" + id + "'", null);
    }


    //Insert Commands

    public void insertUseDetails(String wifiName, int download, int upload, String date, long duration) {
        ContentValues values = new ContentValues();
        values.put("wifi_name", wifiName);
        values.put("amount_download", download);
        values.put("amount_upload", upload);
        values.put("use_date", date);
        values.put("duration", duration);
        cmd.insert("usage_details", null, values);
    }


    public void insertFavorite(String word, String mean, int wordID) {
        ContentValues values = new ContentValues();
        values.put("word", word);
        values.put("mean", mean);
        values.put("word_id", wordID);
        cmd.insert("fav_word", null, values);
    }


    //Update Commands

    public void updatetasvir(String tasvir) {
        ContentValues cv = new ContentValues();
        cv.put("tasvir", tasvir);
        cmd.update("login", cv, null, null);
    }
}
