/*************************************************************************
 * IdlBuilderMojoTest.java
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

package org.openoffice.maven.idl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.openoffice.maven.AbstractMojoTest;
import org.openoffice.maven.idl.IdlBuilderMojo.PackageNameFilter;

/**
 * JUnit test for IdlBuilderMojo.
 * 
 * @author oliver
 * @since 1.2 (02.08.2010)
 */
public final class IdlBuilderMojoTest extends AbstractMojoTest {
    
    /**
     * Sets up the Mojo.
     *
     * @throws Exception the exception
     * @see org.openoffice.maven.AbstractMojoTest#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mojo = new IdlBuilderMojo();
        this.setUpTargetDir();
    }

    /**
     * This unit test was copied from
     * {@link "http://maven.apache.org/plugin-developers/plugin-testing.html"}.
     * But it does not work with maven-plugin-testing-harness 1.1. You need at
     * least version 1.2 of this plugin.
     *
     * @throws Exception in case of error
     */
    public void testMojoGoal() throws Exception {
        File testPom = new File(getBasedir(), "src/main/resources/archetype-resources/pom.xml");
        assertTrue(testPom + " is not a file", testPom.isFile());
        IdlBuilderMojo mojo = (IdlBuilderMojo) lookupMojo("build-idl", testPom);
        assertNotNull(mojo);
    }

    /**
     * This unit test was copied from
     * {@link "https://cwiki.apache.org/confluence/display/MAVENOLD/Maven+Plugin+Harness"}.
     *
     * @throws Exception in case of error
     */
    public void testSettingMojoVariables() throws Exception {
        File value = new File("/opt/");
        setVariableValueToObject(mojo, "ooo", value);
        assertEquals(value, (File) getVariableValueFromObject(mojo, "ooo"));
    }

    /**
     * Test method for {@link org.openoffice.maven.idl.IdlBuilderMojo#execute()}.
     *
     * @throws IllegalAccessException the illegal access exception
     * @throws MojoExecutionException the mojo execution exception
     * @throws MojoFailureException the mojo failure exception
     * @throws IOException if "types.rdb" can't be copied
     */
    public void testExecute() throws IllegalAccessException, MojoExecutionException, MojoFailureException, IOException {
        setUpIdlDir();
        File buildDir = getTargetDir();
        File oooBuildDir = new File(buildDir, "ooo");
        FileUtils.deleteDirectory(oooBuildDir);
        assertFalse(oooBuildDir + " can't be deleted", oooBuildDir.exists());
        FileUtils.copyFile(new File("src/test/resources/types.rdb"), new File(buildDir, "types.rdb"));
        mojo.execute();
        File expectedClassFile = new File(oooBuildDir, "hello/WorldInterface.class");
        assertTrue(expectedClassFile + " was not created", expectedClassFile.exists());
    }

    private void setUpIdlDir() throws IllegalAccessException {
        File idlDir = new File(getBasedir(), "src/main/resources/archetype-resources/src/main/idl");
        setVariableValueToObject(mojo, "idlDir", idlDir);
    }

    /**
     * If there is a directory "CVS" inside a package dir this is probably not
     * a package name but a directory from CVS. So we should ignore this
     * directory (also package names should be in lower case so we could
     * safely ignore it).
     */
    public void testPackageNameFilter() {
        IdlBuilderMojo.PackageNameFilter filter = new PackageNameFilter();
        File dir = new File(getBasedir());
        assertTrue("'de' is a valid package name", filter.accept(dir, "de"));
        assertFalse("'CVS' should be considered as invalid", filter.accept(dir, "CVS"));
    }

}
