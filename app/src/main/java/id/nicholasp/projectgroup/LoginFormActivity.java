package id.nicholasp.projectgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.HashMap;

import id.nicholasp.projectgroup.databinding.ActivityLoginFormBinding;

public class LoginFormActivity extends AppCompatActivity {

    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";

    // key for storing email.
    public static final String USER_KEY = "user_key";

    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    public String user, password, JSON_STRING;
//    EditText emailEdt,passwordEdt;
    ActivityLoginFormBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_form);
        //setContentView(R.layout.activity_main);

        // Initializing EditTexts and our Button
//        emailEdt = findViewById(R.id.idEdtEmail);
//        passwordEdt = findViewById(R.id.idEdtPassword);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        binding.idBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginFormActivity.this,SignUpActivity.class));
            }
        });

        // calling on click listener for login button.
        binding.idBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = binding.idEdtEmail.getText().toString();
                password = binding.idEdtPassword.getText().toString();
                //user = sharedpreferences.getString(EMAIL_KEY, binding.idEdtEmail.getText().toString());
                //password = sharedpreferences.getString(PASSWORD_KEY, binding.idEdtPassword.getText().toString());
                Log.d("user: ",user + " pass: " + password);
                // to check if the user fields are empty or not.
                if (TextUtils.isEmpty(binding.idEdtEmail.getText().toString()) && TextUtils.isEmpty(binding.idEdtPassword.getText().toString())) {
                    // this method will call when email and password fields are empty.
                    Toast.makeText(LoginFormActivity.this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
                } else {
                    cekLogin();
                }
            }
        });


    }

    private void cekLogin() {
        getData();
    }

    private void getData() {
        class GetData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() { // sebelum proses
                super.onPreExecute();
                loading = ProgressDialog.show(LoginFormActivity.this,
                        "Mengambil Data", "Harap Menunggu...",
                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) { // saat proses
                HttpHandler handler = new HttpHandler();
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("user", user);
                hashMap.put("pass", password);

//                String result = handler.sendPostRequest(Konfigurasi.URL_GET_LOGIN,hashMap);
                String result = handler.sendGetRespLogin(ConfigurationLogin.URL_GET_LOGIN,user,password);
                Log.d("res:", result + " user: " + user +" pass: " + password);

                return result;
            }

            @Override
            protected void onPostExecute(String message) { // setelah proses
                super.onPostExecute(message);
                loading.dismiss();
                JSON_STRING = message;
                Log.d("DATA JSON: ", JSON_STRING);
                //{"result":[]}
//                Toast.makeText(MainActivity.this, "msg: " + message, Toast.LENGTH_LONG).show();
                if (message.contains("Warning") || message.contains("error") || message.contains("[]") || message.equals("")  || message.equals(null)) {
                    Toast.makeText(LoginFormActivity.this, "Gagal Login", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(LoginFormActivity.this, "Berhasil Login", Toast.LENGTH_LONG).show();
                    loginBerhasil();
                }
            }
        }
        GetData getDATA = new GetData();
        getDATA.execute();
    }

    private void loginBerhasil() {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        // below two lines will put values for
        // email and password in shared preferences.
        editor.putString(USER_KEY, user);

        // to save our data with key and value.
        editor.apply();

        // starting new activity.
        Intent i = new Intent(LoginFormActivity.this, MainActivity.class);
        i.putExtra("keyUser", user);
        startActivity(i);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (user != null && password != null) {
            Intent i = new Intent(LoginFormActivity.this, MainActivity.class);
            startActivity(i);
        }
    }
}