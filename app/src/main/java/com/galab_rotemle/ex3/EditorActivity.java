package com.galab_rotemle.ex3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ALARM_ID = 111;
    private Button AddButton,DateButton,TimeButton;
    private SQLiteDatabase TodosDB = null;
    public static final String MY_DB_NAME = "TodosDB";
    private EditText title, description, date, time;
    private TextView header;
    private Integer todoId = 0;
    private Calendar cldr;
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "channel_todo";
    private static final CharSequence CHANNEL_NAME = "Todo Channel";
    private int notificationID, tableLastItemId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("ToDo Editor");
        AddButton = findViewById(R.id.addTask);
        AddButton.setOnClickListener(this);
        // get the to_do information
        Bundle bundle = getIntent().getExtras();
        String titleF = bundle.getString("title");
        username = bundle.getString("username");
        todoId = bundle.getInt("Id");
        tableLastItemId = bundle.getInt("tableLastItemId") + 1;
        header =(TextView) findViewById(R.id.header);
        // determine the header (update / add new)
        updateHeader((todoId));

        title = findViewById(R.id.Title);
        description = findViewById(R.id.Description);
        date = findViewById(R.id.Date);
        time = findViewById(R.id.Time);
        // update text
        if(todoId != 0)
            insertTodoFields(bundle);

        //pickers
        DateButton = findViewById(R.id.buttonDate);
        DateButton.setOnClickListener(this);
        TimeButton = findViewById(R.id.buttonTime);
        TimeButton.setOnClickListener(this);

        cldr = Calendar.getInstance();
        openDB();
    }



    @Override
    public void onClick(View v) {
        if(AddButton.getId() == v.getId()) {
            try {
                // validate the fields and add the to_do afterwards
                String validationStr = validateFields();
                if(validationStr.length() != 0) {
                    Toast.makeText(this, validationStr, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(todoId == 0)
                    addNewTodo(username);
                else
                    editTodo();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else if(DateButton.getId() == v.getId()){
            DatePicker();
        }else if(TimeButton.getId() == v.getId()){
            TimePicker();
        }
    }

    public void openDB()
    {
        try
        {
            // Open our DB
            TodosDB = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void addNewTodo(String username) throws ParseException {
        String dateString = date.getText().toString();
        String timeString = time.getText().toString();

        Date dateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dateString + " " + timeString);
        long datetime = dateAndTime.getTime();

        String insertTodoQuery = "INSERT INTO todos (username, title, datetime, description) "
                + "VALUES (? ,? , ? ,?) ";

        TodosDB.execSQL(insertTodoQuery,  new String[] {username, title.getText().toString(),datetime+"", description.getText().toString()});
        // add an alarm in case the date is in the future
        if(checkIfFutureTodo(datetime))
            createOneTimeAlarm(tableLastItemId++, datetime);

        Toast.makeText(this, "Todo was ADDED", Toast.LENGTH_SHORT).show();
        title.setText("");
        description.setText("");
        date.setText("");
        time.setText("");
    }

    private void editTodo() throws ParseException {
        String dateString = date.getText().toString();
        String timeString = time.getText().toString();

        Date dateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dateString + " " + timeString);
        long datetime = dateAndTime.getTime();

        String updateTodoQuery = "UPDATE todos SET title=?, description=?, datetime=? WHERE id=? ";
        TodosDB.execSQL(updateTodoQuery,  new String[] {title.getText().toString(),description.getText().toString(), datetime+"", todoId.toString() });
        Toast.makeText(this, "Todo was UPDATED", Toast.LENGTH_SHORT).show();

        // add an alarm in case the date is in the future
        if(checkIfFutureTodo(datetime))
            createOneTimeAlarm(todoId, datetime);
        // go back after editing
        onBackPressed();
    }

    private void DatePicker(){
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        DatePickerDialog timePicker = new DatePickerDialog(EditorActivity.this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String days = String.format(Locale.getDefault(),"%02d",dayOfMonth);
                String months = String.format(Locale.getDefault(),"%02d",monthOfYear + 1);
                String years = String.format(Locale.getDefault(),"%04d",year);
                date.setText(String.format("%s/%s/%s",days,months,years));
            }
        }, year,month,day);
        timePicker.show();
    }

    private void TimePicker(){
        int hours = cldr.get(Calendar.HOUR_OF_DAY);
        int min = cldr.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(EditorActivity.this, new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                String hourString = String.format(Locale.getDefault(),"%02d",hourOfDay);
                String minutesString = String.format(Locale.getDefault(),"%02d",minutes);
                time.setText(String.format("%s:%s",hourString,minutesString));
            }
        }, hours,min,true);
        timePicker.show();
    }

    private boolean validateDate(){
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        format.setLenient(false);
        try {
            format.parse(date.getText().toString());
        }catch(ParseException ex){
            Log.d("error", "ParseException: " + ex.toString());
            return false;
        }
        return true;
    }

    private boolean validateTime(){
        String timeFormat = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
        Pattern p = Pattern.compile(timeFormat);
        Matcher matcher = p.matcher(time.getText().toString());
        return matcher.matches();

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(EditorActivity.this, ToDoListActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
        this.finish();
    }

    private void insertTodoFields(Bundle bundle) {
        title.setText(bundle.getString("title"));
        description.setText(bundle.getString("description"));
        date.setText(bundle.getString("date"));
        time.setText(bundle.getString("time"));
    }

    private void updateHeader(Integer todoId) {
        if(todoId == 0) {
            header.setText("ADD new Todo");
            AddButton.setText("ADD");
        }
        else {
            header.setText("UPDATE Todo (id="+ todoId.toString() + ")");
            AddButton.setText("UPDATE");
        }
    }
    private String validateFields() {
        String errorMessage = "";
        if(title.getText().length() == 0)
            errorMessage += "Title is missing, ";
        if(description.getText().length() == 0)
            errorMessage += "Description is missing, ";
        if(date.getText().length() == 0)
            errorMessage += "Date is missing, ";
        else if(validateDate() == false)
            errorMessage += "Date is invalid, ";
        if(time.getText().length() == 0)
            errorMessage += "Time is missing, ";
        else if(validateTime() == false)
            errorMessage += "Time is invalid, ";
        return errorMessage;
    }

    // Create an alarm  that will fire a notification
    private void createOneTimeAlarm(Integer todoId, long datetime)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Create Intent and pass fields
        Intent alarmIntent = new Intent(this, AlarmTodoReceiver.class);
        alarmIntent.putExtra("username", this.username);
        alarmIntent.putExtra("todoId", todoId);
        alarmIntent.putExtra("title", this.title.getText().toString());

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this, todoId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, datetime, alarmPendingIntent);
    }

    private boolean checkIfFutureTodo(long todo_datetime) {
        if(todo_datetime > System.currentTimeMillis())
            return true;
        return false;
    }

}