package nu.datavetenskap.foobarkiosk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sumup.merchant.api.SumUpState;

import nu.datavetenskap.foobarkiosk.fragments.AccountFragment;
import nu.datavetenskap.foobarkiosk.fragments.CartFragment;
import nu.datavetenskap.foobarkiosk.fragments.StoreFragment;
import nu.datavetenskap.foobarkiosk.fragments.WebViewFragment;

public class MainActivity extends AppCompatActivity implements
        WebViewFragment.OnFragmentInteractionListener,
        CartFragment.OnCartInteractionListener,
        AccountFragment.OnAccountInteractionListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SumUpState.init(this.getApplicationContext());

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                //return;
            }

            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            final String fragChoice = preferences.getString(getString(R.string.pref_key_implementation), null);
            Log.d(TAG, "Fragmentchoice: " + fragChoice);
            // Create a new Fragment to be placed in the activity layout
            // Add the fragment to the 'fragment_container' FrameLayout
            if (fragChoice != null) {
                Fragment mainFragment;
                if (fragChoice.equals("web")) {
                    mainFragment = new WebViewFragment();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, mainFragment).commit();
                }
                else if (fragChoice.equals("native")) {
                    FoobarAPI.startSingleton(getApplicationContext());
                    mainFragment = new StoreFragment();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, mainFragment).commit();
                }
            }
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCartInteraction(Uri uri) {

    }

    @Override
    public void onAccountProfileDialogOpen() {
        CartFragment frag = (CartFragment) getSupportFragmentManager().findFragmentById(R.id.store_sidebar);
        if (frag != null) {
            frag.sendProfileState();
        }
    }

    @Override
    public void onAccountProfileDialogDismissed() {
        CartFragment frag = (CartFragment) getSupportFragmentManager().findFragmentById(R.id.store_sidebar);
        if (frag != null) {
            frag.returnFromProfile();
        }
    }
}
