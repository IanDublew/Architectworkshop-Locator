package Architect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.architect.R;

import Admin.AdminAddNewProduct;
import Admin.AdminCategoryActivity;

public class ArchitectCategoryActivity extends AppCompatActivity {
    private TextView cottages,farmhouse,modern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_architect_category);


        cottages=(TextView) findViewById(R.id.cottages);
        farmhouse= (TextView) findViewById(R.id.farmhouse);
        modern = (TextView) findViewById(R.id.modern);
        cottages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArchitectCategoryActivity.this, ArchitectAddNewProduct.class);
                intent.putExtra("category","Cottages");
                startActivity(intent);
            }
        });
        farmhouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArchitectCategoryActivity.this, ArchitectAddNewProduct.class);
                intent.putExtra("category","Farmhouse");
                startActivity(intent);
            }
        });

        modern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArchitectCategoryActivity.this,  ArchitectAddNewProduct.class);
                intent.putExtra("category","Modern");
                startActivity(intent);
            }
        });
    }
}