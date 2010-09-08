/*************************************************************************
 *
 * $RCSfile: IdlBuilderMojo.java,v $
 *
 * $Revision: 1.1 $
 *
 * last change: $Author: cedricbosdo $ $Date: 2007/10/08 18:35:15 $
 *
 * The Contents of this file are made available subject to the terms of
 * either of the GNU Lesser General Public License Version 2.1
 *
 * Sun Microsystems Inc., October, 2000
 *
 *
 * GNU Lesser General Public License Version 2.1
 * =============================================
 * Copyright 2000 by Sun Microsystems, Inc.
 * 901 San Antonio Road, Palo Alto, CA 94303, USA
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 * 
 * The Initial Developer of the Original Code is: Sun Microsystems, Inc..
 *
 * Copyright: 2002 by Sun Microsystems, Inc.
 *
 * All Rights Reserved.
 *
 * Contributor(s): Cedric Bosdonnat
 *
 *
 ************************************************************************/
package org.openoffice.maven.idl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.*;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.openoffice.maven.BuildInfo;
import org.openoffice.maven.ConfigurationManager;
import org.openoffice.maven.utils.VisitableFile;

/**
 * Runs the OOo SDK tools to generate the classes file from the IDL files.
 * 
 * @goal build-idl
 * @phase generate-sources
 * 
 * @author Cedric Bosdonnat
 */
public class IdlBuilderMojo extends AbstractMojo {
    
    /**
     * Instantiates a new idl builder mojo.
     */
    public IdlBuilderMojo() {
        super();
        ConfigurationManager.setLog(this.getLog());
    }

    static final class PackageNameFilter implements FilenameFilter {
        public boolean accept(File pDir, String pName) {
            if ("CVS".equals(pName)) {
                return false;
            }
            return Pattern.matches(IDENTIFIER_REGEX, pName);
        }
    }

    private static final String IDENTIFIER_REGEX = "[_a-zA-Z0-9]+";
    
    /**
     * This is where build results go.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     * @readonly
     */
    private File directory;

    /**
     * This is where compiled classes go.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     * @readonly
     */
    private File outputDirectory;

    /**
     * OOo instance to build the extension against.
     * 
     * @parameter
     */
    private File ooo;

    /**
     * OOo SDK installation where the build tools are located.
     * 
     * @parameter
     */
    private File sdk;
    
    /**
     * IDL directory where the IDL sources can be found
     * 
     * @parameter expression="src/main/resources"
     */
    private File idlDir;

    /**
     * Main method of the idl builder Mojo.
     * 
     * <p>This method runs the following tools:
     *   <ol>
     *     <li><code>idlc</code></li>
     *     <li><code>regmerge</code></li>
     *     <li><code>javamaker</code></li>
     *   </ol>
     * </p>
     * 
     * @throws MojoExecutionException is never thrown by this 
     *             implementation.
     * @throws MojoFailureException is thrown if one of the tools
     *             execution fails.
     */
    public void execute() throws MojoExecutionException, 
                                 MojoFailureException {

        try {
            setUp();
            
            // Check if the IDL folder is present
            File idlDir = ConfigurationManager.getIdlDir();
            if (idlDir == null) {
                throw new MojoFailureException(
                    "No IDL folder found among in the resources");
            }
            
            this.getLog().info("IDL folder used: " + idlDir.getPath());

            this.getLog().info("Building IDL files");
            // Build each IDL file
            File idl = ConfigurationManager.getIdlDir();
            VisitableFile idlSources = new VisitableFile(idl.getPath());
            IdlcVisitor idlVisitor = new IdlcVisitor();
            idlSources.accept(idlVisitor);
            
            // Continue only if there were idl files to build
            if (idlVisitor.hasBuildIdlFile()) {

                this.getLog().info("Merging into types.rdb file");
                // Merge the URD files into a types.rdb file
                VisitableFile urdFiles = new VisitableFile(
                       ConfigurationManager.getUrdDir());
                urdFiles.accept(new RegmergeVisitor());

                this.getLog().info("Generating classes from the types.rdb file");
                // Run javamaker against the types.rdb file
                generatesClasses();
            } else {
                this.getLog().warn("No idl file to build");
            }
            
        } catch (Exception e) {
            this.getLog().error("Error during idl-build", e);
            //throw new MojoFailureException("Please check the above errors");
            throw new MojoFailureException("Error during idl-build", e);
        }
    }

    private void setUp() {
        ConfigurationManager.setLog(this.getLog());
        this.getLog().info("Build id: " + new BuildInfo());
        ooo = ConfigurationManager.initOOo(ooo);
        this.getLog().info("OpenOffice.org used: " + ooo.getAbsolutePath());
        sdk = ConfigurationManager.initSdk(sdk);
        this.getLog().info("OpenOffice.org SDK used: " + sdk.getAbsolutePath());
        ConfigurationManager.setIdlDir(idlDir);
        this.getLog().info("idlDir used: " + idlDir.getAbsolutePath());
        ConfigurationManager.setOutput(directory);
        ConfigurationManager.setClassesOutput(outputDirectory);
    }

    /**
     * Generates the java classes from the project <code>types.rdb</code>.
     * 
     * @throws Exception if anything wrong happens
     * @see {@link "http://wiki.services.openoffice.org/wiki/Documentation/DevGuide/WritingUNO/Generating_Source_Code_from_UNOIDL_Definitions"}
     * @see {@link "http://api.openoffice.org/servlets/ReadMsg?listName=dev&msgNo=19839"}
     */
    private void generatesClasses() throws Exception {
        
        String typesFile = ConfigurationManager.getTypesFile();
        
        if (!new File(typesFile).exists()) {
            throw new Exception(
                    "No " + typesFile + " file build: check previous errors");
        }

//        // Compute the command
//        String commandPattern = "javamaker -T{0}.* -nD -Gc -BUCR -O " +
//                "\"{1}\" \"{2}\" -X\"{3}\" -X\"{4}\"";
//
//        String classesDir = ConfigurationManager.getClassesOutput().
//            getPath();
//        String oooTypesFile = ConfigurationManager.getOOoTypesFile();
//
//        // Guess the root module
//        String rootModule = guessRootModule();
//
//        String[] args = {
//            rootModule, 
//            classesDir, 
//            typesFile, 
//            oooTypesFile,
//            ConfigurationManager.getOffapiTypesFile()
//        };
//        String command = MessageFormat.format(commandPattern, (Object[])args);
//
//        this.getLog().info("Running command: " + command);
//        
//        // Run the javamaker command
//        ConfigurationManager.runTool(command);
        
        String classesDir = ConfigurationManager.getClassesOutput().getPath();
        String oooTypesFile = ConfigurationManager.getOOoTypesFile();
        String rootModule = guessRootModule();
        int n = ConfigurationManager.runCommand("javamaker", "-T" + rootModule + ".*", "-nD", "-Gc", "-BUCR", "-O",
                classesDir, typesFile, "-X" + oooTypesFile, "-X" + ConfigurationManager.getOffapiTypesFile());
        if (n != 0) {
            throw new CommandLineException("javamaker exits with " + n);
        }
    }

    /**
     * Guess the root module of the OOo extension API.
     * 
     * <p>The folders containing the IDL files has to follow the
     * same hierarchy than the IDL modules declared in the IDL files.</p>
     * 
     * <p>The root module is the path of IDL folders which are the only
     * children of their parent. A valid IDL folder respects the 
     * regular expression defined by {@link #IDENTIFIER_REGEX}.</p> 
     * 
     * @return the guessed module or an empty string if it can't be guessed.
     */
    private String guessRootModule() {
        
        File idlDir = ConfigurationManager.getIdlDir();
        
        int childCount = 1;
        File currentFile = idlDir;
        
        while (childCount == 1) {
            
            // List only the valid children
            String[] children = currentFile.list(
                    new PackageNameFilter());
            
            childCount = children.length;
            if (childCount == 1) {
                currentFile = new File(currentFile, children[0]);
            }
        }
        
        if (currentFile.equals(idlDir)) {
            this.getLog().warn("no children found in " + idlDir);
            return "";
        }
        String modulePath = currentFile.getPath().substring(
                idlDir.getPath().length() + 1);
        modulePath = FilenameUtils.separatorsToUnix(modulePath);
        String rootModule = modulePath.replaceAll("/", ".");
        return rootModule;
    }
}
