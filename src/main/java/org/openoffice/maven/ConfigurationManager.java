/*************************************************************************
 *
 * $RCSfile: ConfigurationManager.java,v $
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
package org.openoffice.maven;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.model.Resource;

/**
 * Stores the Mojo configuration for use in the build visitors.
 * 
 * @author Cedric Bosdonnat
 *
 */
public class ConfigurationManager {

    /**
     * Path to the URD directory in the output directory.
     */
    private static final String URD_DIR = "urd";

    /**
     * Path to the <code>types.rdb</code> file in the output directory.
     */
    private static final String TYPES_FILE = "types.rdb";

    /**
     * The size of the command array needed for a tool process execution.
     */
    private static final int COMMAND_ARRAY_SIZE = 3;

    /**
     * The path to the IDL directory in the resources directory.
     */
    private static final String IDL_DIR = "idl";

    
    private static File sOoo;
    
    private static File sSdk;
    
    private static File sOutput;

    private static File sClassesOutput;
    
    private static File sResources;
    
    /**
     * @return the folder where OpenOffice.org is installed.
     */
    public static File getOOo() {
        return sOoo;
    }
    
    /**
     * @return the OpenOffice.org <code>types.rdb</code> file path
     */
    public static String getOOoTypesFile() {
        
        String os = System.getProperty("os.name").toLowerCase();
        String oooTypes = new File(getOOo(), "/program/types.rdb").getPath();
        if (os.startsWith("macos")) {
            oooTypes = new File(getOOo(), "/Contents/MacOS/types.rdb").
                getPath();
        }
        
        return oooTypes;
    }
    
    /**
     * @return the folder where OpenOffice.org SDK is installed.
     */
    public static File getSdk() {
        return sSdk;
    }
    
    /**
     * @return the folder where the classes files are generated.
     */
    public static File getClassesOutput() {
        if (!sClassesOutput.exists()) {
            sClassesOutput.mkdirs();
        }
        return sClassesOutput;
    }

    /**
     * @return the path to the folder where URD files should be generated
     */
    public static String getUrdDir() {
        return new File(sOutput, URD_DIR).getPath();
    }

    /**
     * @return the path to the generated <code>types.rdb</code>.
     */
    public static String getTypesFile() {
        return new File(sOutput, TYPES_FILE).getPath();
    }
    
    /**
     * @return the path to the folder containing the IDL files to build
     *         or <code>null</code> if no IDL folder has been found.
     */
    public static File getIdlDir() {
        File idl = null;
        if (sResources != null) {
            idl = new File(sResources, IDL_DIR);
        }
        return idl;
    }
    
    /**
     * Sets the OpenOffice.org installation folder to use for the build.
     * 
     * @param pOoo the OpenOffice.org installation folder.
     */
    public static void setOOo(File pOoo) {
        sOoo = pOoo;
    }
    
    /**
     * Sets the OpenOffice.org SDK installation folder to use for the build.
     * 
     * @param pSdk the OpenOffice.org SDK installation folder.
     */
    public static void setSdk(File pSdk) {
        sSdk = pSdk;
    }
    
    /**
     * Sets the directory where the generated files should go.
     * 
     * @param pOutput the output directory.
     */
    public static void setOutput(File pOutput) {
        sOutput = pOutput;
    }
    
    /**
     * Sets the directory where the generated classes should go.
     * 
     * @param pOutputDirectory the classes output directory.
     */
    public static void setClassesOutput(File pOutputDirectory) {
        sClassesOutput = pOutputDirectory;
    }
    
    /**
     * Sets the directory containing the project resources.
     * 
     * <p>The first resource directory containing and idl sub-folder is
     * stored. All the other ones will not be taken into account.</p>
     * 
     * @param pResources the project resources configuration.
     */
    public static void setResources(List pResources) {
        Iterator iter = pResources.iterator();
        
        boolean found = false;
        while (iter.hasNext() && !found) {
            Resource resource = (Resource)iter.next();
            File resDir = new File(resource.getDirectory());
            
            if (resDir.exists() && resDir.isDirectory()) {
                // Look for the IDL folder
                File potentialIdlDir = new File(resDir, IDL_DIR);
                if (potentialIdlDir.exists() && potentialIdlDir.isDirectory()) {
                    sResources = resDir;
                    found = true;
                }
            }
        }
    }

    /**
     * Runs an OpenOffice.org SDK tool from a command line.
     * 
     * <p>The command line to execute is dependent of the running operating 
     * system. The tool executable should be located in the SDK platform's bin
     * folder.</p>
     * 
     * @param pCommand the command line to execute.
     * 
     * @return The process of the command line execution.
     * 
     * @throws Exception if anything wrong happens during the process creation.
     *             This exception isn't thrown if the process fails: 
     *             this should be handled by the method caller.
     */
    public static Process runTool(String pCommand) throws Exception {
        
        String os = System.getProperty("os.name").toLowerCase();
        String path_sep = System.getProperty("path.separator");
        
        String[] env = new String[0];
        String[] cmd = new String[COMMAND_ARRAY_SIZE];
        
        String sdkBin = getSdkBinPath(sSdk.getPath());
        
        if (os.startsWith("windows")) {
            // Windows environment
            env = new String[1];
            String oooLibs = new File(sOoo, "/program").getCanonicalPath();
            env[0] = "PATH=" + sdkBin + path_sep + oooLibs;
            
            if (os.startsWith("windows 9")) {
                cmd[0] = "command.com";
            } else {
                cmd[0] = "cmd.exe";
            }
            cmd[1] = "/C";
            cmd[2] = pCommand;
            
        } else if (os.startsWith("macos x")) {
            // MacOS environment
            env = new String[2];
            String oooLibs = new File(sOoo, "/Contents/MacOS").
                getCanonicalPath();
            env[0] = "PATH=" + sdkBin;
            env[1] = "DYLD_LIBRARY_PATH=" + oooLibs;
            
            cmd[0] = "sh";
            cmd[1] = "-c";
            cmd[2] = pCommand;
        } else {
            // *NIX environment
            env = new String[2];
            String oooLibs = new File(sOoo, "/program").getCanonicalPath();
            env[0] = "PATH=" + sdkBin;
            env[1] = "LD_LIBRARY_PATH=" + oooLibs;
            
            cmd[0] = "sh";
            cmd[1] = "-c";
            cmd[2] = pCommand;
        }
            
        // Create the process
        Process process = Runtime.getRuntime().exec(cmd, env);
        
        return process;
    }
    
    /**
     * Returns the path to the SDK binaries depending on the OS and the 
     * architecture.
     * 
     * @param pHome the OpenOffice.org SDK home
     * @return the full path to the SDK binaries
     */
    private static String getSdkBinPath(String pHome) {
        String path = null;
        
        // Get the OS and Architecture properties
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        
        if (os.startsWith("windows")) {
            path = "/windows/bin/";
        } else if (os.equals("solaris")) {
            if (arch.equals("sparc")) {
                path = "/solsparc/bin";
            } else {
                path = "/solintel/bin";
            }
        } else if (os.equals("macos x")) {
            path = "/macosx/bin";
        } else  {
            path = "/linux/bin";
        }
        
        return new File(pHome, path).getPath();
    }
}
