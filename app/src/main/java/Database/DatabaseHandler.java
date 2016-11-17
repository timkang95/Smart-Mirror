package Database;
import java.security.PrivateKey;
import java.util.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DatabaseHandler extends SQLiteOpenHelper{
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "users.db";

    // Columns
    private static final String TABLE_USERS  = "users";
    private static final String TABLE_ALARMS = "alarms";
    private static final String TABLE_SONGS  = "songs";
    private static final String TABLE_CLOCKS = "clocks";

    private static final String COLUMN_ID    = "_id";

    private static final String USER_NAME           = "name";
    private static final String USER_NICKNAME       = "nickname";
    private static final String USER_HOME_ADDRESS   = "home";
    private static final String USER_WORK_ADDRESS   = "work";

    private static final String ALARM_NAME = "name";
    private static final String ALARM_USER = "user_id";
    private static final String ALARM_SONG = "song_id";

    private static final String SONG_START = "start";

    private static final String CLOCK_TIME  = "time";
    private static final String CLOCK_DAY   = "day";
    private static final String CLOCK_ALARM = "alarm_id";

    private static final String USER_TABLE_CREATE =
            "create table " + TABLE_USERS + "( "
            + COLUMN_ID         + " integer primary key autoincrement, "
            + USER_NAME         + " text not null, "
            + USER_NICKNAME     + " text, "
            + USER_HOME_ADDRESS + " text not null, "
            + USER_WORK_ADDRESS + " text not null);";

    private static final String ALARM_TABLE_CREATE =
            "create table " + TABLE_ALARMS + "( "
            + COLUMN_ID  + " integer primary key autoincrement, "
            + ALARM_NAME + " text not null, "
            + ALARM_USER + " integer, "
            + ALARM_SONG + " integer);";

    private static final String CLOCK_TABLE_CREATE =
            "create table " + TABLE_CLOCKS + "( "
            + COLUMN_ID  + " integer primary key autoincrement, "
            + CLOCK_TIME + " text not null, "
            + CLOCK_DAY   + " text not null, "
            + CLOCK_ALARM + " integer);";

    private static final String DB_CREATE = USER_TABLE_CREATE + ALARM_TABLE_CREATE + CLOCK_TABLE_CREATE;

    public DatabaseHandler(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(DB_CREATE);
    };
    public void onUpgrade(SQLiteDatabase db, int x, int y){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLOCKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLOCKS);

        db.execSQL(DB_CREATE);
    };

    // Function to add new user
    public void Add_User(User u){
        SQLiteDatabase db = this.getWritableDatabase(); // Opens DB
        ContentValues cv = new ContentValues();         // Object that DB uses to store column values

        cv.put(USER_NAME, u.getName());                 // Stores the user's name
        cv.put(USER_NICKNAME, u.getNickname());             // Stores the user's nickname
        cv.put(USER_HOME_ADDRESS, u.getHomeAddress());  // Stores the user's home address
        cv.put(USER_WORK_ADDRESS, u.getWorkAddress());  // Stores the user's work address

        db.insert(TABLE_USERS, null, cv);               // Puts the new user in the DB
        db.close();                                     // Closes the DB connection
    }

    public void Add_Alarm(Alarm a) {
        SQLiteDatabase db = this.getWritableDatabase(); // Opens DB
        ContentValues cv = new ContentValues();         // Object that DB uses to store column values

        cv.put(ALARM_NAME, a.getName());                // Stores the alarm's name
        cv.put(ALARM_USER, a.getUserId());              // Stores the alarm's userId mapping
        cv.put(ALARM_SONG, a.getSongId());              // Stores the alarm's songId mapping

        db.insert(TABLE_ALARMS, null, cv);               // Puts the new user in the DB
        db.close();                                     // Closes the DB connection
    }

    public void Add_Clock(Clock c) {
        SQLiteDatabase db = this.getWritableDatabase(); // Opens DB
        ContentValues cv = new ContentValues();         // Object that DB uses to store column values

        cv.put(CLOCK_TIME, c.getTime());                // Stores the clocks's time
        cv.put(CLOCK_DAY, c.getDays());                 // Stores the days the clock is used
        cv.put(CLOCK_ALARM, c.getAlarmId());            // Stores the alarm that the clock is tied to

        db.insert(TABLE_ALARMS, null, cv);              // Puts the new user in the DB
        db.close();                                     // Closes the DB connection
    }
    // TODO: Get_XXX
    // TODO: Update_XXX
    // TODO: Delete_XXX
}

