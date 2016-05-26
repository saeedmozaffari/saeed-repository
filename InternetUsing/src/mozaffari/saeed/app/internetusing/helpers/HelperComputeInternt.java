package mozaffari.saeed.app.internetusing.helpers;

public class HelperComputeInternt {

    public static String internetUsedCompute(int amountDownload) {
        String download = amountDownload + " کیلوبایت";
        int downloadDivided;
        int dataDownloadShow;
        if (amountDownload > 1024) {
            downloadDivided = amountDownload % 1024;
            dataDownloadShow = amountDownload / 1024;
            download = dataDownloadShow + " مگابایت ," + downloadDivided + " کیلوبایت";
        }
        return download;
    }
}
