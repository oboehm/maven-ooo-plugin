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
        super.setUp();
        AbstractTest.setUpEnvironment();
    }
    
    protected final void setUpMojo() throws IllegalAccessException {
        setVariableValueToObject(mojo, "ooo", Environment.getOfficeHome());
        setVariableValueToObject(mojo, "sdk", Environment.getOoSdkHome());
        initIdlDir();
        initResources();
    }

    private void initIdlDir() throws IllegalAccessException {
        File idlDir = new File(getBasedir(), "src/main/resources/archetype-resources/src/main/resources/idl");
        setVariableValueToObject(mojo, "idlDir", idlDir);
    }

    private void initResources() throws IllegalAccessException {
        List<Resource> resources = new ArrayList<Resource>();
        Resource rsc = new Resource();
        rsc.setDirectory("src/test/resources");
        resources.add(rsc);
        setVariableValueToObject(mojo, "resources", resources);
    }

}