package unnc.fyp.exercisehelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExerciseMonitorActivity extends AppCompatActivity {
    private String username;
    private DBHelper dbHelper;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_monitor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");

        linearLayout = findViewById(R.id.exercise_record_list);
        TextView exercise_data_name = findViewById(R.id.exercise_data_name);
        exercise_data_name.setTypeface(Typeface.SERIF);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Contact: xysasha@outlook.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DBHelper(this,"ExerciseMusic.db", null, 1);

        checkRecords();
    }

    private void checkRecords(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String comment_get = "";
        String exercise_get = "";
        String date_get = "";
        String table = "Exercise";
        String[] columns = new String[] {"exercisename", "comment","datetime"};
        String selection = "username=?";
        String[] selectionArgs = new String[] {username};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy,null );

        if(cursor.moveToFirst()) {
            do {
                String exercisename = cursor.getString(cursor.getColumnIndex("exercisename"));
                String comment = cursor.getString(cursor.getColumnIndex("comment"));
                String datetime = cursor.getString(cursor.getColumnIndex("datetime"));

                Log.d("******", "exercise name is " +exercisename);
                Log.d("******", "exercise name is " +comment);
                Log.d("******", "exercise name is " +datetime);

                addViewHere(exercisename, comment, datetime);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void addViewHere(String e, String c, String d) {
        // create new text view
        final TextView textview1 = new TextView(this);
        textview1.setText(username);
        final TextView textview2 = new TextView(this);
        textview2.setText(e);
        textview2.setTextSize(26);
        textview2.setTypeface(Typeface.SERIF);
        final TextView textview3 = new TextView(this);
        textview3.setText(c);
        textview3.setTextSize(14);
        textview3.setPadding(0,3,0,0);

        final TextView textview4 = new TextView(this);
        textview4.setText(d);
        textview4.setTextSize(12);
        textview4.setPadding(0,3,0,16);


        //linearLayout.addView(textview1);
        linearLayout.addView(textview2);
        linearLayout.addView(textview3);
        linearLayout.addView(textview4);

    }
}

