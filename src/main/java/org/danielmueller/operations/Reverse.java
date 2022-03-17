package org.danielmueller.operations;

public final class Reverse implements DataOperable
{
    private static Reverse instance;

    private Reverse()
    {

    }

    @Override
    public Object dispatch(final Class<?> clazz, final Object object)
    {
        if (String.class.equals(clazz))
        {
            return reverse((String) object);
        }

        if (Integer.class.equals(clazz))
        {
            return reverse((int) object);
        }

        if (Double.class.equals(clazz))
        {
            return reverse((double) object);
        }

        return reverse(object);
    }

    public Object reverse(Object o)
    {
        throw new UnsupportedOperationException("Unsupported dispatch for class: " + o.getClass());
    }

    public String reverse(String s)
    {
        String reverseString = new StringBuilder(s)
                .reverse()
                .toString();

        if (reverseString.charAt(0) == '0')
        {
            reverseString = reverseString.substring(1);
        }

        if (reverseString.charAt(reverseString.length() - 1) == '-')
        {
            reverseString = "-" + reverseString.substring(0, reverseString.length() - 1);
        }

        return reverseString;
    }

    public int reverse(int i)
    {
        return Integer.parseInt(reverse(String.valueOf(i)));
    }

    public double reverse(double d)
    {
        return Double.parseDouble(reverse(String.valueOf(d)));
    }

    public static Reverse getInstance()
    {
        if (instance == null)
        {
            instance = new Reverse();
        }

        return instance;
    }
}
