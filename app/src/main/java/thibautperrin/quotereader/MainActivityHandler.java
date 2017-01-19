package thibautperrin.quotereader;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import thibautperrin.quotereader.fragments.DialogFragmentDownloading;

import static thibautperrin.quotereader.StaticFields.*;

/**
 * This handler is used to allow the Thread to send data to the UI thread, to display.
 */
public class MainActivityHandler extends Handler {
    private final MainActivity mainActivity;
    private static NewQuoteReceiver vdmsReceiver;
    private static NewQuoteReceiver dtcsReceiver;
    private static NewQuoteReceiver nsfsReceiver;

    public interface
    NewQuoteReceiver {
        void onReceive();
    }

    public static void setVdmsReceiver(NewQuoteReceiver vdmsReceiver) {
        MainActivityHandler.vdmsReceiver = vdmsReceiver;
    }

    public static void setDtcsReceiver(NewQuoteReceiver dtcsReceiver) {
        MainActivityHandler.dtcsReceiver = dtcsReceiver;
    }

    public static void setNsfsReceiver(NewQuoteReceiver quoteReceiver) {
        MainActivityHandler.nsfsReceiver = quoteReceiver;
    }

    MainActivityHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        DialogFragmentDownloading dialog = mainActivity.getDialogFragmentDownloading();
        switch (msg.what) {
            case VDMS_DOWNLOADED:
                if (vdmsReceiver != null) {
                    vdmsReceiver.onReceive();
                }
                if (dialog != null) {
                    dialog.finishVdm(msg.arg1);
                }
                break;
            case DTCS_DOWNLOADED:
                if (dtcsReceiver != null) {
                    dtcsReceiver.onReceive();
                }
                if (dialog != null) {
                    dialog.finishDtc(msg.arg1);
                }
                break;
            case NSFS_DOWNLOADED:
                if (nsfsReceiver != null) {
                    nsfsReceiver.onReceive();
                }
                if (dialog != null) {
                    dialog.finishNsf(msg.arg1);
                }
                break;
            case PARSING_ERROR_VDM:
                Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.websiteChanged, "VDM"), Toast.LENGTH_LONG).show();
                if (dialog != null) {
                    dialog.finishVdm(-1);
                }
                break;
            case PARSING_ERROR_DTC:
                Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.websiteChanged, "DTC"), Toast.LENGTH_LONG).show();
                if (dialog != null) {
                    dialog.finishDtc(-1);
                }
                break;
            case PARSING_ERROR_NSF:
                Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.websiteChanged, "NSF"), Toast.LENGTH_LONG).show();
                if (dialog != null) {
                    dialog.finishNsf(-1);
                }
                break;
            case DOWNLOADING_PROGRESS:
                if (dialog != null) {
                    switch (msg.arg1) {
                        case VDM :
                            dialog.setPercentageVdm(msg.arg2);
                            break;
                        case DTC :
                            dialog.setPercentageDtc(msg.arg2);
                            break;
                        case NSF :
                            dialog.setPercentageNsf(msg.arg2);
                            break;
                    }
                }
                break;
        }
    }
}