package org.danielmueller.operations;

public interface DataOperable
{
    Object dispatch(final Class<?> clazz, final Object object);
}

