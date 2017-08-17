package nu.datavetenskap.foobarkiosk.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import nu.datavetenskap.foobarkiosk.R;
import nu.datavetenskap.foobarkiosk.VolleySingleton;
import nu.datavetenskap.foobarkiosk.models.IProduct;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<IProduct> products;
    private ImageLoader mImageLoader;
    private String URL;

    public CartAdapter(Context context, ArrayList<IProduct> productArrayList, String apiUrl) {
        mImageLoader = VolleySingleton.getInstance(context.getApplicationContext()).getImageLoader();
        products = productArrayList;
        URL  = apiUrl;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final IProduct p = products.get(position);

        holder._name.setText(p.getName());
        holder._amount.setText(String.format("%sx", p.getQty()));
        holder._cost.setText(String.format("%s kr", p.getPrice()));
        holder._img.setDefaultImageResId(R.drawable.icon_product);
        holder.product = p;
        String imgURL = p.getImage();
        if (imgURL != null) {
            holder._img.setImageUrl(URL + imgURL, mImageLoader);
        }
        if (!holder.product.getSelected()) {
            holder._row.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        else {
            holder._row.setBackgroundColor(Color.parseColor("#75aaff"));
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.product_cart_row) LinearLayout _row;
        @Bind(R.id.product_cart_image) NetworkImageView _img;
        @Bind(R.id.product_cart_amount) TextView _amount;
        @Bind(R.id.product_cart_name) TextView _name;
        @Bind(R.id.product_cart_cost) TextView _cost;
        IProduct product;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!");

            if (product.getSelected()) {
                product.setSelected(false);
                _row.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            else {
                product.setSelected(true);
                _row.setBackgroundColor(Color.parseColor("#75aaff"));
            }
        }
    }
}
