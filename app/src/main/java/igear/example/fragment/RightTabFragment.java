package igear.example.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Ref;

import igear.example.R;
import igear.example.classes.MyItemTouchHelperCallback;
import igear.example.classes.MyRecyclerAdapter;
import igear.example.classes.Task;
import igear.example.db.TaskContract;
import igear.example.db.TaskDatabaseHelper;

public class RightTabFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyRecyclerAdapter myRecyclerAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private Task[] taskList;
    private TaskDatabaseHelper myHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rightTabView = inflater.inflate(R.layout.fragment_right_tab, container, false);

        myHelper = new TaskDatabaseHelper(this.getContext());

        recyclerView = (RecyclerView) rightTabView.findViewById(R.id.completedListRecyclerView);

        // use a linear layout manager
        myLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(myLayoutManager);
        taskList = new Task[0];
        myRecyclerAdapter = new MyRecyclerAdapter(taskList, myHelper);
        recyclerView.setAdapter(myRecyclerAdapter);

        RefreshDisplay();

        return rightTabView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            RefreshDisplay();
        }
    }

    private void RefreshDisplay() {
        Cursor cursor = myHelper.getAll(true);
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
}
