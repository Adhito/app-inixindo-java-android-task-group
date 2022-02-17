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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    // Session
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String ID_KEY = "id_key";
    public static final String BALANCE_KEY = "balance_key";
    private int myValue = 0; // nilai awal

    TextView txt_pd_seri_produk, txt_pd_nama_produk, txt_pd_nominaltransaksi, txt_kelipatan, txt_pd_nilai_unit, txt_pd_yield, txt_pd_jatuh_tempo, txt_pd_minimum_transaksi, txt_pd_maksimum_transaksi, txt_pd_kelipatan_transaksi, txt_pd_penerbit, txt_pd_jenis_kupon, txt_pd_mata_uang, txt_pd_pembayaran_kupon;
    Button btn_beli, btn_plus, btn_minus;
    String id_produk, id, balance;
    Toolbar toolbar;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // txt_pd_seri_produk = findViewById(R.id.txt_pd_seri_produk);
        txt_pd_nama_produk = findViewById(R.id.txt_pd_nama_produk);
        // txt_pd_nilai_unit = findViewById(R.id.txt_pd_nilai_unit);
        // txt_pd_yield = findViewById(R.id.txt_pd_yield);
        txt_pd_jatuh_tempo = findViewById(R.id.txt_pd_jatuh_tempo);
        txt_pd_minimum_transaksi = findViewById(R.id.txt_pd_minimum_transaksi);
        txt_pd_maksimum_transaksi = findViewById(R.id.txt_pd_maksimum_transaksi);
        txt_pd_kelipatan_transaksi = findViewById(R.id.txt_pd_kelipatan_transaksi);
        txt_pd_penerbit = findViewById(R.id.txt_pd_penerbit);
        txt_kelipatan = findViewById(R.id.txt_pd_kelipatan);
        txt_pd_nominaltransaksi = findViewById(R.id.txt_pd_nominaltransaksi);
        // txt_pd_jenis_kupon = findViewById(R.id.txt_pd_jenis_kupon);
        // txt_pd_mata_uang = findViewById(R.id.txt_pd_mata_uang);
        // txt_pd_pembayaran_kupon = findViewById(R.id.txt_pd_pembayaran_kupon);
        btn_beli = findViewById(R.id.btn_beli);
        btn_plus = findViewById(R.id.btn_plus);
        btn_minus = findViewById(R.id.btn_minus);

        Intent receiveIntent = getIntent();
        id_produk = receiveIntent.getStringExtra(Configuration.PGW_ID);

        getJSON();

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myValue++;
                int nom = myValue * Integer.valueOf(txt_pd_kelipatan_transaksi.getText().toString());
                if (txt_kelipatan != null) {
                    txt_kelipatan.setText(Integer.toString(myValue));
                    txt_pd_nominaltransaksi.setText(Integer.toString(nom));
                }
            }
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int in = Integer.valueOf(txt_kelipatan.getText().toString());
                if (in <= 0) {
                    myValue = 0;
                    int nom = myValue * Integer.valueOf(txt_pd_kelipatan_transaksi.getText().toString());
                    txt_kelipatan.setText(Integer.toString(myValue));
                    txt_pd_nominaltransaksi.setText(Integer.toString(nom));
                }
                else {
                    myValue--;
                    int nom = myValue * Integer.valueOf(txt_pd_kelipatan_transaksi.getText().toString());
                    txt_kelipatan.setText(Integer.toString(myValue));
                    txt_pd_nominaltransaksi.setText(Integer.toString(nom));
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

            // String seri_produk = object.getString("seri_produk");
            String nama_produk = object.getString("nama_produk");
            // String nilai_unit = object.getString("nilai_unit");
            // String yield = object.getString("yield");
            String jatuh_tempo = object.getString("jatuh_tempo");
            String minimum_transaksi = object.getString("minimum_transaksi");
            String maksimum_transaksi = object.getString("maksimum_transaksi");
            String kelipatan_transaksi = object.getString("kelipatan_transaksi");
            String penerbit = object.getString("penerbit");
            // String jenis_kupon = object.getString("jenis_kupon");
            // String mata_uang = object.getString("mata_uang");
            // String pembayaran_kupon = object.getString("pembayaran_kupon");

            // txt_pd_seri_produk.setText(seri_produk);
            txt_pd_nama_produk.setText(nama_produk);
            // txt_pd_nilai_unit.setText(nilai_unit);
            // txt_pd_yield.setText(yield);
            txt_pd_jatuh_tempo.setText(jatuh_tempo);
            txt_pd_minimum_transaksi.setText("Rp " + minimum_transaksi);
            txt_pd_maksimum_transaksi.setText("Rp " + maksimum_transaksi);
            txt_pd_kelipatan_transaksi.setText(kelipatan_transaksi);
            txt_pd_penerbit.setText(penerbit);
            // txt_pd_jenis_kupon.setText(jenis_kupon);
            // txt_pd_mata_uang.setText(mata_uang);
            // txt_pd_pembayaran_kupon.setText(pembayaran_kupon);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void confirmBuyProduct() {
        sharedpreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        balance = sharedpreferences.getString(BALANCE_KEY, null);

        final double total_transaction = Integer.parseInt(txt_pd_nominaltransaksi.getText().toString().trim());
        final double current_balance = Integer.parseInt(balance);


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
        sharedpreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        balance = sharedpreferences.getString(BALANCE_KEY, null);

        final double total_transaction = Integer.parseInt(txt_pd_nominaltransaksi.getText().toString().trim());
        final double current_balance = Integer.parseInt(balance);

        // With Validation
        if(total_transaction > current_balance){
            Toast.makeText(ProductDetailActivity.this, "Mohon maaf, balance anda tidak mencukupi", Toast.LENGTH_LONG).show();
            Log.d("Balance Gagal", String.valueOf(current_balance));
        }
        else {
            buyProduct();
        }
    }

    private void buyProduct() {
        Intent receiveIntent = getIntent();
        Date date = new Date();
        sharedpreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(ID_KEY, null);

        final String id_detail_user  = id;
        final String id_produk = receiveIntent.getStringExtra(Configuration.PGW_ID);
        final String tgl_beli = new SimpleDateFormat("yyyy-MM-dd").format(date);
        final String harga_unit = txt_pd_kelipatan_transaksi.getText().toString().trim();
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