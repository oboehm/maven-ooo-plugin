/*************************************************************************
 * BuildInfoTest.java
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

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * JUnit test for BuildInfo class.
 * 
 * @author oliver (oliver.boehm@agentes.de)
 * @since 1.1.1 (08.09.2010)
 */
public class BuildInfoTest {
    
    private static final Log log = LogFactory.getLog(BuildInfoTest.class);

    /**
     * Test method for {@link org.openoffice.maven.BuildInfo#getVersion()}.
     */
    @Test
    public void testGetVersion() {
        String version = BuildInfo.getVersion();
        log.info("version=" + version);
        assertNotNull(version);
        assertFalse("unknown version", version.equalsIgnoreCase("unknown"));
    }

    /**
     * Test method for {@link org.openoffice.maven.BuildInfo#getBuildId()}.
     */
    @Test
    public void testGetBuildDate() {
        String value = BuildInfo.getBuildId();
        log.info("build.id=" + value);
        assertNotNull(value);
        assertFalse("unknown build id", value.equalsIgnoreCase("unknown"));
    }

    /**
     * Test method for {@link org.openoffice.maven.BuildInfo#toString()}.
     */
    @Test
    public void testToString() {
        String id = new BuildInfo().toString();
        log.info("toString(): " + id);
        assertNotNull(id);
        assertFalse(id, id.contains("unknown"));
    }

}
