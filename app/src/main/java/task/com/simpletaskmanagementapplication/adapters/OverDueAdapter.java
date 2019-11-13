package task.com.simpletaskmanagementapplication.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import task.com.simpletaskmanagementapplication.R;
import task.com.simpletaskmanagementapplication.dbutils.DbHelper;
import task.com.simpletaskmanagementapplication.dbutils.Task;
import task.com.simpletaskmanagementapplication.fragments.OverdueTasksFragment;

public class OverDueAdapter extends RecyclerView.Adapter<OverDueAdapter.MyViewHolder> {

    private final OverdueTasksFragment overdueTasksFragment;
    private Context context;
    private List<Task> taskList;
    private DbHelper dbHelper;

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


    public OverDueAdapter(Context context, List<Task> taskList, OverdueTasksFragment overdueTasksFragment, DbHelper dbHelper) {
        this.context = context;
        this.taskList = taskList;
        this.overdueTasksFragment = overdueTasksFragment;
        this.dbHelper = dbHelper;
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
        Log.e("Days", getRemainingDays(task.getTaskDueDate()).trim());
        Log.e("hours", getRemainingHours(task.getTaskTimeStamp()).trim());
        holder.taskTitle.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_overdue_task), null, null, null);

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
        holder.taskRemainingDays.setText(getRemainingDays(task.getTaskDueDate()).trim() + " days" + " - " + getRemainingHours(task.getTaskTimeStamp()).trim() + " hours");
        holder.taskTitle.setText(task.getTaskTitle());
        holder.taskTitle.setTextColor(Color.RED);
        holder.taskDueDate.setTextColor(Color.RED);
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
                        setTaskList(dbHelper.getAllOverDueTasks());
                }
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

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private String getRemainingDays(String dueDate) {
        Calendar calCurr = Calendar.getInstance();
        Calendar day = Calendar.getInstance();
        try {
            day.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(dueDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (day.after(calCurr)) {
            return (day.get(Calendar.DAY_OF_MONTH) - (calCurr.get(Calendar.DAY_OF_MONTH))) + "";
        } else {
            return "0";
        }

    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
        if (getItemCount() == 0) {
            overdueTasksFragment.showNoOverdueTasks(true);
        } else {
            overdueTasksFragment.showNoOverdueTasks(false);
        }
    }


}