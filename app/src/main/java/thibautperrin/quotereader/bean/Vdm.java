package thibautperrin.quotereader.bean;


import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class Vdm implements Comparable<Vdm> {
    private final int index;
    private final String content;
    private final String endUrl;

    public Vdm(@NotNull String content, @NotNull String endUrl) {
        this.index = -1;
        this.content = content;
        this.endUrl = endUrl;
    }

    public Vdm(int index, @NotNull String content, @NotNull String endUrl) {
        this.index = index;
        this.content = content;
        this.endUrl = endUrl;
    }

    public String getContent() {
        return content;
    }

    public String getEndUrl() {
        return endUrl;
    }

    public String getUrl() {
        return "http://www.viedemerde.fr" + endUrl;
    }

    @Override
    public String toString() {
        return "VDM : " + content + "\n(" + getUrl() + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Vdm && this.content.equals(((Vdm) o).content);
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
