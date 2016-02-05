package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.Sign;

/**
 * Copyright (c) 2016 Matthias Tonhäuser
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class SignBrowserSearchAdapter extends RecyclerView.Adapter<SignBrowserSearchAdapter.ViewHolder> {

    private final List<Sign> dataset;
    private ViewGroup parent;

    public SignBrowserSearchAdapter(List<Sign> dataset) {
        this.dataset = dataset;
    }

    @Override
    public SignBrowserSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        this.parent = parent;
        return new ViewHolder(LayoutInflater.from(this.parent.getContext()).inflate(R.layout.row_layout_sign_browser_search, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtSignSearchName.setText(dataset.get(position).getNameLocaleDe());
        holder.txtSignSearchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnTxtSignName(dataset.get(position).getName());
            }
        });
    }

    private void handleClickOnTxtSignName(String item) {
        // TODO: handle Click on item here
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView txtSignSearchName;

        public ViewHolder(View v) {
            super(v);
            this.txtSignSearchName = (TextView) v.findViewById(R.id.signNameSearch);
        }
    }

}

