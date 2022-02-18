package id.nicholasp.projectgroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class TopUpActivity extends AppCompatActivity {
    // session
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String ID_KEY = "id_key";

    String myStr,balance,get_balance;
    SharedPreferences sharedpreferences;
    EditText add_balance;
    Button btn_topup_add, btn_topup_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);
        add_balance = findViewById(R.id.add_balance);
        btn_topup_add = findViewById(R.id.btn_topup_add);
        btn_topup_cancel = findViewById(R.id.btn_topup_cancel);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        myStr = sharedpreferences.getString(ID_KEY, null);

        btn_topup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_topup_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_balance = add_balance.getText().toString();
                if (get_balance.equals("") || get_balance.equals("0")){
                    Toast.makeText(TopUpActivity.this, "Nominal Tidak Boleh Kosong Atau 0", Toast.LENGTH_SHORT).show();
                } else if  (Long.parseLong(get_balance) < 100000) {
                    Toast.makeText(TopUpActivity.this, "Minimal Top Up Rp 100.000", Toast.LENGTH_SHORT).show();
                }
                else {
                    addBalance();
                }
            }
        });
    }

    private void addBalance() {
        balance = add_balance.getText().toString().trim();
        confirmTopUp();
    }

    private void confirmTopUp() {
        //Confirmation altert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message");
        builder.setMessage("Nominal Top Up: Rp. " + balance);
        builder.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_info));
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateBalance();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateBalance() {
        class UpdateBalance extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TopUpActivity.this,
                        "Mengubah Data","Harap Tunggu",
                        false,false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put(ConfigurationPortofolio.KEY_LOG_DETAIL_ID,myStr);
                params.put(ConfigurationPortofolio.KEY_LOG_BALANCE,balance);
                HttpHandler handler = new HttpHandler();
                String result = handler.sendPostRequest(ConfigurationPortofolio.URL_ADD_BALANCE, params);

                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();;

                Fragment fragment = null;

                Toast.makeText(TopUpActivity.this,
                        "Pesan: " + s, Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(TopUpActivity.this, MainActivity.class);
                startActivity(myIntent);

            }
        }

        UpdateBalance updateBalance = new UpdateBalance();
        updateBalance.execute();

    }
}