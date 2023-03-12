package ru.yandex.practicum.filmorate;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

public class Util {
    public static <T> List<T> emptyIfNull(List<T> list) {
        return (list == null) ? emptyList() : list;
    }

    public static <T> Set<T> emptyIfNull(Set<T> set) {
        return (set == null) ? emptySet() : set;
    }

    public static String emptyIfNull(String s) {
        return (s == null) ? "" : s;
    }
}
