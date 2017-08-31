package nu.datavetenskap.foobarkiosk.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import nu.datavetenskap.foobarkiosk.R;
import nu.datavetenskap.foobarkiosk.VolleySingleton;
import nu.datavetenskap.foobarkiosk.models.StoreEntity;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
    private ArrayList<StoreEntity> entities;
    private ImageLoader mImageLoader;
    private String URL;
    private static ClickListener clickListener;

    public StoreAdapter(Context context, ArrayList<StoreEntity> productArrayList, String apiUrl) {
        mImageLoader = VolleySingleton.getInstance(context.getApplicationContext()).getImageLoader();
        entities = productArrayList;
        URL  = apiUrl;
    }

    public interface ClickListener {
        void onItemClick(int position, View view);
    }
    public static void setOnItemClickListener(ClickListener clickListener) {
        StoreAdapter.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_grid_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final StoreEntity p = entities.get(position);

        holder._name.setText(p.getName());
        holder._img.setDefaultImageResId(R.drawable.icon_product);
        String imgURL = p.getImage();
        if (imgURL != null) {
            holder._img.setImageUrl(URL + imgURL, mImageLoader);
        }
        else {
            holder._img.setImageUrl(null, mImageLoader);
        }

    }


    @Override
    public int getItemCount() {
        return entities.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.product_image) NetworkImageView _img;
        @Bind(R.id.product_name) TextView _name;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}
