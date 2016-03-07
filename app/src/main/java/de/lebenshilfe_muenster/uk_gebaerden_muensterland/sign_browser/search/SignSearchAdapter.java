package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;

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
public class SignSearchAdapter extends RecyclerView.Adapter<SignSearchAdapter.ViewHolder> {

    private final List<Sign> dataSet;

    public SignSearchAdapter(List<Sign> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public SignSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row_layout, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtSignSearchName.setText(dataSet.get(position).getNameLocaleDe());
        holder.txtSignSearchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnTxtSignName(dataSet.get(position).getName());
            }
        });
    }

    private void handleClickOnTxtSignName(String item) {
        // TODO: handle Click on item here
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView txtSignSearchName;

        public ViewHolder(View v) {
            super(v);
            this.txtSignSearchName = (TextView) v.findViewById(R.id.signNameSearch);
        }
    }

}

