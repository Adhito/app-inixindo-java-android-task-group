package id.nicholasp.projectgroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    // Session
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String ID_KEY = "id_key";
    public static final String BALANCE_KEY = "balance_key";
    private int myValue = 0; // nilai awal

    TextView txt_pd_nama_produk, txt_pd_nominaltransaksi, txt_pd_jatuh_tempo, txt_pd_minimum_transaksi, txt_pd_maksimum_transaksi, txt_pd_kelipatan_transaksi, txt_pd_penerbit;
    EditText txt_kelipatan;
    Button btn_beli, btn_plus, btn_minus;
    String id_produk, id, kt_cal;
    Toolbar toolbar;
    SharedPreferences sharedpreferences;
    Integer nom = 0, max_buy = 0;
    Long balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_pd_nama_produk = findViewById(R.id.txt_pd_nama_produk);
        txt_pd_jatuh_tempo = findViewById(R.id.txt_pd_jatuh_tempo);
        txt_pd_minimum_transaksi = findViewById(R.id.txt_pd_minimum_transaksi);
        txt_pd_maksimum_transaksi = findViewById(R.id.txt_pd_maksimum_transaksi);
        txt_pd_kelipatan_transaksi = findViewById(R.id.txt_pd_kelipatan_transaksi);
        txt_pd_penerbit = findViewById(R.id.txt_pd_penerbit);
        txt_kelipatan = findViewById(R.id.txt_pd_kelipatan);
        txt_pd_nominaltransaksi = findViewById(R.id.txt_pd_nominaltransaksi);
        btn_beli = findViewById(R.id.btn_beli);
        btn_plus = findViewById(R.id.btn_plus);
        btn_minus = findViewById(R.id.btn_minus);

        Intent receiveIntent = getIntent();
        id_produk = receiveIntent.getStringExtra(Configuration.PGW_ID);

        sharedpreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String bal = sharedpreferences.getString(BALANCE_KEY, null);
        balance = Long.parseLong(bal.substring(0, bal.length() - 2));
//        balance = Long.parseLong(sharedpreferences.getString(BALANCE_KEY, null));
        Log.d("BALANCE", "validateCurrentBalance: " + balance);

        getJSON();

        txt_kelipatan.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    myValue = Integer.parseInt(txt_kelipatan.getText().toString());
                    nom = myValue * Integer.valueOf(kt_cal);
                    txt_pd_nominaltransaksi.setText(formatRupiah(Double.parseDouble(Integer.toString(nom))));
            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myValue++;
                nom = myValue * Integer.valueOf(kt_cal);
                if (txt_kelipatan != null) {
                    txt_kelipatan.setText(Integer.toString(myValue));
                    txt_pd_nominaltransaksi.setText(formatRupiah(Double.parseDouble(Integer.toString(nom))));
                }
            }
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_kelipatan.getText().toString().equals("")) {
                    txt_kelipatan.setText("0");
                }
                else {
                    int in = Integer.valueOf(txt_kelipatan.getText().toString());
                    if (in <= 0) {
                        myValue = 0;
                        nom = myValue * Integer.valueOf(kt_cal);
                        txt_kelipatan.setText(Integer.toString(myValue));
                        txt_pd_nominaltransaksi.setText(formatRupiah(Double.parseDouble(Integer.toString(nom))));
                    }
                    else {
                        myValue--;
                        nom = myValue * Integer.valueOf(kt_cal);
                        txt_kelipatan.setText(Integer.toString(myValue));
                        txt_pd_nominaltransaksi.setText(formatRupiah(Double.parseDouble(Integer.toString(nom))));
                    }
                }
            }
        });

        btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmBuyProduct();
            }
        });
    }

    private void getJSON() {
        // Get peserta data from MySQL throught Web-API with JSON format
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            // Override PreExecute (Ctrl + O select the onPreExecute)
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(
                        ProductDetailActivity.this,
                        "Mengambil data obligasi",
                        "Harap Menunggu ...",
                        false,
                        false);
            }

            // Override doInBackground (Ctrl + O select the doInBackground)
            @Override
            protected String doInBackground(Void... voids) {
                HttpHandler handler = new HttpHandler();
                String result = handler.sendGetResponse(Configuration.GET_DETAIL_PRODUCT, id_produk);
                Log.d("GET DETAIL PRODUCT", result);
                return result;
            }

            // Override onPostExecute (Ctrl + O select the onPostExecute)
            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                displayDetailData(message);
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void displayDetailData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Configuration.TAG_JSON_ARRAY);
            JSONObject object = result.getJSONObject(0);

            String nama_produk = object.getString("nama_produk");
            String jatuh_tempo = object.getString("jatuh_tempo");
            String minimum_transaksi = object.getString("minimum_transaksi");
            String maksimum_transaksi = object.getString("maksimum_transaksi");
            String kelipatan_transaksi = object.getString("kelipatan_transaksi");
            kt_cal = kelipatan_transaksi;
            String kt_cal = object.getString("kelipatan_transaksi");
            String penerbit = object.getString("penerbit");

            txt_pd_nama_produk.setText(nama_produk);
            txt_pd_jatuh_tempo.setText(jatuh_tempo);
            txt_pd_minimum_transaksi.setText(formatRupiah(Double.parseDouble(minimum_transaksi)));
            txt_pd_maksimum_transaksi.setText(formatRupiah(Double.parseDouble(maksimum_transaksi)));
            max_buy = Integer.parseInt(maksimum_transaksi);
            txt_pd_kelipatan_transaksi.setText(formatRupiah(Double.parseDouble(kelipatan_transaksi)));
            txt_pd_penerbit.setText(penerbit);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void confirmBuyProduct() {


        final double total_transaction = nom;

        // Show confirmation alert dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi pembelian obligasi");
        builder.setMessage("Total nominal : " + total_transaction);
        builder.setIcon(getResources().getDrawable(android.R.drawable.ic_input_add));
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                validateCurrentBalance();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void validateCurrentBalance() {
        if(txt_kelipatan.getText().toString().trim().equals("0") || txt_kelipatan.getText().toString().trim().equals("")){
            Toast.makeText(ProductDetailActivity.this, "Tidak Dapat Beli 0 Unit", Toast.LENGTH_SHORT).show();
        } else if (nom > max_buy) {
            Toast.makeText(ProductDetailActivity.this, "Melebihi Maximum", Toast.LENGTH_SHORT).show();
        } else if (nom > balance) {
            Toast.makeText(ProductDetailActivity.this, "Saldo Tidak Cukup", Toast.LENGTH_SHORT).show();
        }else {
            buyProduct();
        }
//        sharedpreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//        balance = sharedpreferences.getString(BALANCE_KEY, null);
//
//        final double total_transaction = nom;
//        final double current_balance = Integer.parseInt(balance);
//
//        // With Validation
//        if(total_transaction > current_balance){
//            Toast.makeText(ProductDetailActivity.this, "Mohon maaf, balance anda tidak mencukupi", Toast.LENGTH_LONG).show();
//            Log.d("Balance Gagal", String.valueOf(current_balance));
//        }
//        else {
//        }
//        buyProduct();
    }

    private void buyProduct() {
        Intent receiveIntent = getIntent();
        Date date = new Date();
        sharedpreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(ID_KEY, null);

        final String id_detail_user  = id;
        final String id_produk = receiveIntent.getStringExtra(Configuration.PGW_ID);
        final String tgl_beli = new SimpleDateFormat("yyyy-MM-dd").format(date);
        final String harga_unit = kt_cal;
        final String jumlah_unit = txt_kelipatan.getText().toString().trim();

        class BuyProduct extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProductDetailActivity.this,
                        "Menyimpan Obligasi",
                        "Harap Tunggu ...",
                        false,
                        false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                // Create hashmap to store values which will be sent to HttpHandler
                HashMap<String, String> params = new HashMap<>();
                params.put("id_detail_user", id_detail_user);
                params.put("id_produk", id_produk);
                params.put("tgl_beli", tgl_beli);
                params.put("harga_unit", harga_unit);
                params.put("jumlah_unit", jumlah_unit);
                HttpHandler handler = new HttpHandler();

                // Create HttpHandler to send data with sendPostRequest
                String result = handler.sendPostRequest(Configuration.URL_ADD_PRODUCT, params);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.d("Pesan", s);
            }
        }
        BuyProduct buyProduct = new BuyProduct();
        buyProduct.execute();

        // Back to home after add process
        startActivity(new Intent(ProductDetailActivity.this, MainActivity.class));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}