package com.galab_rotemle.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("ToDo Editor");
        button = findViewById(R.id.addTask);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //todo: when add the task
        if(button.getId() == v.getId()) {
            Intent intent = new Intent(EditorActivity.this, ToDoListActivity.class);
            Bundle bundle = getIntent().getExtras();
            String username = bundle.getString("username");
            intent.putExtra("username",username);
            startActivity(intent);
        }
    }
}