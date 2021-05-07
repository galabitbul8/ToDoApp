package com.galab_rotemle.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private SQLiteDatabase TodosDB = null;
    public static final String MY_DB_NAME = "TodosDB";
    private EditText title, descirption, date, time;
    private Integer todoId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("ToDo Editor");
        button = findViewById(R.id.addTask);
        button.setOnClickListener(this);
        title = findViewById(R.id.Title);
        descirption = findViewById(R.id.Description);
        date = findViewById(R.id.Date);
        time = findViewById(R.id.Time);
        Log.d("myLog", "onCreate editor: ");
        openDB();
    }

    @Override
    public void onClick(View v) {
        //todo: when add the task
        if(button.getId() == v.getId()) {
//            Intent intent = new Intent(EditorActivity.this, ToDoListActivity.class);
            Bundle bundle = getIntent().getExtras();
            String username = bundle.getString("username");
//            intent.putExtra("username",username);
//            startActivity(intent);
            addNewTodo(username);

        }
    }

    public void openDB()
    {
        try
        {
            // Open our DB
            TodosDB = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);
            Log.d("myLog", "openDB: ");

        }
        catch (Exception e)
        {
            Log.d("debug", "Error Opening Database");
        }
    }

    private void addNewTodo(String username) {
        Log.d("myLog", "beforeaddNewTodo: ");
        // TODO: get the date and time values and store the integer representation
        String insertTodoQuery = "INSERT INTO todos (username, title, datetime, description) "
                + "VALUES (? ,? , '25/02/1990' ,?) ";

        TodosDB.execSQL(insertTodoQuery,  new String[] {username, title.getText().toString(), descirption.getText().toString()});
        Toast.makeText(this, "Todo was ADDED", Toast.LENGTH_SHORT).show();
        //TODO: add the ALARM notification thing after validating the date time info
    }
}