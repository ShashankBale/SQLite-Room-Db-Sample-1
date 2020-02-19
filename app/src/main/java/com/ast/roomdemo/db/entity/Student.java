package com.ast.roomdemo.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Student implements Serializable {
    //primary
    @PrimaryKey(autoGenerate = true)
    private int id;


    /** Personal Details */
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "mobile_number")
    private long mobileNumber;


    /** Course Detail & Status */
    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "course")
    private String course;

    @ColumnInfo(name = "course_completed")
    private boolean courseCompleted ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public boolean isCourseCompleted() {
        return courseCompleted;
    }

    public void setCourseCompleted(boolean courseCompleted) {
        this.courseCompleted = courseCompleted;
    }
}
