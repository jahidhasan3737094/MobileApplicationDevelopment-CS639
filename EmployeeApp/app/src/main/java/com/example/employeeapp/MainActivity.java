package com.example.employeeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseReference myRef;
    private static final String TAG = "MyActivity";
    private EditText input_firstName;
    private EditText input_lastName;
    private long rowCount = 0;
    private ListView employeeList;
    String[] employeeNames;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Hide keyboard if it's showing after creation
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_firstName = findViewById(R.id.enter_FirstName);
        input_lastName = findViewById(R.id.enter_LastName);
        employeeList = findViewById(R.id.employeeList);

        input_firstName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        input_lastName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        /// instance of the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("employees");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d(TAG, dataSnapshot.toString());

                rowCount = dataSnapshot.getChildrenCount();
                Log.d(TAG, "Count is: " + rowCount);
                List<Employee> employees = new ArrayList<>();

                for (DataSnapshot postSnapshot1: dataSnapshot.getChildren()) {
                    Employee emp = postSnapshot1.getValue(Employee.class);
                    employees.add(emp);
                }

                Collections.sort(employees);

                employeeNames = new String[(int) rowCount];
                int c = 0;
                for (Employee emp: employees) {
                    employeeNames[c] = emp.toString();
                    Log.d(TAG, emp.toString());
                    c = c + 1;
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.listview, R.id.textView, employeeNames);
                employeeList.setAdapter(arrayAdapter);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public void addEmployee(View view) {
        // Get the search string from the input field.
        String firstName = input_firstName.getText().toString();
        String lastName = input_lastName.getText().toString();

        if (firstName.isEmpty() || lastName.isEmpty()) {

            if (firstName.isEmpty()) {
                input_firstName.setHint("Enter first name");
            }

            if (lastName.isEmpty()) {
                input_lastName.setHint("Enter last name");
            }

            Toast toast = Toast.makeText(this, "Enter first and last names", Toast.LENGTH_SHORT);
            toast.show();

            return;
        }

        // Clear the previous inputs
        input_firstName.setText("");
        input_lastName.setText("");

        // Reset Hint values
        input_firstName.setHint("Enter first name");
        input_lastName.setHint("Enter last name");

        // Get a reference to where new entry will go - this avoids having to guess the offset
        DatabaseReference newPostRef  = myRef.push();
        Employee emp = new Employee(lastName, firstName);
        newPostRef.setValue(emp);

        // Return focus back to first name edit text
        input_firstName.requestFocus();

        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }
}