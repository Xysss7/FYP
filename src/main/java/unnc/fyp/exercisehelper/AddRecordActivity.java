package unnc.fyp.exercisehelper;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Calendar;
import android.app.DatePickerDialog;

public class AddRecordActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private String username;
    private Spinner spinner_exercise_add;
    private EditText comment_add;
    private TextView mDatePicker;

    private DatePicker datePicker;
    private String comment;
    private String exercise = "Others..";
    private String date;
    private Button save;
    private Calendar cal;
    private int year;
    private int month;
    private int day;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");




        spinner_exercise_add = findViewById(R.id.spinner_exercise_add);
        comment_add = findViewById(R.id.comment_add);
        //datePicker = findViewById(R.id.datePicker);
        mDatePicker = findViewById(R.id.mDatePicker);
        //获取日历的一个对象
        cal = Calendar.getInstance();
        //获取年月日秒
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH)+1;//Calendar的月因为默认月份从0开始
        day = cal.get(Calendar.DAY_OF_MONTH);//当月几日
        mDatePicker.setText(year + "/" + month + "/" + day); // preset the day into today

        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddRecordActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Log.d(TAG, "onDateSet: date: " + year + "/" + month + "/" + dayOfMonth);
                int rm = month + 1;
                mDatePicker.setText(year + "/" + rm + "/" + dayOfMonth);
            }
        };


        save = findViewById(R.id.BtnSaveRecord);

        comment = comment_add.getText().toString();
        setSpinner();
        dbHelper = new DBHelper(this,"ExerciseMusic.db", null, 1);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment = comment_add.getText().toString();
                date = mDatePicker.getText().toString();
                saveToDB(username, exercise, comment, date);
            }
        });

    }

    private void setSpinner() {
        String[] e_type = new String[]{"Others..","Treadmill", "Elliptical Machine", "Sit-ups", "Squat", "Step-ups"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, e_type);  //创建一个数组适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式

        spinner_exercise_add = super.findViewById(R.id.spinner_exercise_add);
        spinner_exercise_add.setAdapter(adapter);
        spinner_exercise_add.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exercise = parent.getItemAtPosition(position).toString();          //store the style name selected in string
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void saveToDB(String u, String e, String c, String d) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", u);
        values.put("exercisename", e);
        values.put("comment", c);
        values.put("datetime", d);

        db.insert("Exercise", null, values);
        Toast.makeText(this, "save into database success", Toast.LENGTH_SHORT).show();



    }
}
