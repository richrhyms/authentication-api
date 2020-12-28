package com.richotaru.authenticationapi.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */
public interface AppRepository {

    <E> long count(Class<E> type);

    @Transactional(readOnly = true)
    <E> Optional<E> findFirst(Class<E> type);

    <E> Long countByProperty(Class<E> type, String propertyName, Object propertyValue);

    <E> Optional<E> findById(Class<E> type, Object id);

    <E> E persist(E e);

    <E> E merge(E e);

    void remove(Object e);

    <E> List<E> findByIds(Class<E> type, Collection<Long> ids);

    <T> T unproxy(Class<T> tClass, T entity);

    <E> List<E> getByProperty(Class<E> tClass, String propertyName, Object propertyValue);

//    <E> JPAQuery<E> startJPAQuery();

    <E> JPAQuery<E> startJPAQuery(EntityPath<E> entityPath);

    <E> QueryResults<E> fetchResults(JPAQuery<E> query);

    <E> List<E> fetchResultList(JPAQuery<E> query);

    <E> Optional<E> fetchOne(JPAQuery<E> query);

    @Transactional(readOnly = true)
    <E> Optional<E> findFirstByField(Class<E> type, String columnName, Object value);

    <E, T> QueryResults<T> fetchResults(JPAQuery<E> query, QueryResultTransformer<E, T> transformer);
}
