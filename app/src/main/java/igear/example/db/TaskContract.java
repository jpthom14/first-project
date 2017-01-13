package igear.example.db;

import android.provider.BaseColumns;

public class TaskContract {
    public class TaskEntry implements BaseColumns {
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_SORT_ORDER = "sort_order";
        public static final String COL_TASK_COMPLETED = "completed";
        public static final String COL_TASK_CATEGORY = "category";
    }
}
