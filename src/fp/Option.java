package fp;

import fp.functions.Supplier;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * User: jooyunghan
 * Date: 4/7/15 2:00 AM
 */
public class Option<T> {
    private final T value;

    private static final Option<?> NONE = new Option<Object>(null);

    @Override
    public String toString() {
        return isPresent() ? "Option.some(" + value + '}' : "Option.none";
    }


    public Option(T value) {
        this.value = value;
    }

    public static <T> Option<T> none() {
        return (Option<T>) NONE;
    }

    public static <T> Option<T> some(T value) {
        return new Option<>(value);
    }

    public boolean isPresent() {
        return value != null;
    }

    public <S> Option<S> map(Function<T, S> f) {
        return isPresent() ? some(f.apply(value)) : none();
    }

    public <S> Option<S> flatMap(Function<T, Option<S>> f) {
        return isPresent() ? f.apply(value) : none();
    }

    public Option<T> filter(Predicate<T> f) {
        return isPresent() && f.test(value) ? this : none();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Option<?> option = (Option<?>) o;

        return !(value != null ? !value.equals(option.value) : option.value != null);

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    public Option<T> orElse(Supplier<Option<T>> f) {
        return isPresent()? this: f.get();
    }

    public T getOrElse(Supplier<T> f) {
        return isPresent()? value: f.get();
    }
}