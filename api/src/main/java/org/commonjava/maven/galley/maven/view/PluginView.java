package org.commonjava.maven.galley.maven.view;

import org.commonjava.maven.galley.maven.GalleyMavenException;
import org.commonjava.maven.galley.maven.defaults.MavenPluginDefaults;
import org.w3c.dom.Element;

public class PluginView
    extends ProjectVersionRefView
{

    private final MavenPluginDefaults pluginDefaults;

    protected PluginView( final MavenPomView pomView, final Element element, final MavenPluginDefaults pluginDefaults )
    {
        super( pomView, element, "pluginManagement/plugins/plugin" );
        this.pluginDefaults = pluginDefaults;
    }

    public boolean isManaged()
    {
        return pomView.resolveXPathToNodeFrom( element, "ancestor::pluginManagement" ) != null;
    }

    @Override
    public synchronized String getVersion()
        throws GalleyMavenException
    {
        if ( super.getVersion() == null )
        {
            setVersion( pluginDefaults.getDefaultVersion( getGroupId(), getArtifactId() ) );
        }

        return super.getVersion();
    }

    @Override
    public synchronized String getGroupId()
    {
        final String gid = super.getGroupId();
        if ( gid == null )
        {
            setGroupId( pluginDefaults.getDefaultGroupId( getArtifactId() ) );
        }

        return super.getGroupId();
    }

}
