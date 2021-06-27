package ViewHolder;

import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.architect.R;

import Interface.itemClickListner;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

public TextView txtProductName, txtProductDescription, txtProductPrice,txtProductSeller,txtArchitectLocationLink;
public ImageView imageView;
public itemClickListner listner;

  public ProductViewHolder(View itemView)
  {
      super(itemView);
    imageView = (ImageView) itemView.findViewById(R.id.product_image);
    txtProductName = (TextView) itemView.findViewById(R.id.product_name);
    txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
      txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
      txtProductSeller = (TextView) itemView.findViewById(R.id.product_seller_view);
    txtArchitectLocationLink = (TextView) itemView.findViewById(R.id.product_seller_location);


  }


  public void setListner(itemClickListner listner) {
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {

    listner.onClick(view, getAdapterPosition(),false);

    }
}
