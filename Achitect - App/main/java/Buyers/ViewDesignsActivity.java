package Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.architect.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import Model.Products;
import ViewHolder.ProductViewHolder;

public class ViewDesignsActivity extends AppCompatActivity {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_designs);



ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");


recyclerView=findViewById(R.id.recycler_menu);
recyclerView.setHasFixedSize(true);
layoutManager = new LinearLayoutManager(this);
recyclerView.setLayoutManager(layoutManager);


    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef, Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                        productViewHolder.txtProductName.setText(products.getPname());
                        productViewHolder.txtProductPrice.setText("Price="+products.getPrice()+"Ksh");
                        productViewHolder.txtProductDescription.setText(products.getDescription());
                        productViewHolder.txtProductSeller.setText(products.getSeller());
                        productViewHolder.txtArchitectLocationLink.setText(products.getLocation());
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);

                        productViewHolder.txtArchitectLocationLink.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(products.getLocation()));
                                startActivity(browserIntent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        return new ProductViewHolder(view);
                    }
                };

recyclerView.setAdapter(adapter);
adapter.startListening();

    }
}
