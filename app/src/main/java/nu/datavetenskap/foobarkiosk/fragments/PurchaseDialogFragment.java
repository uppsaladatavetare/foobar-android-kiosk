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


public class PurchaseDialogFragment extends DialogFragment implements
        View.OnClickListener {
    private static final String TAG = "PurchaseDialogFragment";

    @Bind(R.id.purchase_cash_btn) Button _cashBtn;
    @Bind(R.id.purchase_foocash_btn) Button _fooCBtn;
    @Bind(R.id.purchase_credit_btn) Button _creditBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_purchase, container, false);

        ButterKnife.bind(this, view);

//        Log.d(TAG, "Cost1: " + getArguments().getFloat("costTotal"));
//        Log.d(TAG, "Cost2: " + getArguments().getFloat("costTotal"));
        determineFooCashAbility();

        return view;
    }

    private void determineFooCashAbility() {
        if (!getArguments().getBoolean("loggedIn")){
            _fooCBtn.setEnabled(false);
            _fooCBtn.setText("Not logged in with foo card");
            return;
        }
        if (getArguments().getFloat("balance") < getArguments().getFloat("costTotal")) {
            _fooCBtn.setEnabled(false);
            _fooCBtn.setText("Insufficient funds");
            return;
        }
        _fooCBtn.setEnabled(true);
        _fooCBtn.setText("FooCash\n" + getArguments().getString("name"));

    }

    public static PurchaseDialogFragment newInstance(IState state) {
        PurchaseDialogFragment f = new PurchaseDialogFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        Log.d(TAG, "New instance");
        IAccount account = state.getAccount();
        if (account != null) {
            args.putBoolean("loggedIn", true);
            args.putString("name", state.getAccount().getName());
            args.putFloat("balance", state.getAccount().getBalance());
        }
        else {
            args.putBoolean("loggedIn", false);
        }
        args.putFloat("costTotal", state.getPurchaseCost());
        f.setArguments(args);

        return f;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.purchase_cash_btn:
                cashPurchase();
                break;
            case R.id.purchase_foocash_btn:
                fooCashPurchase();
                break;
            case R.id.purchase_credit_btn:
                creditCardPurchase();
                break;
            default:
                break;
        }
    }

    private void cashPurchase() {

    }

    private void fooCashPurchase() {

    }

    private void creditCardPurchase() {

    }

    public void updateAccessRights(final IAccount account) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (account == null){
                    _fooCBtn.setEnabled(false);
                    _fooCBtn.setText("Not logged in with foo card");
                    return;
                }
                if (account.getBalance() < getArguments().getFloat("costTotal")) {
                    _fooCBtn.setEnabled(false);
                    _fooCBtn.setText("Insufficient funds");
                    return;
                }
                Log.d(TAG, "Balance: " + account.getBalance());
                Log.d(TAG, "Cost: " + getArguments().getFloat("costTotal"));
                _fooCBtn.setEnabled(true);
                _fooCBtn.setText("FooCash\n" + account.getName());
            }
        });



    }
}
