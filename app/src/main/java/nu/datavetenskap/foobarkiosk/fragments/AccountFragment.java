package nu.datavetenskap.foobarkiosk.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import net.glxn.qrgen.android.QRCode;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import nu.datavetenskap.foobarkiosk.R;
import nu.datavetenskap.foobarkiosk.models.IAccount;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAccountInteractionListener} interface
 * to handle interaction events.
 */
public class AccountFragment extends Fragment {
    private static final String TAG = "AccountFragment";


    private OnAccountInteractionListener mListener;
    private SharedPreferences pref;
    private IAccount account;

    @Bind(R.id.account_name) TextView _aName;
    @Bind(R.id.account_balance) TextView _aBalance;
    @Bind(R.id.account_profile) ImageButton _btnProfile;

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(IAccount account) {
        AccountFragment f = new AccountFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        Log.d(TAG, "New instance: " + account.getName());
        args.putString("name", account.getName());
        args.putBoolean("isComplete", account.getIsComplete());
        args.putString("token", account.getToken());
        args.putFloat("balance", account.getBalance());
        f.setArguments(args);

        return f;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);

        _aName.setText(getName());
        _aBalance.setText(getBalance());

        _btnProfile.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openProfileDialog();

            }
        });

        warnIfIncompleteAccount();
        return view;
    }

    private void openProfileDialog() {
        final String hostAPI = PreferenceManager.getDefaultSharedPreferences(getActivity()).
                getString(getContext().getString(R.string.pref_key_fooapi_host), "");

        String url = hostAPI + "/profile/" + getToken();
        Log.d(TAG, "urls: " + url);

        if (mListener != null) {
            mListener.onAccountProfileDialogOpen();

            AlertDialog.Builder alertadd = new AlertDialog.Builder(getActivity());
            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View view = factory.inflate(R.layout.dialog_account_profile, null);
            alertadd.setView(view);
            alertadd.setTitle(getName());
            ImageView image = (ImageView) view.findViewById(R.id.profile_qr_image);

            Bitmap myBitmap = QRCode.from(url).withSize(250, 250).bitmap();

            image.setImageBitmap(myBitmap);

            alertadd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Log.d(TAG, "Dialog dismissed");
                    mListener.onAccountProfileDialogDismissed();
                }
            });

            alertadd.show();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAccountInteractionListener) {
            mListener = (OnAccountInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAccountInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private String getName() {
        return getArguments().getString("name");
    }

    private String getBalance() {
        float balance = getArguments().getFloat("balance", 0);
        if(balance == (int) balance)
            return String.format(Locale.getDefault() ,"%d kr",(int)balance);
        else
            return String.format("%s kr", balance);
    }

    private String getToken() {
        return getArguments().getString("token");
    }

    private void warnIfIncompleteAccount() {
        if (!getArguments().getBoolean("isComplete")) {

            Animation mAnimation = new AlphaAnimation(1, 0);
            mAnimation.setDuration(1000);
            mAnimation.setInterpolator(new LinearInterpolator());
            mAnimation.setRepeatCount(Animation.INFINITE);
            mAnimation.setRepeatMode(Animation.REVERSE);
            _btnProfile.startAnimation(mAnimation);
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAccountInteractionListener {
        // TODO: Update argument type and name
        void onAccountProfileDialogOpen();
        void onAccountProfileDialogDismissed();
    }
}
