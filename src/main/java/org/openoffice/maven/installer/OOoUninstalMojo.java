package org.openoffice.maven.installer;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.*;
import org.openoffice.maven.ConfigurationManager;

/**
 * 
 * @author Frederic Morin <frederic.morin.8@gmail.com>
 * 
 * @goal uninstall
 */
public class OOoUninstalMojo extends AbstractMojo {

    /**
     * @parameter default-value="${project.attachedArtifacts}
     * @required
     * @readonly
     */
    private List<Artifact> attachedArtifacts;

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
     * <p>This method uninstall an openoffice plugin package.</p>
     * 
     * @throws MojoExecutionException
     *             if there is a problem during the packaging execution.
     * @throws MojoFailureException
     *             if the packaging can't be done.
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        ooo = ConfigurationManager.initOOo(ooo);
        getLog().info("OpenOffice.org used: " + ooo.getAbsolutePath());

        sdk = ConfigurationManager.initSdk(sdk);
        getLog().info("OpenOffice.org SDK used: " + sdk.getAbsolutePath());

        Artifact unoPlugin = null;
        for (Artifact attachedArtifact : attachedArtifacts) {
            String extension = FilenameUtils.getExtension(attachedArtifact.getFile().getPath());
            if ("zip".equals(extension) || "oxt".equals(extension)) {
                unoPlugin = attachedArtifact;
                break;
            }
        }

        if (unoPlugin == null) {
            throw new MojoExecutionException("Could not find plugin artefact (.zip)");
        }
        
        File unoPluginFile = unoPlugin.getFile();

        try {
            String os = System.getProperty("os.name").toLowerCase();
            String unopkg = "unopkg";
            if (os.startsWith("windows"))
                unopkg = "unopkg.com";
//            String[] cmd = new String[] { unopkg, //
//                    "remove", //
//                    unoPluginFile.getCanonicalPath(), //
//            };

            getLog().info("Uninstalling plugin to OOo... please wait");
//            Process process = ConfigurationManager.runTool(cmd);
//            { // read std input
//                String message = "";
//                BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String line = buffer.readLine();
//                while (null != line) {
//                    message += line + "\n";
//                    line = buffer.readLine();
//                }
//                if (message.length() > 0)
//                    getLog().info(message);
//            }
//            
//            int returnCode = process.exitValue();
//            boolean success = returnCode == 0;
//            
//            if (success)
//                getLog().info("Plugin installed successfully");
//            else
//                throw new MojoExecutionException("undpkg renurned in error. Code: " + returnCode);
            int returnCode = ConfigurationManager.runCommand(unopkg, "remove", unoPluginFile.getCanonicalPath());            
            if (returnCode == 0) {
                getLog().info("Plugin installed successfully");
            } else {
                throw new MojoExecutionException("'unopkg remove " + unoPluginFile + "' returned with " + returnCode);
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Error while uninstalling package to OOo.", e);
        }
    }

}
