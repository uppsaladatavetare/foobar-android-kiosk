package nu.datavetenskap.foobarkiosk;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import nu.datavetenskap.foobarkiosk.preferences.ThunderClientDialogPreference;

public class ThunderListener {
    private static final String TAG = "ThunderListener";
    private static ThunderListener mInstance;
    private ThunderListenerInteraction mListener;
    private SharedPreferences preferences;
    private WebView _web;

    private ThunderListener(Context context) {
        Log.d(TAG, "Entered Constructor");
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        if (context instanceof ThunderListenerInteraction) {
            mListener = (ThunderListenerInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ThunderListenerInteraction");
        }

        _web = new WebView(context.getApplicationContext());
        WebView.setWebContentsDebuggingEnabled(true);


        WebSettings webSettings = _web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        _web.addJavascriptInterface(new ThunderListener.WebAppInterface(context.getApplicationContext()), "Android");




        final String host = preferences.getString(ThunderClientDialogPreference.PREF_IP, "");
        final String clientKey = preferences.getString(ThunderClientDialogPreference.PREF_PUBLIC, "");
        _web.loadDataWithBaseURL("file:///android_asset/", webCode(host, clientKey), "text/html", "UTF-8", null);
    }

    public static synchronized ThunderListener getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ThunderListener(context);
        }
        return mInstance;
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

    public static void Destroy() {
        mInstance._web.clearCache(true);
        mInstance._web.clearHistory();
        mInstance._web.destroy();
        mInstance = null;
    }

    public interface ThunderListenerInteraction {
        void retrieveAccountFromCard(String data);
        void retrieveProductFromBarcode(String data);
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
        public void parseNewCard(final String data) {
            Log.d(TAG, "parsing card: " + data);
            mListener.retrieveAccountFromCard(data);
        }

        @JavascriptInterface
        public void parseNewProduct(final String data){
            Log.d(TAG, "parsing product: " + data);
            mListener.retrieveProductFromBarcode(data);
        }
    }
}
