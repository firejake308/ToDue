package com.insertcoolnamehere.todue;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    private String dateToSet = "NONE";
    private TaskListDbHelper mDbHelper;

    private Button dueDateEntry;
    private Button doDateEntry;

    private Date dueDate;
    private Date doDate;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        dueDateEntry = (Button) findViewById(R.id.due_date_entry);
        dueDateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateToSet = "DUE";
                showDatePickerDialog();
            }
        });

        doDateEntry = (Button) findViewById(R.id.do_date_entry);
        doDateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateToSet = "DO";
                showDatePickerDialog();
            }
        });

        // initialize spinner
        Spinner categorySelector = (Spinner) findViewById(R.id.category_selector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.task_categories, android.R.layout.simple_spinner_dropdown_item);
        categorySelector.setAdapter(adapter);
        categorySelector.setOnItemSelectedListener(this);

        // initialize database stuff
        mDbHelper = new TaskListDbHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_save_changes:
                saveChanges();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        selectedCategory = getResources().getStringArray(R.array.task_categories)[pos];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void saveChanges() {
        EditText titleEntry = (EditText) findViewById(R.id.title_entry);
        String title = titleEntry.getText().toString();

        // check for nulls
        if (TextUtils.isEmpty(title)) {
            titleEntry.setError(getString(R.string.error_no_title));
            return;
        }
        if (dueDate == null) {
            dueDateEntry.setError(getString(R.string.error_no_due_date));
            return;
        }
        if (doDate == null) {
            doDateEntry.setError(getString(R.string.error_no_do_date));
            return;
        }

        // if everything is filled out, create a new task object
        DailyTaskListFragment.addDailyTask(new Task(title, doDate, dueDate, selectedCategory));
        // add to database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskListContract.TaskListEntry.COLUMN_NAME_TITLE, title);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        values.put(TaskListContract.TaskListEntry.COLUMN_NAME_DO_DATE, format.format(doDate));
        values.put(TaskListContract.TaskListEntry.COLUMN_NAME_DUE_DATE, format.format(dueDate));
        values.put(TaskListContract.TaskListEntry.COLUMN_NAME_CATEGORY, selectedCategory);

        long newRowId = db.insert(TaskListContract.TaskListEntry.TABLE_NAME, null, values);

        // go back to main activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showDatePickerDialog() {
        new DatePickerFragment().setListener(this).show(getSupportFragmentManager(), "datePicker");
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // format the date the way we want it
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
        String displayDate = "";
        Date date = new Date();
        try {
            date = formatter.parse(month + "/" + day + "/" + year);
            displayDate = displayFormat.format(date);
        } catch (ParseException e) {
            Log.e("NewTaskActivity", "Error parsing date", e);
        }


        switch(dateToSet) {
            case "DUE":
                dueDateEntry.setText(String.format(getString(R.string.display_due_date), displayDate));
                dueDate = date;
                break;
            case "DO":
                doDateEntry.setText(String.format(getString(R.string.display_do_date), displayDate));
                doDate = date;
                break;
        }
        dateToSet = "NONE";
    }
}
