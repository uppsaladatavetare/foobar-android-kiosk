package nu.datavetenskap.foobarkiosk;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import nu.datavetenskap.foobarkiosk.models.IProduct;
import nu.datavetenskap.foobarkiosk.models.statemodels.IState;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CartFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    @Bind(R.id.sidebar_text_view) TextView _txt;
    @Bind(R.id.web_viewer) WebView _web;

    public CartFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart_list, container, false);

        ButterKnife.bind(this, view);


        WebView.setWebContentsDebuggingEnabled(true);


        _web.loadDataWithBaseURL("file:///android_asset/", webCode(), "text/html", "UTF-8", null);

        WebSettings webSettings = _web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        _web.addJavascriptInterface(new WebAppInterface(getContext().getApplicationContext()), "Android");

        _txt.setText("Ready for input");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
        //mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    private String webCode() {
        Activity a = getActivity();
        return "<script src=\"https://cdn.jsdelivr.net/sockjs/1/sockjs.min.js\"></script>" +
                "<script src=\"file:///android_asset/thunderpush.js\"></script>" +
                "<script> Thunder.connect('" + a.getString(R.string.thunderpush_host) + "', '" + a.getString(R.string.thunderpush_client_key) + "', ['products', 'cards', 'state'], {log: true}); \n" +
                "Thunder.listen(function(message) { Android.parseData(JSON.stringify(message)); }); </script>";
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void parseData(final String data) {
            //                final JSONObject json = new JSONObject(data);
//                final String str = json.toString(2);

            Log.d("Statemachine", data);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Gson gson = new Gson();
                    IState state = gson.fromJson(data, IState.class);
                    //Log.d("Statemachine", state.toString());
                    ArrayList<IProduct> products = state.getProducts();

                    String string = "";
                    for (IProduct p : products) {
                        string += p.getName() + "\n";
                    }


                    _txt.setText(string);
                }
            });


            Toast.makeText(mContext, "Data received", Toast.LENGTH_SHORT).show();
        }
    }
}
