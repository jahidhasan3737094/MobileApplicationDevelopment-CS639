package com.example.conversionjahidhasanv2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText dollarInput, euroResult;
    Button convertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dollarInput = findViewById(R.id.dollar_input);
        euroResult = findViewById(R.id.euro_result);
        convertButton = findViewById(R.id.convert_button);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double dollarValue = Double.parseDouble(dollarInput.getText().toString());
                double euroValue = dollarValue * 0.88;
                euroResult.setText(String.format("%.2f", euroValue));
            }
        });
    }
}