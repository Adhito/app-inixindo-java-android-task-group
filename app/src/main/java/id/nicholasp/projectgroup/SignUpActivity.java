package id.nicholasp.projectgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import javax.xml.transform.Result;

import id.nicholasp.projectgroup.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    String user, pass, re_pass, sid, nama, email, hp, JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        binding.btnRAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = binding.txtRUser.getText().toString().trim();
                pass = binding.txtRPass.getText().toString().trim();
                re_pass = binding.txtRRePass.getText().toString().trim();
                sid = binding.txtRSid.getText().toString().trim();
                nama = binding.txtRName.getText().toString().trim();
                email = binding.txtRMail.getText().toString().trim();
                hp = binding.txtRPhone.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (user.equals("") || pass.equals("") || sid.equals("") || nama.equals("") || email.equals("")) {
//                    Toast.makeText(SignUpActivity.this, "Semua Data Wajib Diisi", Toast.LENGTH_LONG).show();
                    Toast toast= Toast.makeText(SignUpActivity.this,
                            "Semua Data Wajib Diisi", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 200);
                    toast.show();
                } else if (!(pass.equals(re_pass))) {
//                    Toast.makeText(SignUpActivity.this, "Password dan Re-Password \nHarus Sama", Toast.LENGTH_LONG).show();
                    Toast toast= Toast.makeText(SignUpActivity.this,
                            "Password dan Re-Password \nHarus Sama", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 200);
                    toast.show();
                } else if (!(email.matches(emailPattern) && email.length() > 0)) {
//                    Toast.makeText(SignUpActivity.this, "Format Email Salah", Toast.LENGTH_LONG).show();
                    Toast toast= Toast.makeText(SignUpActivity.this,
                            "Format Email Salah", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 200);
                    toast.show();
                } else {
                    cekUser();
                }
            }
        });

        binding.btnRCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginFormActivity.class));
            }
        });

    }

    private void simpanData() {
//        final String sid = binding.txtRSid.getText().toString().trim();
//        final String nama = binding.txtRName.getText().toString().trim();

        class SimpanData extends AsyncTask<Void, Void, String> {
//            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(ConfigurationLogin.KEY_LOG_USER, user);
                params.put(ConfigurationLogin.KEY_LOG_PASS, pass);
                params.put(ConfigurationLogin.KEY_LOG_SID, sid);
                params.put(ConfigurationLogin.KEY_LOG_NAMA, nama);
                params.put(ConfigurationLogin.KEY_LOG_EMAIL, email);
                params.put(ConfigurationLogin.KEY_LOG_HP, hp);
                HttpHandler handler = new HttpHandler();
                String result = handler.sendPostRequest(ConfigurationLogin.URL_ADD_LOGIN, params);
                Log.d("res", result + " sid " + sid + " nama " + nama +
                        " user " + user + " pass " + pass + " email " + email + " hp " + hp);
                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                Toast.makeText(SignUpActivity.this, "Sign Up Berhasil",
                        Toast.LENGTH_LONG).show();
                clearText();
                Intent myIntent = new Intent(SignUpActivity.this, LoginFormActivity.class);
                myIntent.putExtra("keyName", "materi");
                startActivity(myIntent);
            }
        }
        SimpanData simpanData = new SimpanData();
        simpanData.execute();
    }

    private void clearText() {
        binding.txtRUser.setText("");
        binding.txtRPass.setText("");
        binding.txtRRePass.setText("");
        binding.txtRSid.setText("");
        binding.txtRName.setText("");
        binding.txtRMail.setText("");
        binding.txtRPhone.setText("");
    }

    private void cekUser() {
        class CekUser extends AsyncTask<Void, Void, String> { // boleh membuat class dalam method (Inner Class)
            ProgressDialog loading;

            @Override
            protected void onPreExecute() { // sebelum proses
                super.onPreExecute();
//                loading = ProgressDialog.show(SignUpActivity.this,
//                        "Mengambil Data", "Harap Menunggu...",
//                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) { // saat proses
                HttpHandler handler = new HttpHandler();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user", user);
                String result = handler.sendGetResponse(ConfigurationLogin.URL_GET_USER, user);
                Log.d("res:", result + " user: " + user);
                Log.d("user:", user);

                return result;
            }

            @Override
            protected void onPostExecute(String message) { // setelah proses
                super.onPostExecute(message);
//                loading.dismiss();
                JSON_STRING = message;
                Log.d("DATA JSON: ", JSON_STRING);
                if (!(message.contains("Warning") || message.contains("error") || message.contains("[]"))) {
                    Toast.makeText(SignUpActivity.this, "Username Sudah Ada", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(SignUpActivity.this, "Berhasil", Toast.LENGTH_LONG).show();
                    simpanData();
                }
            }
        }
        CekUser cekUser = new CekUser();
        cekUser.execute();
    }
}