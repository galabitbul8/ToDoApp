package com.galab_rotemle.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity implements View.OnClickListener, ListView.OnItemClickListener {

    private String username;
    private boolean commingFromEdit = false;
    private FloatingActionButton floatingActionButton;
    private SharedPreferences.Editor editor;
    private SQLiteDatabase TodosDB = null;
    private ArrayList<TodoInfo> todosList;
    public static final String MY_DB_NAME = "TodosDB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Create our list
        todosList = new ArrayList<TodoInfo>();

        // Create the adapter
        TodosListAdapter todoAdapter = new TodosListAdapter(this, todosList);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = findViewById(R.id.todosListID);
        listView.setAdapter(todoAdapter);

        //TODO: add implement for ListView.OnItemClickListener
//        listView.setOnItemClickListener(this);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        setTitle("Todo List" + "("+username+")");

        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sp.edit();

        floatingActionButton = findViewById(R.id.addTask);
        floatingActionButton.setOnClickListener(this);

        openDB();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exitmenu, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        // TODO: check what should we do in case of going back
        Log.d("myLog", "onBackPressed: ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId() == R.id.exit){
           editor.putBoolean("isLogged", false);
           editor.commit();
           Intent intent = new Intent(ToDoListActivity.this, LoginActivity.class);
           startActivity(intent);
       }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(floatingActionButton.getId() == v.getId()) {
            commingFromEdit = true;
            Intent intent = new Intent(ToDoListActivity.this, EditorActivity.class);
            intent.putExtra("username",username);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        TodoInfo todoInfo = todosList.get(position);
        Log.d("myLog", "onItemClick: " + todoInfo.getTitle());
    }

    private void loadListData() {
        String loadDataQuery = "SELECT title, description, datetime FROM todos"
                + " WHERE username = ? ";
            Cursor cursor = TodosDB.rawQuery(loadDataQuery, new String[] {username});
        Log.d("myLog1", "before loadListData: ");
        if(cursor.moveToFirst()) {
            String title, description;
            Integer datetime;
            do {
                title = cursor.getString(cursor.getColumnIndex("title"));
                description = cursor.getString(cursor.getColumnIndex("description"));
                datetime = cursor.getInt(cursor.getColumnIndex("datetime"));
                todosList.add(new TodoInfo(title, description, datetime));

            } while(cursor.moveToNext());
            cursor.close();
        }
    }

    public void openDB()
    {
        try
        {
            // Open our DB
            TodosDB = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);
            loadListData();
        }
        catch (Exception e)
        {
            Log.d("debug", "Error Opening Database");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("myLog1", "onResume: com " + commingFromEdit);
        if(commingFromEdit) {
            loadListData();
        }
        commingFromEdit = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("myLog1", "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("myLog1", "onStop: ");
    }
}