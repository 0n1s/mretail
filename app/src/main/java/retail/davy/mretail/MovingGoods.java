package retail.davy.mretail;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static retail.davy.mretail.LoginClass.useruser;

public class MovingGoods extends AppCompatActivity {
ListView listView;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    private Context mContext;
    public List<ItemData> listitems;

    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_goods);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView=(ListView)findViewById(R.id.movinggoods);
        getJSON();
    }


    public void getJSON()
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(MovingGoods.this);
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
                String res=rh.sendPostRequest(URLs.main_url+"item_fetcher245.php",employees);
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









        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);//done here
        layoutManager = new GridLayoutManager(mContext, 1);
        recyclerView.setLayoutManager(layoutManager);













            //  new AlertDialog.Builder(MovingGoods.this).setMessage(s).show();

        //  Toast.makeText(this, s, Toast.LENGTH_LONG).show();

        Log.d("items", s);
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
                    String trxID = jo.getString("trxID");
                    HashMap<String, String> employees = new HashMap<>();
                    employees.put("date", dateexe);
                    employees.put("id", trxID);
                    employees.put("name", jo.getString("content"));
                    list.add(employees);
                }
                else
                {
                }
            }

        } catch (JSONException e) {
            Toast.makeText(this,String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
        ListAdapter adapter = new SimpleAdapter(MovingGoods.this, list, R.layout.shoppinglists,
                new String[]{"name", "price","date"}, new int[]{R.id.date, R.id.price, R.id.textView26});
        listView.setAdapter(adapter);
    }


}
