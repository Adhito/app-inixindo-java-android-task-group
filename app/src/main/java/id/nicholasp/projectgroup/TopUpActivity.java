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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

public class TopUpActivity extends AppCompatActivity {
    // session
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String ID_KEY = "id_key";
    public static final String BALANCE_KEY = "balance_key";

    String myStr, balance, get_balance,bal, val_balance = "";
    SharedPreferences sharedpreferences;
    EditText add_balance;
    Button btn_topup_add, btn_topup_cancel, btn_onemil, btn_fivemil, btn_tenmil, btn_fiftymil, btn_clear;
    Long balance_session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        Locale localeID = new Locale("in", "ID");
        add_balance = findViewById(R.id.add_balance);
        btn_topup_add = findViewById(R.id.btn_topup_add);
        btn_topup_cancel = findViewById(R.id.btn_topup_cancel);
        btn_onemil = findViewById(R.id.btn_onemil);
        btn_fivemil = findViewById(R.id.btn_fivemil);
        btn_tenmil = findViewById(R.id.btn_tenmil);
        btn_fiftymil = findViewById(R.id.btn_fiftymil);
        btn_clear = findViewById(R.id.btn_clear);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        myStr = sharedpreferences.getString(ID_KEY, null);
        bal = sharedpreferences.getString(BALANCE_KEY, null);
        balance_session = Long.parseLong(bal.substring(0, bal.length() - 2));

        btn_topup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_onemil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_balance.setText("1,000,000");
            }
        });

        btn_fivemil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_balance.setText("5,000,000");
            }
        });

        btn_tenmil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_balance.setText("10,000,000");
            }
        });

        btn_fiftymil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_balance.setText("50,000,000");
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_balance.setText("");
            }
        });

        btn_topup_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_balance = add_balance.getText().toString().replaceAll(",", "");
                if (get_balance.equals("") || get_balance.equals("0")) {
                    Toast.makeText(TopUpActivity.this, "Nominal Tidak Boleh Kosong Atau 0", Toast.LENGTH_SHORT).show();
                } else if (Long.parseLong(get_balance) < 100000) {
                    Toast.makeText(TopUpActivity.this, "Minimal Top Up Rp 100.000", Toast.LENGTH_SHORT).show();
                } else {
                    addBalance();
                }
            }
        });

        add_balance.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!s.toString().equals(val_balance)) {
                    add_balance.removeTextChangedListener(this);

                    String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
                    String cleanString = s.toString().replaceAll(replaceable, "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }
                    NumberFormat formatter = NumberFormat.getCurrencyInstance();
                    formatter.setMaximumFractionDigits(0);
                    String formatted = formatter.format((parsed)).toString().replaceAll("£", "");
                    formatted = formatted.substring(1);
                    val_balance = formatted;
                    add_balance.setText(val_balance);
                    add_balance.setSelection(formatted.length());
                    add_balance.addTextChangedListener(this);
                }
            }});
    }

    private void addBalance() {
        balance = add_balance.getText().toString().trim().replaceAll(",", "");
        confirmTopUp();
    }

    private void confirmTopUp() {
        String text_balance = add_balance.getText().toString().trim();
        //Confirmation altert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message");
        builder.setMessage("Nominal Top Up: Rp. " + text_balance);
        builder.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_info));
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", null);
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
        Log.d("BALANCESESSION", "BalanceSession: " + balance_session);
        Log.d("BALANCE", "Balance: " + balance);
        String total_balance = String.valueOf(balance_session + Long.parseLong(balance));
        Log.d("BALANCE", "Balance: " + total_balance);
        class UpdateBalance extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TopUpActivity.this,
                        "Mengubah Data", "Harap Tunggu",
                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(ConfigurationPortofolio.KEY_LOG_DETAIL_ID, myStr);
                params.put(ConfigurationPortofolio.KEY_LOG_BALANCE, total_balance);
                HttpHandler handler = new HttpHandler();
                String result = handler.sendPostRequest(ConfigurationPortofolio.URL_ADD_BALANCE, params);

                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                ;

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