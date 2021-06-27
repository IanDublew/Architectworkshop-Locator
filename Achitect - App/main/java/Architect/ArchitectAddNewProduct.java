package Architect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.architect.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import Admin.AdminAddNewProduct;
import Admin.AdminCategoryActivity;

public class ArchitectAddNewProduct extends AppCompatActivity {
    private String CategoryName, Description, Price, Pname, Seller,Location, saveCurrentDate, saveCurrentTime;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice, InputSeller,InputLocation;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_architect_add_new_product);
        CategoryName = getIntent().getExtras().get("category").toString();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Button addNewProductButton = (Button) findViewById(R.id.architect_add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.architect_select_product_image);
        InputProductName = (EditText) findViewById(R.id.architect_product_name);
        InputProductPrice = (EditText) findViewById(R.id.architect_product_price);
        InputProductDescription = (EditText) findViewById(R.id.architect_product_description);
        InputSeller = (EditText) findViewById(R.id.architect_product_seller);
        InputLocation =(EditText) findViewById(R.id.architect_location_link);
        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
            }
        });
    }
    private void OpenGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri= data.getData();
            InputProductImage.setImageURI(ImageUri);

        }}

    @SuppressLint("ShowToast")
    private void ValidateProductData()
    {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();
        Seller = InputSeller.getText().toString();
        Location = InputLocation.getText().toString();
        if (ImageUri==null)
        {
            Toast.makeText(this, "Product Image IS Mandatory..", Toast.LENGTH_SHORT);
        }
        else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please Write Product Description..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please Write Product Price..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please Write Product Product Name..", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Seller))
        {
            Toast.makeText(this, "Please Write Seller's Name..", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Location))
        {
            Toast.makeText(this, "Please Write Location Link..", Toast.LENGTH_SHORT).show();
        }
        else{
            StoreProductInformation();
        }

    }
    private void StoreProductInformation()
    {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        productRandomKey = saveCurrentDate + saveCurrentTime;
        StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey);
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(ArchitectAddNewProduct.this, "Error" + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ArchitectAddNewProduct.this, "Image uploaded Successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw Objects.requireNonNull(task.getException());
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl = Objects.requireNonNull(task.getResult()).toString();
                            Toast.makeText(ArchitectAddNewProduct.this, "Product Image Url Is Received Successfully", Toast.LENGTH_SHORT).show();
                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",CategoryName);
        productMap.put("price",Price);
        productMap.put("pname",Pname);
        productMap.put("seller",Seller);
        productMap.put("location",Location);

        ProductsRef.child(productRandomKey). updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ArchitectAddNewProduct.this, "Product Is Added Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ArchitectAddNewProduct.this, ArchitectHomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

}