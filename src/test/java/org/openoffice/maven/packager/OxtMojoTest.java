/*************************************************************************
 * AbstractOxtMojoTest.java
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

package org.openoffice.maven.packager;

import java.io.File;
import java.util.ArrayList;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.openoffice.maven.AbstractMojoTest;

/**
 * JUnit test for OxtMojo class.
 * 
 * @author oliver (oliver.boehm@agentes.de)
 * @since 1.1.1 (13.08.2010)
 */
public final class OxtMojoTest extends AbstractMojoTest {

    /**
     * Set up the mojo.
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        mojo = (OxtMojo) lookupMojo("oxt", testPom);
        assertNotNull(mojo);
        this.setUpMojo();
    }
    
    /**
     * Test method for {@link tOxtMojo#execute()}.
     *
     * @throws MojoExecutionException the mojo execution exception
     * @throws MojoFailureException the mojo failure exception
     * @throws IllegalAccessException the illegal access exception
     */
    public void testExecute() throws MojoExecutionException, MojoFailureException, IllegalAccessException {
        mojo.execute();
    }
    
    protected void setUpMojo() throws IllegalAccessException {
        super.setUpMojo();
        setUpAbstractOxtMojo();
        setVariableValueToObject(mojo, "attachedArtifacts", new ArrayList<Artifact>());
        File classesDir = new File(this.getTargetDir(), "classes");
        setVariableValueToObject(mojo, "classesDirectory", classesDir);
        setVariableValueToObject(mojo, "defaultManifestFile", new File(classesDir, "META-INF/MANIFEST.MF"));
    }
    
    private void setUpAbstractOxtMojo() throws IllegalAccessException {
        setVariableValueToObject(mojo, "finalName", "testFinalName");
        setVariableValueToObject(mojo, "jarArchiver", new JarArchiver());
        setUpProject4Mojo();
    }

    private void setUpProject4Mojo() throws IllegalAccessException {
        File baseDir = new File(getBasedir(), "src/main/resources/archetype-resources");
        File pomFile = new File(baseDir, "pom.xml");
        try {
            MavenProject project = new MavenProject();
            String groupId = "org.openoffice.dev.tests";
            String artifactId = "ooo-ext-test";
            Artifact artifact = new DefaultArtifact(groupId, artifactId,
                    VersionRange.createFromVersion("1.1.1-SNAPSHOT"), "test", "type", "classifier", null);
            project.addAttachedArtifact(artifact);
            project.setArtifact(artifact);
            project.setGroupId(groupId);
            project.setArtifactId(artifactId);
            project.setBasedir(baseDir);
            project.setFile(pomFile);
            setVariableValueToObject(mojo, "project", project);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

}
