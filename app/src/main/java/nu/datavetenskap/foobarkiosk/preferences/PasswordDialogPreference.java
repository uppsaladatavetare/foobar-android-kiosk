package nu.datavetenskap.foobarkiosk.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import nu.datavetenskap.foobarkiosk.R;


public class PasswordDialogPreference extends DialogPreference {
    static final String TAG = "HostCredentialsPrefs";
    private static String passSetSummary  = "Settings will now require password";
    private static String passSummary = "No password set";
    EditText pass1, pass2;
    private String strPass1, strPass2;
    TextView error;
    private Boolean passedCriteria = false;
    private Boolean passwordSet;
    private Button positiveButton;

    public PasswordDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.dialog_settings_credentials);
    }



    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        Log.d(TAG,"onBindDialogView");





        pass2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                strPass1 = pass1.getText().toString();
                strPass2 = pass2.getText().toString();
                passedCriteria = strPass1.equals(strPass2);

                if (passedCriteria) {

                    error.setText(R.string.dialog_pass_match);
                    positiveButton.setText("OK");


                } else {

                    error.setText(R.string.dialog_pass_not_match);
                    positiveButton.setText("Clear");
                }
            }  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                AlertDialog aDlg = (AlertDialog) getDialog();
                positiveButton = aDlg.getButton(AlertDialog.BUTTON_POSITIVE);
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setNegativeButton(null, null);
    }

    @Override
    protected View onCreateDialogView() {
        // Guide for this technique found at:
        // http://alexfu.tumblr.com/post/23683149440/android-dev-custom-dialogpreference
        Log.d(TAG,"onCreateDialogView");
        View root = super.onCreateDialogView();
        pass1 = (EditText) root.findViewById(R.id.thunder_public);
        pass2 = (EditText) root.findViewById(R.id.thunder_secret);
        error = (TextView) root.findViewById(R.id.TextView_PwdProblem);
        positiveButton = (Button) root.findViewById(AlertDialog.BUTTON_POSITIVE);

        return root;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        passwordSet = false;

        final String chosenPass = pass2.getText().toString();
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();
        if (positiveResult){
            Context c = getContext().getApplicationContext();
            if (passedCriteria) {
                Log.d(TAG,"Clicked Save");
                passwordSet = true;
                editor.putString("password", chosenPass);

            }
            else {
                Log.d(TAG,"Clicked Clear");
                editor.remove("password");
            }
            editor.apply();
        }
        else {
            Log.d(TAG,"Clicked Cancel");
        }
        callChangeListener(getSummaryText());
    }

    public String getSummaryText() {
        if (passwordSet) {
            return passSetSummary;
        }
        return passSummary;
    }

}