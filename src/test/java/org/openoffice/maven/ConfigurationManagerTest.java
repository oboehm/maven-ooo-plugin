/*************************************************************************
 * ConfigurationManagerTest.java
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Test;


/**
 * JUnit test for ConfigurationManager.
 * 
 * @author oliver
 * @since 1.1.1 (30.07.2010)
 */
public final class ConfigurationManagerTest extends AbstractTest {
    
    private static final Log log = new SystemStreamLog();
    
//    /**
//     * The path to OpenOffice home is set up here. At the moment this is
//     * hardcoded for my MacBook. Better approach would be to use a system
//     * property.
//     */
//    @BeforeClass
//    public static void setUpBeforeClass() {
//        ConfigurationManager.setOOo(new File("/opt/ooo/OpenOffice.org.app"));
//        ConfigurationManager.setSdk(new File("/opt/ooo/OpenOffice.org3.2_SDK"));
//    }

    /**
     * Test method for {@link org.openoffice.maven.ConfigurationManager#getOOoTypesFile()}.
     */
    @Test
    public void testGetOOoTypesFile() {
        File typesFile = new File(ConfigurationManager.getOOoTypesFile());
        assertTrue(typesFile + " not found", typesFile.exists());
    }
    
    /**
     * Test method for {@link org.openoffice.maven.ConfigurationManager#getOffapiTypesFile()}.
     */
    @Test
    public void testGetOffapiTypesFile() {
        File typesFile = new File(ConfigurationManager.getOffapiTypesFile());
        assertTrue(typesFile + " not found", typesFile.exists());
    }
    
    /**
     * Test method for {@link ConfigurationManager#getIdlDir()}.
     */
    @Test
    public void testGetIdlDir() {
        File idlDir = ConfigurationManager.getIdlDir();
        assertNotNull(idlDir);
        assertTrue(idlDir + " is no directory", idlDir.isDirectory());
    }
    
    /**
     * Test method for {@link ConfigurationManager#runTool(String)}.
     */
    @Test
    public synchronized void testRunTool() throws Exception {
        Process process = ConfigurationManager.runTool("date");
        assertEquals(0, process.waitFor());
    }
    
    /**
     * Test method for {@link ConfigurationManager#runTool(String)}.
     */
    @Test
    public synchronized void testRunIdlc() throws Exception {
        log.info("running 'idcl -h'...");
        Process process = ConfigurationManager.runTool("idlc", "-h");
        InputStream istream = process.getInputStream();
        log.info(IOUtils.toString(istream));
        assertEquals(0, process.waitFor());
    }

    /**
     * Test method for {@link ConfigurationManager#runTool(String)}.
     */
    @Test
    public synchronized void testRunRegmerge() {
        log.info("running 'regmerge -h'...");
        try {
            ConfigurationManager.runTool("regmerge", "-h");
        } catch (Exception e) {
            String msg = e.getMessage();
            assertTrue(msg, msg.contains("unknown option"));
        }
    }

}
