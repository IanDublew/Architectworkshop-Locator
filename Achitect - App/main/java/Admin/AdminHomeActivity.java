package Admin;

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

import Architect.ArchitectCategoryActivity;
import Architect.ArchitectHomeActivity;
import Buyers.HomeActivity;
import Buyers.ViewDesignsActivity;

public class AdminHomeActivity extends AppCompatActivity {
    private TextView ViewDesigns,AddDesigns,Logout;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        ViewDesigns = (TextView) findViewById(R.id.admin_view_designs);
        AddDesigns = (TextView) findViewById(R.id.admin_add_designs);
        Logout = (TextView) findViewById(R.id.admin_logout);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        ViewDesigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminHomeActivity.this, "Opening Designs ", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

                Intent intent = new Intent(AdminHomeActivity.this, ViewDesignsActivity.class);
                startActivity(intent);
            }
        });
        AddDesigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminHomeActivity.this, "Opening Add Designs", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

                Intent intent = new Intent(AdminHomeActivity.this, AdminAddNewProduct.class);
                startActivity(intent);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminHomeActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });
    }
}