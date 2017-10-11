package nu.datavetenskap.foobarkiosk.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import nu.datavetenskap.foobarkiosk.FoobarAPI;
import nu.datavetenskap.foobarkiosk.R;
import nu.datavetenskap.foobarkiosk.adapters.CartAdapter;
import nu.datavetenskap.foobarkiosk.models.IAccount;
import nu.datavetenskap.foobarkiosk.models.IProduct;
import nu.datavetenskap.foobarkiosk.models.Product;
import nu.datavetenskap.foobarkiosk.models.statemodels.IState;
import nu.datavetenskap.foobarkiosk.models.statemodels.PurchaseState;
import nu.datavetenskap.foobarkiosk.preferences.ThunderClientDialogPreference;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link OnCartInteractionListener} interface
 * to handle interaction events.
 */
public class CartFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "CartFragment";


    private OnCartInteractionListener mListener;
    private SharedPreferences preferences;
    private IState activeState;
    private String purchaseStateCache;


    @Bind(R.id.cart_product_list) RecyclerView _cart;
    @Bind(R.id.cart_decrease_btn) ImageButton _decBtn;
    @Bind(R.id.cart_increase_btn) ImageButton _incBtn;
    @Bind(R.id.cart_delete_btn) ImageButton _delBtn;
    @Bind(R.id.cart_clear_btn) ImageButton _clearBtn;
    @Bind(R.id.initiate_purchase_button) Button _purchaseBtn;
    AccountFragment _acc;
    ArrayList<IProduct> productList;
    LinearLayoutManager mLinearLayoutManager;
    CartAdapter cartAdapter;
    WebView _web;


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        final String host = preferences.getString(ThunderClientDialogPreference.PREF_IP, "");
        final String clientKey = preferences.getString(ThunderClientDialogPreference.PREF_PUBLIC, "");
        _web.loadDataWithBaseURL("file:///android_asset/", webCode(host, clientKey), "text/html", "UTF-8", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart_list, container, false);

        ButterKnife.bind(this, view);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        _web = new WebView(getContext().getApplicationContext());
        WebView.setWebContentsDebuggingEnabled(true);


        WebSettings webSettings = _web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        _web.addJavascriptInterface(new WebAppInterface(getContext().getApplicationContext()), "Android");
        activeState = new IState();

        mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        _cart.setLayoutManager(mLinearLayoutManager);
        productList =activeState.getProducts();
        cartAdapter = new CartAdapter(getContext(), productList,
                preferences.getString(getString(R.string.pref_key_fooapi_host), ""));
        _cart.setAdapter(cartAdapter);

        _incBtn.setOnClickListener(this);
        _decBtn.setOnClickListener(this);
        _delBtn.setOnClickListener(this);
        _clearBtn.setOnClickListener(this);
        buttonsSetEnable(false);

        _purchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runPurchase();
            }
        });


        FoobarAPI.sendStateToThunderpush(activeState);

        return view;
    }

    private void runPurchase() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.
        FragmentManager fm = getFragmentManager();

        // Create and show the dialog.
        PurchaseDialogFragment newFragment = PurchaseDialogFragment.newInstance(activeState);
        newFragment.show(fm, "dialog");

    }

    public IAccount retrieveAccountFromCard(final String card) {
        final IAccount[] account = {null};
        FoobarAPI.getAccountFromCard(card, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("StringRequest", response);
                Gson gson = new Gson();
                account[0] = gson.fromJson(response, IAccount.class);

                account[0].setCardId(card);
                updateState(account[0]);
            }
        });
        return account[0];
    }

    private void addAccountFragment(IAccount account) {
        _acc = AccountFragment.newInstance(account);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_top, R.anim.slide_out_to_top);
        ft.add(R.id.account_fragment, _acc).commit();
    }


    public void addProductToCart(Product prod) {
        Log.d(TAG, "Add product to cart");
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).equals(prod)) {
                productList.get(i).incrementAmount();
                cartAdapter.notifyItemChanged(i);
                FoobarAPI.sendStateToThunderpush(activeState);
                return;
            }
        }
        productList.add(new IProduct(prod));
        cartAdapter.notifyItemInserted(productList.size());
        if (activeState.getPurchaseState().equals(PurchaseState.WAITING)) {
            activeState.setPurchaseState(PurchaseState.ONGOING);
            buttonsSetEnable(true);
        }

        FoobarAPI.sendStateToThunderpush(activeState);

    }

    private void updateState(IAccount account) {
        if (activeState.getAccount() == null) {
            activeState.setAccount(account);
            addAccountFragment(account);
            if (activeState.getPurchaseState().equals(PurchaseState.WAITING)) {
                activeState.setPurchaseState(PurchaseState.ONGOING);
                buttonsSetEnable(true);
            }
            FoobarAPI.sendStateToThunderpush(activeState);
        }
    }

    private void buttonsSetEnable(Boolean b) {
        _clearBtn.setEnabled(b);
        _incBtn.setEnabled(b);
        _decBtn.setEnabled(b);
        _delBtn.setEnabled(b);
    }

    private void updateState() {
        Log.d("CartFragment", "State updated");


    }





    private String webCode(String host, String clientKey) {
        return "<script src=\"https://cdn.jsdelivr.net/sockjs/1/sockjs.min.js\"></script>" +
                "<script src=\"file:///android_asset/thunderpush.js\"></script>" +
                "<script> Thunder.connect('" + host + "', '" + clientKey + "', ['products', 'cards', 'state'], {log: true}); \n" +
                    "Thunder.listen(function(data) { " +
                        "if (data.channel === \"state\") Android.parseNewState(data.payload);" +
                        "if (data.channel === \"cards\") Android.parseNewCard(data.payload);" +
                        "if (data.channel === \"products\") Android.parseNewProduct(data.payload); }); </script>";
    }


    public void sendProfileState() {
        purchaseStateCache = activeState.getPurchaseState();
        activeState.setPurchaseState(PurchaseState.PROFILE);
        FoobarAPI.sendStateToThunderpush(activeState);
    }


    public void returnFromProfile() {
        activeState.setPurchaseState(purchaseStateCache);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cart_clear_btn) {
            clearCart();
            return;
        }
        for (IProduct p : productList) {
            if (!p.getSelected()) {continue;}

            switch (v.getId()) {
                case R.id.cart_decrease_btn:
                    if (p.getQty() <= 1) {
                        //p.setSelected(false);
                        productList.remove(p);
                    }
                    else {p.decrementAmount();}
                    break;
                case R.id.cart_increase_btn:
                    p.incrementAmount();
                    break;
                case R.id.cart_delete_btn:
                    //p.setSelected(false);
                    productList.remove(p);
                    break;
                default:
                    break;
            }
        }
        cartAdapter.notifyDataSetChanged();
        FoobarAPI.sendStateToThunderpush(activeState);
    }

    public void clearCart() {
        if (_acc != null) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_from_top, R.anim.slide_out_to_top);
            ft.remove(_acc).commit();
            _acc = null;
        }
        buttonsSetEnable(false);
        activeState.clearState();
        cartAdapter.notifyDataSetChanged();
        FoobarAPI.sendStateToThunderpush(activeState);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCartInteractionListener {
        // TODO: Update argument type and name
        void onCartInteraction(Uri uri);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCartInteractionListener) {
            mListener = (OnCartInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCartInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    private class WebAppInterface {

        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }
        /** Show a toast from the web page */
        @JavascriptInterface
        public void parseNewState(final String data) {
//            Log.d(TAG, "New State: " + data);
//            Gson gson = new Gson();
//            final IState iState = gson.fromJson(data, IState.class);
//
//            PurchaseDialogFragment frag = (PurchaseDialogFragment) getActivity().getFragmentManager().findFragmentByTag("dialog");
//            if (frag != null) {
//                frag.updateAccessRights(iState.getAccount());
//            }

        }

        @JavascriptInterface
        public void parseNewCard(final String data){
            IAccount account = retrieveAccountFromCard(data);

            PurchaseDialogFragment frag = (PurchaseDialogFragment) getFragmentManager().findFragmentByTag("dialog");
            if (frag != null) {
                frag.updateAccessRights(account);
            }
        }

        @JavascriptInterface
        public void parseNewProduct(final String data){
            FoobarAPI.getProductFromBarcode(data, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("StringRequest", response);
                    Gson gson = new Gson();
                    Product p = gson.fromJson(response, Product.class);

                    addProductToCart(p);
                }
            });
        }
    }
}
