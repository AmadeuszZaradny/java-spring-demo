package pl.zaradny.springApp.domain;

import pl.zaradny.springApp.exceptions.TooLongProductDescriptionException;

public final class Description {

    private final String text;

    private Description(String text) {
        this.text = text;
    }

    public static Description build(String text){
        if(text.length() > 400) {
            throw new TooLongProductDescriptionException();
        }
        return new Description(text);
    }

    public String getText() {
        return text;
    }
}

