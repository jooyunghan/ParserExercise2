package fp.parser;

import fp.Option;
import fp.Pair;
import fp.Quadruple;
import fp.Triple;
import fp.functions.Consumer;
import fp.functions.Function;
import fp.functions.Predicate;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fp.Lists.cons;
import static fp.Option.none;
import static fp.Option.some;
import static fp.Pair.pair;
import static fp.Quadruple.quadruple;
import static fp.Triple.triple;

public interface Parser<T> {
    Option<Pair<T, String>> parse(String s);

    Parser<Object> fail = (s) -> none();
    Parser<Character> item = (s) -> s.isEmpty()
            ? none()
            : some(pair(s.charAt(0), s.substring(1)));
    Parser<String> rest = (s) -> some(pair(s, ""));

    static Parser<Character> char_(char c) {
        return item.filter(x -> x == c);
    }

    Parser<Character> digit = item.filter(Character::isDigit);
    Parser<Character> letter = item.filter(Character::isLetter);
    Parser<Character> space = item.filter(Character::isWhitespace);

    Parser<Integer> integer = regex("^(\\+|-)?\\d+").map(Integer::valueOf);
    Parser<Double> double_ = regex("^([-+]?(\\d+\\.?\\d*|\\d*\\.?\\d+))").map(Double::valueOf);

    static Parser<String> string(String prefix) {
        return (s) -> s.startsWith(prefix) ? some(pair(prefix, s.substring(prefix.length()))) : none();
    }

    static Parser<String> regex(String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return (s) -> {
            Matcher matcher = pattern.matcher(s);
            if (matcher.lookingAt()) {
                return some(pair(matcher.group(), s.substring(matcher.end())));
            } else {
                return none();
            }
        };
    }

    default <S> Parser<T> prefix(Parser<S> p) {
        return p.flatMap(ignore -> this);
    }

    default <S> Parser<List<T>> sepBy(Parser<S> sep) {
        Parser<T> prefixed = prefix(sep);
        return flatMap(t -> prefixed.many().map(ts -> cons(t, ts)));
    }

    default Parser<List<T>> many() {
        return many1().or(Collections.emptyList());
    }

    default Parser<List<T>> many1() {
        return flatMap(t -> many().map(ts -> cons(t, ts)));
    }

    static <T, S> Parser<Pair<T, S>> seq(Parser<T> p1, Parser<S> p2) {
        return p1.flatMap((T t) -> p2.map((S s) -> pair(t, s)));
    }

    static <T, S, R> Parser<Triple<T, S, R>> seq(Parser<T> p1, Parser<S> p2, Parser<R> p3) {
        return p1.flatMap((T t) -> p2.flatMap((S s) -> p3.map((R r) -> triple(t, s, r))));
    }

    static <A, B, C, D> Parser<Quadruple<A, B, C, D>> seq(Parser<A> p1, Parser<B> p2, Parser<C> p3, Parser<D> p4) {
        return p1.flatMap(a -> p2.flatMap(b -> p3.flatMap(c -> p4.map(d -> quadruple(a, b, c, d)))));
    }

    default Parser<T> or(T defaultValue) {
        return (s) -> parse(s).orElse(() -> some(pair(defaultValue, s)));
    }

    default Parser<T> or(Parser<T> alt) {
        return (s) -> parse(s).orElse(() -> alt.parse(s));
    }

    default <S> Parser<S> map(Function<T, S> f) {
        return (s) -> parse(s).map(pair -> pair(f.apply(pair._1), pair._2));
    }

    default Parser<T> filter(Predicate<T> p) {
        return (s) -> parse(s).filter(pair -> p.test(pair._1));
    }

    default <S> Parser<S> flatMap(Function<T, Parser<S>> f) {
        return (s) -> parse(s).flatMap(pair -> f.apply(pair._1).parse(pair._2));
    }

    default <S> Parser<T> followedBy(Parser<S> lookAhead) {
        return (s) -> parse(s).flatMap(pair ->
                lookAhead.parse(pair._2).isSome() ? some(pair) : none());
    }

    default <S> Parser<T> notFollowedBy(Parser<S> lookAhead) {
        return (s) -> parse(s).flatMap(pair ->
                lookAhead.parse(pair._2).isSome() ? none() : some(pair));
    }

    default Parser<T> tap(Consumer<T> f) {
        return map(t -> {
            f.accept(t);
            return t;
        });
    }
}
