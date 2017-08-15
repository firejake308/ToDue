package com.insertcoolnamehere.todue;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.insertcoolnamehere.todue.dummy.DummyContent;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DailyTaskListFragment.OnListFragmentInteractionListener{
    private ViewPager mViewPager;
    private TaskListDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initalize view pager
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new TaskListsPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(android.R.drawable.ic_dialog_email);

        // initialize toolbar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        // initialize db stuff
        mDbHelper = new TaskListDbHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Task item) {

    }

    @Override
    public void onDismissItem(Task item) {
        new DeleteRowTask(item).execute();
    }

    private class DeleteRowTask extends AsyncTask<Void, Void, Integer> {
        private final int SUCCESS = 0;
        private final int FAILURE = 1;
        private Task taskToDelete;

        DeleteRowTask(Task taskToDelete) {
            this.taskToDelete = taskToDelete;
        }

        public Integer doInBackground(Void... params) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            String whereClause = TaskListContract.TaskListEntry.COLUMN_NAME_TITLE + " = ? AND "
                    + TaskListContract.TaskListEntry.COLUMN_NAME_DO_DATE + " = ?";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String[] whereArgs = {taskToDelete.getTitle(), format.format(taskToDelete.getDueDate())};
            db.delete(TaskListContract.TaskListEntry.TABLE_NAME, whereClause, whereArgs);

            return SUCCESS;
        }
    }
}
