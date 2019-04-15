package pl.zaradny.springApp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class DescriptionDto {

    private final String text;

    @JsonCreator
    public DescriptionDto(@JsonProperty("text") String text) {
        this.text = text;
    }

    public DescriptionDto(Description description){
        if(description != null) {
            this.text = description.getText();
        }else {
            this.text = null;
        }
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DescriptionDto that = (DescriptionDto) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
