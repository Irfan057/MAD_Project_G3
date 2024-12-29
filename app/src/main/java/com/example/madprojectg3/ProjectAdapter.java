package com.example.madprojectg3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ProjectAdapter extends BaseAdapter {

    private Context context;
    private List<Project> projectList;

    public ProjectAdapter(Context context, List<Project> projectList) {
        this.context = context;
        this.projectList = projectList;
    }

    @Override
    public int getCount() {
        return projectList.size();
    }

    @Override
    public Object getItem(int position) {
        return projectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            // Inflate the layout for each list item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_project, null);
        }

        // Get the current project
        Project project = projectList.get(position);

        // Find views and set the data
        TextView projectNameTextView = view.findViewById(R.id.projectNameTextView);

        // Set the project name to the TextView
        projectNameTextView.setText(project.getName());

        return view;
    }
}
