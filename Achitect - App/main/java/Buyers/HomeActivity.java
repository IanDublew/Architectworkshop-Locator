package Buyers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.architect.MainActivity;
import com.example.architect.R;

public class HomeActivity extends AppCompatActivity {
    private TextView Designs,Logout;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Designs = (TextView) findViewById(R.id.view_designs);
        Logout = (TextView) findViewById(R.id.logout);
        loadingBar = new ProgressDialog(this);

        Designs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Opening Designs", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

                Intent intent = new Intent(HomeActivity.this, ViewDesignsActivity.class);
                startActivity(intent);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });



    }}
