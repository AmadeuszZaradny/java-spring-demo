package pl.zaradny.springApp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageDto {
    private final String url;


    @JsonCreator
    public ImageDto(@JsonProperty("url") String url) {
        this.url = url;
    }

    public ImageDto(Image image){
        if(image != null) {
            this.url = image.getUrl().toString();
        }else{
            this.url = null;
        }
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageDto imageDto = (ImageDto) o;
        return Objects.equals(url, imageDto.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
