/*************************************************************************
 * OOoInstalMojoTest.java
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
package org.openoffice.maven.installer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.*;
import org.openoffice.maven.AbstractMojoTest;

/**
 * JUnit test for OOoInstalMojo.
 * 
 * @author oliver (oliver.boehm@agentes.de)
 * @since 1.1.1 (28.08.2010)
 */
public class OOoInstalMojoTest extends AbstractMojoTest {
    
    /**
     * Set up the mojo.
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        this.setUp("install");
    }
    
    /**
     * Sets the mojo for phase "install" or "uninstall".
     *
     * @param phase "install" or "uninstall"
     * @throws Exception the exception
     */
    protected void setUp(final String phase) throws Exception {
        super.setUp();
        mojo = (AbstractMojo) lookupMojo(phase, TEST_POM);
        assertNotNull(mojo);
        this.setUpProject4Mojo();
    }

    /**
     * Test method for {@link OOoInstalMojo#execute()}.
     *
     * @throws MojoExecutionException the mojo execution exception
     * @throws MojoFailureException the mojo failure exception
     * @throws IllegalAccessException the illegal access exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void testExecute() throws MojoExecutionException, MojoFailureException, IllegalAccessException, IOException {
        FileUtils.copyFile(new File("src/test/resources/testFinalName.oxt"), new File(getTargetDir(),
                "ooo/testFinalName.oxt"));
        mojo.execute();
    }

}
