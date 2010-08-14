package org.openoffice.maven.installer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.openoffice.maven.ConfigurationManager;

/**
 * @author Frederic Morin <frederic.morin.8@gmail.com>
 * @goal install
 * @phase install
 */
public class OOoInstalMojo extends AbstractMojo {

    /**
     * The Maven project.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

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
     * <p>
     * This method install an openoffice plugin package to the specified
     * openoffice installation
     * </p>
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

        File unoPluginFile = project.getArtifact().getFile();
        if (!unoPluginFile.exists()) {
            throw new MojoExecutionException("Could not find plugin artefact [" + unoPluginFile + "]");
        }

        try {
            String os = System.getProperty("os.name").toLowerCase();
            String unopkg = "unopkg";
            if (os.startsWith("windows"))
                unopkg = "unopkg.com";
            String[] cmd = new String[] { unopkg, //
                            "add", //
                            "-f", //
                            unoPluginFile.getCanonicalPath(), //
            };

            getLog().info("Installing plugin to OOo... please wait");
            Process process = ConfigurationManager.runTool(cmd);
            String message = "";
            { // read std input
                BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = buffer.readLine();
                while (null != line) {
                    message += line + "\n";
                    line = buffer.readLine();
                }
                if (message.length() > 0)
                    getLog().info(message);
                buffer = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                line = buffer.readLine();
                while (null != line) {
                    message += line + "\n";
                    line = buffer.readLine();
                }
                if (message.length() > 0)
                    getLog().info(message);
            }

            int returnCode = process.waitFor();
            boolean success = returnCode == 0;

            if (success) {
                getLog().info("Plugin installed successfully");
            } else {
                System.out.println("\nRunning: [" + StringUtils.join(cmd, " ") + "]");
                throw new MojoExecutionException("unopkg renurned in error. Code: " + returnCode + "\n" + //
                        message);
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Error while installing package to OOo.", e);
        }
    }
}
