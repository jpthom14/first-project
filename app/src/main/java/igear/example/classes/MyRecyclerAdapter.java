package igear.example.classes;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import igear.example.R;
import igear.example.db.TaskContract;
import igear.example.db.TaskDatabaseHelper;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private List<Task> mDataset;
    private TaskDatabaseHelper taskDatabaseHelper;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                //switch the sort order of the tasks
                Task first = mDataset.get(i);
                Task second = mDataset.get(i + 1);
                taskDatabaseHelper.update(first.ID, first.Title, second.Sort_Order, 0);
                taskDatabaseHelper.update(second.ID, second.Title, first.Sort_Order, 0);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                //switch the sort order of the tasks
                Task first = mDataset.get(i);
                Task second = mDataset.get(i - 1);
                taskDatabaseHelper.update(first.ID, first.Title, second.Sort_Order, 0);
                taskDatabaseHelper.update(second.ID, second.Title, first.Sort_Order, 0);
            }
        }
        UpdateFromDatabase();
    }

    @Override
    public void onItemDismiss(int position) {
        //delete from task, move to completed task (database)
        Task chosenTask = mDataset.get(position);
        taskDatabaseHelper.update(chosenTask.ID, chosenTask.Title, chosenTask.Sort_Order, 1);
        UpdateFromDatabase();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            TextView textView = (TextView) v.findViewById(R.id.task_text);
            mTextView = textView;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRecyclerAdapter(Task[] myDataset, TaskDatabaseHelper helper) {
        mDataset = new LinkedList(Arrays.asList(myDataset));
        notifyDataSetChanged();
        taskDatabaseHelper = helper;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).Title);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addItems(Task[] items){
        mDataset = new LinkedList(Arrays.asList(items));
        notifyDataSetChanged();
    }

    private void UpdateFromDatabase(){
        Cursor cursor = taskDatabaseHelper.getAll(false);
        Task[] taskList = new Task[cursor.getCount()];
        int arrayIndex = 0;
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            int sortorder = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_SORT_ORDER));
            Task newTask = new Task(id, title, sortorder);
            taskList[arrayIndex] = newTask;
            arrayIndex++;
        }

        addItems(taskList);
        cursor.close();
    }
}
