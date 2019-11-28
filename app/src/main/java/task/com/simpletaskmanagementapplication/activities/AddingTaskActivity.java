package task.com.simpletaskmanagementapplication.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.google.android.material.textfield.TextInputEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import task.com.simpletaskmanagementapplication.R;
import task.com.simpletaskmanagementapplication.dbutils.DbHelper;
import task.com.simpletaskmanagementapplication.dbutils.Task;

public class AddingTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextInputEditText etTaskTitle, etTaskDetails;
    private TextView tvTaskDate;
    private AppCompatCheckBox cbPriority;
    private String priority = "Normal";
    private DatePickerDialog dpd;
    private DbHelper dbHelper;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_task);
        initViews();
    }

    private void initViews() {

        dbHelper = new DbHelper(this);

        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                AddingTaskActivity.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Initial day selection
        );
        etTaskTitle = findViewById(R.id.et_task_title);
        tvTaskDate = findViewById(R.id.tv_task_date);
        tvTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");
            }
        });
        etTaskDetails = findViewById(R.id.et_task_details);
        cbPriority = findViewById(R.id.cb_priority);
        cbPriority.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    priority = "High";
                } else {
                    priority = "Normal";
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_add_task) {
            if (dataIsValid()) {
                Task newTask = new Task(etTaskTitle.getText().toString().trim(), etTaskDetails.getText().toString(), currentDate, priority, 0);
                long rowInserted = dbHelper.insertTask(newTask);
                if (rowInserted != -1) {
                    Toast.makeText(this, "New Task is added", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show();

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean dataIsValid() {
        if (TextUtils.isEmpty(etTaskTitle.getText().toString().trim())) {
            etTaskTitle.setError(getString(R.string.empty_field));
            return false;
        }
        if (TextUtils.isEmpty(tvTaskDate.getText().toString().trim())) {
            Toast.makeText(this, "Invalid date!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etTaskDetails.getText().toString().trim())) {
            etTaskDetails.setText("");
        }
        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        tvTaskDate.setText("");
        String day = dayOfMonth + "";
        if (day.trim().length() == 1) {
            day = "0" + day;
        }
        String month = (monthOfYear + 1) + "";
        if (month.trim().length() == 1) {
            month = "0" + month;
        }

        tvTaskDate.setText(day.trim() + "/" + month.trim() + "/" + year);
        currentDate = (day.trim() + "/" + month.trim() + "/" + year);
        Log.e("jobDate", currentDate);
    }
}
