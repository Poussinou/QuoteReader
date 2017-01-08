package thibautperrin.quotereader;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import thibautperrin.quotereader.fragments.DialogFragmentDownloading;
import thibautperrin.quotereader.reader.DownloadThread;
import thibautperrin.quotereader.fragments.PlaceHolderFragmentDTC;
import thibautperrin.quotereader.fragments.PlaceHolderFragmentNSF;
import thibautperrin.quotereader.fragments.PlaceHolderFragmentVDM;

public class MainActivity extends AppCompatActivity {

    private DownloadThread downloadThread;
    private DialogFragmentDownloading dialogFragmentDownloading;
    private MainActivityHandler handler;

    public DialogFragmentDownloading getDialogFragmentDownloading() {
        return dialogFragmentDownloading;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        handler = new MainActivityHandler(this);
        downloadThread = new DownloadThread(this, handler);
        downloadThread.start();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
                if (isConnected) {
                    downloadThread.update();
                    showDialog();
                } else {
                    Snackbar.make(view, R.string.noInternetConnection, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        downloadThread.quit();
        super.onDestroy();
    }

    void showDialog() {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        dialogFragmentDownloading = DialogFragmentDownloading.newInstance();
        dialogFragmentDownloading.show(ft, "dialog");
    }

    public void onDialogDismiss() {
        dialogFragmentDownloading = null;
    }

    public void downloadFinished() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialogFragmentDownloading != null) {
                    dialogFragmentDownloading.dismiss();
                }
            }
        }, 1000);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return PlaceHolderFragmentVDM.newInstance();
                case 1:
                    return PlaceHolderFragmentDTC.newInstance();
                default:
                    return PlaceHolderFragmentNSF.newInstance();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "VDM";
                case 1:
                    return "DTC";
                case 2:
                    return "NSF";
            }
            return null;
        }
    }
}
