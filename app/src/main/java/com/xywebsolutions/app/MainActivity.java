package com.xywebsolutions.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xywebsolutions.app.util.ApiHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    TextView tvCompanyName, tvContact, tvPhone, tvWebsite;
    LinearLayout llPhoneSupport,llVisitWeb;
    EditText input_full_name, input_company_main, input_phone, input_email, input_comment;
    Button btnSubmit;

    String name, company, phone, address, content;

    ProgressDialog progressDialog;
    String resSubmit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
        tvContact = (TextView) findViewById(R.id.tvContact);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvWebsite = (TextView) findViewById(R.id.tvWebsite);

        llPhoneSupport = (LinearLayout) findViewById(R.id.llPhoneSupport);
        llVisitWeb = (LinearLayout) findViewById(R.id.llVisitWeb);

        input_full_name = (EditText) findViewById(R.id.input_full_name);
        input_company_main = (EditText) findViewById(R.id.input_company_main);
        input_phone = (EditText) findViewById(R.id.input_phone);
        input_email = (EditText) findViewById(R.id.input_email);
        input_comment = (EditText) findViewById(R.id.input_comment);

        input_comment.setHorizontallyScrolling(false);
        input_comment.setMinLines(5);
        input_comment.setMaxLines(Integer.MAX_VALUE);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvCompanyName.setLetterSpacing((float) 0.05);
            tvContact.setLetterSpacing((float) 0.2);
            tvPhone.setLetterSpacing((float) 0.15);
            tvWebsite.setLetterSpacing((float) 0.15);
            btnSubmit.setLetterSpacing((float) 0.2);
        }

        UsPhoneNumberFormatter addLineNumberFormatter = new UsPhoneNumberFormatter(
                new WeakReference<EditText>(input_phone));
        input_phone.addTextChangedListener(addLineNumberFormatter);

        setValue();

        llPhoneSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcCallPhone();
            }
        });

        llVisitWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.xywebsolutions.com/";
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = input_full_name.getText().toString().trim();
                String companyName = input_company_main.getText().toString().trim();
                String phone = input_phone.getText().toString().trim();
                String email = input_email.getText().toString().trim();
                String content = input_comment.getText().toString().trim();

                if (fullName.equals("")) {
                    input_full_name.setError("Please enter name");
                    input_full_name.requestFocus();
                } else if (companyName.equals("")) {
                    input_company_main.setError("Please enter company name");
                    input_company_main.requestFocus();
                } else if (phone.equals("")) {
                    input_phone.setError("Please enter phone number");
                    input_phone.requestFocus();
                } else if (phone.length() < 14) {
                    input_phone.setError("Phone number is invalid");
                    input_phone.requestFocus();
                } else if (email.equals("")) {
                    input_email.setError("Please enter email");
                    input_email.requestFocus();
                } else if(!email.equals("") && !isValidEmail(email)) {
                    input_email.setError("Email is invalid");
                    input_email.requestFocus();
                } else if (content.equals("")) {
                    input_comment.setError("How can we help?");
                    input_comment.requestFocus();
                } else {
                    funcSubmit(fullName, companyName, email, phone, content);
                    input_phone.setText("");
                    input_email.setText("");
                    input_comment.setText("");
                    keyboardHide(input_comment);
                }

            }
        });

    }

    public void funcCallPhone() {
        try {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
            phoneIntent.setData(Uri.parse("tel:" + "+1(973)968-5187"));
            startActivity(phoneIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "Call failed, please try again later!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setValue() {
        name = SignInActivity.name;
        company = SignInActivity.company;

        input_full_name.setText(name);
        input_company_main.setText(company);
        input_phone.requestFocus();
    }

    public void funcSubmit(final String name, final String company_name, final String email, final String phone, final String content){
        new AsyncTask(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setCancelable(true);
                progressDialog.setMessage("Please waiting..");
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.show();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    ApiHandler apiHandler = new ApiHandler();
                    resSubmit = apiHandler.submit(name, company_name, email, phone, content);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;

            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                progressDialog.dismiss();

                try {
                    JSONObject rootObject = new JSONObject(resSubmit);
                    if (!rootObject.getBoolean("result")) {
                        Toast.makeText(MainActivity.this, rootObject.getString("message"), Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Toast.makeText(MainActivity.this, rootObject.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(MainActivity.this, "Server error", Toast.LENGTH_LONG).show();
                    Log.e("e", "e", e);
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    public static void keyboardHide(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Check email is valid
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Format Number phone
    public static class UsPhoneNumberFormatter implements TextWatcher {
        //This TextWatcher sub-class formats entered numbers as 1 (123) 456-7890
        private boolean mFormatting; // this is a flag which prevents the
        // stack(onTextChanged)
        private boolean clearFlag, checkNum1 = false;
        private int mLastStartLocation;
        private String mLastBeforeText;
        private WeakReference<EditText> mWeakEditText;

        public UsPhoneNumberFormatter(WeakReference<EditText> weakEditText) {
            this.mWeakEditText = weakEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            if (after == 0 && s.toString().equals("1 ")) {
                clearFlag = true;
            }
            mLastStartLocation = start;
            mLastBeforeText = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO: Do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Make sure to ignore calls to afterTextChanged caused by the work
            // done below
            if (!mFormatting) {
                mFormatting = true;
                int curPos = mLastStartLocation;
                String beforeValue = mLastBeforeText;
                String currentValue = s.toString();
                String formattedValue = formatUsNumber(s);
                if (currentValue.length() > beforeValue.length()) {
                    int setCusorPos = formattedValue.length()
                            - (beforeValue.length() - curPos);
                    mWeakEditText.get().setSelection(setCusorPos < 0 ? 0 : setCusorPos);
                    if (checkNum1) {
                        mWeakEditText.get().setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                        Log.e("check", "1");
                    } else {
                        mWeakEditText.get().setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
                        Log.e("check", "2");
                    }
                } else {
                    int setCusorPos = formattedValue.length()
                            - (currentValue.length() - curPos);
                    if (setCusorPos > 0 && !Character.isDigit(formattedValue.charAt(setCusorPos - 1))) {
                        setCusorPos--;
                    }
                    mWeakEditText.get().setSelection(setCusorPos < 0 ? 0 : setCusorPos);
                }
                mFormatting = false;
            }
        }

        private String formatUsNumber(Editable text) {
            StringBuilder formattedString = new StringBuilder();
            // Remove everything except digits
            int p = 0;
            while (p < text.length()) {
                char ch = text.charAt(p);
                if (!Character.isDigit(ch)) {
                    text.delete(p, p + 1);
                } else {
                    p++;
                }
            }
            // Now only digits are remaining
            String allDigitString = text.toString();

            int totalDigitCount = allDigitString.length();

            if (totalDigitCount == 0
                    || (totalDigitCount > 10 && !allDigitString.startsWith("1"))
                    || totalDigitCount > 11) {
                // May be the total length of input length is greater than the
                // expected value so we'll remove all formatting
                text.clear();
                text.append(allDigitString);
                return allDigitString;
            }
            int alreadyPlacedDigitCount = 0;
            // Only '1' is remaining and user pressed backspace and so we clear
            // the edit text.
//            if (allDigitString.equals("1") && clearFlag) {
//                text.clear();
//                clearFlag = false;
//                checkNum1 = false;
//                return "";
//            }
//            if (allDigitString.startsWith("1")) {
//                formattedString.append("1 ");
//                alreadyPlacedDigitCount++;
//                checkNum1 = true;
//            }
            // The first 3 numbers beyond '1' must be enclosed in brackets "()"
            if (totalDigitCount - alreadyPlacedDigitCount > 3) {
                formattedString.append("("
                        + allDigitString.substring(alreadyPlacedDigitCount,
                        alreadyPlacedDigitCount + 3) + ") ");
                alreadyPlacedDigitCount += 3;
            }
            // There must be a '-' inserted after the next 3 numbers
            if (totalDigitCount - alreadyPlacedDigitCount > 3) {
                formattedString.append(allDigitString.substring(
                        alreadyPlacedDigitCount, alreadyPlacedDigitCount + 3)
                        + "-");
                alreadyPlacedDigitCount += 3;
            }
            // All the required formatting is done so we'll just copy the
            // remaining digits.
            if (totalDigitCount > alreadyPlacedDigitCount) {
                formattedString.append(allDigitString
                        .substring(alreadyPlacedDigitCount));
            }

            text.clear();
            text.append(formattedString.toString());
            return formattedString.toString();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
