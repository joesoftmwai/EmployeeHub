package com.joesoft.employeeapp.services;

import com.joesoft.employeeapp.models.Employee;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EmpService {
    @GET("employees")
    Call<List<Employee>> getEmployees();

    @GET("employees/{name}")
    Call<Employee> getEmployee(@Path("name")String name);


//    @GET("Music")
//    Call<List<Music>> getMusicList();
}


