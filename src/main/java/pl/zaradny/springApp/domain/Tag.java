package pl.zaradny.springApp.domain;

import com.google.common.base.Preconditions;
import pl.zaradny.springApp.exceptions.EmptyTagException;

import java.util.Objects;

public final class Tag {

    private final String name;

    private Tag(String name) {
        this.name = name;
    }

    public static Tag build(String name){
        try {
            Preconditions.checkNotNull(name);
            Preconditions.checkArgument(name != "");
        }catch (IllegalArgumentException | NullPointerException e) {
            throw new EmptyTagException();
        }
        return new Tag(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
