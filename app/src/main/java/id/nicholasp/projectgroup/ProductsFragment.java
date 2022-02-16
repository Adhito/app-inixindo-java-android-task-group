package id.nicholasp.projectgroup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ProgressDialog loading;
    private String JSON_STRING;
    ListView listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        listview = view.findViewById(R.id.listViewProduct);
        ListView lv = (ListView) view.findViewById(R.id.listViewProduct);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                Log.d("ListView: ", "Product Clicked");
//                Intent myIntent = new Intent(getActivity(), ProductDetailActivity.class);
//                HashMap<String, String> map = (HashMap) parent.getItemAtPosition(i);
//                String id_product = map.get(Configuration.TAG_JSON_ID_PRODUCT).toString();
//                myIntent.putExtra(Configuration.PGW_ID, id_product);
//                Log.d("ProductsFragment Log", id_product);
//                startActivity(myIntent);

            }
        });
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
                displayAllData();
            }
        }
        GetJsonData getJsonData = new GetJsonData();
        getJsonData.execute();
    }

    private void displayAllData() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String seri_produk = object.getString("seri_produk");
                String yield = object.getString("yield");
                String jatuh_tempo = object.getString("jatuh_tempo");
                String nilai_unit = object.getString("nilai_unit");

                HashMap<String, String> produk = new HashMap<>();
                produk.put("seri_produk", seri_produk);
                produk.put("yield", yield);
                produk.put("jatuh_tempo", jatuh_tempo);
                produk.put("nilai_unit", nilai_unit);
                list.add(produk);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // adapter untuk meletakkan array list kedalam list view
        ListAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.list_produk_layout,
                new String[]{"seri_produk", "jatuh_tempo", "yield", "nilai_unit"},
                new int[]{R.id.txt_nama_produk, R.id.txt_jatuh_tempo, R.id.txt_yield, R.id.txt_nilai_unit}
        );

        listview.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Event-handling when one of the list is selected
        Log.d("InstrukturFragment Log", "clicked");
    }

    @Override
    public void onClick(View view) {

    }
}