package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.video;

import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchActivity;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video.SignVideoFragment;

public class SignSearchVideoActivity extends AppCompatActivity {

    private static final String TAG = SignSearchVideoActivity.class.getSimpleName();
    public static final String EXTRA = "extra";
    private String originalQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() " + this.hashCode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_video_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Validate.notNull(getSupportActionBar(), "SupportActionBar is null.");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(StringUtils.EMPTY);
        final Intent intent = getIntent();
        final Bundle bundle = intent.getBundleExtra(EXTRA);
        Validate.notNull(bundle, "The bundle supplied to the activity is null.");
        this.originalQuery = bundle.getString(SignSearchActivity.QUERY);
        Validate.notNull(this.originalQuery, "Query string supplied to this activity is null.");
        final Parcelable parcelledSign = bundle.getParcelable(SignVideoFragment.SIGN_TO_SHOW);
        Validate.notNull(parcelledSign, "Parcelled sign supplied to this activity is null.");
        final SignVideoFragment signVideoFragment = new SignVideoFragment();
        final Bundle args = new Bundle();
        args.putParcelable(SignVideoFragment.SIGN_TO_SHOW, parcelledSign);
        signVideoFragment.setArguments(args);
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.searchVideoActivityContentFrame, signVideoFragment, "SIGN_VIDEO_TAG");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected() " + this.hashCode());
        switch (item.getItemId()) {
            case android.R.id.home:
                final Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.putExtra(SearchManager.QUERY, this.originalQuery);
                NavUtils.navigateUpTo(this, upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
