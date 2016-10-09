package thibautperrin.quotereader;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import static thibautperrin.quotereader.StaticFields.*;

/**
 * This handler is used to allow the Thread to send data to the UI thread, to display.
 */
public class MainActivityHandler extends Handler {
    private final MainActivity mainActivity;
    private static NewQuoteReceiver vdmsReceiver;
    private static NewQuoteReceiver dtcsReceiver;
    private static NewQuoteReceiver nsfsReceiver;

    public interface NewQuoteReceiver {
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

    public MainActivityHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case VDMS_DOWNLOADED:
                if (vdmsReceiver != null) {
                    vdmsReceiver.onReceive();
                }
                Toast.makeText(mainActivity, getVdmMessage(msg.arg1), Toast.LENGTH_LONG).show();
                break;
            case DTCS_DOWNLOADED:
                if (dtcsReceiver != null) {
                    dtcsReceiver.onReceive();
                }
                Toast.makeText(mainActivity, getDtcMessage(msg.arg1), Toast.LENGTH_LONG).show();
                break;
            case NSFS_DOWNLOADED:
                if (nsfsReceiver != null) {
                    nsfsReceiver.onReceive();
                }
                Toast.makeText(mainActivity, getNsfMessage(msg.arg1), Toast.LENGTH_LONG).show();
                break;
            case PARSING_ERROR_VDM:
                Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.websiteChanged, "VDM"), Toast.LENGTH_LONG).show();
                break;
            case PARSING_ERROR_DTC:
                Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.websiteChanged, "DTC"), Toast.LENGTH_LONG).show();
                break;
            case PARSING_ERROR_NSF:
                Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.websiteChanged, "NSF"), Toast.LENGTH_LONG).show();
                break;
        }
    }

    private String getVdmMessage(int number) {
        if (number == 0) {
            return mainActivity.getResources().getString(R.string.noOneDownloaded, "VDM");
        } else if (number == 1) {
            return mainActivity.getResources().getString(R.string.onlyOneDownloaded, "VDM");
        } else {
            return mainActivity.getResources().getString(R.string.severalDownloaded, number, "VDM");
        }
    }

    private String getDtcMessage(int number) {
        if (number == 0) {
            return mainActivity.getResources().getString(R.string.noOneDownloaded, "DTC");
        } else if (number == 1) {
            return mainActivity.getResources().getString(R.string.onlyOneDownloaded, "DTC");
        } else {
            return mainActivity.getResources().getString(R.string.severalDownloaded, number, "DTC");
        }
    }

    private String getNsfMessage(int number) {
        if (number == 0) {
            return mainActivity.getResources().getString(R.string.noOneDownloaded, "NSF");
        } else if (number == 1) {
            return mainActivity.getResources().getString(R.string.onlyOneDownloaded, "NSF");
        } else {
            return mainActivity.getResources().getString(R.string.severalDownloaded, number, "NSF");
        }
    }
}