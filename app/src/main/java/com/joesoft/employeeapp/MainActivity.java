package com.joesoft.employeeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.joesoft.employeeapp.models.Employee;
import com.joesoft.employeeapp.services.EmpService;
import com.joesoft.employeeapp.services.ServiceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private SmartMaterialSpinner<String> mEmpSpinner;
    private ProgressBar mProgressBar;
    private List<String> provinceList;

    private Employee mSelectedEmployee;
    private List<Employee> mEmployees = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mEmpSpinner = findViewById(R.id.emp_spinner);
        mProgressBar = findViewById(R.id.emp_progress_bar);

        settingEmpAdapter();
    }

    private void settingEmpAdapter() {

        EmpService empService = ServiceBuilder.buildService(EmpService.class);
        Call<List<Employee>> empRequest = empService.getEmployees();

        mProgressBar.setVisibility(View.VISIBLE);
        empRequest.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                Log.d("MAin Activity", "onResponse: " + response.message());

                if (response.isSuccessful()) {

                    mEmployees = response.body();

                    List<String> employees = new ArrayList<>();

                    for (int i=0; i<mEmployees.size(); i++) {
                        employees.add(mEmployees.get(i).getName());
                    }

                    mEmpSpinner.setItem(employees);
                    mEmpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                            // Create the Intent object of this class Context() to Second_activity class
                            Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);

                            intent.putExtra("name", employees.get(position));

                            // start the Intent
                            startActivity(intent);
                            // startActivity(new Intent(MainActivity.this, ReviewActivity.class));
                            Toast.makeText(MainActivity.this, employees.get(position), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    // Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                } else if(response.code() == 401) { // 401 - user is unauthorised - redirect to login page
                    Toast.makeText(MainActivity.this, "Your session has expired", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to retrieve items", Toast.LENGTH_SHORT).show();
                }

                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Log.d("MAin Activity", "onFailure: " + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(MainActivity.this, "A connection error occurred", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to retrieve items", Toast.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });


    }
}