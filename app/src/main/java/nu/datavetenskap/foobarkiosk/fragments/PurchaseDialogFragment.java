package nu.datavetenskap.foobarkiosk.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import nu.datavetenskap.foobarkiosk.R;
import nu.datavetenskap.foobarkiosk.models.IAccount;
import nu.datavetenskap.foobarkiosk.models.statemodels.IState;


public class PurchaseDialogFragment extends DialogFragment {
    private static final String TAG = "PurchaseDialogFragment";

    @Bind(R.id.purchase_cash_btn) Button _cashBtn;
    @Bind(R.id.purchase_foocash_btn) Button _fooCBtn;
    @Bind(R.id.purchase_credit_btn) Button _creditBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_purchase, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    public static PurchaseDialogFragment newInstance(IState state) {
        PurchaseDialogFragment f = new PurchaseDialogFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        Log.d(TAG, "New instance");
        IAccount account = state.getAccount();
        if (account != null) {
            args.putBoolean("loggedIn", true);
            args.putFloat("balance", state.getAccount().getBalance());
        }
        else {
            args.putBoolean("loggedIn", false);
        }
        f.setArguments(args);

        return f;

    }
}
