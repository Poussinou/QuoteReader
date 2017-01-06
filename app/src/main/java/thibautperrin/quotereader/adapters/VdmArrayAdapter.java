package thibautperrin.quotereader.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import thibautperrin.quotereader.MainActivityHandler;
import thibautperrin.quotereader.R;
import thibautperrin.quotereader.bean.Vdm;
import thibautperrin.quotereader.dao.DaoVdm;

public class VdmArrayAdapter extends ArrayAdapter<Vdm> implements MainActivityHandler.NewQuoteReceiver {

    private final Context context;
    private final List<Vdm> vdms;

    public VdmArrayAdapter(Context context, int resource, List<Vdm> vdms) {
        super(context, resource, vdms);
        this.context = context;
        this.vdms = vdms;
        loadVdms();
        MainActivityHandler.setVdmsReceiver(this);
    }

    private void loadVdms() {
        try {
            DaoVdm daoVdm = new DaoVdm(context);
            daoVdm.open();
            List<Vdm> loadedVdms = daoVdm.getVdm();
            daoVdm.close();
            addAll(loadedVdms);
        } catch (SQLiteException ex) {
            Log.e("VdmArrayAdapter", "Database is probably locked", ex);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Vdm vdm = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.vdm_view, parent, false);
        }
        TextView textViewContent = (TextView) convertView.findViewById(R.id.vdm_content);
        TextView textViewNumber= (TextView) convertView.findViewById(R.id.vdm_number);
        textViewContent.setText(vdm.getContent());
        textViewNumber.setText(String.format(Locale.getDefault(), "%d", vdm.getNumber()));
        return convertView;
    }

    @Override
    public void addAll(@NonNull Collection<? extends Vdm> collection) {
        for(Vdm vdm : collection) {
            if (!this.vdms.contains(vdm)) {
                this.vdms.add(vdm);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        Collections.sort(vdms);
        super.notifyDataSetChanged();
    }

    @Override
    public void onReceive() {
        loadVdms();
    }
}
