package Architect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.architect.MainActivity;
import com.example.architect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ArchitectRegistrationActivity extends AppCompatActivity {

    private Button sellerLoginBegin, registerButton;
    private EditText nameInput,phoneInput, emailInput, passwordInput;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_architect_registration);

        sellerLoginBegin = (Button) findViewById(R.id.seller_already_btn);
        loadingBar = new ProgressDialog(this);
        nameInput = findViewById(R.id.seller_name);
        phoneInput = findViewById(R.id.seller_phone);
        passwordInput = findViewById(R.id.seller_password);
        emailInput = findViewById(R.id.seller_email);
        mAuth = FirebaseAuth.getInstance();
registerButton = findViewById(R.id.seller_register_btn);

sellerLoginBegin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(ArchitectRegistrationActivity.this, ArchitectLoginActivity.class);
        startActivity(intent);
    }
});

registerButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        registerSeller();
    }
});

    }

    private void registerSeller() {

        final String name = nameInput.getText().toString();
        final String phone = phoneInput.getText().toString();
       final String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(!name.equals("")&&!phone.equals("")&&!email.equals("")&&!password.equals(""))
        {
            loadingBar.setTitle("Registering");
            loadingBar.setMessage("Verifying Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        final DatabaseReference rootRef;
                        rootRef = FirebaseDatabase.getInstance().getReference();

                        String sid = mAuth.getCurrentUser().getUid();
                        HashMap<String, Object>sellerMap = new HashMap<>();
                        sellerMap.put("sid", sid);
                        sellerMap.put("phone", phone);
                        sellerMap.put("email", email);
                        sellerMap.put("name", name);

                        rootRef.child("Sellers").child(sid).updateChildren(sellerMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ArchitectRegistrationActivity.this,  "Architect Registration Is Successful",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(ArchitectRegistrationActivity.this, ArchitectHomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                    }
                }
            });
        }else
        {

            Toast.makeText(this,"Please Complete the Registration Form",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
    }
}