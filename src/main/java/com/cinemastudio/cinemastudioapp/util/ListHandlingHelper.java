package com.cinemastudio.cinemastudioapp.util;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

public class ListHandlingHelper {
    public static <T> boolean listEqualsNotIncludingOrder(List<T> list1, List<T> list2) {
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }
}
