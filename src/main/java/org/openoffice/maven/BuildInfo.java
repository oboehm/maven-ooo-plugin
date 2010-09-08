/*************************************************************************
 * BuildInfo.java
 *
 * The Contents of this file are made available subject to the terms of
 * either of the GNU Lesser General Public License Version 2.1
 * 
 * GNU Lesser General Public License Version 2.1
 * =============================================
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 *
 * Contributor(s): oliver.boehm@agentes.de
 ************************************************************************/

package org.openoffice.maven;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;

/**
 * This class provides some information when the plugin was build like the
 * version, the build number or date.
 * 
 * @author oliver (oliver.boehm@agentes.de)
 * @since 1.1.1 (08.09.2010)
 */
public final class BuildInfo {
    
    /** The Constant log. */
    private static final Log log = ConfigurationManager.getLog();
    
    /** The Constant infos. */
    private static final Properties infos = loadBuildProperties();
    
    /** The Constant KEY_BUILD_ID. */
    private static final String KEY_BUILD_ID = "build.id";

    /** The Constant KEY_VERSION. */
    private static final String KEY_VERSION = "version";
    
    /**
     * Gets the version.
     *
     * @return the version
     */
    public static String getVersion() {
        return infos.getProperty(KEY_VERSION, "unknown");
    }
    
    /**
     * Gets the build id.
     *
     * @return the build id
     */
    public static String getBuildId() {
        return infos.getProperty(KEY_BUILD_ID, "unknown");
    }
    
    /**
     * Load build properties.
     *
     * @return the properties
     */
    private static Properties loadBuildProperties() {
        InputStream istream = BuildInfo.class.getResourceAsStream("build.properties");
        Properties props = new Properties();
        try {
            props.load(istream);
            return props;
        } catch (IOException e) {
            log.warn("Can't load build.properties - using internal defaults!");
            return getDefaultProperties();
        } finally {
            IOUtils.closeQuietly(istream);
        }
    }
    
    /**
     * Gets the default properties.
     *
     * @return the default properties
     */
    private static Properties getDefaultProperties() {
        Properties props = new Properties();
        props.put(KEY_BUILD_ID, "unknown");
        props.put(KEY_VERSION, "unknown");
        return props;
    }

    /**
     * Returns the build id.
     *
     * @return the build id
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getVersion() + " " + getBuildId();
    }

}
