package com.ast.roomdemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ast.roomdemo.db.AsterixSolnDatabase;
import com.ast.roomdemo.db.DbBuilderSingleton;
import com.ast.roomdemo.db.dao.StudentDao;
import com.ast.roomdemo.db.entity.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StudentAdapter.OnStudentClickListener {

    private final String TAG = "MainActivity";

    RecyclerView rvStudent;
    ProgressBar pbStudent;
    TextView tvNoDataFound;
    FloatingActionButton fabAddStudent;

    List<Student> alStudent = new ArrayList<>();
    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        populateUI();
        addListeners();
    }


    private void initUI() {
        rvStudent = findViewById(R.id.rvStudent);
        pbStudent = findViewById(R.id.progressBar);
        tvNoDataFound = findViewById(R.id.tvNoDataFound);
        fabAddStudent = findViewById(R.id.fabAddStudent);
    }

    private void populateUI() {
        rvStudent.setLayoutManager(new LinearLayoutManager(this));
        rvStudent.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        studentAdapter = new StudentAdapter(this, alStudent, this);

        rvStudent.setAdapter(studentAdapter);
        getSupportActionBar().setTitle("Students List");
        new GetAllStudentAsync().execute();
    }

    private void updateStudentUI(List<Student> alDbStudent) {
        alStudent.clear();
        alStudent.addAll(alDbStudent);
        studentAdapter.notifyDataSetChanged();

        if (alStudent.isEmpty())
            tvNoDataFound.setVisibility(View.VISIBLE);
        else
            tvNoDataFound.setVisibility(View.GONE);
    }

    private List<Student> getData() {
        DbBuilderSingleton dbs = DbBuilderSingleton.getInstance(getApplicationContext());
        AsterixSolnDatabase db = dbs.getDb();
        StudentDao studentDao = db.getStudentDao();
        return studentDao.getAll();
    }

    private void addListeners() {
        fabAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate to Next screen to add student
                Intent intent = new Intent(getApplicationContext(), AddStudentActivity.class);
                startActivityForResult(intent, 1001);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e(TAG, "requestCode : " + requestCode);
        switch (requestCode) {
            case 1001:
                if (resultCode == RESULT_OK) {
                    Log.e(TAG, "New Student Added Request");
                    new GetAllStudentAsync().execute();
                }
                break;
            case 1002:
                if (resultCode == RESULT_OK) {
                    Log.e(TAG, "Student Updated or Deleted Request");
                    new GetAllStudentAsync().execute();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStudentClick(int position) {
        Student student = alStudent.get(position);
        Intent intent = UpdateStudentActivity.getIntention(this, student);
        startActivityForResult(intent, 1002);
    }

    private class GetAllStudentAsync extends AsyncTask<Void, Void, Boolean> {
        List<Student> alDbStudent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbStudent.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            alDbStudent = getData();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            pbStudent.setVisibility(View.GONE);
            if (flag != null && flag == true) {
                updateStudentUI(alDbStudent);
            } else {
                toast("Something went wrong");
            }
        }
    }

}
