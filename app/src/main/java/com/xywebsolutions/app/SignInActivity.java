package com.xywebsolutions.app;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xywebsolutions.app.util.SharedPreferencesHandler;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignInActivity extends AppCompatActivity {
    EditText input_name, input_company;
    Button btnLogin;

    public static String company, name;

    private SharedPreferencesHandler spHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        spHandler = new SharedPreferencesHandler(this);
        String getName = spHandler.getStringSavedPreferences("name");
        String getCompany = spHandler.getStringSavedPreferences("company");

        input_name = (EditText) findViewById(R.id.input_name);
        input_company = (EditText) findViewById(R.id.input_company);

        input_name.setText(getName);
        input_company.setText(getCompany);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnLogin.setLetterSpacing((float) 0.2);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = input_name.getText().toString().trim();
                company = input_company.getText().toString().trim();

                if (name.trim().equals("")) {
                    input_name.setError("Please enter your name!");
                    input_name.requestFocus();
                } else if (company.trim().equals("")) {
                    input_company.setError("Please enter company!");
                    input_company.requestFocus();
                } else {
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    saveData();
                }


            }
        });
    }

    private void saveData(){
        spHandler.setPreferences("name", name);
        spHandler.setPreferences("company", company);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
