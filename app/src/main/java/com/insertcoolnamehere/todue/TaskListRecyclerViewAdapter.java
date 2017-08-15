package com.insertcoolnamehere.todue;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.insertcoolnamehere.todue.DailyTaskListFragment.OnListFragmentInteractionListener;
import com.insertcoolnamehere.todue.dummy.DummyContent.DummyItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter<TaskListRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private final List<Task> mValues;
    private final OnListFragmentInteractionListener mListener;

    public TaskListRecyclerViewAdapter(List<Task> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd", Locale.ENGLISH);
        Date doDate = mValues.get(position).getDoDate();
        Date dueDate = mValues.get(position).getDueDate();
        holder.mDateView.setText(String.format(holder.mDateView.getContext().getString(R.string.task_display_date_format), formatter.format(doDate), formatter.format(dueDate)));

        // set icon based on category
        switch (holder.mItem.getCategory()) {
            case "Biology":
                holder.mIconView.setImageDrawable(holder.mView.getContext().getDrawable(R.drawable.ic_category_biology));
                holder.mIconView.setBackground(holder.mView.getContext().getDrawable(R.drawable.background_biology));
                break;
            case "Chemistry":
                holder.mIconView.setImageDrawable(holder.mView.getContext().getDrawable(R.drawable.ic_category_chemistry));
                holder.mIconView.setBackground(holder.mView.getContext().getDrawable(R.drawable.background_chemistry));
                break;
            case "Calculus":
                holder.mIconView.setImageDrawable(holder.mView.getContext().getDrawable(R.drawable.ic_category_calculus));
                holder.mIconView.setBackground(holder.mView.getContext().getDrawable(R.drawable.background_calculus));
                break;
            case "Personal":
            default:
                holder.mIconView.setImageDrawable(holder.mView.getContext().getDrawable(R.drawable.ic_category_personal));
                holder.mIconView.setBackground(holder.mView.getContext().getDrawable(R.drawable.background_personal));
                break;
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onItemMove(int fromPos, int toPos) {
        Task displacedTask = mValues.get(toPos);
        mValues.set(toPos, mValues.get(fromPos));
        mValues.set(fromPos, displacedTask);
        notifyItemMoved(fromPos, toPos);
    }

    @Override
    public void onItemDismiss(int pos) {
        mValues.remove(pos);
        mListener.onDismissItem(mValues.get(pos));
        notifyItemRemoved(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mDateView;
        public final ImageView mIconView;
        public Task mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.id);
            mDateView = (TextView) view.findViewById(R.id.content);
            mIconView = (ImageView) view.findViewById(R.id.category_icon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDateView.getText() + "'";
        }
    }
}
