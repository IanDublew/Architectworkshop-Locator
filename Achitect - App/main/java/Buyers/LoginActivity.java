package Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Admin.AdminCategoryActivity;

import com.example.architect.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Admin.AdminHomeActivity;
import Model.Users;
import Prevalent.Prevalent;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {


    private EditText InputPhoneNumber,InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink;
    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        chkBoxRememberMe = (CheckBox) findViewById((R.id.remember_me_chkb));
        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Log In Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Log In");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });

    }
    private void loginUser() {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

         if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "PhoneNumber cannot be Empty", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Insert password", Toast.LENGTH_SHORT).show();
        }
         else
         {
             loadingBar.setTitle("Logging In");
             loadingBar.setMessage("Verifying Credentials");
             loadingBar.setCanceledOnTouchOutside(true);
             loadingBar.show();

             ValidatelephoneNumber(phone,password);
         }
    }

    private void ValidatelephoneNumber(String phone, String password) {
    AllowAccessToAccount(phone,password);

    }

    private void AllowAccessToAccount( String phone, String password) {

        if( chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(phone).exists()) {
                    Users usersData = (Users) snapshot.child(parentDbName).child(phone).getValue(Users.class);
                    assert usersData != null;
                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {

                            if(parentDbName.equals("Admins")){
                                Toast.makeText(LoginActivity.this, "Admin Logged In Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                            }
                            else if(parentDbName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Log In Is Successful", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        } else{
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Incorrect  LoginButton.setText(\"Log In Admin\");\n" +
                                    "                AdminLink.setVisibility(View.INVISIBLE);\n" +
                                    "                NotAdminLink.setVisibility(View.VISIBLE);\n" +
                                    "                parentDbName = \"Admins\";", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "Account with this Number " + phone + " Does Not Exist", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}