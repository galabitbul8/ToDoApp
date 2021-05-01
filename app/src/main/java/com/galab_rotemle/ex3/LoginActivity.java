package com.galab_rotemle.ex3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login;
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle(getString(R.string.titleLogin));

        login = findViewById(R.id.login);
        login.setOnClickListener(this);

        username = findViewById(R.id.username);

    }


    @Override
    public void onClick(View v) {
        // todo : if user and password currect
        if(login.getId() == v.getId()) {
            Intent intent = new Intent(LoginActivity.this, ToDoListActivity.class);
            intent.putExtra("username",username.getText().toString());
            startActivity(intent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuItem about = menu.add("About");
        MenuItem exit = menu.add("Exit");

        about.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("About App");
                dialog.setMessage("ToDo App ("+getPackageName()+")\n\nBy Rotem Levy & Gal David Abitbul, 18/05/2021");

                dialog.show();
                return true;
            }
        });
        exit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        finish(); // kill the app
                    }
                });
                dialog.setIcon(R.drawable.exit);
                dialog.setTitle("Exit App");
                dialog.setMessage("Do you really want to exit ToDo App");
                dialog.setNegativeButton("NO",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    { }
                });
                dialog.show();
                return true;
            }
        });

        return true;
    }

}