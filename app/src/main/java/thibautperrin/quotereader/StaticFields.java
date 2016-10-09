package thibautperrin.quotereader;

/**
 * This file contains database file names and "what" codes for handler.
 */
public class StaticFields {
    public static final String VDMS_DATABASE_FILE = "vdms.db";
    public static final String NSFS_DATABASE_FILE = "nsfs.db";
    public static final String DTCS_DATABASE_FILE = "dtcs.db";

    public static final int VDMS_DOWNLOADED = 2;
    public static final int DTCS_DOWNLOADED = 4;
    public static final int NSFS_DOWNLOADED = 6;
    public static final int PARSING_ERROR_VDM = 7;
    public static final int PARSING_ERROR_DTC = 8;
    public static final int PARSING_ERROR_NSF = 9;
}
