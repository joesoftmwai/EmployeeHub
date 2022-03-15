package com.joesoft.employeeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.joesoft.employeeapp.models.Employee;
import com.joesoft.employeeapp.services.EmpService;
import com.joesoft.employeeapp.services.ServiceBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    private TextView mTvName;
    private TextView mTvEmail;
    private Button mBtnReview;
    private Employee mEmployee;
    private TextInputEditText mTextInputPerformance, mTextInputNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mTvName = findViewById(R.id.tv_name);
        mTvEmail = findViewById(R.id.tv_email);
        mBtnReview = findViewById(R.id.btn_review);

        mTextInputPerformance = findViewById(R.id.textInputPerformance);
        mTextInputNotes = findViewById(R.id.textInputNotes);

        getSelectedEmp();


        mBtnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewEmployee();
            }
        });

    }

    private void getSelectedEmp() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        EmpService empService = ServiceBuilder.buildService(EmpService.class);
        final Call<Employee> request = empService.getEmployee(name);
        request.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                mEmployee = response.body();
                mTvName.setText(name);
                mTvEmail.setText(mEmployee.getEmail());
                // Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reviewEmployee() {
        if (!mTextInputPerformance.getText().toString().isEmpty() && !mTextInputNotes.getText().toString().isEmpty()) {
            Toast.makeText(ReviewActivity.this,
                    "Employee " + mTvName.getText() + ": email " + mTvEmail.getText() + " Reviewed successfully" + "\n" +
                            "Performance: "+ mTextInputPerformance.getText() + "\n" +
                            "Notes: "+ mTextInputNotes.getText(),
                    Toast.LENGTH_LONG).show();

            startActivity(new Intent(ReviewActivity.this, MainActivity.class));

        } else {
            Toast.makeText(ReviewActivity.this,
                    "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

}