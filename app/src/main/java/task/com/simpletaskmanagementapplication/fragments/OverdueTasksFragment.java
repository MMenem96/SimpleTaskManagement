package task.com.simpletaskmanagementapplication.fragments;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import task.com.simpletaskmanagementapplication.R;
import task.com.simpletaskmanagementapplication.adapters.OverDueAdapter;
import task.com.simpletaskmanagementapplication.alarmservice.NotificationPublisher;
import task.com.simpletaskmanagementapplication.dbutils.DbHelper;
import task.com.simpletaskmanagementapplication.dbutils.Task;


public class OverdueTasksFragment extends Fragment {


    private RecyclerView rvTasks;
    private DbHelper dbHelper;
    private List<Task> taskList = new ArrayList<>();
    private OverDueAdapter overDueAdapter;
    private TextView tvEmpty;

    public OverdueTasksFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overdue_tasks, container, false);
        rvTasks = view.findViewById(R.id.rv_tasks);

        dbHelper = new DbHelper(getContext());
        tvEmpty = view.findViewById(R.id.tv_empty);
        taskList.addAll(dbHelper.getAllOverDueTasks());
        overDueAdapter = new OverDueAdapter(getContext(), taskList, this, dbHelper);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvTasks.setLayoutManager(mLayoutManager);
        rvTasks.setAdapter(overDueAdapter);
        return view;
    }

    public void showNoOverdueTasks(boolean show) {
        if (show) {
            tvEmpty.setVisibility(View.VISIBLE);

        } else {
            scheduleNotification(getNotification("You have overdue tasks"), 5000);
            tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refereshFragment();
    }

    public void refereshFragment() {
        overDueAdapter.setTaskList(dbHelper.getAllOverDueTasks());
    }

    public void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(getContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public Notification getNotification(String content) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder builder = new Notification.Builder(getContext());
        builder.setContentTitle("Simple Tasks Management App");
        builder.setContentText(content);
        builder.setSound(alarmSound);
        builder.setSmallIcon(R.drawable.ic_calendar);
        return builder.build();
    }

}
