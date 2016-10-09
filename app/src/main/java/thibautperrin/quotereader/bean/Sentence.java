package thibautperrin.quotereader.bean;

import org.jetbrains.annotations.NotNull;

/**
 * A DTC is a dialog made of several sentences.
 */
public class Sentence {
    private final String content;
    private final String author;

    public Sentence(@NotNull String author, @NotNull String content) {
        this.author = author;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return author + " " + content + "\n";
    }
}
