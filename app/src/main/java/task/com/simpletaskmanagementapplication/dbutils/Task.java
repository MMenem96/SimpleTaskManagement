package task.com.simpletaskmanagementapplication.dbutils;

import java.io.Serializable;

public class Task implements Serializable {

    public static final String TABLE_NAME = "tasks";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TASK_TITLE = "title";
    public static final String COLUMN_TASK_DETAILS = "details";
    public static final String COLUMN_TASK_DUE_DATE = "date";
    public static final String COLUMN_TASK_PRIORITY = "priority";
    public static final String COLUMN_TASK_status = "status";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String taskTitle;
    private String taskDetails;
    private String taskDueDate;
    private String taskPriority;
    private int taskCompletionStatus;
    private String taskTimeStamp;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TASK_TITLE + " TEXT,"
                    + COLUMN_TASK_DETAILS + " TEXT,"
                    + COLUMN_TASK_DUE_DATE + " TEXT,"
                    + COLUMN_TASK_PRIORITY + " TEXT,"
                    + COLUMN_TASK_status + " INTEGER,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";


    public Task() {

    }


    public Task(int id, String taskTitle, String taskDetails, String taskDueDate, String taskPriority, int taskCompletionStatus, String taskTimeStamp) {
        this.id = id;
        this.taskTitle = taskTitle;
        this.taskDetails = taskDetails;
        this.taskDueDate = taskDueDate;
        this.taskPriority = taskPriority;
        this.taskCompletionStatus = taskCompletionStatus;
        this.taskTimeStamp = taskTimeStamp;
    }

    public Task(String taskTitle, String taskDetails, String taskDueDate, String taskPriority, int taskCompletionStatus) {
        this.taskTitle = taskTitle;
        this.taskDetails = taskDetails;
        this.taskDueDate = taskDueDate;
        this.taskPriority = taskPriority;
        this.taskCompletionStatus = taskCompletionStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }

    public String getTaskDueDate() {
        return taskDueDate;
    }

    public void setTaskDueDate(String taskDueDate) {
        this.taskDueDate = taskDueDate;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public int getTaskCompletionStatus() {
        return taskCompletionStatus;
    }

    public void setTaskCompletionStatus(int taskCompletionStatus) {
        this.taskCompletionStatus = taskCompletionStatus;
    }

    public String getTaskTimeStamp() {
        return taskTimeStamp;
    }

    public void setTaskTimeStamp(String taskTimeStamp) {
        this.taskTimeStamp = taskTimeStamp;
    }
}
