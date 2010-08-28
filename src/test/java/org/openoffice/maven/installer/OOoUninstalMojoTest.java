/*************************************************************************
 * OOoUninstalMojoTest.java
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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.*;
import org.openoffice.maven.AbstractMojoTest;

/**
 * JUnit test for OOoUninstalMojo.
 * 
 * @author oliver (oliver.boehm@agentes.de)
 * @since 1.1.1 (28.08.2010)
 */
public class OOoUninstalMojoTest extends AbstractMojoTest {
    
    private static final Log log = LogFactory.getLog(OOoUninstalMojoTest.class);
    
    /**
     * Set up the mojo.
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        mojo = (AbstractMojo) lookupMojo("uninstall", TEST_POM);
        assertNotNull(mojo);
        Artifact artifact = createArtifact();
        List<Artifact> artifacts = new ArrayList<Artifact>(1);
        artifacts.add(artifact);
        setVariableValueToObject(mojo, "attachedArtifacts", artifacts);
    }
    
    /**
     * Test method for {@link OOoUninstalMojo#execute()}.
     *
     * @throws MojoFailureException the mojo failure exception
     * @throws IllegalAccessException the illegal access exception
     */
    public void testExecute() throws MojoFailureException, IllegalAccessException {
        try {
            mojo.execute();
            log.info("Uninstall was successful");
        } catch (MojoExecutionException canhappen) {
            log.info("Uninstall reports an error", canhappen);
        }
    }

}
