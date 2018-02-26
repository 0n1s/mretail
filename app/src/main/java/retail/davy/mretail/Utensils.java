package retail.davy.mretail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static retail.davy.mretail.LoginClass.useruser;


public class Utensils extends AppCompatActivity {
    ListView listView;


    String content;
    String price;
    String uID;
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utensils);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().setTitle("My orders");
        listView=(ListView)findViewById(R.id.listview);
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
                final  String itemid =   map.get("id");
                //getJSON2(itemid);

            }});

        Intent intent=getIntent();
        uID=intent.getStringExtra("");


        getJSON(useruser);

        //Toast.makeText(this, useruser, Toast.LENGTH_SHORT).show();
    }

    public void getJSON(String user)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(Utensils.this);


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("Loading items");
                progressDialog.show();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> employees = new HashMap<>();
                employees.put("owner_id", useruser);
                String res=rh.sendPostRequest(URLs.main_url+"item_fetcher2.php",employees);

                return res;

            }
            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                progressDialog.dismiss();

                showthem(s);
            }


        }
        GetJSON jj =new GetJSON();
        jj.execute();
    }



    private void showthem(String s)
    {


        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            String itemID="";
            String succes="0";
            for (int i = 0; i < result.length(); i++)
            {  JSONObject jo = result.getJSONObject(i);
                succes=jo.getString("success");
                if (succes.equals("1"))
                {
                    String dateexe = jo.getString("date");
                    content = jo.getString("content");
                    price = jo.getString("price");
                    String trxID = jo.getString("trxID");
                    //String name = jo.getString("food_name");
                    StringBuilder  builder = new StringBuilder();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy' 'HH:mm:ss:S");

                    HashMap<String, String> employees = new HashMap<>();
                    employees.put("date", dateexe);
                    employees.put("name", content);
                    employees.put("price", price);
                    employees.put("id", trxID);
                    list.add(employees);
                }
                else
                {

                }





            }


        } catch (JSONException e) {

            Toast.makeText(this,String.valueOf(e), Toast.LENGTH_SHORT).show();

        }

        ListAdapter adapter = new SimpleAdapter(Utensils.this, list, R.layout.shoppinglists,
                new String[]{"name", "price","id","date"}, new int[]{R.id.date, R.id.price, R.id.textView26});
        listView.setAdapter(adapter);
    }


}
