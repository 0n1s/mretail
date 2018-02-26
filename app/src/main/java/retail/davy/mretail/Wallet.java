package retail.davy.mretail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Wallet extends AppCompatActivity
{


    Button button3;
    TextView textView31;
    EditText editText2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        button3= (Button)findViewById(R.id.button3);
        textView31 = (TextView)findViewById(R.id.textView31);
        editText2 = (EditText)findViewById(R.id.editText2);

        button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

            }
        });





    }
}
