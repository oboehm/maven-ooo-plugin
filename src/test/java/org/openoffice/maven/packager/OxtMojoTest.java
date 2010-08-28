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

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.openoffice.maven.AbstractMojoTest;

/**
 * JUnit test for OxtMojo class.
 * 
 * @author oliver (oliver.boehm@agentes.de)
 * @since 1.1.1 (13.08.2010)
 */
public final class OxtMojoTest extends AbstractMojoTest {
    
    private static final Log log = LogFactory.getLog(OxtMojoTest.class);
    private static final File OXT_DIR = new File(getBasedir(), "src/main/resources/archetype-resources/src/main/oxt");
    
    /**
     * Set up the mojo.
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        mojo = (OxtMojo) lookupMojo("oxt", TEST_POM);
        assertNotNull(mojo);
        this.setUpMojo();
    }
    
    protected void setUpMojo() throws IllegalAccessException {
        this.setUpTargetDir();
        setUpAbstractOxtMojo();
        setVariableValueToObject(mojo, "attachedArtifacts", new ArrayList<Artifact>());
        File classesDir = new File(getTargetDir(), "classes");
        setVariableValueToObject(mojo, "classesDirectory", classesDir);
        setVariableValueToObject(mojo, "defaultManifestFile", new File(classesDir, "META-INF/MANIFEST.MF"));
    }
    
    private void setUpAbstractOxtMojo() throws IllegalAccessException {
        setVariableValueToObject(mojo, "finalName", TEST_FINAL_NAME);
        setVariableValueToObject(mojo, "jarArchiver", new JarArchiver());
        setUpOxtDir();
        setUpProject4Mojo();
    }
    
    private void setUpOxtDir() throws IllegalAccessException {
        setVariableValueToObject(mojo, "oxtDir", OXT_DIR);
    }

    /**
     * Test method for {@link OxtMojo#execute()}.
     *
     * @throws MojoExecutionException the mojo execution exception
     * @throws MojoFailureException the mojo failure exception
     * @throws IllegalAccessException the illegal access exception
     */
    public void testExecute() throws MojoExecutionException, MojoFailureException, IllegalAccessException {
        mojo.execute();
    }
    
    /**
     * Test create archive - just to see what happens.
     *
     * @throws MojoExecutionException the mojo execution exception
     * @throws ZipException the zip exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void testCreateArchive() throws MojoExecutionException, ZipException, IOException {
        OxtMojo oxtMojo = (OxtMojo) mojo;
        File archive = oxtMojo.createArchive();
        log.info("created archive: " + archive);
        assertTrue(archive + " is not a file", archive.isFile());
    }
    
    /**
     * We want to exclude "Readme.txt" from the created OXT file. This is
     * tested here.
     * <br/>
     * NOTE: It is marked as broken because we must first prepare UnoPackage
     *       to handle includes/excludes
     *
     * @throws MojoExecutionException the mojo execution exception
     * @throws MojoFailureException the mojo failure exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ZipException the zip exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void testExcludes() throws MojoExecutionException, MojoFailureException, IllegalAccessException, ZipException, IOException {
        String[] excludes = { "README.txt" };
        setVariableValueToObject(mojo, "excludes", excludes);
        mojo.execute();
        checkArchive();
        ZipFile zip = new ZipFile(OXT_FILE);
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.getName().equals(excludes[0])) {
                fail(entry.getName() + " should be excluded!");
            }
        }
    }
    
    /**
     * Test manifest recognition. If there is already a manifest file available
     * this one should be used.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws IllegalAccessException the illegal access exception
     * @throws MojoExecutionException the mojo execution exception
     * @throws MojoFailureException the mojo failure exception
     */
    public void testManifestRecognition() throws IOException, IllegalAccessException, MojoExecutionException,
            MojoFailureException {
        String manifestContent = "<Test-Manifest/>";
        File testDir = createOxtDirWithManifest(manifestContent);
        setVariableValueToObject(mojo, "oxtDir", testDir);
        mojo.execute();
        checkArchive();
        String content = getManifestContent();
        assertEquals(manifestContent, content);
    }

    private static File createOxtDirWithManifest(String manifestContent) throws IOException {
        File testDir = new File(OUTPUT_DIRECTORY, "oxt");
        FileUtils.copyDirectory(OXT_DIR, testDir);
        File metaInfDir = new File(testDir, "META-INF");
        metaInfDir.mkdir();
        assertTrue(metaInfDir.getAbsolutePath(), metaInfDir.isDirectory());
        File manifestFile = new File(metaInfDir, "manifest.xml");
        FileUtils.writeStringToFile(manifestFile, manifestContent);
        return testDir;
    }
    
    private static void checkArchive() throws ZipException, IOException {
        assertTrue(OXT_FILE + " is not a file", OXT_FILE.isFile());
        ZipFile zip = new ZipFile(OXT_FILE);
        ZipEntry entry = zip.getEntry("META-INF/manifest.xml");
        assertNotNull("no manifest inside", entry);
        zip.close();
    }
    
    private static String getManifestContent() throws ZipException, IOException {
        ZipFile zip = new ZipFile(OXT_FILE);
        try {
            ZipEntry entry = zip.getEntry("META-INF/manifest.xml");
            InputStream istream = zip.getInputStream(entry);
            return IOUtils.toString(istream);
        } finally {
            zip.close();
        }
    }

}
