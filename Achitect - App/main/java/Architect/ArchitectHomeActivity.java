package Architect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.architect.MainActivity;
import com.example.architect.R;
import com.google.firebase.auth.FirebaseAuth;

import Buyers.ViewDesignsActivity;

public class ArchitectHomeActivity extends AppCompatActivity {
    private TextView ViewDesigns,AddDesigns,Logout;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_architect_home);


        ViewDesigns = (TextView) findViewById(R.id.view_architect_designs);
        AddDesigns = (TextView) findViewById(R.id.add_designs);
        Logout = (TextView) findViewById(R.id.architect_logout);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();


        ViewDesigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ArchitectHomeActivity.this, "Opening Designs ", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

                Intent intent = new Intent(ArchitectHomeActivity.this, ViewDesignsActivity.class);
                startActivity(intent);
            }
        });
        AddDesigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ArchitectHomeActivity.this, "Opening Add Designs", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

                Intent intent = new Intent(ArchitectHomeActivity.this, ArchitectCategoryActivity.class);
                startActivity(intent);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Toast.makeText(ArchitectHomeActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

                Intent intent = new Intent(ArchitectHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}