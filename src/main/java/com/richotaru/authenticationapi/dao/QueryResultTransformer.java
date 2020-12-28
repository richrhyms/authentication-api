package com.richotaru.authenticationapi.dao;

public interface QueryResultTransformer<E, T> {

    T transaform(E e);
}
