package task.com.simpletaskmanagementapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import task.com.simpletaskmanagementapplication.R;
import task.com.simpletaskmanagementapplication.activities.AddingTaskActivity;
import task.com.simpletaskmanagementapplication.adapters.TasksAdapter;
import task.com.simpletaskmanagementapplication.dbutils.DbHelper;
import task.com.simpletaskmanagementapplication.dbutils.Task;

public class HomeFragment extends Fragment {
    private FloatingActionButton fbAddTask;
    private RecyclerView rvTasks;
    private DbHelper dbHelper;
    private TasksAdapter tasksAdapter;
    private List<Task> taskList = new ArrayList<>();
    private TextView tvEmpty;

    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        fbAddTask = view.findViewById(R.id.fB_add_task);
        tvEmpty = view.findViewById(R.id.tv_empty);
        rvTasks = view.findViewById(R.id.rv_tasks);
        fbAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddingTaskActivity.class));
            }
        });

        dbHelper = new DbHelper(getContext());
        taskList.addAll(dbHelper.getAllTasks());
        tasksAdapter = new TasksAdapter(getContext(), taskList, dbHelper, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvTasks.setLayoutManager(mLayoutManager);
        rvTasks.setAdapter(tasksAdapter);
        return view;
    }

    public void showNoOverdueTasks(boolean show) {
        if (show) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refereshFragment();

    }

    public void refereshFragment() {
        tasksAdapter.setTaskList(dbHelper.getAllTasks());
    }

}
