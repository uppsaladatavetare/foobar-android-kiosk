package nu.datavetenskap.foobarkiosk;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import nu.datavetenskap.foobarkiosk.Settings.BuildConfig;
import nu.datavetenskap.foobarkiosk.adapters.ProductGridAdapter;
import nu.datavetenskap.foobarkiosk.models.Product;

/**
 * A placeholder fragment containing a simple view.
 */
public class StoreFragment extends Fragment {

    ProductGridAdapter productGrid;
    RequestQueue queue;

    @Bind(R.id.btn_click) Button _btn;
    @Bind(R.id.grid_view) GridView _grid;

    public StoreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        ButterKnife.bind(this, view);

        productGrid = new ProductGridAdapter(this.getContext(), new ArrayList<Product>());

        _grid.setAdapter(productGrid);




        // Instantiate a RequestQueue from the Volley Singleton
        queue = VolleySingleton.getInstance(this.getContext().getApplicationContext()).getRequestQueue();
        final String url = "http://10.0.2.2:8000/api/products/";

        _btn.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //updateState();

                StringRequest stringReq = new StringRequest(Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                addAllProducts(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("VolleyError", error.toString());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", getActivity().getString(R.string.foobar_api_token));
                        return headers;
                    }
                };
                queue.add(stringReq);

            }
        });


        return view;
    }

    void addAllProducts(String str) {

        Gson gson = new Gson();
        Product[] products = gson.fromJson(str, Product[].class);
        productGrid.addAll(products);

        productGrid.notifyDataSetChanged();
    }

    private void updateState() {

        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... arg0) {

                String[] stateTest = {"{\"state\":{\"account\":{},\"products\":{\"products\":[{\"code\":\"7310500088853\",\"selected\":false,\"loading\":false,\"id\":\"e963428a-d719-422b-8d6e-5c062fe822e3\",\"name\":\"Bilys pizza\",\"qty\":1,\"price\":13,\"image\":\"http://localhost:7331/localhost:8000/media/product/7310500088853.png\"},{\"code\":\"7611612221351\",\"selected\":false,\"loading\":false,\"id\":\"4cd81a41-4e13-4b63-b833-da59dfe0faeb\",\"name\":\"Pepsi Max Ginger\",\"qty\":1,\"price\":7,\"image\":\"http://localhost:7331/localhost:8000null\"},{\"code\":\"7340083438684\",\"selected\":false,\"loading\":false,\"id\":\"7f5b3961-3654-40ad-9cea-fe0f35eb926c\",\"name\":\"Delicatoboll\",\"qty\":1,\"price\":6,\"image\":\"http://localhost:7331/localhost:8000/media/product/7340083438684.png\"}],\"page\":0,\"maxPage\":0},\"purchase\":{\"state\":\"ONGOING\"}}}"};

                ThunderClient tc = new ThunderClient("192.168.1.21", 8080, getString(R.string.thunderpush_client_key), getString(R.string.thunderpush_secret_key));

                try {
                    tc.sendMessageToChannel("state", stateTest);
                } catch (IOException e) {
                    Log.e("StateUpdater", "Update state failed");
                    e.printStackTrace();
                }



                return null;
            }


        };
        connectionThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void addProductToCart() {





        StringRequest stringReq = new StringRequest(Request.Method.GET,
                "http://10.0.2.2:8000/api/products/?code=7310500088853",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("StringRequest", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", getActivity().getString(R.string.foobar_api_token));
                return headers;
            }
        };
        queue.add(stringReq);




//        CartFragment cartFrag = (CartFragment)
//                getChildFragmentManager().findFragmentById(R.id.store_sidebar);
//        if (cartFrag != null ) {
//            cartFrag.addProductToCart();
//        }
//        else {
//            throw new RuntimeException(
//                    "Removed CartFragment from layout but not redirected method" );
//        }


    }


}
