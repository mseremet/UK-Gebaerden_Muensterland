package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;

/**
 * Copyright (c) 2016 Matthias Tonhäuser
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class SignBrowserAdapter extends RecyclerView.Adapter<SignBrowserAdapter.ViewHolder> {

    private List<String> dataset;

    public SignBrowserAdapter(List<String> dataset) {
        this.dataset = dataset;
    }

    @Override
    public SignBrowserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_sign_browser, parent, false));
    }

    public void handleClick(String item) {
        int position = this.dataset.indexOf(item);
        // TODO: handle Click on item here
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String name = dataset.get(position);
        holder.txtHeader.setText(dataset.get(position));
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick(name);
            }
        });
        holder.txtFooter.setText("Footer: " + dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtHeader;
        public TextView txtFooter;

        public ViewHolder(View v) {
            super(v);
            this.txtHeader = (TextView) v.findViewById(R.id.firstLine);
            this.txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }

}
