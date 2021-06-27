package Architect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.architect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class ArchitectLoginActivity extends AppCompatActivity {
    private Button LoginSellerButton;
    private ProgressDialog loadingBar;
    private EditText emailInput, passwordInput;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_architect_login);

        LoginSellerButton =findViewById(R.id.seller_login_btn);
        emailInput = findViewById(R.id.seller_login_email);
        passwordInput = findViewById(R.id.seller_login_password);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        LoginSellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginSeller();
            }
        });

    }

    private void LoginSeller() {

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        if (!email.equals("") && !password.equals("")) {
            loadingBar.setTitle("Architect Logging In");
            loadingBar.setMessage("Verifying Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(ArchitectLoginActivity.this, ArchitectHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

        }else{
            Toast.makeText(this,"Please complete Log in Form", Toast.LENGTH_SHORT).show();
        }
    }
}