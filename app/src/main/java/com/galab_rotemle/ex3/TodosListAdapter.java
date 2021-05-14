package com.galab_rotemle.ex3;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class TodosListAdapter extends ArrayAdapter<TodoInfo> implements Filterable {
    private ArrayList<TodoInfo> myList;
    private ArrayList<TodoInfo> myFullList;

    public TodosListAdapter(Activity context, ArrayList<TodoInfo> androidFlavors)
    {
        super(context, 0, androidFlavors);
        this.myList = androidFlavors;
        Log.d("myLog5", "TodosListAdapter: " + androidFlavors.size());
        myFullList = new ArrayList<TodoInfo>(myList);
    }

    public void updateList(ArrayList<TodoInfo> androidFlavors) {
        Log.d("myLog5", "updateList: " + androidFlavors.size());
        this.myList = androidFlavors;
        myFullList = new ArrayList<TodoInfo>(myList);

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



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    //no constraint given, just return all the data. (no search)
                    results.count = myFullList.size();
                    results.values = myFullList;
                } else {//do the search
                    ArrayList<TodoInfo> resultsData = new ArrayList<>();
                    String searchStr = constraint.toString().toUpperCase();
                    Log.d("myLog5", "performFiltering size: " + myFullList.size());

                    for (TodoInfo todo : myFullList) {
                        if (todo.getTitle().toUpperCase().contains(searchStr) || todo.getDescription().toUpperCase().contains(searchStr))
                            resultsData.add(todo);
                    }

                    results.count = resultsData.size();
                    results.values = resultsData;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                myList.clear();
                myList.addAll((ArrayList<TodoInfo>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
