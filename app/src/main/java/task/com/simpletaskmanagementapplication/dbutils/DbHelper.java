package task.com.simpletaskmanagementapplication.dbutils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tasks_db";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tasks table
        db.execSQL(Task.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Task.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }


    public long insertTask(Task task) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Task.COLUMN_TASK_TITLE, task.getTaskTitle());
        values.put(Task.COLUMN_TASK_DETAILS, task.getTaskDetails());
        values.put(Task.COLUMN_TASK_DUE_DATE, task.getTaskDueDate());
        values.put(Task.COLUMN_TASK_PRIORITY, task.getTaskPriority());
        values.put(Task.COLUMN_TASK_status, task.getTaskCompletionStatus());

        // insert row
        long id = db.insert(Task.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Task getTask(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Task.TABLE_NAME,
                new String[]{Task.COLUMN_ID,
                        Task.COLUMN_TASK_TITLE,
                        Task.COLUMN_TASK_DETAILS,
                        Task.COLUMN_TASK_DUE_DATE,
                        Task.COLUMN_TASK_PRIORITY,
                        Task.COLUMN_TASK_status,
                        Task.COLUMN_TIMESTAMP},
                Task.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare task object
        Task task = new Task(
                cursor.getInt(cursor.getColumnIndex(Task.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_TITLE)),
                cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_DETAILS)),
                cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_DUE_DATE)),
                cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_PRIORITY)),
                cursor.getInt(cursor.getColumnIndex(Task.COLUMN_TASK_status)),
                cursor.getString(cursor.getColumnIndex(Task.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return task;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String test = "substr(" + Task.COLUMN_TASK_DUE_DATE + ",7,4)||'-'||substr(" + Task.COLUMN_TASK_DUE_DATE + ",4,2)||'-'||substr(" + Task.COLUMN_TASK_DUE_DATE + ",1,2)";

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Task.TABLE_NAME + " WHERE " + Task.COLUMN_TASK_status + "=0" + " ORDER BY date(" +
                test + ")<date('now')  ASC," + Task.COLUMN_TIMESTAMP + "  DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(Task.COLUMN_ID)));
                task.setTaskTitle(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_TITLE)));
                task.setTaskDetails(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_DETAILS)));
                task.setTaskDueDate(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_DUE_DATE)));
                Log.e("dueDates", cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_DUE_DATE)));
                task.setTaskPriority(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_PRIORITY)));
                task.setTaskCompletionStatus(cursor.getInt(cursor.getColumnIndex(Task.COLUMN_TASK_status)));
                task.setTaskTimeStamp(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TIMESTAMP)));

                tasks.add(task);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return tasks list
        return tasks;
    }

    public List<Task> getAllOverDueTasks() {


        List<Task> tasks = new ArrayList<>();


        String test = "substr(" + Task.COLUMN_TASK_DUE_DATE + ",7,4)||'-'||substr(" + Task.COLUMN_TASK_DUE_DATE + ",4,2)||'-'||substr(" + Task.COLUMN_TASK_DUE_DATE + ",1,2) < date('now')";
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Task.TABLE_NAME + " WHERE " + Task.COLUMN_TASK_status + "=0" + " AND " + test + " ORDER BY " +
                Task.COLUMN_TASK_DUE_DATE + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(Task.COLUMN_ID)));
                task.setTaskTitle(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_TITLE)));
                task.setTaskDetails(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_DETAILS)));
                task.setTaskDueDate(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_DUE_DATE)));
                Log.e("dueDate", cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_DUE_DATE)));
                task.setTaskPriority(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_PRIORITY)));
                task.setTaskCompletionStatus(cursor.getInt(cursor.getColumnIndex(Task.COLUMN_TASK_status)));
                task.setTaskTimeStamp(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TIMESTAMP)));

                tasks.add(task);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return tasks list
        return tasks;
    }

    public List<Task> getAllCompletedTasks() {
        List<Task> tasks = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Task.TABLE_NAME + " WHERE " + Task.COLUMN_TASK_status + "=1" + " ORDER BY " +
                Task.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(Task.COLUMN_ID)));
                task.setTaskTitle(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_TITLE)));
                task.setTaskDetails(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_DETAILS)));
                task.setTaskDueDate(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_DUE_DATE)));
                task.setTaskPriority(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK_PRIORITY)));
                task.setTaskCompletionStatus(cursor.getInt(cursor.getColumnIndex(Task.COLUMN_TASK_status)));
                task.setTaskTimeStamp(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TIMESTAMP)));

                tasks.add(task);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return tasks list
        return tasks;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Task.COLUMN_TASK_TITLE, task.getTaskTitle());
        values.put(Task.COLUMN_TASK_DETAILS, task.getTaskDetails());
        values.put(Task.COLUMN_TASK_DUE_DATE, task.getTaskDueDate());
        values.put(Task.COLUMN_TASK_PRIORITY, task.getTaskPriority());
        values.put(Task.COLUMN_TASK_status, task.getTaskCompletionStatus());
        // updating row
        return db.update(Task.TABLE_NAME, values, Task.COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Task.TABLE_NAME, Task.COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
    }
}
