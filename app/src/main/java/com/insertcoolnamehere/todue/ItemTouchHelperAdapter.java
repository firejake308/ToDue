package com.insertcoolnamehere.todue;

/**
 * Created by hassa on 8/14/2017.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPos, int toPos);

    void onItemDismiss(int pos);
}
