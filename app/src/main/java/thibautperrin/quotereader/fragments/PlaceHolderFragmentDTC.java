package thibautperrin.quotereader.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import thibautperrin.quotereader.adapters.DtcArrayAdapter;
import thibautperrin.quotereader.R;
import thibautperrin.quotereader.bean.Dtc;

/**
 * A placeholder fragment containing a set of Dtc.
 */

public class PlaceHolderFragmentDTC extends Fragment {

    /**
     * Default constructor without argument. A placeholder framgent should not have
     * arguments to facilitates re-construction. That the reason why we use a Bundle
     * for the arguments.
     */
    public PlaceHolderFragmentDTC() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_quote);
        final ArrayList<Dtc> dtcs = new ArrayList<>();
        DtcArrayAdapter dtcArrayAdapter = new DtcArrayAdapter(rootView.getContext(), R.layout.dtc_view, dtcs);
        listView.setAdapter(dtcArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = dtcs.get(position).getUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        return rootView;
    }

    public static PlaceHolderFragmentDTC newInstance() {
        return new PlaceHolderFragmentDTC();
    }
}
