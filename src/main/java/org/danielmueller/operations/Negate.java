package org.danielmueller.operations;

public final class Negate implements DataOperable
{
    private static DataOperable instance;

    private Negate()
    {

    }

    @Override
    public Object dispatch(final Class<?> clazz, final Object object)
    {
        if (Integer.class.equals(clazz))
        {
            return neg((int) object);
        }

        if (Double.class.equals(clazz))
        {
            return neg((double) object);
        }

        return neg(object);
    }

    public Object neg(Object o)
    {
        throw new UnsupportedOperationException();
    }

    public double neg(double d)
    {
        return (-1) * d;
    }

    public int neg(int i)
    {
        return (-1) * i;
    }

    public static DataOperable getInstance()
    {
        if (instance == null)
        {
            instance = new Negate();
        }

        return instance;
    }
}
