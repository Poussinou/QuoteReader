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

import thibautperrin.quotereader.R;
import thibautperrin.quotereader.adapters.VdmArrayAdapter;
import thibautperrin.quotereader.bean.Vdm;

/**
 * A placeholder fragment containing a set of Vdm.
 */

public class PlaceHolderFragmentVDM extends Fragment {

    /**
     * Default constructor without argument. A placeholder framgent should not have
     * arguments to facilitates re-construction. That the reason why we use a Bundle
     * for the arguments.
     */
    public PlaceHolderFragmentVDM() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_quote);
        final ArrayList<Vdm> vdms = new ArrayList<>();
        VdmArrayAdapter vdmArrayAdapter = new VdmArrayAdapter(rootView.getContext(), R.layout.vdm_view, vdms);
        listView.setAdapter(vdmArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = vdms.get(position).getUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        return rootView;
    }

    public static PlaceHolderFragmentVDM newInstance() {
        return new PlaceHolderFragmentVDM();
    }
}
