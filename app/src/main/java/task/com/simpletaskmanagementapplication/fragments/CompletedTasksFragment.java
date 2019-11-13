package task.com.simpletaskmanagementapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import task.com.simpletaskmanagementapplication.R;
import task.com.simpletaskmanagementapplication.adapters.CompletedTasksAdapter;
import task.com.simpletaskmanagementapplication.dbutils.DbHelper;
import task.com.simpletaskmanagementapplication.dbutils.Task;

public class CompletedTasksFragment extends Fragment {

    private RecyclerView rvTasks;
    private DbHelper dbHelper;
    private CompletedTasksAdapter completedTasksAdapter;
    private List<Task> completedTaskList = new ArrayList<>();
    private TextView tvEmpty;

    public CompletedTasksFragment() {
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
        View view = inflater.inflate(R.layout.fragment_completed_tasks, container, false);
        tvEmpty = view.findViewById(R.id.tv_empty);
        rvTasks = view.findViewById(R.id.rv_tasks);

        dbHelper = new DbHelper(getContext());
        completedTaskList.addAll(dbHelper.getAllCompletedTasks());
        completedTasksAdapter = new CompletedTasksAdapter(getContext(), completedTaskList,this,dbHelper);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvTasks.setLayoutManager(mLayoutManager);
        rvTasks.setAdapter(completedTasksAdapter);
        return view;
    }

    public void showNoOverdueTasks(boolean show) {
        if (show) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }


    public void refereshFragment() {
        completedTasksAdapter.setTaskList(dbHelper.getAllCompletedTasks());
    }

}
