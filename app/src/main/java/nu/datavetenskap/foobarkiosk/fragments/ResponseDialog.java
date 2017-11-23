package nu.datavetenskap.foobarkiosk.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import nu.datavetenskap.foobarkiosk.R;
import nu.datavetenskap.foobarkiosk.models.IAccount;


public class ResponseDialog extends DialogFragment implements View.OnClickListener {

    @Bind(R.id.response_dialog_spinner) ProgressBar _spinner;
    @Bind(R.id.response_dialog_text) TextView _text;
    @Bind(R.id.response_dialog_cancelBtn) Button _cancelBtn;
    @Bind(R.id.response_dialog_image) ImageView _img;

    private boolean dismissible = false;
    private Handler dismissalHandler;
    private Runnable dismissalRunner = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_response, container, false);
        ButterKnife.bind(this, view);

        _cancelBtn.setOnClickListener(this);
        dismissalHandler = new Handler();

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        dismiss();

    }

    @Override
    public void dismiss() {
        dismissalHandler.removeCallbacks(dismissalRunner);
        Log.e("ResponseDialog", "Dismissal of dialog");
        super.dismiss();
    }

    public boolean isDismissible() {return dismissible;}

    public void onSuccess(final float p) { onSuccess(p, null); }

    public void onSuccess(final float purchaseCost, final IAccount account) {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                _spinner.setVisibility(View.GONE);
                _img.setVisibility(View.VISIBLE);
                _text.setText("Congratulations!\n Your purchase was " + purchaseCost + " kr.");

                if (account != null) {
                    _text.append("\nYou have "+ (account.getBalance() - purchaseCost) +" kr left.");
                }
                _text.setVisibility(View.VISIBLE);
                _cancelBtn.setVisibility(View.VISIBLE);

                dismissible = true;

                dismissalHandler.postDelayed(dismissalRunner, 4000);

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        CartFragment fragment = (CartFragment) getFragmentManager().findFragmentById(R.id.store_sidebar);
//                        if (fragment != null) {
//                            fragment.retrieveProductFromBarcode("7310500088853");
//                        }
//                    }
//                }, 2000);


            }
        }, 1500);
    }
}
