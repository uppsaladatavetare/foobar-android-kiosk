package nu.datavetenskap.foobarkiosk.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import nu.datavetenskap.foobarkiosk.R;
import nu.datavetenskap.foobarkiosk.models.Product;



public class ProductGridAdapter extends ArrayAdapter<Product> {

    private Context mContext;
    private ArrayList<Product> mProducts;

    public ProductGridAdapter(Context c, ArrayList<Product> products) {
        super(c, R.layout.product_grid_layout, products);
        mContext = c;
        mProducts = products;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//        The ViewHolder holds all references to elements in the chosen layout
        ViewHolder holder;
        Product product = getItem(position);

        if(convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.product_grid_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }


        holder._name.setText(product.getName());


        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.product_image) ImageView _img;
        @Bind(R.id.product_name) TextView _name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
