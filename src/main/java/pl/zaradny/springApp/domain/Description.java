package pl.zaradny.springApp.domain;

public final class Description {

    private final String text;

    public Description(String text) {
        this.text = text;
    }

    public Description(DescriptionDto descriptionDto) {
        this.text = descriptionDto.getText();
    }

    public String getText() {
        return text;
    }
}

