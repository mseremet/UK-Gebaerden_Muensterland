package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.SignDAO;

public class SignBrowserSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_browser_search);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        this.recyclerView = (RecyclerView) this.findViewById(R.id.signSearchRecyclerView);
        this.recyclerView.setHasFixedSize(true); // performance fix
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(new SignBrowserSearchAdapter(new ArrayList<Sign>()));
        new LoadSignsByNameTask().execute(query);
    }

    private class LoadSignsByNameTask extends AsyncTask<String, Void, List<Sign>> {

        @Override
        protected List<Sign> doInBackground(String... params) {
            final SignDAO signDAO = SignDAO.getInstance(SignBrowserSearchActivity.this);
            signDAO.open();
            final List<Sign> signs = signDAO.read(params[0]);
            signDAO.close();
            return signs;
        }

        @Override
        protected void onPostExecute(List<Sign> result) {
            recyclerView.swapAdapter(new SignBrowserSearchAdapter(result),true);
        }
    }
}
