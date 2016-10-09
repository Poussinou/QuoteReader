package thibautperrin.quotereader.bean;


import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class Vdm implements Comparable<Vdm>{
    private final int index;
    private final String content;
    private final long number;
    private final String endUrl;

    public Vdm(@NotNull String content, long number, @NotNull String endUrl) {
        this.index = -1;
        this.content = content;
        this.number = number;
        this.endUrl = endUrl;
    }

    public Vdm(int index, @NotNull String content, long number, @NotNull String endUrl) {
        this.index = index;
        this.content = content;
        this.number = number;
        this.endUrl = endUrl;
    }

    public String getContent() {
        return content;
    }

    public long getNumber() {
        return number;
    }

    public String getEndUrl() {
        return endUrl;
    }

    public String getUrl() {
        return "http://www.viedemerde.fr" + endUrl;
    }

    @Override
    public String toString() {
        return "VDM #" + number + " : " + content + "\n(" + getUrl() + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Vdm && this.number == ((Vdm) o).number;
    }

    @Override
    public int compareTo(@NonNull Vdm another) {
        if (this.index > another.index) {
            return -1;
        } else if (this.index < another.index) {
            return 1;
        } else {
            return 0;
        }
    }
}
