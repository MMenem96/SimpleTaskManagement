package task.com.simpletaskmanagementapplication.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import task.com.simpletaskmanagementapplication.R;
import task.com.simpletaskmanagementapplication.activities.EditTaskActivity;
import task.com.simpletaskmanagementapplication.dbutils.DbHelper;
import task.com.simpletaskmanagementapplication.dbutils.Task;
import task.com.simpletaskmanagementapplication.fragments.HomeFragment;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {

    private final DbHelper dbHelper;
    private final HomeFragment homeFragment;
    private Context context;
    private List<Task> taskList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskRemainingDays, taskDueDate;
        Switch statusSwitch;
        CardView cardTask;

        public MyViewHolder(View view) {
            super(view);
            taskDueDate = view.findViewById(R.id.tv_task_date);
            taskTitle = view.findViewById(R.id.tv_task_title);
            taskRemainingDays = view.findViewById(R.id.tv_task_remaining_days);
            statusSwitch = view.findViewById(R.id.switch_completion_status);
            cardTask = view.findViewById(R.id.card_task);
        }
    }


    public TasksAdapter(Context context, List<Task> taskList, DbHelper dbHelper, HomeFragment homeFragment) {
        this.context = context;
        this.taskList = taskList;
        this.dbHelper = dbHelper;
        this.homeFragment = homeFragment;
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_task_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.statusSwitch.setChecked(false);
        SimpleDateFormat format1 = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("MMMM");
        SimpleDateFormat format3 = new SimpleDateFormat("dd");
        Date date = null;
        try {
            date = format1.parse(task.getTaskDueDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String monthName = format2.format(date);
        String day = format3.format(date);
        holder.taskDueDate.setText(day + "\n" + monthName.substring(0, 3));
        holder.taskRemainingDays.setText(getRemainingDays(task.getTaskDueDate()) + " days" + " - " + getRemainingHours(task.getTaskTimeStamp()).trim() + " hours");
        holder.taskTitle.setText(task.getTaskTitle());

        Date Currentdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());

        int currentDateNu = Integer.valueOf(sdf.format(Currentdate));
        if (currentDateNu > Integer.valueOf(task.getTaskDueDate())) {
            holder.taskTitle.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_overdue_task), null, null, null);
            holder.taskTitle.setTextColor(Color.RED);
            holder.taskDueDate.setTextColor(Color.RED);
        } else if (currentDateNu == Integer.valueOf(task.getTaskDueDate())) {
            holder.taskTitle.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_today_task), null, null, null);
            holder.taskTitle.setTextColor(Color.BLACK);
            holder.taskDueDate.setTextColor(Color.BLACK);
        } else {
            holder.taskTitle.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_task), null, null, null);
            holder.taskTitle.setTextColor(Color.BLACK);
            holder.taskDueDate.setTextColor(Color.BLACK);
        }

        if (task.getTaskPriority().equals("High")) {
            holder.cardTask.setBackgroundColor(Color.YELLOW);
        } else {
            holder.cardTask.setBackgroundColor(Color.WHITE);

        }

        holder.statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Task clickedTask = taskList.get(position);
                    clickedTask.setTaskCompletionStatus(1);
                    int updatingValue = dbHelper.updateTask(clickedTask);
                    if (updatingValue == 1)
                        setTaskList(dbHelper.getAllTasks());
                }
            }
        });

        holder.cardTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(context, EditTaskActivity.class);
                editIntent.putExtra("task", taskList.get(position));
                context.startActivity(editIntent);
            }
        });
        holder.cardTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                dbHelper.deleteTask(taskList.get(position));
                                Toast.makeText(context, "Task Deleted successfully", Toast.LENGTH_SHORT).show();
                                homeFragment.refereshFragment();
                                notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this task?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                return true;
            }
        });
    }

    private String getRemainingHours(String taskTimeStamp) {

        String hourseRemainig = "0";
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        try {

            Date oldDate = dateFormat.parse(taskTimeStamp);
            Date currentDate = new Date();
            long diff = oldDate.getTime() - currentDate.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            hourseRemainig = hours + "";
            if (hours >= 0) {
                hourseRemainig = hours + "";
            } else {
                hourseRemainig = Math.abs(hours) + "";

            }
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return hourseRemainig;
    }

    private String getRemainingDays(String dueDate) {
        Calendar calCurr = Calendar.getInstance();
        Calendar day = Calendar.getInstance();
        try {
            Date date = new SimpleDateFormat("ddMMyyyy").parse(dueDate);
            String dateText = new SimpleDateFormat("dd/MM/yyyy").format(date);
            day.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(dateText));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (day.after(calCurr)) {
            return (day.get(Calendar.DAY_OF_MONTH) - (calCurr.get(Calendar.DAY_OF_MONTH))) + "";
        } else {
            return "0";
        }

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
        if (getItemCount() == 0) {
            homeFragment.showNoOverdueTasks(true);
        } else {
            homeFragment.showNoOverdueTasks(false);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}