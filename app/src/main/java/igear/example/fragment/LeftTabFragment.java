package igear.example.fragment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;


import igear.example.classes.MyItemTouchHelperCallback;
import igear.example.classes.MyRecyclerAdapter;
import igear.example.R;
import igear.example.classes.Task;
import igear.example.db.TaskDatabaseHelper;
import igear.example.db.TaskContract;

///Our class extending fragment
public class LeftTabFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyRecyclerAdapter myRecyclerAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private Task[] taskList;
    private TaskDatabaseHelper myHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View leftTabView = inflater.inflate(R.layout.fragment_left_tab, container, false);
        myHelper = new TaskDatabaseHelper(this.getContext());

        recyclerView = (RecyclerView) leftTabView.findViewById(R.id.todoListRecyclerView);

        // use a linear layout manager
        myLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(myLayoutManager);
        taskList = new Task[0];
        myRecyclerAdapter = new MyRecyclerAdapter(taskList, myHelper);
        recyclerView.setAdapter(myRecyclerAdapter);

        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(myRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        RefreshDisplay();
        FloatingActionButton addTaskButton = (FloatingActionButton) leftTabView.findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAddTaskDialog("");
            }
        });

        return leftTabView;
    }

    private void RefreshDisplay() {
        Cursor cursor = myHelper.getAll(false);
        taskList = new Task[cursor.getCount()];
        int arrayIndex = 0;
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            int sortorder = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_SORT_ORDER));
            Task newTask = new Task(id, title, sortorder);
            taskList[arrayIndex] = newTask;
            arrayIndex++;
        }

        myRecyclerAdapter.addItems(taskList);
        cursor.close();
    }

    private void ShowAddTaskDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Add Task");

        final EditText input = new EditText(this.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(text);

        builder.setView(input);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String task_Text = input.getText().toString();
                int itemCount = myRecyclerAdapter.getItemCount();
                myHelper.add(task_Text, itemCount + 1);
                RefreshDisplay();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertToShow = builder.create();
        alertToShow.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertToShow.show();
    }


}
