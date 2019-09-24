package com.asterixsolution.sqliteroomdbdemo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asterixsolution.sqliteroomdbdemo.db.entity.Student;

import java.util.List;

class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    List<Student> alStudent;
    private Activity activity;
    private OnStudentClickListener mOnStudentClickListener;

    public StudentAdapter(Activity activity, List<Student> alStudent, OnStudentClickListener mOnStudentClickListener) {
        this.activity = activity;
        this.alStudent = alStudent;
        this.mOnStudentClickListener = mOnStudentClickListener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(itemView, mOnStudentClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = alStudent.get(position);

        holder.tvIdValue.setText("" + student.getId());
        holder.tvNameValue.setText(student.getName());
        holder.tvMobileNoValue.setText("" + student.getMobileNumber());
        holder.tvCourseValue.setText(student.getCourse());
        holder.tvCourseCompleteValue.setText(student.isCourseCompleted() ? "Yes" : "No");
        holder.tvYearValue.setText("" + student.getYear());

        //holder.tvStudentDetails.setTag(student);
        /*holder.tvStudentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Student selectedStudent = (Student) view.getTag();
                Intent intent = UpdateStudentActivity.getIntention(activity, selectedStudent);
                activity.startActivityForResult(intent, 1002);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return alStudent.size();
    }

    public interface OnStudentClickListener {
        void onStudentClick(int position);
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final View rvParent;
        private final TextView tvIdValue, tvNameValue, tvMobileNoValue, tvCourseValue, tvCourseCompleteValue, tvYearValue;
        private OnStudentClickListener onStudentClickListener;

        public StudentViewHolder(@NonNull View itemView, OnStudentClickListener onStudentClickListener) {
            super(itemView);
            tvIdValue = itemView.findViewById(R.id.tvIdValue);
            tvNameValue = itemView.findViewById(R.id.tvNameValue);
            tvMobileNoValue = itemView.findViewById(R.id.tvMobileNoValue);
            tvCourseValue = itemView.findViewById(R.id.tvCourseValue);
            tvCourseCompleteValue = itemView.findViewById(R.id.tvCourseCompleteValue);
            tvYearValue = itemView.findViewById(R.id.tvYearValue);
            rvParent = itemView.findViewById(R.id.rvParent);
            this.onStudentClickListener = onStudentClickListener;
            rvParent.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onStudentClickListener.onStudentClick(getAdapterPosition());
        }
    }
}
