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

import thibautperrin.quotereader.adapters.NsfArrayAdapter;
import thibautperrin.quotereader.R;
import thibautperrin.quotereader.bean.Nsf;

/**
 * A placeholder fragment containing a set of Nsf.
 */

public class PlaceHolderFragmentNSF extends Fragment {

    /**
     * Default constructor without argument. A placeholder framgent should not have
     * arguments to facilitates re-construction. That the reason why we use a Bundle
     * for the arguments.
     */
    public PlaceHolderFragmentNSF() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_quote);
        final ArrayList<Nsf> nsfs = new ArrayList<>();
        NsfArrayAdapter nsfArrayAdapter = new NsfArrayAdapter(rootView.getContext(), R.layout.nsf_view, nsfs);
        listView.setAdapter(nsfArrayAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String url = nsfs.get(position).getUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Nsf nsf = nsfs.get(position);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_quote, "NSF", nsf.getContent(), nsf.getUrl()));
                startActivity(Intent.createChooser(intent, nsf.getContent()));
            }
        });
        return rootView;
    }

    public static PlaceHolderFragmentNSF newInstance() {
        return new PlaceHolderFragmentNSF();
    }
}
