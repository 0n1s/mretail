package retail.davy.mretail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import static retail.davy.mretail.LoginClass.useruser;
import static retail.davy.mretail.MainActivity.MyPREFERENCES;

public class Wallet2 extends AppCompatActivity
{
    Button button3;
    TextView textView31;
    EditText editText2;
    EditText editText5;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();


        textView31 = (TextView)findViewById(R.id.textView31);
        textView31.setText(sharedpreferences.getString("balance", "0"));

        button3= (Button)findViewById(R.id.button3);

        editText2 = (EditText)findViewById(R.id.editText2);

        editText5 = (EditText)findViewById(R.id.editText5);

        button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loginnow(useruser, editText2.getText().toString(), editText5.getText().toString());
            }
        });




    }


    public void loginnow (final String email,final String amt, final String phone_number)
    {
        class AddEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(Wallet2.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("amt",amt);
                params.put("phone",phone_number);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(URLs.main_url+"wallet.php", params);
                return res;

            }

            @Override
            protected void onPostExecute(final String s)
            {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if(s.equals(0))
                {
                    Toast.makeText(Wallet2.this, "an er", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    new AlertDialog.Builder(Wallet2.this).setMessage(s).setTitle("your request to top up your wallet is being processed!").show();
                }

            }
        }
        AddEmployee ae = new AddEmployee();
        ae.execute();


    }

}
