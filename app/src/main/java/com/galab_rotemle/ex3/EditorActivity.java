package com.galab_rotemle.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        setTitle("ToDo Editor");
    }
}