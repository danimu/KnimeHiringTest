package org.danielmueller.operations;

import java.util.Objects;

public final class Capitalize implements DataOperable
{
    private static Capitalize instance;

    private Capitalize()
    {

    }

    @Override
    public Object dispatch(Class<?> clazz, Object object)
    {
        if (String.class.equals(clazz))
        {
            return capitalize((String) object);
        }

        return capitalize(object);
    }

    public Object capitalize(Object o)
    {
        throw new UnsupportedOperationException("Unsupported dispatch for class: " + o.getClass());
    }

    public String capitalize(String s)
    {
        return s.toUpperCase();
    }

    public static Capitalize getInstance()
    {
        if (Objects.isNull(instance))
        {
            instance = new Capitalize();
        }

        return instance;
    }
}
