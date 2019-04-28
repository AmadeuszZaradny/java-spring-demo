package pl.zaradny.springApp.domain;

import java.net.URL;
import java.util.Objects;

public final class Image {

    private final URL url;

    private Image(URL url) {
        this.url = url;
    }

    public static Image build(URL url){
        return new Image(url);
    }

    public URL getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(url, image.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
