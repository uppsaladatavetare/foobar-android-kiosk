package nu.datavetenskap.foobarkiosk.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import nu.datavetenskap.foobarkiosk.R;


public class ThunderClientDialogPreference extends DialogPreference {
    private static final String TAG = "ThunderclientPrefs";
    public static final String PREF_IP = "thunder_ip";
    public static final String PREF_PORT = "thunder_port";
    public static final String PREF_PUBLIC = "thunder_public_key";
    public static final String PREF_SECRET = "thunder_secret_key";

    private EditText ipText, portText, publicText, secretText;

    public ThunderClientDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.dialog_thunderclient_credentials);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        Log.d(TAG,"onBindDialogView");

        SharedPreferences sp = getSharedPreferences();
        ipText.setText(sp.getString(PREF_IP, "http://"));
        final Integer port = sp.getInt(PREF_PORT, 0);
        portText.setText(port.toString());
        publicText.setText(sp.getString(PREF_PUBLIC, ""));
        secretText.setText(sp.getString(PREF_SECRET, ""));
    }

    @Override
    protected View onCreateDialogView() {
        // Guide for this technique found at:
        // http://alexfu.tumblr.com/post/23683149440/android-dev-custom-dialogpreference
        Log.d(TAG,"onCreateDialogView");
        View root = super.onCreateDialogView();
        ipText = (EditText) root.findViewById(R.id.thunder_ip);
        portText = (EditText) root.findViewById(R.id.thunder_port);
        publicText = (EditText) root.findViewById(R.id.thunder_public);
        secretText = (EditText) root.findViewById(R.id.thunder_secret);
        return root;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult){
            Log.d(TAG,"Clicked Save");
            SharedPreferences sp = getSharedPreferences();
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(PREF_IP, ipText.getText().toString());
            editor.putInt(PREF_PORT, Integer.valueOf(portText.getText().toString()));
            editor.putString(PREF_PUBLIC, publicText.getText().toString());
            editor.putString(PREF_SECRET, secretText.getText().toString());
            editor.apply();
        }
        else {
            Log.d(TAG,"Clicked Cancel");
        }
    }

}