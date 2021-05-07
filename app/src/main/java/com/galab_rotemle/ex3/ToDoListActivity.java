package com.galab_rotemle.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ToDoListActivity extends AppCompatActivity implements View.OnClickListener {

    private String username;
    private FloatingActionButton floatingActionButton;
    private SharedPreferences.Editor editor;
    private SQLiteDatabase TodosDB = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        setTitle("Todo List" + "("+username+")");

        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sp.edit();

        floatingActionButton = findViewById(R.id.addTask);
        floatingActionButton.setOnClickListener(this);
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
            Intent intent = new Intent(ToDoListActivity.this, EditorActivity.class);
            intent.putExtra("username",username);
            startActivity(intent);
        }
    }
}