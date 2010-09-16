/*************************************************************************
 * EnvironmentTest.java
 * The Contents of this file are made available subject to the terms of
 * either of the GNU Lesser General Public License Version 2.1
 * GNU Lesser General Public License Version 2.1
 * =============================================
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
 * Contributor(s): oliver.boehm@agentes.de
 ************************************************************************/

package org.openoffice.maven;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

/**
 * Test class for Environment.
 * 
 * @author oliver
 * @since 1.2 (31.07.2010)
 */
public class EnvironmentTest extends AbstractTest {

    /**
     * Test method for {@link org.openoffice.maven.Environment#getOfficeHome()}.
     */
    @Test
    public void testGetOfficeHome() {
        File dir = Environment.getOfficeHome();
        assertTrue(dir + " is no directory", dir.isDirectory());
    }

    /**
     * Test method for
     * {@link org.openoffice.maven.Environment#getOfficeBaseHome()}.
     */
    @Test
    public void testGetOfficeBaseHome() {
        File dir = Environment.getOfficeBaseHome();
        assertTrue(dir + " is no directory", dir.isDirectory());
    }

    /**
     * Test method for {@link org.openoffice.maven.Environment#getOoSdkHome()}.
     */
    @Test
    public void testGetOoSdkHome() {
        File dir = Environment.getOoSdkHome();
        assertTrue(dir + " is no directory", dir.isDirectory());
    }

    /**
     * Test method for
     * {@link org.openoffice.maven.Environment#getOoSdkUreHome()}.
     */
    @Test
    public void testGetOoSdkUreHome() {
        File dir = Environment.getOoSdkUreHome();
        assertTrue(dir + " is no directory", dir.isDirectory());
    }

    /**
     * Test method for
     * {@link org.openoffice.maven.Environment#getOoSdkUreLibDir()}.
     */
    @Test
    public void testGetOoSdkUreLibDir() {
        File dir = Environment.getOoSdkUreLibDir();
        assertTrue(dir + " is no directory", dir.isDirectory());
    }

    /**
     * Test method for
     * {@link org.openoffice.maven.Environment#getOoSdkUreBinDir()}.
     */
    @Test
    public void testGetOoSdkUreBinDir() {
        File dir = Environment.getOoSdkUreBinDir();
        assertTrue(dir + " is no directory", dir.isDirectory());
    }

    @Test
    public void guessOfficeHomeFromPATH() {
        String path = "/opt/OpenOffice.org/basis-link" + File.pathSeparator
                + "/opt/OpenOffice.org/Contents/basis-link/ure-link/bin" + File.pathSeparator
                + "/opt/OpenOffice.org/Contents/MacOS" + File.pathSeparator + "/opt/OpenOffice.org/Basis";
        File officeHome = Environment.guessOfficeHomeFromPATH(path);
        assertNotNull(officeHome);
        assertEquals(new File("/opt/OpenOffice.org"), officeHome);
    }

}
