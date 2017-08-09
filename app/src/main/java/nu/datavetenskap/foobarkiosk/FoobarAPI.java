package nu.datavetenskap.foobarkiosk;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import nu.datavetenskap.foobarkiosk.preferences.ThunderClientDialogPreference;

public class FoobarAPI {
    private static final String STATE = "state/";

    private static Boolean completeSingleton = false;
    private static RequestQueue queue;
    private static String API_HOST;
    private static String API_AUTH;
    private static String THUNDER_URL;
    private static String THUNDER_SECRET;

    public static void startSingleton(Context context) {
        // Instantiate a RequestQueue from the Volley Singleton
        queue = VolleySingleton.getInstance(context).getRequestQueue();
        // Fill all static fields
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        API_HOST = pref.getString(context.getString(R.string.pref_key_fooapi_host), "");
        API_AUTH = pref.getString(context.getString(R.string.pref_key_fooapi_auth_token), "");
        String thunderHost = pref.getString(ThunderClientDialogPreference.PREF_IP, "");
        String thunderPublic = pref.getString(ThunderClientDialogPreference.PREF_PUBLIC, "");
        THUNDER_URL = thunderHost + "/api/1.0.0/" + thunderPublic + "/channels/";
        THUNDER_SECRET = pref.getString(ThunderClientDialogPreference.PREF_SECRET, "");

        completeSingleton = true;
    }


    public static void getProducts(Response.Listener<String> onResponse) {
        checkCompleteness();
        sendAPIRequest("/api/products/", onResponse);
    }

    public static void getProductFromBarcode(String barcode, Response.Listener<String> onResponse) {
        checkCompleteness();
        sendAPIRequest("/api/products/?code=" + barcode, onResponse);
    }

    public static void getAccountFromCard(String card, Response.Listener<String> stringRequest) {
        checkCompleteness();
        sendAPIRequest("/api/accounts/" + card + "/", stringRequest);
    }

    public static void sendStateToThunderpush(String state) {
        checkCompleteness();
        sendThunderMsgChannel(STATE, state);

    }







    private static void sendAPIRequest(String apiPath, Response.Listener<String> onResponse) {
        final String url = API_HOST + apiPath;

        StringRequest stringReq = new StringRequest(Request.Method.GET,
                url, onResponse,
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
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", API_AUTH);
                return headers;
            }
        };
        queue.add(stringReq);
    }

    private static void sendThunderMsgChannel(String channel, final String Msg) {
        Log.d("FoobarAPI", "Send to thunderpush: " + Msg);
        StringRequest stringReq = new StringRequest(Request.Method.POST,
                THUNDER_URL + channel,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FoobarAPI", "Thunderpush response: " + response);
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
                headers.put("X-Thunder-Secret-Key", THUNDER_SECRET);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                try {
                    return Msg.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", Msg, "utf-8");
                    return null;

                }
            }
        };
        queue.add(stringReq);
    }


    private static void checkCompleteness() {
        if (!completeSingleton) {
            throw new RuntimeException("Must run method startSingleton before calling methods from FoobarAPI");
        }
    }
}
