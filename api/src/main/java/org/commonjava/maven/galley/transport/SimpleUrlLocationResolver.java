package org.commonjava.maven.galley.transport;

import java.util.Iterator;
import java.util.List;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Named;

import org.commonjava.maven.galley.TransferException;
import org.commonjava.maven.galley.model.Location;
import org.commonjava.maven.galley.model.SimpleLocation;
import org.commonjava.maven.galley.spi.transport.LocationExpander;
import org.commonjava.maven.galley.spi.transport.LocationResolver;
import org.commonjava.maven.galley.spi.transport.Transport;
import org.commonjava.maven.galley.spi.transport.TransportManager;

/**
 * Implementation of {@link LocationResolver} that constructs a {@link SimpleLocation}, then uses the following
 * validation logic:<br/>
 * 
 * <ol>
 *   <li>Use the provided {@link LocationExpander} to expand to one or more concrete locations, or return 
 *          null if none are returned.</li>
 *   <li>Use the provided {@link TransportManager} to validate that all expanded locations are valid.</li>
 *   <li>Return the list of expanded locations</li>
 * </ol>
 * 
 * @author jdcasey
 */
@Alternative
@Named
public class SimpleUrlLocationResolver
    implements LocationResolver
{

    @Inject
    private LocationExpander locationExpander;

    @Inject
    private TransportManager transportManager;

    protected SimpleUrlLocationResolver()
    {
    }

    public SimpleUrlLocationResolver( final LocationExpander locationExpander, final TransportManager transportManager )
    {
        this.locationExpander = locationExpander;
        this.transportManager = transportManager;
    }
    
    @Override
    public final List<Location> resolve( final String spec )
        throws TransferException
    {
        final List<Location> locations = locationExpander.expand( new SimpleLocation( spec ) );
        if ( locations == null || locations.isEmpty() )
        {
            throw new TransferException( "Invalid location: '%s'. Location expansion returned no results.", spec );
        }

        for ( final Iterator<Location> iterator = locations.iterator(); iterator.hasNext(); )
        {
            final Location loc = iterator.next();

            // normally, this will probably throw an exception if no transport is available.
            // in case it's not, remove the location if the transport is null.
            final Transport transport = transportManager.getTransport( loc );
            if ( transport == null )
            {
                iterator.remove();
            }
        }

        if ( locations == null || locations.isEmpty() )
        {
            throw new TransferException( "Invalid location: '%s'. No transports available for expanded locations.",
                                         spec );
        }

        return locations;
    }

}
