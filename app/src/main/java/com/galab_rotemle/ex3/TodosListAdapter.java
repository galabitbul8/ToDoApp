package com.galab_rotemle.ex3;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class TodosListAdapter extends ArrayAdapter<TodoInfo> {

    public TodosListAdapter(Activity context, ArrayList<TodoInfo> androidFlavors)
    {
        super(context, 0, androidFlavors);
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent)
//    {
//    }
}
