package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
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

    private final List<Sign> dataSet;
    private final Context context;
    private final SignBrowserUIFragment signBrowserUIFragment;

    public SignBrowserAdapter(SignBrowserUIFragment signBrowserUIFragment, Context context, List<Sign> dataSet) {
        this.signBrowserUIFragment = signBrowserUIFragment;
        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public SignBrowserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_sign_browser, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String name = dataSet.get(position).getName();
        final String nameLocaleDe = dataSet.get(position).getNameLocaleDe();
        holder.txtSignName.setText(nameLocaleDe);
        holder.txtSignName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnTxtSignName(dataSet.get(position));
            }
        });
        holder.txtSignMnemonic.setText(dataSet.get(position).getMnemonic());
        final DecimalFormat decimalFormat = new DecimalFormat(" 0;-0");
        holder.txtSignLearningProgress.setText(decimalFormat.format(dataSet.get(position).getLearningProgress()));
        holder.checkBoxStarred.setChecked(dataSet.get(position).isStarred());
        holder.checkBoxStarred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnCheckBoxStarred(dataSet.get(position));
            }
        });
    }

    private void handleClickOnTxtSignName(Sign sign) {
        this.signBrowserUIFragment.onTxtSignNameClicked(sign);
    }

    private void handleClickOnCheckBoxStarred(Sign sign) {
        new UpdateSignTask().execute(sign);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
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

    private class UpdateSignTask extends AsyncTask<Sign, Void, Void> {

        @Override
        protected Void doInBackground(Sign... params) {
            if (1 == params.length) {
                final Sign sign = params[0];
                if (sign.isStarred()) {
                    sign.setStarred(false);
                } else {
                    sign.setStarred(true);
                }
                if (null != SignBrowserAdapter.this.context) {
                    final SignDAO signDAO = SignDAO.getInstance(SignBrowserAdapter.this.context);
                    signDAO.open();
                    signDAO.update(sign);
                    signDAO.close();
                }
            }
            return null;
        }
    }

}
