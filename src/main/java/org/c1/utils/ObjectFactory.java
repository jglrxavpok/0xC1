package org.c1.utils;

public interface ObjectFactory<T>
{
    T createNew(Class<T> typeClass);
}
