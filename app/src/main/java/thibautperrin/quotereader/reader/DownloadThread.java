package thibautperrin.quotereader.reader;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import thibautperrin.quotereader.MainActivityHandler;
import thibautperrin.quotereader.bean.Dtc;
import thibautperrin.quotereader.bean.Nsf;
import thibautperrin.quotereader.bean.Vdm;
import thibautperrin.quotereader.dao.DaoDtc;
import thibautperrin.quotereader.dao.DaoNsf;
import thibautperrin.quotereader.dao.DaoVdm;
import thibautperrin.quotereader.reader.parserException.NotExistingUrlException;
import thibautperrin.quotereader.reader.parserException.WebPageChangedException;

import static thibautperrin.quotereader.StaticFields.DOWNLOADING_PROGRESS;
import static thibautperrin.quotereader.StaticFields.DTC;
import static thibautperrin.quotereader.StaticFields.DTCS_DOWNLOADED;
import static thibautperrin.quotereader.StaticFields.NSF;
import static thibautperrin.quotereader.StaticFields.NSFS_DOWNLOADED;
import static thibautperrin.quotereader.StaticFields.PARSING_ERROR_DTC;
import static thibautperrin.quotereader.StaticFields.PARSING_ERROR_NSF;
import static thibautperrin.quotereader.StaticFields.PARSING_ERROR_VDM;
import static thibautperrin.quotereader.StaticFields.VDM;
import static thibautperrin.quotereader.StaticFields.VDMS_DOWNLOADED;

/**
 * This thread takes care of downloading the new VDM, DTC, NSF and to retrieve these in database.
 */
public class DownloadThread extends Thread {

    private final Context context;
    private final MainActivityHandler handler;
    private final AtomicBoolean download = new AtomicBoolean(false);
    private static final int quantityVdm = 100;
    private static final int quantityDtc = 100;
    private static final int quantityNsf = 100;
    private final AtomicBoolean shouldContinue = new AtomicBoolean(true);

    public DownloadThread(Context context, MainActivityHandler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void run() {
        while (shouldContinue.get()) {
            while (download.get()) {
                Thread threadVdm = new Thread() {
                    @Override
                    public void run() {
                        downloadVdm();
                    }
                };
                threadVdm.start();
                Thread threadDtc = new Thread() {
                    @Override
                    public void run() {
                        downloadDtc();
                    }
                };
                threadDtc.start();
                Thread threadNsf = new Thread() {
                    @Override
                    public void run() {
                        downloadNsf();
                    }
                };
                threadNsf.start();
                try {
                    threadVdm.join();
                    threadDtc.join();
                    threadNsf.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                download.set(false);
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
                break;
            }
        }
    }

    public void quit() {
        shouldContinue.set(false);
        this.interrupt();
    }

    private void downloadVdm() {
        DaoVdm daoVdm = new DaoVdm(context);
        daoVdm.open();
        daoVdm.startTransaction();
        List<Vdm> oldVdms = daoVdm.getVdm();
        ArrayList<Vdm> newVdms = new ArrayList<>();
        pageLoop:
        for (int pageNumber = 0; ; pageNumber++) {
            List<Vdm> page;
            try {
                page = Parser.getVdmPage(pageNumber);
            } catch (NotExistingUrlException | WebPageChangedException ex) {
                daoVdm.rollbackTransaction();
                daoVdm.close();
                ex.printStackTrace();
                handler.obtainMessage(PARSING_ERROR_VDM).sendToTarget();
                return;
            }

            // Loop on the VDMs of the web page:
            for (Vdm vdm : page) {
                if (oldVdms.contains(vdm)) {
                    break pageLoop;
                }
                newVdms.add(vdm);
                handler.obtainMessage(DOWNLOADING_PROGRESS, VDM, newVdms.size()).sendToTarget();
                if (newVdms.size() >= quantityVdm) {
                    daoVdm.free();
                    break pageLoop;
                }
            }
            page.clear();
        }
        for (int i = newVdms.size() - 1; i >= 0; i--) {
            daoVdm.addVdm(newVdms.get(i));
        }
        daoVdm.keepOnlyLastVdms(quantityVdm);
        daoVdm.commitTransaction();
        handler.obtainMessage(VDMS_DOWNLOADED, newVdms.size(), 0).sendToTarget();
        daoVdm.close();
        oldVdms.clear();
        newVdms.clear();
    }

    private void downloadDtc() {
        DaoDtc daoDtc = new DaoDtc(context);
        daoDtc.open();
        daoDtc.startTransaction();
        int noLast = daoDtc.getLastDtcNumber();
        int quantityDownloaded = 0;
        pageLoop:
        for (int pageNumber = 1; ; pageNumber++) {
            List<Dtc> dtcs;
            try {
                dtcs = Parser.getDtcPage(pageNumber);
            } catch (NotExistingUrlException | WebPageChangedException e) {
                daoDtc.rollbackTransaction();
                daoDtc.close();
                handler.obtainMessage(PARSING_ERROR_DTC).sendToTarget();
                return;
            }

            // Loop on the DTCs of the web page:
            for (Dtc dtc : dtcs) {
                if (dtc.getNumber() <= noLast) {
                    break pageLoop;
                }
                quantityDownloaded++;
                daoDtc.addDtc(dtc);
                handler.obtainMessage(DOWNLOADING_PROGRESS, DTC, quantityDownloaded).sendToTarget();
                if (quantityDownloaded >= quantityDtc) {
                    break pageLoop;
                }
            }
        }
        daoDtc.keepOnlyLastDtcs(quantityDtc);
        daoDtc.commitTransaction();
        handler.obtainMessage(DTCS_DOWNLOADED, quantityDownloaded, 0).sendToTarget();
        daoDtc.close();
    }

    private void downloadNsf() {
        DaoNsf daoNsf = new DaoNsf(context);
        daoNsf.open();
        daoNsf.startTransaction();
        long noLast = daoNsf.getLastNsfNumber();
        int numberDownloaded = 0;
        pageLoop:
        for (int pageNumber = 1; ; pageNumber++) {
            List<Nsf> nsfs;
            try {
                nsfs = Parser.getNsfPage(pageNumber);
            } catch (NotExistingUrlException | WebPageChangedException e) {
                daoNsf.rollbackTransaction();
                daoNsf.close();
                handler.obtainMessage(PARSING_ERROR_NSF).sendToTarget();
                return;
            }

            // Loop on the NSFs of the web page:
            for (Nsf nsf : nsfs) {
                if (nsf.getNumber() <= noLast) {
                    break pageLoop;
                }
                numberDownloaded++;
                daoNsf.addNsf(nsf);
                handler.obtainMessage(DOWNLOADING_PROGRESS, NSF, numberDownloaded).sendToTarget();
                if (numberDownloaded >= quantityNsf) {
                    break pageLoop;
                }
            }
        }
        daoNsf.keepOnlyLastNsfs(quantityNsf);
        daoNsf.commitTransaction();
        handler.obtainMessage(NSFS_DOWNLOADED, numberDownloaded, 0).sendToTarget();
        daoNsf.close();
    }

    public void update() {
        download.set(true);
    }
}
