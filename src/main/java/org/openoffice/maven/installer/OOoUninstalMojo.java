package org.openoffice.maven.installer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
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
     * @required
     */
    private File ooo;

    /**
     * OOo SDK installation where the build tools are located.
     * 
     * @parameter
     * @required
     */
    private File sdk;

    /**
     * <p>This method install an openoffice plugin package to the specified
     * openoffice installation</p>
     * 
     * @throws MojoExecutionException
     *             if there is a problem during the packaging execution.
     * @throws MojoFailureException
     *             if the packaging can't be done.
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        ConfigurationManager.setOOo(ooo);
        getLog().debug("OpenOffice.org used: " + ooo.getAbsolutePath());

        ConfigurationManager.setSdk(sdk);
        getLog().debug("OpenOffice.org SDK used: " + sdk.getAbsolutePath());

        Artifact unoPlugin = null;
        for (Artifact attachedArtifact : attachedArtifacts) {
            if ("zip".equals(FilenameUtils.getExtension(attachedArtifact.getFile().getPath()))) {
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
            String[] cmd = new String[] { unopkg, //
                    "remove", //
                    unoPluginFile.getCanonicalPath(), //
            };

            getLog().info("Installing plugin to OOo... please wait");
            Process process = ConfigurationManager.runTool(cmd);
            { // read std input
                String message = "";
                BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = buffer.readLine();
                while (null != line) {
                    message += line + "\n";
                    line = buffer.readLine();
                }
                if (message.length() > 0)
                    getLog().info(message);
            }
            
            int returnCode = process.exitValue();
            boolean success = returnCode == 0;
            
            if (success)
                getLog().info("Plugin installed successfully");
            else
                throw new MojoExecutionException("undpkg renurned in error. Code: " + returnCode);
        } catch (Exception e) {
            throw new MojoExecutionException("Error while installing package to OOo.", e);
        }
    }

}
