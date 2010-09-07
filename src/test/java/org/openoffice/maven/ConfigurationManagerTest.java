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

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.junit.Test;


/**
 * JUnit test for ConfigurationManager.
 * 
 * @author oliver
 * @since 1.1.1 (30.07.2010)
 */
public final class ConfigurationManagerTest extends AbstractTest {
    
    private static final Log log = LogFactory.getLog(ConfigurationManagerTest.class);
    
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
    
//    /**
//     * Test method for {@link ConfigurationManager#runTool(String)}.
//     * Because it does not run on Windows (I think the date command
//     * waits here for input) the test can be deactivated.
//     */
//    @Test
//    public synchronized void testRunTool() throws Exception {
//        Process process = ConfigurationManager.runTool("date");
//        assertEquals(0, process.waitFor());
//    }
    
    /**
     * Test method for {@link ConfigurationManager#runCommand(String)}.
     */
    @Test
    public synchronized void testRunCommand() throws CommandLineException {
        String command = SystemUtils.IS_OS_WINDOWS ? "dir" : "date";
        log.info("running " + command + "...");
        int ret = ConfigurationManager.runCommand(command);
        assertEquals(0, ret);
    }
    
//    /**
//     * Test method for {@link ConfigurationManager#runTool(String)}.
//     */
//    @Test
//    public synchronized void testRunIdlc() throws Exception {
//        log.info("running 'idcl -h'...");
//        Process process = ConfigurationManager.runTool("idlc", "-h");
//        InputStream istream = process.getInputStream();
//        log.info(IOUtils.toString(istream));
//        assertEquals(0, process.waitFor());
//    }

    /**
     * Test method for {@link ConfigurationManager#runCommand(String...)}.
     */
    @Test
    public synchronized void testRunIdlcCommand() throws Exception {
        log.info("running 'idcl -h'...");
        int ret = ConfigurationManager.runCommand("idlc", "-h");
        assertEquals(0, ret);
    }

    /**
     * Test to see if "regmerge" works.
     *
     * @throws CommandLineException the command line exception
     */
    @Test
    public synchronized void testRunRegmerge() throws CommandLineException {
        log.info("running 'regmerge -h'...");
        ConfigurationManager.runCommand("regmerge", "-h");
    }

    /**
     * Test method for {@link ConfigurationManager#runCommand(String)} with the
     * unopkg command. 'unopkg' is normally placed in the search path of OOo
     * (and not the OOo SDK).
     * 
     * @throws CommandLineException the command line exception
     */
    @Test
    public synchronized void testRunUnopkg() throws CommandLineException {
        log.info("running 'unopkg -V'...");
        ConfigurationManager.runCommand("unopkg", "-V");
    }

}
