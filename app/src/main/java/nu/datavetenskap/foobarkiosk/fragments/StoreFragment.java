package nu.datavetenskap.foobarkiosk.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import nu.datavetenskap.foobarkiosk.FoobarAPI;
import nu.datavetenskap.foobarkiosk.R;
import nu.datavetenskap.foobarkiosk.adapters.StoreAdapter;
import nu.datavetenskap.foobarkiosk.models.Product;

/**
 * A placeholder fragment containing a simple view.
 */
public class StoreFragment extends Fragment {
    private static final int PRODUCT_COLUMNS = 4;

    CartFragment cartFragment;

    ArrayList<Product> storeProductList;
    StoreAdapter storeAdapter;
    GridLayoutManager mgridLayoutManager;

    @Bind(R.id.btn_get_products) Button _btnProducts;
    @Bind(R.id.btn_send_state) Button _btnState;
    @Bind(R.id.btn_get_card) Button _btncard;
    @Bind(R.id.grid_view) RecyclerView _grid;
    @Bind(R.id.btn_get_random) Button _btnRand;

    public StoreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        ButterKnife.bind(this, view);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());


        storeProductList = new ArrayList<>();

        mgridLayoutManager = new GridLayoutManager(getActivity(), PRODUCT_COLUMNS);
        _grid.setLayoutManager(mgridLayoutManager);
        storeAdapter = new StoreAdapter(getContext(), storeProductList,
                pref.getString(getString(R.string.pref_key_fooapi_host), ""));
        storeAdapter.setOnItemClickListener(new StoreAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                cartFragment.addProductToCart(storeProductList.get(position));
            }
        });
        _grid.setAdapter(storeAdapter);

        cartFragment = (CartFragment) getChildFragmentManager().findFragmentById(R.id.store_sidebar);


        _btnProducts.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getProductList();
                //retrieveProductFromBarcode("7310500088853");
                //retrieveAccountFromCard("1337");

            }
        });

        _btnState.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //updateStateWithThunderClient();
                //updateStateWithVolley();
                FoobarAPI.sendThunderMsgChannel("products/", "7340083438684");

            }
        });
        _btncard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartFragment.retrieveAccountFromCard("1337");
            }
        });

        _btnRand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] text = {"7340083438684", "7611612221351", "7310500088853", "1234567890"};
                Random r = new Random();
                int i = r.nextInt(text.length);

                cartFragment.retrieveProductFromBarcode(text[i]);
            }
        });

        return view;
    }



    private void addAllProducts(String str) {

        Gson gson = new Gson();
        Product[] products = gson.fromJson(str, Product[].class);

        for (Product product : products) {
            storeProductList.add(product);
            storeAdapter.notifyItemInserted(storeProductList.size());

        }

    }


    private void getProductList() {
        FoobarAPI.getProducts(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                addAllProducts(response);
            }
        });
    }


}
