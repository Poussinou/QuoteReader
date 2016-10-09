package thibautperrin.quotereader.bean;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class Dtc implements Comparable<Dtc>{
    private final List<Sentence> content;
    private final int number;

    public Dtc(@NotNull List<Sentence> content, int number) {
        this.content = content;
        this.number = number;
    }

    public @NotNull List<Sentence> getContent() {
        return content;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "DTC #" + number + " : " + content;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Dtc && this.number == ((Dtc) o).number;
    }

    @Override
    public int compareTo(@NonNull Dtc another) {
        if (this.number > another.number) {
            return -1;
        } else if (this.number < another.number) {
            return 1;
        } else {
            return 0;
        }
    }

    public String getUrl() {
        return "http://www.danstonchat.com/" + number + ".html";
    }
}
