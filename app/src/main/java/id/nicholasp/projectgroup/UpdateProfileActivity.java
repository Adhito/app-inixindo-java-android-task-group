package id.nicholasp.projectgroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import id.nicholasp.projectgroup.databinding.ActivityUpdateProfileBinding;

public class UpdateProfileActivity extends AppCompatActivity {

    // session
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String ID_KEY = "id_key";
    public static final String NAMA_KEY = "nama_key";
    public static final String SID_KEY = "sid_key";
    public static final String EMAIL_KEY = "email_key";
    public static final String HP_KEY = "hp_key";
    SharedPreferences sharedpreferences;

    String id_detail, sid, nama, email, hp, get_nama, get_email, get_hp;
    ActivityUpdateProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id_detail = sharedpreferences.getString(ID_KEY, null);
        nama = sharedpreferences.getString(NAMA_KEY, null);
        sid = sharedpreferences.getString(SID_KEY, null);
        email = sharedpreferences.getString(EMAIL_KEY, null);
        hp = sharedpreferences.getString(HP_KEY, null);

        Log.d("data: ", id_detail + "-" + nama+"-"+sid+"-"+email+"-"+hp);

        binding.etNama.setText(nama);
        binding.etEmail.setText(email);
        binding.etHp.setText(hp);

        binding.btnProfileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_nama = binding.etNama.getText().toString().trim();
                get_email = binding.etEmail.getText().toString().trim();
                get_hp = binding.etHp.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (get_nama.equals("") || get_email.equals("") || get_hp.equals("")){
                    Toast.makeText(UpdateProfileActivity.this, "Semua Data Harus Di isi", Toast.LENGTH_SHORT).show();
                } else if (!(get_email.matches(emailPattern) && get_email.length() > 0)) {
                    Toast.makeText(UpdateProfileActivity.this, "Format Email Salah", Toast.LENGTH_SHORT).show();
                } else {
                    confirmUpdate();
                }
            }
        });
    }

    private void confirmUpdate() {
        //Confirmation altert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("Confirm Update");
        builder.setMessage("Are you sure to update this data?");
        builder.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_info));
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateProfile();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateProfile() {
        class UpdateBalance extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(UpdateProfileActivity.this,
//                        "Mengubah Data", "Harap Tunggu",
//                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(ConfigurationPortofolio.KEY_LOG_DETAIL_ID, id_detail);
                params.put(ConfigurationPortofolio.KEY_LOG_NAMA, get_nama);
                params.put(ConfigurationPortofolio.KEY_LOG_EMAIL, get_email);
                params.put(ConfigurationPortofolio.KEY_LOG_HP, get_hp);
                HttpHandler handler = new HttpHandler();
                String result = handler.sendPostRequest(ConfigurationPortofolio.URL_EDIT_PROFILE, params);
                Log.d("result:",result);

                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                loading.dismiss();
                Log.d("msg:",s);

//                Toast.makeText(UpdateProfileActivity.this, "Berhasil Update", Toast.LENGTH_LONG).show();
                Toast toast= Toast.makeText(UpdateProfileActivity.this,
                        "Berhasil Update", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 200);
                toast.show();
                Intent myIntent = new Intent(UpdateProfileActivity.this, LoadingActivity.class);
                startActivity(myIntent);

            }
        }

        UpdateBalance updateBalance = new UpdateBalance();
        updateBalance.execute();

    }
}