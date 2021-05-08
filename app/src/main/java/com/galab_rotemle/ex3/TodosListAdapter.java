package com.galab_rotemle.ex3;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TodosListAdapter extends ArrayAdapter<TodoInfo> {

    public TodosListAdapter(Activity context, ArrayList<TodoInfo> androidFlavors)
    {
        super(context, 0, androidFlavors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = View.inflate(getContext(), R.layout.list_item, null);
        }
        TodoInfo currentTodoInfo = getItem(position);

        TextView titleName = convertView.findViewById(R.id.titleID);
        titleName.setText(currentTodoInfo.getTitle());

        TextView descriptionName = convertView.findViewById(R.id.descriptionID);
        descriptionName.setText(currentTodoInfo.getDescription());

        TextView dateName = convertView.findViewById(R.id.dateID);
        dateName.setText(currentTodoInfo.getDate());

        TextView timeName = convertView.findViewById(R.id.timeID);
        timeName.setText(currentTodoInfo.getTime());

        return convertView;
    }
}
