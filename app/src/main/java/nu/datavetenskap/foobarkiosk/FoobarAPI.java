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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import nu.datavetenskap.foobarkiosk.models.IProduct;
import nu.datavetenskap.foobarkiosk.models.statemodels.IState;
import nu.datavetenskap.foobarkiosk.preferences.ThunderClientDialogPreference;

public class FoobarAPI {
    private static final String STATE = "state/";

    private static Boolean completeSingleton = false;
    private static RequestQueue queue;
    private static String API_HOST;
    private static String API_AUTH;
    private static String THUNDER_URL;
    private static String THUNDER_SECRET;
    private static Gson gson;

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
        gson = new Gson();

        completeSingleton = true;
    }


    public static void getProducts(Response.Listener<String> onResponse) {
        checkCompleteness();
        sendAPIRequest("/api/products/", Request.Method.GET, onResponse);
    }

    public static void getProductFromBarcode(String barcode, Response.Listener<String> onResponse) {
        checkCompleteness();
        sendAPIRequest("/api/products/?code=" + barcode, Request.Method.GET, onResponse);
    }

    public static void getAccountFromCard(String card, Response.Listener<String> stringRequest) {
        checkCompleteness();
        sendAPIRequest("/api/accounts/" + card + "/", Request.Method.GET, stringRequest);
    }

    public static void sendCashPurchaseRequest(IState state, Response.Listener<String> onResponse) {
        checkCompleteness();
        sendPurchaseRequest(
                constructPurchaseRequest(state, false), onResponse);
    }

    public static void sendFooCashPurchaseRequest(IState state, Response.Listener<String> onResponse) {
        checkCompleteness();
        sendPurchaseRequest(
                constructPurchaseRequest(state, true), onResponse);
    }

    private static void sendPurchaseRequest(String purchaseBody, Response.Listener<String> responseListener) {
        Log.d("FoobarAPI", "Body: " + purchaseBody);
        sendAPIRequest("/api/purchases/", Request.Method.POST, purchaseBody, responseListener);
    }

    private static String constructPurchaseRequest(IState state, Boolean includeAccountID) {
        try {

            JSONObject json = new JSONObject();
            if (includeAccountID) {
                json.put("account_id", state.getAccount().getId());
            }
            else {
                json.put("account_id", JSONObject.NULL);
            }
            JSONArray arr = new JSONArray();
            for (IProduct p : state.getProducts()) {
                if (p.isReady()) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", p.getId());
                    obj.put("qty", p.getQty());
                    arr.put(obj);
                }
            }
            json.put("products", arr);

            Log.d("FoobarAPI", json.toString());
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendStateToThunderpush(IState state) {
        checkCompleteness();
        sendThunderMsgChannel(STATE, gson.toJson(state));

    }

    public static void sendBarcodeToThunderpush(final String str) {
        sendThunderMsgChannel("products/",str);
    }
    public static void sendFooCardToThunderpush(final String str) {
        sendThunderMsgChannel("cards/",str);
    }







    private static void sendAPIRequest(String apiPath, int requestMethod, Response.Listener<String> onResponse) {
        final String url = API_HOST + apiPath;

        StringRequest stringReq = new StringRequest(requestMethod,
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

    private static void sendAPIRequest(String apiPath, int requestMethod, final String jsonBody, Response.Listener<String> onResponse) {
        final String url = API_HOST + apiPath;

        StringRequest stringReq = new StringRequest(requestMethod,
                url, onResponse,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String s = new String(error.networkResponse.data, "UTF-8");
                            Log.e("VolleyError", s);
                        } catch (UnsupportedEncodingException ignore) {}
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", API_AUTH);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonBody, "utf-8");
                    return null;

                }
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
