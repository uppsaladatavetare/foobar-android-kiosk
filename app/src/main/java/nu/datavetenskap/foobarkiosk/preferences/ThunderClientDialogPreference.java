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
    private static final String HOST_PREF = "thunder_host";
    private static final String PUBLIC_KEY_PREF = "thunder_public_key";
    private static final String SECRET_KEY_PREF = "thunder_secret_key";

    private EditText hostText, publicText, secretText;

    public ThunderClientDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.dialog_thunderclient_credentials);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        Log.d(TAG,"onBindDialogView");

        SharedPreferences sp = getSharedPreferences();
        hostText.setText(sp.getString(HOST_PREF, ""));
        publicText.setText(sp.getString(PUBLIC_KEY_PREF, ""));
        secretText.setText(sp.getString(SECRET_KEY_PREF, ""));
    }

    @Override
    protected View onCreateDialogView() {
        // Guide for this technique found at:
        // http://alexfu.tumblr.com/post/23683149440/android-dev-custom-dialogpreference
        Log.d(TAG,"onCreateDialogView");
        View root = super.onCreateDialogView();
        hostText = (EditText) root.findViewById(R.id.thunder_host);
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
            editor.putString(HOST_PREF, hostText.getText().toString());
            editor.putString(PUBLIC_KEY_PREF, publicText.getText().toString());
            editor.putString(SECRET_KEY_PREF, secretText.getText().toString());
            editor.apply();
        }
        else {
            Log.d(TAG,"Clicked Cancel");
        }
    }

}