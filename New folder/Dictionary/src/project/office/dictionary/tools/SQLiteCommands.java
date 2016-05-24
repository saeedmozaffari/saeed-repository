package project.office.dictionary.tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLiteCommands extends SQLiteOpenHelper {

    //Insert Commands
    //Select Commands
    //Update Commands
    //Delete Commands

    public final String      path                       = "data/data/project.office.dictionary/databases/";
    public final String      Name                       = "dic";
    public SQLiteDatabase    cmd;

    private static final int VER_LAUNCH                 = 21;
    private static final int VER_SESSION_FEEDBACK_URL   = 22;
    private static final int VER_SESSION_NOTES_URL_SLUG = 23;

    private static final int DATABASE_VERSION           = VER_SESSION_NOTES_URL_SLUG;


    //PRAGMA user_version = 4;

    public SQLiteCommands(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        // TODO Auto-generated constructor stub
    }


    public SQLiteCommands(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }


    public SQLiteCommands(Context context, int version) {
        super(context, "dic", null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase arg0) {}

    /*
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("LOG", "onUpgrade() from " + oldVersion + " to " + newVersion);
    
        // NOTE: This switch statement is designed to handle cascading database
        // updates, starting at the current version and falling through to all
        // future upgrade cases. Only use "break;" when you want to drop and
        // recreate the entire database.
        int version = oldVersion;
    
        switch (version) {
            case VER_LAUNCH:
                // Version 22 added column for session feedback URL.
                db.execSQL("ALTER TABLE " + Tables.SESSIONS + " ADD COLUMN " + SessionsColumns.SESSION_FEEDBACK_URL + " TEXT");
                version = VER_SESSION_FEEDBACK_URL;
    
            case VER_SESSION_FEEDBACK_URL:
                // Version 23 added columns for session official notes URL and slug.
                db.execSQL("ALTER TABLE " + Tables.SESSIONS + " ADD COLUMN " + SessionsColumns.SESSION_NOTES_URL + " TEXT");
                db.execSQL("ALTER TABLE " + Tables.SESSIONS + " ADD COLUMN " + SessionsColumns.SESSION_SLUG + " TEXT");
                version = VER_SESSION_NOTES_URL_SLUG;
        }
    
        Log.d("LOG", "after upgrade logic, at version " + version);
        if (version != DATABASE_VERSION) {
            Log.w("LOG", "Destroying old data during upgrade");
    
            db.execSQL("DROP TABLE IF EXISTS " + Tables.BLOCKS);
    
            // ... delete all your tables ...
    
            onCreate(db);
        }
    }
    */


    /*
     
       @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //create temp. table to hold data
             db.execSQL("CREATE TEMPORARY TABLE contacts_backup (_id INTEGER PRIMARY KEY AUTOINCREMENT, column_1 TEXT, column_2 TEXT);");
    //insert data from old table into temp table
    
    db.execSQL("INSERT INTO contacts_backup SELECT _id, column_1, column_2 FROM contacts ");
    //drop the old table now that your data is safe in the temporary one
    db.execSQL("DROP TABLE contacts");
    //recreate the table this time with all 4 columns
    db.execSQL("CREATE TABLE contacts (_id INTEGER PRIMARY KEY AUTOINCREMENT, column_1 TEXT, column_2 TEXT, column_3 TEXT);");
    //fill it up using null for the column_3
    db.execSQL("INSERT INTO contacts SELECT _id, column_1, column_2, null FROM contacts_backup");
    //then drop the temporary table
    db.execSQL("DROP TABLE contacts_backup");
    
    }
      
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        //create temp. table to hold data
        Log.i("LOG", "1");
        db.execSQL("CREATE TEMPORARY TABLE word_backup (id INTEGER PRIMARY KEY AUTOINCREMENT, word TEXT, nword TEXT, lang TEXT, source TEXT, visit INTEGER, fav INTEGER);");
        Log.i("LOG", "2");
        //insert data from old table into temp table
        db.execSQL("INSERT INTO word_backup SELECT id, word, nword, lang, source, visit, fav FROM words ");
        Log.i("LOG", "3");
        //drop the old table now that your data is safe in the temporary one
        db.execSQL("DROP TABLE words");
        Log.i("LOG", "4");
        //recreate the table this time with all 4 columns
        db.execSQL("CREATE TABLE words (id INTEGER PRIMARY KEY AUTOINCREMENT,  word TEXT, nword TEXT, lang TEXT, source TEXT, visit INTEGER, fav INTEGER);");
        Log.i("LOG", "5");
        //fill it up using null for the column_3
        db.execSQL("INSERT INTO words SELECT id, word, nword, lang, source, visit, fav, null FROM words_backup");
        Log.i("LOG", "6");
        //then drop the temporary table
        db.execSQL("DROP TABLE words_backup");
        Log.i("LOG", "7");
        */
    }


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
        //Log.i("LOG", "Version : " + cmd.getVersion());
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
            Log.i("LOG", "1");
            db = SQLiteDatabase.openDatabase(path + Name, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch (SQLException e) {}
        return db != null ? true : false;
    }


    public void copydatabase() throws IOException {
        Log.i("LOG", "2");
        //setVersion(2);
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
        // return ((Long) DatabaseUtils.longForQuery(this, "PRAGMA user_version;", null)).intValue();
    }


    //Select Commands

    public Cursor selectAll() {//SELECT * FROM Table_Name LIMIT 5
        Cursor cursor = cmd.rawQuery("SELECT * FROM dictionary LIMIT 20 OFFSET " + G.offset, null);
        return cursor;
    }


    public Cursor selectVisitNumber(String word) {
        Cursor cursor = cmd.rawQuery("SELECT * FROM dictionary WHERE word = '" + word + "'", null);
        return cursor;
    }


    public Cursor selectVisitSort() {
        String[] columns = { "id", "word", "mean", "visit" };
        Cursor c = cmd.query("dictionary", columns, "visit > 0", null, null, null, "visit DESC");
        return c;
    }


    public Cursor selectFavorite() {
        Cursor cursor = cmd.rawQuery("SELECT * FROM fav_word", null);
        return cursor;
    }


    public Cursor selectMeaning(int id) {
        Cursor cursor = cmd.rawQuery("SELECT * FROM dictionary WHERE id = '" + id + "'", null);
        return cursor;
    }


    public Cursor selectInfo(String table, int wordID) {
        Cursor cursor = cmd.rawQuery("SELECT * FROM " + table + " WHERE word_id = " + wordID, null);
        return cursor;
    }


    public Cursor selectUserWords() {
        Cursor cursor = cmd.rawQuery("SELECT * FROM user_words", null);
        return cursor;
    }

    //selectEnglishWords

    //Cursor cursor = cmd.rawQuery("SELECT * FROM" + table + " WHERE word LIKE '" + characters + "%' group by word", null);
    //Cursor cursor = m_db.query(MY_TABLE, new String[] {"rowid","Word"},"Word LIKE '?'", new String[]{name+"%"}, null, null, null);

    //SELECT * FROM todo WHERE word LIKE '%" + wordsearch + "%' group by word
    //SELECT * FROM todo WHERE word LIKE '" + characters + "%' group by word
    //select name from memberdb where name like '% LIM %' OR name like "LIM %" OR name like "% LIM" OR name like "LIM"


    //String[] columns = { "id", "word", "mean" };
    //String[] selectionArgs = { characters + "%" };
    //Cursor cursor = cmd.query(table, columns, "word LIKE ? ", selectionArgs, null, null, null);

    public Cursor selectEnglishWords(String table, String characters) {
        Cursor cursor = cmd.rawQuery("SELECT * FROM " + table + " WHERE word LIKE '" + characters + "%'", null);
        return cursor;
    }


    public Cursor selectPersianWords(String table, String characters) {
        Cursor cursor = cmd.rawQuery("SELECT * FROM " + table + " WHERE mean LIKE '" + characters + "%'", null);
        return cursor;
    }


    public int selectIsFavorite(String word) {
        Cursor cursor = cmd.rawQuery("SELECT * FROM fav_word WHERE word = '" + word + "'", null);
        int favorite = cursor.getCount();
        return favorite;
    }

    /*
    public int selectIsFavorite(String word) {
        Cursor cursor = cmd.rawQuery("SELECT * FROM fav_word WHERE word = " + word, null);
        int favorite = 0;
        while (cursor.moveToNext()) {
            favorite = cursor.getInt(cursor.getColumnIndex("word"));
        }
        return favorite;
    }
    */


    public int getRowCount(String table) {
        Cursor cursor = cmd.rawQuery("SELECT * FROM " + table, null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public String namayesh(int field, int row, String table) {
        android.database.Cursor cursor = cmd.rawQuery("SELECT * FROM " + table, null);
        cursor.moveToPosition(row);
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    //Delete Commands

    public void deleteUserWords(int id) {
        cmd.delete("user_words", " id = '" + id + "'", null);
    }


    public void deleteFavorite(String word) {
        cmd.delete("fav_word", " word = '" + word + "'", null);
    }


    //Insert Commands

    public void insertFavorite(String word, String mean, int wordID) {
        ContentValues values = new ContentValues();
        values.put("word", word);
        values.put("mean", mean);
        values.put("word_id", wordID);
        cmd.insert("fav_word", null, values);
    }

    /*
    public void insertFavorite(String table, int wordID, String word, String mean) {
        ContentValues values = new ContentValues();
        values.put("word_id", wordID);
        values.put("word", word);
        values.put("nword", mean);
        cmd.insert(table, null, values);
    }
    */


    public void insertUserWords(String table, String word, String meaning) {
        ContentValues values = new ContentValues();
        values.put("word", word);
        values.put("mean", meaning);
        cmd.insert(table, null, values);
    }


    //Update Commands

    public void updatetasvir(String tasvir) {
        ContentValues cv = new ContentValues();
        cv.put("tasvir", tasvir);
        cmd.update("login", cv, null, null);
    }


    public void updateFavorite(int fav, int id) {
        ContentValues cv = new ContentValues();
        cv.put("fav", fav);
        cmd.update("word", cv, "id=" + id, null);
    }


    public void updateVisit(int visit, int id) {
        ContentValues cv = new ContentValues();
        cv.put("visit", visit);
        cmd.update("dictionary", cv, "id=" + id, null);
    }

}
