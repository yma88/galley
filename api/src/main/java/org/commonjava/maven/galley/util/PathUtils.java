package org.commonjava.maven.galley.util;


public final class PathUtils
{

    public static final String ROOT = "/";

    private static final String[] ROOT_ARRY = { ROOT };

    private PathUtils()
    {
    }

    public static String[] parentPath( final String path )
    {
        final String[] parts = path.split( "/" );
        if ( parts.length == 1 )
        {
            return ROOT_ARRY;
        }
        else
        {
            final String[] parentParts = new String[parts.length - 1];
            System.arraycopy( parts, 0, parentParts, 0, parentParts.length );
            return parentParts;
        }
    }

    public static String normalize( final String... path )
    {
        if ( path == null || path.length < 1 )
        {
            return ROOT;
        }

        final StringBuilder sb = new StringBuilder();
        for ( String part : path )
        {
            if ( part.length() < 1 || "/".equals( part ) )
            {
                continue;
            }

            while ( part.charAt( 0 ) == '/' )
            {
                if ( part.length() < 2 )
                {
                    continue;
                }

                part = part.substring( 1 );
            }

            while ( part.charAt( part.length() - 1 ) == '/' )
            {
                if ( part.length() < 2 )
                {
                    continue;
                }

                part = part.substring( 0, part.length() - 1 );
            }

            if ( sb.length() > 0 )
            {
                sb.append( '/' );
            }

            sb.append( part );
        }

        return sb.toString();
    }

}
