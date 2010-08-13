package org.openoffice.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.openoffice.maven.idl.IdlBuilderMojo;

public abstract class AbstractMojoTest extends AbstractMojoTestCase {

    protected AbstractMojo mojo = new IdlBuilderMojo();

    /**
     * Set up the mojo.
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        if (mojo == null) {
            throw new IllegalStateException("you must first initialize mojo!");
        }
        super.setUp();
        AbstractTest.setUpEnvironment();
        this.setUpMojo();
    }
    
    private void setUpMojo() throws IllegalAccessException {
        setVariableValueToObject(mojo, "ooo", Environment.getOfficeHome());
        setVariableValueToObject(mojo, "sdk", Environment.getOoSdkHome());
        setUpResources();
        setUpTargetDir();
    }

    private void setUpTargetDir() throws IllegalAccessException {
        File buildDir = this.getTargetDir();
        setVariableValueToObject(mojo, "directory", buildDir);
        setVariableValueToObject(mojo, "outputDirectory", new File(buildDir, "ooo"));
    }
    
    /**
     * Gets the target dir.
     *
     * @return the target dir
     */
    protected File getTargetDir() {
        return new File(getBasedir(), "target");
    }

    private void setUpResources() throws IllegalAccessException {
        List<Resource> resources = new ArrayList<Resource>();
        Resource rsc = new Resource();
        rsc.setDirectory("src/test/resources");
        resources.add(rsc);
        setVariableValueToObject(mojo, "resources", resources);
    }

}