package mozaffari.saeed.app.internetusing.helpers;

public class HelperComputeInternt {

    public static String internetUsedComputeFa(int amountDownload) {
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


    public static String internetUsedComputeEn(int amountDownload) {
        String download = amountDownload + " kb";
        int downloadDivided;
        int dataDownloadShow;
        if (amountDownload > 1024) {
            downloadDivided = amountDownload % 1024;
            dataDownloadShow = amountDownload / 1024;
            //download = dataDownloadShow + " مگابایت ," + downloadDivided + " کیلوبایت";
            download = dataDownloadShow + "." + downloadDivided + " mb";
        }
        return download;
    }
}
