package com.datn.commonbase.client;

public interface Function<R, T> {
    R apply(T t) throws Exception;
}
