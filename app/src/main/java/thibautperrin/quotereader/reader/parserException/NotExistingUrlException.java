package thibautperrin.quotereader.reader.parserException;

/**
 * This exception is thrown when the website's URL tried by the parser doesn't exist.
 * It could be due to two main reasons:
 * --> The website changed.
 * --> We chose a page number too big or too low.
 */
public class NotExistingUrlException extends Exception {

    public NotExistingUrlException(String url, Throwable throwable) {
        super("Unable to connect to " + url, throwable);
    }
}
