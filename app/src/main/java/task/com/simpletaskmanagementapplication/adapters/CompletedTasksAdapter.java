package task.com.simpletaskmanagementapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import task.com.simpletaskmanagementapplication.R;
import task.com.simpletaskmanagementapplication.dbutils.DbHelper;
import task.com.simpletaskmanagementapplication.dbutils.Task;
import task.com.simpletaskmanagementapplication.fragments.CompletedTasksFragment;

public class CompletedTasksAdapter extends RecyclerView.Adapter<CompletedTasksAdapter.MyViewHolder> {

    private final CompletedTasksFragment completedTasksFragment;
    private final DbHelper dbHelper;
    private Context context;
    private List<Task> taskList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskRemainingDays;
        CardView cardTask;

        public MyViewHolder(View view) {
            super(view);
            taskTitle = view.findViewById(R.id.tv_task_title);
            taskRemainingDays = view.findViewById(R.id.tv_task_remaining_days);
            cardTask = view.findViewById(R.id.card_task);
        }
    }


    public CompletedTasksAdapter(Context context, List<Task> taskList, CompletedTasksFragment completedTasksFragment, DbHelper dbHelper) {
        this.context = context;
        this.taskList = taskList;
        this.completedTasksFragment = completedTasksFragment;
        this.dbHelper = dbHelper;
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_task_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Task task = taskList.get(position);
        SimpleDateFormat format1 = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("MMMM");
        SimpleDateFormat format3 = new SimpleDateFormat("dd");
        SimpleDateFormat format4 = new SimpleDateFormat("yyyy");
        Date date = null;
        try {
            date = format1.parse(task.getTaskDueDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String monthName = format2.format(date);
        String day = format3.format(date);
        String year = format4.format(date);
        holder.taskRemainingDays.setText(day + "-" + monthName.substring(0, 3) + "-" + year);
        holder.taskTitle.setText(task.getTaskTitle());


    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
        if (getItemCount() == 0) {
            completedTasksFragment.showNoOverdueTasks(true);
        } else {
            completedTasksFragment.showNoOverdueTasks(false);
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