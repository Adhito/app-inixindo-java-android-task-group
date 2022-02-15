package id.nicholasp.projectgroup;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductsFragment extends Fragment {
    private ProgressDialog loading;
    private String JSON_STRING;
    ListView listview;
    String url = "http://192.168.1.101/api_task_group/produk/get_all_produk.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        listview = view.findViewById(R.id.listViewProduct);
        getJsonData();

        return view;
    }

    public void getJsonData() {
        class GetJsonData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(getContext(), "Ambil Data Instruktur", "Harap menunggu...", false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HttpHandler handler = new HttpHandler();
                String result = handler.sendGetResponse(Configuration.GET_ALL_PRODUCT);
                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                JSON_STRING = message;
                Log.d("DATA_JSON: ", JSON_STRING);

                // menampilkan data json kedalam list view
                displayAllDataInstruktur();
            }
        }
        GetJsonData getJsonData = new GetJsonData();
        getJsonData.execute();
    }

    private void displayAllDataInstruktur() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String seri_produk = object.getString("seri_produk");
//                String nama_ins = object.getString(Konfigurasi.TAG_JSON_INS_NAMA);
//                String email_ins = object.getString(Konfigurasi.TAG_JSON_INS_EMAIL);

                HashMap<String, String> instruktur = new HashMap<>();
                instruktur.put("seri_produk", seri_produk);
//                instruktur.put(Konfigurasi.TAG_JSON_INS_NAMA, nama_ins);
//                instruktur.put(Konfigurasi.TAG_JSON_INS_EMAIL, email_ins);
                list.add(instruktur);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // adapter untuk meletakkan array list kedalam list view
        ListAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.list_produk_layout,
                new String[]{"seri_produk"},
                new int[]{R.id.txt_nama_produk}
        );

        listview.setAdapter(adapter);
    }
}