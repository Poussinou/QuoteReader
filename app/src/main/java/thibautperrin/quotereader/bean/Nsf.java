package thibautperrin.quotereader.bean;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class Nsf implements Comparable<Nsf>{
    private final String content;
    private final int number;
    private final String date;

    public Nsf(@NotNull String content, int number, @NotNull String date) {
        this.content = content;
        this.number = number;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public int getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return "http://www.nuitsansfolie.com/nsf/" + number;
    }

    @Override
    public String toString() {
        return "NSF #" + number + " (" + date + ") : " + content;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Nsf && this.number == ((Nsf) o).number;
    }

    @Override
    public int compareTo(@NonNull Nsf another) {
        if (this.number > another.number) {
            return -1;
        } else if (this.number < another.number) {
            return 1;
        } else {
            return 0;
        }
    }
}
