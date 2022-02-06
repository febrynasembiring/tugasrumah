package com.example.tugasrumah;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DataCustomer extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private String JSON_STRING;
    private ListView listdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_customer);

        listdata = findViewById(R.id.listdata);

        listdata.setOnItemClickListener(this);

        getJSON();
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DataCustomer.this, "Processing Data", "Please wait...", false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HttpHandler handler = new HttpHandler();
                String result = handler.sendGetResp(konfigurasi.URL_GET_ALL);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 10);

                JSON_STRING = s;
                Log.d("Data_JSON", JSON_STRING);


                displayCustomer();

            }


        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();

    }

    private void displayCustomer() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray jsonArray = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            Log.d("Data_JSON_LIST: ", JSON_STRING);


            for (int i=0;i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString(konfigurasi.TAG_JSON_ID);
                String name = object.getString(konfigurasi.TAG_JSON_NAME);
                String city = object.getString(konfigurasi.TAG_JSON_CITY);
                String card = object.getString(konfigurasi.TAG_JSON_CARD);

                HashMap<String, String> cust = new HashMap<>();
                cust.put(konfigurasi.TAG_JSON_ID, id);
                cust.put(konfigurasi.TAG_JSON_NAME, name);
                cust.put(konfigurasi.TAG_JSON_CITY,city );
                cust.put(konfigurasi.TAG_JSON_CARD, card.toUpperCase());

                arrayList.add(cust);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                getApplicationContext(), arrayList, R.layout.cust_layout,
                new String[] {konfigurasi.TAG_JSON_NAME, konfigurasi.TAG_JSON_CITY, konfigurasi.TAG_JSON_CARD},
                new int[] {R.id.name_ext, R.id.city_ext, R.id.card_ext}
        );
        listdata.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(DataCustomer.this, DataCustomerExtension.class);
        HashMap<String, String> map = (HashMap) adapterView.getItemAtPosition(i);
        String customer_id = map.get(konfigurasi.TAG_JSON_ID).toString();
        intent.putExtra(konfigurasi.ID, customer_id);
        startActivity(intent);
    }

    public static class HTTPHandler {
    }
}

