package nu.datavetenskap.foobarkiosk.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import nu.datavetenskap.foobarkiosk.R;


public class ResponseDialog extends DialogFragment implements View.OnClickListener {

    @Bind(R.id.response_dialog_spinner) ProgressBar _spinner;
    @Bind(R.id.response_dialog_text) TextView _text;
    @Bind(R.id.response_dialog_cancelBtn) Button _cancelBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_response, container, false);
        ButterKnife.bind(this, view);

        _cancelBtn.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        dismiss();

    }

    public void onSuccess() {
        _spinner.setVisibility(View.GONE);
        _text.setVisibility(View.VISIBLE);
        _cancelBtn.setVisibility(View.VISIBLE);

        CartFragment fragment = (CartFragment) getFragmentManager().findFragmentById(R.id.store_sidebar);
        if (fragment != null) {
            fragment.clearCart();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 4000);
    }
}
