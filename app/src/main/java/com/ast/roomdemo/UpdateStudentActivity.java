package com.ast.roomdemo;

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
import com.ast.roomdemo.db.DbBuilderSingleton;
import com.ast.roomdemo.db.entity.Student;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateStudentActivity extends AppCompatActivity {

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
    private Student oldStudent;

    public static Intent getIntention(Activity prevActivity, Student student) {
        Intent intent = new Intent(prevActivity, UpdateStudentActivity.class);
        intent.putExtra("oldStudent", student);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);
        ButterKnife.bind(this);
        initObjects();
        populateUI();
    }

    private void initObjects() {
        oldStudent = (Student) getIntent().getSerializableExtra("oldStudent");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button on ActionBar
    }

    private void populateUI() {
        etName.setText(oldStudent.getName());
        etMobileNo.setText("" + oldStudent.getMobileNumber());
        etYear.setText("" + oldStudent.getYear());

        String strSelectedCourse = oldStudent.getCourse();
        String[] stringArray = getResources().getStringArray(R.array.course_array);
        ArrayList alCourses = new ArrayList<>(Arrays.asList(stringArray));
        int selectedCourseIndex = alCourses.indexOf(strSelectedCourse);
        spinnerCourse.setSelection(selectedCourseIndex);

        if (oldStudent.isCourseCompleted()) {
            rbCourseCompletedYes.setChecked(true);
        } else {
            rbCourseCompletedNo.setChecked(true);
        }


    }

    @OnClick(R.id.btnSubmit)
    public void onClickAddStudent() {
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

            Student updatedStudent = new Student();
            updatedStudent.setId(oldStudent.getId());
            updatedStudent.setName(strName);
            updatedStudent.setMobileNumber(lMobileNo);
            updatedStudent.setYear(iYear);
            updatedStudent.setCourse(strCourse);
            updatedStudent.setCourseCompleted(isCourseCompleted);

            new UpdateStudentAsync().execute(updatedStudent);
        }
    }


    @OnClick(R.id.btnDelete)
    public void onClickDelete() {
        new DeleteStudentAsync().execute(oldStudent);
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

    private void showSuccessDialog(boolean isDelete) {
        new AlertDialog.Builder(this)
                .setTitle("Attention")
                .setMessage(isDelete ? "Student Delete Successfully" : "Student Updated Successfully")
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private class UpdateStudentAsync extends AsyncTask<Student, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Student... students) {
            Student student = students[0];
            DbBuilderSingleton dbs = DbBuilderSingleton.getInstance(getApplicationContext());
            dbs.getDb().getStudentDao().update(student);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showProgress(false);
            showSuccessDialog(false);
        }
    }

    private class DeleteStudentAsync extends AsyncTask<Student, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Student... students) {
            Student student = students[0];
            DbBuilderSingleton dbs = DbBuilderSingleton.getInstance(getApplicationContext());
            dbs.getDb().getStudentDao().delete(student);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showProgress(false);
            showSuccessDialog(true);
        }
    }
}

