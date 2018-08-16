package nu.datavetenskap.foobarkiosk.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nu.datavetenskap.foobarkiosk.R;
import nu.datavetenskap.foobarkiosk.VolleySingleton;
import nu.datavetenskap.foobarkiosk.models.Product;



public class ProductGridAdapter extends ArrayAdapter<Product> {

    private Context mContext;
    private ImageLoader mImageLoader;
    private String URL;

    public ProductGridAdapter(Context c, ArrayList<Product> products, String apiURL) {
        super(c, R.layout.product_grid_layout, products);
        mContext = c;
        mImageLoader = VolleySingleton.getInstance(c.getApplicationContext()).getImageLoader();
        URL  = apiURL;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//        The ViewHolder holds all references to elements in the chosen layout
        ViewHolder holder;
        Product product = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.product_grid_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            holder._name.setText(product.getName());
            String imgURL = product.getImage();
            holder._img.setDefaultImageResId(R.drawable.icon_product);
            if (imgURL != null) {
                holder._img.setImageUrl(URL + imgURL, mImageLoader);
            }
        }

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.product_image) NetworkImageView _img;
        @BindView(R.id.product_name) TextView _name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
