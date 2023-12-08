package com.kumar.todolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private EditText taskEditText;
    private ListView taskListView;
    private ArrayAdapter<String> taskAdapter;
    private ArrayList<String> taskList=new ArrayList<>();
    private static final String PREFS_NAME = "MyAppPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //taskEditText=findViewById(R.id.taskEditText);
        taskListView=findViewById(R.id.taskListView);

        taskAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,taskList);
        taskListView.setAdapter(taskAdapter);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDeleteConfirmationDialog(i);
            }
        });
        loadTasks();


    }

    public void showDeleteConfirmationDialog(final int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this task?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteTask(position);
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    public void deleteTask(int position){
        taskList.remove(position);
        taskAdapter.notifyDataSetChanged();
    }
    public void addTask(View view){
        // Create an AlertDialog to prompt the user for a task
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a Task");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTask = input.getText().toString();
                if (!newTask.isEmpty()) {
                    taskList.add(newTask);
                    taskAdapter.notifyDataSetChanged();
                    saveTasks();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }

    // Load tasks from SharedPreferences
    private void loadTasks() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> tasksSet = preferences.getStringSet("tasks", new HashSet<String>());
        taskList.addAll(tasksSet);
        taskAdapter.notifyDataSetChanged();
    }

    // Save tasks to SharedPreferences
    private void saveTasks() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> tasksSet = new HashSet<>(taskList);
        editor.putStringSet("tasks", tasksSet);
        editor.apply();
    }


}


