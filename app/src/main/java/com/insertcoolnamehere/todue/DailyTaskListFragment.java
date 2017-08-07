package com.insertcoolnamehere.todue;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.insertcoolnamehere.todue.dummy.DummyContent;
import com.insertcoolnamehere.todue.dummy.DummyContent.DummyItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A fragment representing a list of Tasks.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DailyTaskListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private TaskListRecyclerViewAdapter mAdapter;
    private TaskListDbHelper mDbHelper;

    private static ArrayList<Task> dailyTasks = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DailyTaskListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DailyTaskListFragment newInstance(int columnCount) {
        DailyTaskListFragment fragment = new DailyTaskListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        // initialize db stuff
        mDbHelper = new TaskListDbHelper(getContext());
        new FetchSavedTasksTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mAdapter = new TaskListRecyclerViewAdapter(dailyTasks, mListener);
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }

    public static void addDailyTask(Task task) {
        if (!dailyTasks.contains(task))
            dailyTasks.add(task);

        // resort task list
        for (int i = 0; i < dailyTasks.size(); i++) {
            for(int j = 0; j < dailyTasks.size()-1; j++) {
                if (dailyTasks.get(j).compareTo(dailyTasks.get(j+1)) > 0) {
                    // switch if the previous task has a later do date
                    Task laterTask = dailyTasks.get(j);
                    dailyTasks.set(j, dailyTasks.get(j+1));
                    dailyTasks.set(j+1, laterTask);
                }
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Task item);
    }

    private class FetchSavedTasksTask extends AsyncTask<Void, Void, Integer> {
        private final int SUCCESS = 0;
        private final int OTHER_FAILURE = 1;

        public Integer doInBackground(Void... params) {
            // fetch tasks from db
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            // which columns from the db will I actually use?
            String[] projection = {
                    TaskListContract.TaskListEntry.COLUMN_NAME_TITLE,
                    TaskListContract.TaskListEntry.COLUMN_NAME_DO_DATE,
                    TaskListContract.TaskListEntry.COLUMN_NAME_DUE_DATE,
                    TaskListContract.TaskListEntry.COLUMN_NAME_CATEGORY
            };

            String sortOrder = TaskListContract.TaskListEntry.COLUMN_NAME_DO_DATE + " DESC";

            Cursor cursor = db.query(
                    TaskListContract.TaskListEntry.TABLE_NAME,  // desired table
                    projection,                                 // columns to return
                    null,                                       // columns for WHERE clause
                    null,                                       // values for WHERE clause
                    null,                                       // don't group rows
                    null,                                       // don't filter by group
                    sortOrder                                   // sort order
            );

            while(cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(TaskListContract.TaskListEntry.COLUMN_NAME_TITLE));
                String doDateStr = cursor.getString(cursor.getColumnIndex(TaskListContract.TaskListEntry.COLUMN_NAME_DO_DATE));
                String dueDateStr = cursor.getString(cursor.getColumnIndex(TaskListContract.TaskListEntry.COLUMN_NAME_DUE_DATE));
                String category = cursor.getString(cursor.getColumnIndex(TaskListContract.TaskListEntry.COLUMN_NAME_CATEGORY));

                // convert strings to date
                Date doDate = new Date();
                Date dueDate = new Date();
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    doDate = format.parse(doDateStr);
                    dueDate = format.parse(dueDateStr);
                } catch(ParseException e) {
                    Log.e("ParseException", "couldn't parse date from db", e);
                }
                addDailyTask(new Task(title, doDate, dueDate, category));
            }
            cursor.close();

            return SUCCESS;
        }

        @Override
        public void onPostExecute(Integer result) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
