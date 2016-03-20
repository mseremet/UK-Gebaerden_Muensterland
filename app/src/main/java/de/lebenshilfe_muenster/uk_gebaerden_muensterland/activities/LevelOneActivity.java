package de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video.SignVideoUIFragment;

public class LevelOneActivity extends AppCompatActivity {

    public static final String FRAGMENT_TO_SHOW = "fragment_to_show";
    public static final String EXTRA = "extra";
    private static final String TAG = LevelOneActivity.class.getSimpleName();
    private static final String SIGN_VIDEO_TAG = "sign_video_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_one_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(StringUtils.EMPTY);
        }
        final Intent intent = getIntent();
        final Bundle bundle = intent.getBundleExtra(EXTRA);
        if (null == bundle) {
            throw new IllegalArgumentException("The bundle supplied to the activity is null");
        }
        final String fragmentToShow = bundle.getString(FRAGMENT_TO_SHOW, StringUtils.EMPTY);
        if (SignVideoUIFragment.class.getSimpleName().equals(fragmentToShow)) {
            final Parcelable sign = bundle.getParcelable(SignVideoUIFragment.SIGN_TO_SHOW);
            SignVideoUIFragment signVideoUIFragment = new SignVideoUIFragment();
            final Bundle args = new Bundle();
            args.putParcelable(SignVideoUIFragment.SIGN_TO_SHOW, sign);
            signVideoUIFragment.setArguments(args);
            setFragment(signVideoUIFragment, SIGN_VIDEO_TAG);
        } else {
            throw new IllegalArgumentException("Cannot show the fragment with name: " + fragmentToShow);
        }
    }

    private void setFragment(Fragment fragment, String fragmentTag) {
        Log.d(TAG, "setFragment: " + fragmentTag);
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame_level_one, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
