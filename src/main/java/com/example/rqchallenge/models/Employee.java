package com.example.rqchallenge.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Employee{
    public static final String TYPE = "employee";
    @SerializedName("id")
    private String id;
    @SerializedName("employee_name")
    private String name;
    @SerializedName("employee_salary")
    private String salary;
    @SerializedName("employee_age")
    private String age;
    @SerializedName("profile_image")
    private String imageUrl;

}
