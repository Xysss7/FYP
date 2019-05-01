package unnc.fyp.exercisehelper;
/**
 * Created by zy18816 on 2019/4/1.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    // database name
    public static final String DB_NAME = "my_database.db";
    // table name
    public static final String TABLE_NAME = "PodcastComment";
    // database version id
    public static final int DB_VERSION = 1;
    public static final String PODCASTLINK = "podcastlink";
    public static final String COMMENT = "comment";

    private Context mContext;
    public static final String CREATE_USER = "create table User ("
            + "id integer primary key autoincrement, "
            + "username text, "
            + "password text)";
    public static final String CREATE_EXERCISE = "create table Exercise ("
            + "id integer primary key autoincrement, "
            + "username text, "
            + "exercisename text, "
            + "comment text, "
            + "datetime text)";
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    // When the database file is created, the initialization operation is performed and executed only once
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_EXERCISE);
        Log.i("dt", "Dataase created succeeded");
    }

    // This method is executed when the database version is updated
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}