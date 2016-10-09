package thibautperrin.quotereader.reader.parserException;

/**
 * This exception is thrown when the parsed website
 * changed in a way that we can't parse it in the same way as before.
 */
public class WebPageChangedException extends Exception {
    public WebPageChangedException(String detailMessage, String url) {
        super(detailMessage + "(URL: " + url + ")");
    }

    public WebPageChangedException(String detailMessage, String url, Throwable throwable) {
        super(detailMessage + "(URL: " + url + ")", throwable);
    }
}
