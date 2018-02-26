package retail.davy.mretail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static int item_id=0;
    public static JSONArray   jsonArray ;
    ListView listView;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    private Context mContext;
    RecyclerView.LayoutManager layoutManager;
    String actvity_title;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String uID;
    SharedPreferences sharedpreferences;
    public List<ItemData> listitems;
    SwipeRefreshLayout swipeRefreshLayout;
    public String fetched_data_foods;
    EditText editText7;
    Button button5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_main);



editText7 = (EditText)findViewById(R.id.editText7);

button5 =(Button)findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String searchlocation = editText7.getText().toString();

                fetch_certain_location(searchlocation);

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mContext=MainActivity.this;
       // Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mobile Retail");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                    Intent intent = new Intent(MainActivity.this, Cart.class);
                    startActivity(intent);

            }
        });


        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe) ;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                fetch_items();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent=getIntent();
        uID=intent.getStringExtra("email");
       // Toast.makeText(this, "Welcome "+uID, Toast.LENGTH_SHORT).show();

        fetch_items();




    }


    public void fetch_certain_location(String location)
    {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing your request");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.main_url+"itemfetcher.php?search_string="+location,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        progressDialog.cancel();
                        swipeRefreshLayout.setRefreshing(false);
                        fetched_data_foods=response;

                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);//done here
                        layoutManager = new GridLayoutManager(mContext, 1);
                        recyclerView.setLayoutManager(layoutManager);
                        JSONObject jsonObject = null;
                        ItemData itemdata;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray("result");


                            listitems = new ArrayList<>();
                            for (int i = 0; i < result.length(); i++)
                            {
                                JSONObject jo = result.getJSONObject(i);
                                String item_price=jo.getString("price");
                                String image_url=jo.getString("image");
                                String item_name=jo.getString("name");
                                String quantity_remaining = jo.getString("quantity_remaining");
                                String item_description=jo.getString("contents");
                                String category=jo.getString("category");
                                String distributor_name=jo.getString("distributor_name");
                                String minimum_order_quantity=jo.getString("minimum_order_quantity");
                                String qntytype=jo.getString("qntytype");
                                //    public ItemData(String item_description, String qntytype, String category, String retailer, String min_buy, String item_price, String image_url, String quantity_remaining, String item_name)
                                itemdata = new ItemData(item_description, qntytype, category,distributor_name ,minimum_order_quantity,item_price, image_url, quantity_remaining, item_name);
                                listitems.add(itemdata);
                            }
                            adapter = new MyAdapter(listitems,MainActivity.this);
                            recyclerView.setAdapter(adapter);



                        } catch (JSONException e)
                        {


                          //  Toast.makeText(MainActivity.this, String.valueOf(e), Toast.LENGTH_SHORT).show();

                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                // Toast.makeText(mContext, String.valueOf(error), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);

    }



    public  void fetch_items()
    {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing your request");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.main_url+"item_fetcher.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        progressDialog.cancel();
                        swipeRefreshLayout.setRefreshing(false);
                        fetched_data_foods=response;


                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);//done here
                        layoutManager = new GridLayoutManager(mContext, 1);
                        recyclerView.setLayoutManager(layoutManager);

                        JSONObject jsonObject = null;
                        ItemData itemdata;


                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray("result");


                            listitems = new ArrayList<>();
                            for (int i = 0; i < result.length(); i++)
                            {
                                JSONObject jo = result.getJSONObject(i);
                                String item_price=jo.getString("price");
                                String image_url=jo.getString("image");
                                String item_name=jo.getString("name");
                                String quantity_remaining = jo.getString("quantity_remaining");
                                String item_description=jo.getString("contents");
                                String category=jo.getString("category");
                                String distributor_name=jo.getString("distributor_name");
                                String minimum_order_quantity=jo.getString("minimum_order_quantity");
                                String qntytype=jo.getString("qntytype");
                                //    public ItemData(String item_description, String qntytype, String category, String retailer, String min_buy, String item_price, String image_url, String quantity_remaining, String item_name)
                                itemdata = new ItemData(item_description, qntytype, category,distributor_name ,minimum_order_quantity,item_price, image_url, quantity_remaining, item_name);
                                listitems.add(itemdata);
                            }
                            adapter = new MyAdapter(listitems,MainActivity.this);
                            recyclerView.setAdapter(adapter);



                        } catch (JSONException e)
                        {


                            Toast.makeText(MainActivity.this, String.valueOf(e), Toast.LENGTH_SHORT).show();

                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
               // Toast.makeText(mContext, String.valueOf(error), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {

            Intent intent = new Intent(MainActivity.this, Cart.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_camera)
        {
            Intent intent = new Intent(MainActivity.this, Utensils.class);
            intent.putExtra("category", "offers");
            intent.putExtra("uDI",uID);
            startActivity(intent);
        }
        if(id == R.id.wallet)
        {
            Intent intent = new Intent(MainActivity.this, Wallet2.class);
            intent.putExtra("category", "offers");
            intent.putExtra("uDI",uID);
            startActivity(intent);
        }

        if(id == R.id.Logout)
        {
            Intent intent = new Intent(MainActivity.this, LoginClass.class);
            startActivity(intent);
            this.finish();
        }

        if(id == R.id.movingoods)
        {
            Intent intent = new Intent(MainActivity.this, MovingGoods.class);
            startActivity(intent);
           // this.finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

