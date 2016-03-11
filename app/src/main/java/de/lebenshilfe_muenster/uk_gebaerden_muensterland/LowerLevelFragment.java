package de.lebenshilfe_muenster.uk_gebaerden_muensterland;

import android.app.Fragment;
import android.os.Bundle;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities.MainActivity;

/**
 * Abstract class for fragments which cannot be reached from the main activity directly.
 * <p/>
 * Created by mtonhaeuser on 08.03.2016.
 */
public abstract class LowerLevelFragment extends Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        final MainActivity mainActivity = (MainActivity) getActivity();
//        final ActionBar supportActionBar = mainActivity.getMainActivitySupportActionBar();
//        supportActionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
//        supportActionBar.setDisplayHomeAsUpEnabled(true);
//        supportActionBar.setDisplayShowHomeEnabled(true);
//        mainActivity.getMainActivityToggle().setDrawerIndicatorEnabled(false);
//        mainActivity.getMainActivityToggle().setHomeAsUpIndicator(0);

    }

}
