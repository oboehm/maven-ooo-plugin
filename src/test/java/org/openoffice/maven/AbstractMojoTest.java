package org.openoffice.maven;

import java.io.File;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;

public abstract class AbstractMojoTest extends AbstractMojoTestCase {

    protected static final File TEST_POM = new File(getBasedir(), "src/main/resources/archetype-resources/pom.xml");
    protected static final String TEST_GROUP_ID = "org.openoffice.dev.tests";
    protected static final String TEST_ARTIFACT_ID = "ooo-ext-test";
    protected static final File OUTPUT_DIRECTORY = new File(getTargetDir(), "ooo");
    protected static final String TEST_FINAL_NAME = "testFinalName";
    protected static final File OXT_FILE = new File(OUTPUT_DIRECTORY, TEST_FINAL_NAME + ".oxt");
    protected AbstractMojo mojo;

    /**
     * Set up the mojo.
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        AbstractTest.setUpEnvironment();
    }
    
    protected void setUpTargetDir() throws IllegalAccessException {
        File buildDir = getTargetDir();
        setVariableValueToObject(mojo, "directory", buildDir);
        setVariableValueToObject(mojo, "outputDirectory", OUTPUT_DIRECTORY);
    }
    
    protected void setUpProject4Mojo() throws IllegalAccessException {
        File baseDir = new File(getBasedir(), "src/main/resources/archetype-resources");
        File pomFile = new File(baseDir, "pom.xml");
        try {
            MavenProject project = new MavenProject();
            Artifact artifact = createArtifact();
            project.addAttachedArtifact(artifact);
            project.setArtifact(artifact);
            project.setGroupId(TEST_GROUP_ID);
            project.setArtifactId(TEST_ARTIFACT_ID);
            project.setBasedir(baseDir);
            project.setFile(pomFile);
            setVariableValueToObject(mojo, "project", project);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    protected static Artifact createArtifact() {
        Artifact artifact = new DefaultArtifact(TEST_GROUP_ID, TEST_ARTIFACT_ID,
                VersionRange.createFromVersion("1.1.1-SNAPSHOT"), "test", "type", "classifier", null);
        artifact.setFile(OXT_FILE);
        return artifact;
    }

    /**
     * Gets the target dir.
     *
     * @return the target dir
     */
    protected static File getTargetDir() {
        return new File(getBasedir(), "target");
    }

}