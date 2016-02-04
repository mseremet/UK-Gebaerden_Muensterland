package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.SignDAO;

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

    private final List<Sign> dataset;
    private ViewGroup parent;

    public SignBrowserAdapter(List<Sign> dataset) {
        this.dataset = dataset;
    }

    @Override
    public SignBrowserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        this.parent = parent;
        return new ViewHolder(LayoutInflater.from(this.parent.getContext()).inflate(R.layout.row_layout_sign_browser, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String name = dataset.get(position).getName();
        final String nameLocaleDe = dataset.get(position).getNameLocaleDe();
        holder.txtSignName.setText(nameLocaleDe);
        holder.txtSignName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnTxtSignName(name);
            }
        });
        holder.txtSignMnemonic.setText(dataset.get(position).getMnemonic());
        final DecimalFormat decimalFormat = new DecimalFormat(" 0;-0");
        holder.txtSignLearningProgress.setText(decimalFormat.format(dataset.get(position).getLearningProgress()));
        holder.checkBoxStarred.setChecked(dataset.get(position).isStarred());
        holder.checkBoxStarred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             handleClickOnCheckBoxStarred(dataset.get(position));
            }
        });
    }

    public void handleClickOnTxtSignName(String item) {
        int position = this.dataset.indexOf(item);
        // TODO: handle Click on item here
        // notifyItemRemoved(position);
    }

    private void handleClickOnCheckBoxStarred(Sign sign) {
        if(sign.isStarred()) {
            sign.setStarred(false);
        } else {
            sign.setStarred(true);
        }
        SignDAO signDAO = SignDAO.getInstance(this.parent.getContext());
        signDAO.open();
        signDAO.update(sign);
        signDAO.close();
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView txtSignName;
        public final TextView txtSignMnemonic;
        public final TextView txtSignLearningProgress;
        public final CheckBox checkBoxStarred;

        public ViewHolder(View v) {
            super(v);
            this.txtSignName = (TextView) v.findViewById(R.id.signName);
            this.txtSignMnemonic = (TextView) v.findViewById(R.id.mnemonic);
            this.txtSignLearningProgress = (TextView) v.findViewById(R.id.learningProgressValue);
            this.checkBoxStarred = (CheckBox) v.findViewById(R.id.starred);
        }
    }

}
