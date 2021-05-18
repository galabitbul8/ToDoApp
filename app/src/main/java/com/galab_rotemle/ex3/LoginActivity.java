package com.galab_rotemle.ex3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login;
    private EditText username, password;
    private SQLiteDatabase TodosDB = null;
    public static final String MY_DB_NAME = "TodosDB";
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sp.edit();
        boolean isLogged = sp.getBoolean("isLogged", false);

        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        login = findViewById(R.id.login);

        if(!isLogged) {
            login.setOnClickListener(this);
            login.setEnabled((false));
        }
        createDB();
        if(isLogged) {
            Intent intent = new Intent(LoginActivity.this, ToDoListActivity.class);
            intent.putExtra("username",sp.getString("username", ""));
            startActivity(intent);
            this.finish();
        }
        else {
            setTitle(getString(R.string.titleLogin));
            username = findViewById(R.id.username);
            password = findViewById(R.id.password);
        }
    }

    @Override
    public void onClick(View v) {
        if(login.getId() == v.getId()) {
            // check if both fields aren't empty
            if(username.getText().length() == 0 || password.getText().length() == 0) {
                Toast.makeText(this, "Please provide username and password", Toast.LENGTH_SHORT).show();
                return;
            }
            String getUserQuery = "SELECT name, password FROM users WHERE name = ? ";
            Cursor cursor = TodosDB.rawQuery(getUserQuery, new String[] {username.getText().toString()});
            if(cursor.moveToFirst()) {
                if(!password.getText().toString().equals(cursor.getString(1))) {
                    Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else {
                TodosDB.execSQL("INSERT INTO users(name, password) VALUES (?, ?)", new String[] {username.getText().toString(), password.getText().toString()});
            }
            cursor.close();
            editor.putBoolean("isLogged", true);
            editor.putString("username", username.getText().toString());
            editor.commit();
            Intent intent = new Intent(LoginActivity.this, ToDoListActivity.class);
            intent.putExtra("username",username.getText().toString());
            startActivity(intent);
            this.finish();
        }
    }

    public void createDB()
    {
        try
        {
            // Opens a current database or creates it
            TodosDB = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);
            String createUserTableQuery = "CREATE TABLE IF NOT EXISTS users (name VARCHAR primary key, password VARCHAR);";
            String createTodosTableQuery = "CREATE TABLE IF NOT EXISTS todos (id integer primary key, username VARCHAR, title VARCHAR, description VARCHAR, datetime VARCHAR );";

            TodosDB.execSQL(createUserTableQuery);
            TodosDB.execSQL(createTodosTableQuery);
        }
        catch (Exception e)
        {
            Log.d("debug", "Error Creating Database");
        }
        // Make login clickable after the database is loaded/created
        login.setEnabled(true);
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