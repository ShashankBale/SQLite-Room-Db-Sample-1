package com.asterixsolution.sqliteroomdbdemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.asterixsolution.sqliteroomdbdemo.db.DbClient;
import com.asterixsolution.sqliteroomdbdemo.db.entity.Student;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddStudentActivity extends AppCompatActivity {

    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.etName)
    TextInputEditText etName;
    @BindView(R.id.tilMobileNo)
    TextInputLayout tilMobileNo;
    @BindView(R.id.etMobileNo)
    TextInputEditText etMobileNo;
    @BindView(R.id.tilYear)
    TextInputLayout tilYear;
    @BindView(R.id.etYear)
    TextInputEditText etYear;
    @BindView(R.id.spinnerCourse)
    Spinner spinnerCourse;
    @BindView(R.id.rgCourseCompleted)
    RadioGroup rgCourseCompleted;
    @BindView(R.id.rbCourseCompletedYes)
    RadioButton rbCourseCompletedYes;
    @BindView(R.id.rbCourseCompletedNo)
    RadioButton rbCourseCompletedNo;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        ButterKnife.bind(this);

        initObject();
    }

    private void initObject() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button on ActionBar
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @OnClick(R.id.btnSubmit)
    public void onClickAddStudent(View v) {
        String strName = "" + etName.getText();
        String strMobileNo = "" + etMobileNo.getText();
        String strYear = "" + etYear.getText();
        String strCourse = "" + spinnerCourse.getSelectedItem();
        boolean isCourseCompleted = rbCourseCompletedYes.isChecked();
        if (isValidInput(strName,
                strMobileNo,
                strYear,
                strCourse)) {
            long lMobileNo = Long.parseLong(strMobileNo);
            int iYear = Integer.parseInt(strYear);

            Student student = new Student();
            //student.setId();
            student.setName(strName);
            student.setMobileNumber(lMobileNo);
            student.setYear(iYear);
            student.setCourse(strCourse);
            student.setCourseCompleted(isCourseCompleted);

            new AddStudentAsync().execute(student);
        }
    }

    private boolean isValidInput(String strName, String strMobileNo, String strYear, String strCourse) {
        boolean isValid = true;

        tilName.setErrorEnabled(false);
        tilMobileNo.setErrorEnabled(false);
        tilYear.setErrorEnabled(false);

        /** Check Validation for Name */
        if (TextUtils.isEmpty(strName.trim())) {
            tilName.setError("Name cannot be empty");
            isValid = false;
        }

        /** Check Validation for Mobile No. */
        if (TextUtils.isEmpty(strMobileNo.trim())) {
            tilMobileNo.setError("Mobile Number cannot be empty");
            isValid = false;
        } else if (strMobileNo.trim().length() != 10) {
            tilMobileNo.setError("Enter valid Mobile Number");
            isValid = false;
        } else {
            try {
                Long.parseLong(strMobileNo);
            } catch (NumberFormatException e) {
                tilMobileNo.setError("Mobile Number should be numeric");
                isValid = false;
            }
        }


        /** Check Validation for Mobile No. */
        if (TextUtils.isEmpty(strYear.trim())) {
            tilYear.setError("Year cannot be empty");
            isValid = false;
        } else if (strYear.trim().length() != 4) {
            tilYear.setError("Enter valid Year");
            isValid = false;
        } else {
            try {
                Integer.parseInt(strYear);
            } catch (NumberFormatException e) {
                tilYear.setError("Year should be numeric");
                isValid = false;
            }
        }


        /** Check Validation for Course */
        if (TextUtils.isEmpty(strCourse)) {
            tilYear.setError("Select Valid Course");
            isValid = false;
        }

        return isValid;
    }


    private void showProgress(boolean flag) {
        progressBar.setVisibility(flag ? View.VISIBLE : View.GONE);
        etName.setEnabled(!flag);
        etMobileNo.setEnabled(!flag);
        etYear.setEnabled(!flag);
        spinnerCourse.setEnabled(!flag);
        rgCourseCompleted.setEnabled(!flag);
        btnSubmit.setEnabled(!flag);
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Attention")
                .setMessage("Student Added Successfully")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                })
                .show();
    }

    private class AddStudentAsync extends AsyncTask<Student, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Student... students) {
            Student student = students[0];
            DbClient dbClient = DbClient.getInstance(getApplicationContext());
            dbClient.getDb().getStudentDao().insert(student);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showProgress(false);
            showSuccessDialog();
        }
    }
}
