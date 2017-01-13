package igear.example.classes;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;


public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}