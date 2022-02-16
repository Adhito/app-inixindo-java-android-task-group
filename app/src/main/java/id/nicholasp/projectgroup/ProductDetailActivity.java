package id.nicholasp.projectgroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txt_pd_seri_produk, txt_pd_nama_produk, txt_pd_nilai_unit, txt_pd_yield, txt_pd_jatuh_tempo, txt_pd_minimum_transaksi, txt_pd_maksimum_transaksi, txt_pd_kelipatan_transaksi, txt_pd_penerbit, txt_pd_jenis_kupon, txt_pd_mata_uang, txt_pd_pembayaran_kupon;
    Button btn_beli;
    String id_produk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        txt_pd_seri_produk = findViewById(R.id.txt_pd_seri_produk);
        txt_pd_nama_produk = findViewById(R.id.txt_pd_nama_produk);
        txt_pd_nilai_unit = findViewById(R.id.txt_pd_nilai_unit);
        txt_pd_yield = findViewById(R.id.txt_pd_yield);
        txt_pd_jatuh_tempo = findViewById(R.id.txt_pd_jatuh_tempo);
        txt_pd_minimum_transaksi = findViewById(R.id.txt_pd_minimum_transaksi);
        txt_pd_maksimum_transaksi = findViewById(R.id.txt_pd_maksimum_transaksi);
        txt_pd_kelipatan_transaksi = findViewById(R.id.txt_pd_kelipatan_transaksi);
        txt_pd_penerbit = findViewById(R.id.txt_pd_penerbit);
        txt_pd_jenis_kupon = findViewById(R.id.txt_pd_jenis_kupon);
        txt_pd_mata_uang = findViewById(R.id.txt_pd_mata_uang);
        txt_pd_pembayaran_kupon = findViewById(R.id.txt_pd_pembayaran_kupon);
        btn_beli = findViewById(R.id.btn_beli);


        Intent receiveIntent = getIntent();
        id_produk = receiveIntent.getStringExtra(Configuration.PGW_ID);

        getJSON();
        btn_beli.setOnClickListener(this);
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


            String seri_produk = object.getString("seri_produk");
            String nama_produk = object.getString("nama_produk");
            String nilai_unit = object.getString("nilai_unit");
            String yield = object.getString("yield");
            String jatuh_tempo = object.getString("jatuh_tempo");
            String minimum_transaksi = object.getString("minimum_transaksi");
            String maksimum_transaksi = object.getString("maksimum_transaksi");
            String kelipatan_transaksi = object.getString("kelipatan_transaksi");
            String penerbit = object.getString("penerbit");
            String jenis_kupon = object.getString("jenis_kupon");
            String mata_uang = object.getString("mata_uang");
            String pembayaran_kupon = object.getString("pembayaran_kupon");

            txt_pd_seri_produk.setText(seri_produk);
            txt_pd_nama_produk.setText(nama_produk);
            txt_pd_nilai_unit.setText(nilai_unit);
            txt_pd_yield.setText(yield);
            txt_pd_jatuh_tempo.setText(jatuh_tempo);
            txt_pd_minimum_transaksi.setText(minimum_transaksi);
            txt_pd_maksimum_transaksi.setText(maksimum_transaksi);
            txt_pd_kelipatan_transaksi.setText(kelipatan_transaksi);
            txt_pd_penerbit.setText(penerbit);
            txt_pd_jenis_kupon.setText(jenis_kupon);
            txt_pd_mata_uang.setText(mata_uang);
            txt_pd_pembayaran_kupon.setText(pembayaran_kupon);

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_beli){
            confirmBuyProduct();
        } else {
            Toast.makeText(getApplicationContext(), "Potential Other Button", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmBuyProduct() {
        // Show confirmation alert dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Memperbarui data instruktur");
        builder.setMessage("Apakah anda ingin memperbarui instruktur ini ?");
        builder.setIcon(getResources().getDrawable(android.R.drawable.ic_input_add));
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Buy product", Toast.LENGTH_SHORT).show();
                // buyProduct();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

//    private void buyProduct() {
//        final String nama_ins = edit_nama_ins.getText().toString().trim();
//        final String email_ins = edit_email_ins.getText().toString().trim();
//        final String hp_ins = edit_hp_ins.getText().toString().trim();
//
//        class BuyProduct extends AsyncTask<Void, Void, String> {
//            ProgressDialog loading;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(InstrukturTambahActivity.this,
//                        "Menyimpan Obligasi",
//                        "Harap Tunggu ...",
//                        false,
//                        false);
//            }
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                // Create hashmap to store values which will be sent to HttpHandler
//                HashMap<String, String> params = new HashMap<>();
//                params.put("nama_ins", nama_ins);
//                params.put("email_ins", email_ins);
//                params.put("hp_ins", hp_ins);
//                HttpHandler handler = new HttpHandler();
//
//                // Create HttpHandler to send data with sendPostRequest
//                String result = handler.sendPostRequest(Configuration.URL_INSTRUKTUR_ADD, params);
//                return result;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//                Toast.makeText(
//                        ProductDetailActivity.this,
//                        "pesan:" + s,
//                        Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//        BuyProduct buyProduct = new BuyProduct();
//        buyProduct.execute();
//
//        // Back to home after add process
//        // startActivity(new Intent(InstrukturTambahActivity.this, MainActivity.class));
//
//        // Back to instruktur fragment after add process
//        Intent myIntent = new Intent(ProductDetailActivity.this, MainActivity.class);
//        myIntent.putExtra("KeyName", "Instruktur");
//        startActivity(myIntent);
//    }


}