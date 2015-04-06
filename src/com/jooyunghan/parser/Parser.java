package com.jooyunghan.parser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jooyunghan.parser.Lists.cons;
import static com.jooyunghan.parser.Pair.pair;
import static com.jooyunghan.parser.Triple.triple;
import static java.util.Optional.empty;

public interface Parser<T> {
    Optional<Pair<T, String>> parse(String s);

    Parser<Object> fail = (s) -> empty();
    Parser<Character> item = (s) -> s.isEmpty()
            ? empty()
            : Optional.of(pair(s.charAt(0), s.substring(1)));

    static Parser<Character> char_(char c) {
        return item.filter(x -> x == c);
    }

    Parser<Character> digit = item.filter(Character::isDigit);
    Parser<Character> letter = item.filter(Character::isLetter);
    Parser<Character> space = item.filter(Character::isWhitespace);

    Parser<Integer> integer = regex("^(\\+|-)?\\d+").map(Integer::valueOf);
    Parser<Double> double_ = regex("^([-+]?(\\d+\\.?\\d*|\\d*\\.?\\d+))").map(Double::valueOf);

    static Parser<String> string(String prefix) {
        return (s) -> s.startsWith(prefix) ? Optional.of(pair(prefix, s.substring(prefix.length()))) : empty();
    }

    static Parser<String> regex(String regex) {
        Pattern pattern = Pattern.compile(regex);
        return (s) -> {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find() && matcher.start() == 0) {
                return Optional.of(pair(matcher.group(), s.substring(matcher.end())));
            } else {
                return empty();
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

    default Parser<T> or(T defaultValue) {
        return (s) -> {
            Optional<Pair<T, String>> result = parse(s);
            return result.isPresent() ? result : Optional.of(pair(defaultValue, s));
        };
    }

    default Parser<T> or(Parser<T> alt) {
        return (s) -> {
            Optional<Pair<T, String>> result = parse(s);
            return result.isPresent() ? result : alt.parse(s);
        };
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
        return (s) -> parse(s).flatMap(pair -> lookAhead.parse(pair._2).map(ignore -> pair));
    }
}
