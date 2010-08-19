package org.openoffice.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public abstract class AbstractMojoTest extends AbstractMojoTestCase {

    protected static final File TEST_POM = new File(getBasedir(), "src/main/resources/archetype-resources/pom.xml");
    protected static final File OUTPUT_DIRECTORY = new File(getTargetDir(), "ooo");
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
    
    protected void setUpMojo() throws IllegalAccessException {
        setVariableValueToObject(mojo, "ooo", Environment.getOfficeHome());
        setVariableValueToObject(mojo, "sdk", Environment.getOoSdkHome());
        setUpTargetDir();
    }

    private void setUpTargetDir() throws IllegalAccessException {
        File buildDir = getTargetDir();
        setVariableValueToObject(mojo, "directory", buildDir);
        setVariableValueToObject(mojo, "outputDirectory", OUTPUT_DIRECTORY);
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