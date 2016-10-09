package thibautperrin.quotereader.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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
import thibautperrin.quotereader.bean.Nsf;
import thibautperrin.quotereader.dao.DaoNsf;

public class NsfArrayAdapter extends ArrayAdapter<Nsf> implements MainActivityHandler.NewQuoteReceiver {
    private final Context context;
    private final List<Nsf> nsfs;

    public NsfArrayAdapter(Context context, int resource, List<Nsf> nsfs) {
        super(context, resource, nsfs);
        this.context = context;
        this.nsfs = nsfs;
        loadNsf();
        MainActivityHandler.setNsfsReceiver(this);
    }

    private void loadNsf() {
        DaoNsf daoNsf = new DaoNsf(context);
        daoNsf.open();
        List<Nsf> loadedNsfs = daoNsf.getNsf();
        daoNsf.close();
        addAll(loadedNsfs);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Nsf nsf = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.nsf_view, parent, false);
        }
        TextView textViewContent = (TextView) convertView.findViewById(R.id.nsf_content);
        TextView textViewNumber= (TextView) convertView.findViewById(R.id.nsf_number);
        TextView textViewDate = (TextView) convertView.findViewById(R.id.nsf_date);
        textViewContent.setText(nsf.getContent());
        textViewNumber.setText(String.format(Locale.getDefault(), "%d", nsf.getNumber()));
        textViewDate.setText(nsf.getDate());
        return convertView;
    }

    @Override
    public void addAll(@NonNull Collection<? extends Nsf> collection) {
        for(Nsf nsf : collection) {
            if (!this.nsfs.contains(nsf)) {
                this.nsfs.add(nsf);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        Collections.sort(nsfs);
        super.notifyDataSetChanged();
    }

    @Override
    public void onReceive() {
        loadNsf();
    }
}
