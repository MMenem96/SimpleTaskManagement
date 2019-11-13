package task.com.simpletaskmanagementapplication.activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import task.com.simpletaskmanagementapplication.R;
import task.com.simpletaskmanagementapplication.alarmservice.NotificationPublisher;
import task.com.simpletaskmanagementapplication.fragments.CompletedTasksFragment;
import task.com.simpletaskmanagementapplication.fragments.HomeFragment;
import task.com.simpletaskmanagementapplication.fragments.OverdueTasksFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FloatingActionButton fBAddTask;
    private BottomNavigationView toDoBottomNavigationView;
    private FragmentManager fragmentManager;
    private CompletedTasksFragment completedTasksFragment;
    private HomeFragment homeFragment;
    private OverdueTasksFragment overdueTasksFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Calling method to initialize all the views
        initViews();
    }

    private void initViews() {

        toDoBottomNavigationView = findViewById(R.id.todo_navigation);
        toDoBottomNavigationView.setOnNavigationItemSelectedListener(this);

        completedTasksFragment = new CompletedTasksFragment();
        homeFragment = new HomeFragment();
        overdueTasksFragment = new OverdueTasksFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frame_container, completedTasksFragment, "1").hide(completedTasksFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame_container, overdueTasksFragment, "3").hide(overdueTasksFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame_container, homeFragment, "2").commit();

        toDoBottomNavigationView.setSelectedItemId(R.id.menu_home);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_completed_tasks:
                fragmentManager.beginTransaction().hide(homeFragment).hide(overdueTasksFragment).show(completedTasksFragment).commit();
                this.setTitle(getString(R.string.completed_tasks_title_text));
                completedTasksFragment.refereshFragment();
                return true;
            case R.id.menu_home:
                fragmentManager.beginTransaction().hide(completedTasksFragment).hide(overdueTasksFragment).show(homeFragment).commit();
                this.setTitle(getString(R.string.app_name));
                return true;
            case R.id.menu_overdue_tasks:
                fragmentManager.beginTransaction().hide(completedTasksFragment).hide(homeFragment).show(overdueTasksFragment).commit();
                this.setTitle(getString(R.string.overdue_tasks_title_text));
                overdueTasksFragment.refereshFragment();
                return true;
        }
        return false;
    }


}
