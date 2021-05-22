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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity implements View.OnClickListener, ListView.OnItemClickListener, ListView.OnItemLongClickListener, SearchView.OnQueryTextListener {

    private String username;
    private boolean commingFromEdit = false;
    private FloatingActionButton floatingActionButton;
    private SharedPreferences.Editor editor;
    private SQLiteDatabase TodosDB = null;
    private ArrayList<TodoInfo> todosList;
    private TodosListAdapter todoAdapter ;
    public static final String MY_DB_NAME = "TodosDB";
    private SearchView searchBar;
    private int tableLastItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Create our list
        todosList = new ArrayList<TodoInfo>();

        // Create the adapter
        todoAdapter = new TodosListAdapter(this, todosList);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = findViewById(R.id.todosListID);
        listView.setAdapter(todoAdapter);
        listView.setTextFilterEnabled((true));
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        searchBar =(SearchView) findViewById(R.id.searchView);
        searchBar.setOnQueryTextListener(this);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        setTitle("Todo List" + "("+username+")");

        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sp.edit();

        floatingActionButton = findViewById(R.id.addTask);
        floatingActionButton.setOnClickListener(this);

        openDB();
    }

    @Override
    public boolean onQueryTextChange(String text) {
        todoAdapter.notifyDataSetChanged();
        // Perform our search for every text change
        todoAdapter.getFilter().filter(text);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // currently there's no need to implement submitting
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exitmenu, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        // TODO: check what should we do in case of going back - for now we will close the activity
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId() == R.id.exit){
           editor.putBoolean("isLogged", false);
           editor.commit();
           Intent intent = new Intent(ToDoListActivity.this, LoginActivity.class);
           startActivity(intent);
           this.finish();
       }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(floatingActionButton.getId() == v.getId()) {
            commingFromEdit = true;
            Intent intent = new Intent(ToDoListActivity.this, EditorActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("tableLastItemId", tableLastItemId);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        TodoInfo todoInfo = todosList.get(position);
        commingFromEdit = true;
        Intent intent = new Intent(ToDoListActivity.this, EditorActivity.class);

        intent.putExtra("title",todoInfo.getTitle());
        intent.putExtra("description",todoInfo.getDescription());
        intent.putExtra("date",todoInfo.getDate());
        intent.putExtra("time",todoInfo.getTime());
        intent.putExtra("Id",todoInfo.getId());
        intent.putExtra("username",username);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        DeleteTodoDialog(this, position);
        return true;
    }

    public void DeleteTodoDialog(Context context,int index){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ToDoListActivity.this);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String deleteTask = "DELETE FROM todos WHERE id=" + todosList.get(index).getId() + ";";
                TodosDB.execSQL(deleteTask);

                todosList.remove(index);
                todoAdapter.notifyDataSetChanged();
                Toast.makeText(context, "Todo was deleted", Toast.LENGTH_SHORT).show();

            }


        });

        dialog.setIcon(R.drawable.exit);
        dialog.setTitle("Delete Todo");
        dialog.setMessage("Are you sure you want to delete " + todosList.get(index).getTitle() + "?");
        dialog.setNegativeButton("NO",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            { }
        });
        dialog.show();

    }

    private void loadListData() {
        String loadDataQuery = "SELECT id,title, description, datetime FROM todos"
                + " WHERE username = ? ";
            Cursor cursor = TodosDB.rawQuery(loadDataQuery, new String[] {username});
        if(cursor.moveToFirst()) {
            int id;
            String title, description;
            long datetime;
            do {
                id =  cursor.getInt(cursor.getColumnIndex("id"));
                title = cursor.getString(cursor.getColumnIndex("title"));
                description = cursor.getString(cursor.getColumnIndex("description"));
                datetime = cursor.getLong(cursor.getColumnIndex("datetime"));
                todosList.add(new TodoInfo(id,title, description, datetime));
            } while(cursor.moveToNext());
            tableLastItemId = id;
            todoAdapter.updateList(todosList);
            todoAdapter.notifyDataSetChanged();
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
        if(commingFromEdit) {
            loadListData();
        }
        commingFromEdit = false;
    }

}