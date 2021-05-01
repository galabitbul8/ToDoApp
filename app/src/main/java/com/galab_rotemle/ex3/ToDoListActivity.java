package com.galab_rotemle.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ToDoListActivity extends AppCompatActivity implements View.OnClickListener {

    private String username;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        setTitle("Todo List" + "("+username+")");
        floatingActionButton = findViewById(R.id.addTask);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exitmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId() == R.id.exit){
           Intent intent = new Intent(ToDoListActivity.this, LoginActivity.class);
           startActivity(intent);
       }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(floatingActionButton.getId() == v.getId()) {
            Intent intent = new Intent(ToDoListActivity.this, EditorActivity.class);
            startActivity(intent);
        }
    }
}