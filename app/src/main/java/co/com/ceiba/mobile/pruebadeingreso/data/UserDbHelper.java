package co.com.ceiba.mobile.pruebadeingreso.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper {
    public static final int databaseVersion = 1;
    public static final String databaseName = "Users.db";

    public UserDbHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT," +
                "email TEXT," +
                "phone TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long createUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert("user",
                null,
                user.toContentValues());
    }

    public Cursor getUser(String userId){
        SQLiteDatabase db = getWritableDatabase();
        return db.query("user",
                null,
                "id = ?",
                new String[] { userId },
                null,
                null,
                null,
                null);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query("user",
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
