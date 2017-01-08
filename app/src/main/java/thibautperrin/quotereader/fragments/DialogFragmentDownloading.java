package thibautperrin.quotereader.fragments;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import thibautperrin.quotereader.MainActivity;
import thibautperrin.quotereader.R;

/**
 * Created by thibaut perrin on 07/01/2017.
 * This class is used to display a dialog while downloading.
 */

public class DialogFragmentDownloading extends DialogFragment {

    private int nbVdm = 0;
    private int nbDtc = 0;
    private int nbNsf = 0;
    private boolean isFinishedVdm = false;
    private boolean isFinishedDtc = false;
    private boolean isFinishedNsf = false;
    private ProgressBar progressBarDownloadingVdm;
    private ProgressBar progressBarDownloadingDtc;
    private ProgressBar progressBarDownloadingNsf;
    private TextView textViewDownloadedVdm;
    private TextView textViewDownloadedDtc;
    private TextView textViewDownloadedNsf;

    public static DialogFragmentDownloading newInstance() {
        return new DialogFragmentDownloading();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int style = DialogFragment.STYLE_NORMAL;
        int theme = android.R.style.Theme_Holo_Dialog;
        setStyle(style, theme);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setTitle(getString(R.string.downloading));
        }
        View v = inflater.inflate(R.layout.fragment_dialog_downloading, container, false);
        progressBarDownloadingVdm = (ProgressBar) v.findViewById(R.id.progressBarDownloadingVdm);
        progressBarDownloadingDtc = (ProgressBar) v.findViewById(R.id.progressBarDownloadingDtc);
        progressBarDownloadingNsf = (ProgressBar) v.findViewById(R.id.progressBarDownloadingNsf);

        textViewDownloadedVdm = (TextView) v.findViewById(R.id.vdm_downloaded);
        textViewDownloadedDtc = (TextView) v.findViewById(R.id.dtc_downloaded);
        textViewDownloadedNsf = (TextView) v.findViewById(R.id.nsf_downloaded);

        if (isFinishedVdm) {
            textViewDownloadedVdm.setVisibility(View.VISIBLE);
            progressBarDownloadingVdm.setVisibility(View.INVISIBLE);
            textViewDownloadedVdm.setText(nbVdm);
        } else {
            progressBarDownloadingVdm.setVisibility(View.VISIBLE);
            textViewDownloadedVdm.setVisibility(View.INVISIBLE);
            progressBarDownloadingVdm.setProgress(nbVdm);
        }

        if (isFinishedDtc) {
            textViewDownloadedDtc.setVisibility(View.VISIBLE);
            progressBarDownloadingDtc.setVisibility(View.INVISIBLE);
            textViewDownloadedDtc.setText(nbDtc);
        } else {
            progressBarDownloadingDtc.setVisibility(View.VISIBLE);
            textViewDownloadedDtc.setVisibility(View.INVISIBLE);
            progressBarDownloadingDtc.setProgress(nbDtc);
        }
        if (isFinishedNsf) {
            textViewDownloadedNsf.setVisibility(View.VISIBLE);
            progressBarDownloadingNsf.setVisibility(View.INVISIBLE);
            textViewDownloadedNsf.setText(nbNsf);
        } else {
            progressBarDownloadingNsf.setVisibility(View.VISIBLE);
            textViewDownloadedNsf.setVisibility(View.INVISIBLE);
            progressBarDownloadingNsf.setProgress(nbNsf);
        }
        return v;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        ((MainActivity) getActivity()).onDialogDismiss();
        super.onDismiss(dialog);
    }

    public void setPercentageVdm(int pctVdm) {
        this.nbVdm = pctVdm;
        if (!isFinishedVdm && progressBarDownloadingVdm != null) {
            progressBarDownloadingVdm.setProgress(pctVdm);
        }
    }

    public void setPercentageDtc(int pctDtc) {
        this.nbDtc = pctDtc;
        if (!isFinishedDtc && progressBarDownloadingDtc != null) {
            progressBarDownloadingDtc.setProgress(pctDtc);
        }
    }

    public void setPercentageNsf(int pctNsf) {
        this.nbNsf = pctNsf;
        if (!isFinishedNsf && progressBarDownloadingNsf != null) {
            progressBarDownloadingNsf.setProgress(pctNsf);
        }
    }

    public void finishVdm(int numberVdm) {
        this.nbVdm = numberVdm;
        isFinishedVdm = true;
        if (progressBarDownloadingVdm != null) {
            progressBarDownloadingVdm.setVisibility(View.INVISIBLE);
        }
        if (textViewDownloadedVdm != null) {
            textViewDownloadedVdm.setVisibility(View.VISIBLE);
            textViewDownloadedVdm.setText(getStringNbDownloaded(nbVdm));
        }
        testEverythingFinished();
    }

    public void finishDtc(int nbDtc) {
        this.nbDtc = nbDtc;
        isFinishedDtc = true;
        if (progressBarDownloadingDtc != null) {
            progressBarDownloadingDtc.setVisibility(View.INVISIBLE);
        }
        if (textViewDownloadedDtc != null) {
            textViewDownloadedDtc.setVisibility(View.VISIBLE);
            textViewDownloadedDtc.setText(getStringNbDownloaded(nbDtc));
        }
        testEverythingFinished();
    }

    public void finishNsf(int nbNsf) {
        this.nbNsf = nbNsf;
        isFinishedNsf = true;
        if (progressBarDownloadingNsf != null) {
            progressBarDownloadingNsf.setVisibility(View.INVISIBLE);
        }
        if (textViewDownloadedNsf != null) {
            textViewDownloadedNsf.setVisibility(View.VISIBLE);
            textViewDownloadedNsf.setText(getStringNbDownloaded(nbNsf));
        }
        testEverythingFinished();
    }

    private String getStringNbDownloaded(int nb) {
        if (nb < 2) {
            return getString(R.string.singularDownloaded, nb);
        } else {
            return getString(R.string.pluralDownloaded, nb);
        }
    }

    private void testEverythingFinished() {
        if (isFinishedVdm && isFinishedDtc && isFinishedNsf) {
            ((MainActivity) getActivity()).downloadFinished();
        }
    }

}
