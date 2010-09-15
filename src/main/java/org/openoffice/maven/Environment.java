/*************************************************************************
 * Environment.java
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

package org.openoffice.maven;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.maven.plugin.logging.Log;
import org.openoffice.maven.utils.FileFinder;

/**
 * This class handles the differences between the support OS.
 * For the names and initalization look at "setsdkenv_unix.sh" which is
 * delivered with the OpenOffice SDK.
 * <br/>
 * At the moment of writing only Mac OS X is support. Other OS'es
 * will follow.
 * 
 * @author oliver
 * @since 1.2 (31.07.2010)
 */
public final class Environment {
    
    private static final String OFFICE_HOME = "OFFICE_HOME";
    private static final String OFFICE_BASE_HOME = "OFFICE_BASE_HOME";
    private static final String OO_SDK_HOME = "OO_SDK_HOME";
    private static final String OO_SDK_URE_HOME = "OO_SDK_URE_HOME";
    private static File officeHome = guessOfficeHome();
    private static File officeBaseHome = getenvAsFile(OFFICE_BASE_HOME);
    private static File ooSdkHome = guessOoSdkHome();
    private static File ooSdkUreHome = getenvAsFile(OO_SDK_URE_HOME);
    
    /** Utility class - no need to instantiate it. */
    private Environment() {}
    
    private static final Log getLog() {
        return ConfigurationManager.getLog();
    }
    
    private static File guessOfficeHome() {
        File home = getenvAsFile(OFFICE_HOME);
        if (home != null) {
            return home;
        }
        home = guessOfficeHomeFromPATH();
        if (home != null) {
            return home;
        }
        if (SystemUtils.IS_OS_LINUX) {
            home = tryDirs("/opt/openoffice.org3", "/usr/lib/openoffice");
        } else if (SystemUtils.IS_OS_MAC) {
            home = tryDirs("/Applications/OpenOffice.org.app", "/opt/ooo/OpenOffice.org.app");
        } else if (SystemUtils.IS_OS_WINDOWS) {
            home = tryDirs("C:/programs/OpenOffice.org3", "C:/Programme/OpenOffice.org3",
                    "C:/programs/OpenOffice.org 3", "C:/Programme/OpenOffice.org 3",
                    "C:/Program Files (x86)/OpenOffice.org 3");
        } else {
            home = tryDirs("/opt/openoffice.org3");
        }
        if (home == null) {
            getLog().debug("office home not found - must be set via configuration '<ooo>...</ooo>'");
        }
        return home;
    }
    
    private static File guessOfficeHomeFromPATH() {
        String path = getenv("PATH");
        return guessOfficeHomeFromPATH(path);
    }

    /**
     * Guess office home from search path.
     * This method has default visibility for testing.
     *
     * @param path the path
     * @return the file
     */
    static File guessOfficeHomeFromPATH(String path) {
        String[] pathElements = StringUtils.split(path, File.pathSeparatorChar);
        String baseDirName = getBaseDirName();
        for (int i = 0; i < pathElements.length; i++) {
            String pathname = FilenameUtils.separatorsToUnix(pathElements[i]);
            pathname = FilenameUtils.normalizeNoEndSeparator(pathname);
            int n = pathname.lastIndexOf(baseDirName);
            if (n > 0) {
                return new File(pathname.substring(0, n));
            }
        }
        return null;
    }
    
    private static File guessOoSdkHome() {
        File home = getenvAsFile(OO_SDK_HOME);
        if (home != null) {
            return home;
        }
        if (SystemUtils.IS_OS_LINUX) {
            home = tryDirs("/opt/openoffice.org/basis3.2/sdk", "/usr/lib/openoffice/basis3.2/sdk");
        } else if (SystemUtils.IS_OS_MAC) {
            home = tryDirs("/Applications/OpenOffice.org3.2_SDK", "/opt/ooo/OpenOffice.org3.2_SDK");
        } else if (SystemUtils.IS_OS_WINDOWS) {
            home = tryDirs(officeHome + "/Basis/sdk", "C:/Programme/OOsdk", "C:/sdk/OpenOffice.org_3.2_SDK/sdk",
                    "C:/OO_SDK/sdk");
        } else {
            home = FileFinder.tryDirs(new File(officeHome, "Basis/sdk"), new File("/opt/openoffice.org/basis3.2/sdk"));
        }
        if (home == null) {
            getLog().debug("SDK home not found - must be set via configuration '<sdk>...</sdk>'");
        }
        return home;
    }
    
    private static File tryDirs(final String... dirnames) {
        for (int i = 0; i < dirnames.length; i++) {
            File dir = new File(dirnames[i]);
            if (dir.isDirectory()) {
                return dir;
            }
        }
        return null;
    }

    /**
     * Sets home directory of OpenOffice (OFFICE_HOME).
     *
     * @param dir home directory of OpenOffice
     */
    public static synchronized void setOfficeHome(File dir) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir + " is not a directory");
        }
        officeHome = dir;
    }
    
    /**
     * Returns the home directory of OpenOffice (OFFICE_HOME).
     * 
     * @return home directory of OpenOffice
     */
    public static synchronized File getOfficeHome() {
        if (officeHome == null) {
            throw new IllegalStateException("call setOfficeHome() if environment \"" + OFFICE_HOME + "\" not set");
        }
        return officeHome;
    }
    
    /**
     * Returns the base directory of OpenOffice (OFFICE_BASE_HOME).
     * 
     * @return base directory of OpenOffice
     */
    public static synchronized File getOfficeBaseHome() {
        if (officeBaseHome == null) {
            officeBaseHome = new File(getOfficeHome(), getBaseDirName());
        }
        return officeBaseHome;
    }

    private static String getBaseDirName() {
        if (SystemUtils.IS_OS_MAC) {
            return "Contents/basis-link";
        } else if (SystemUtils.IS_OS_WINDOWS) {
            return "Basis";
        } else {
            return "basis-link";
        }
    }

    /**
     * Sets home directory of OpenOffice SDK.
     *
     * @param dir home directory of OpenOffice SDK
     */
    public static synchronized void setOoSdkHome(File dir) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir + " is not a directory");
        }
        ooSdkHome = dir;
    }
    
    /**
     * Returns the home directory of OpenOffice SDK (OO_SDK_HOME).
     * @return home directory of OpenOffice SDK
     */
    public static synchronized File getOoSdkHome() {
        if (ooSdkHome == null) {
            throw new IllegalStateException("call setOoSdkHome() if environment \"" + OO_SDK_HOME + "\" not set");
        }
        return ooSdkHome;
    }

    /**
     * Returns the home directory of URE installation directory
     * (OO_SDK_URE_HOME).
     * 
     * @return URE home directory
     */
    public static synchronized File getOoSdkUreHome() {
        if (ooSdkUreHome == null) {
            if (SystemUtils.IS_OS_WINDOWS) {
                ooSdkUreHome = FileFinder.tryDirs(new File(getOfficeHome(), "URE"), getOoSdkHome());
            } else {
                ooSdkUreHome = new File(getOfficeBaseHome(), "ure-link");
            }
        }
        return ooSdkUreHome;
    }
    
    /**
     * Returns the library directory of OpenOffice SDK (OO_SDK_URE_LIB_DIR).
     * This can be uses as path for DYLD_LIBRARY_PATH (Mac OS X).
     * 
     * @return directory with dynamic libraries
     */
    public static synchronized File getOoSdkUreLibDir() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return new File(getOoSdkUreHome(), "bin");
        } else {
            return new File(getOoSdkUreHome(), "lib");
        }
    }
    
    /**
     * Returns the bin path of OpenOffice URE (OO_SDK_URE_BIN_DIR).
     * 
     * @return bin path
     */
    public static synchronized File getOoSdkUreBinDir() {
        return new File(getOoSdkUreHome(), "bin");
    }
    
    private static String getenv(String name) {
        String value = System.getenv(name);
        if (value == null) {
            value = System.getProperty(name);
        }
        return value;
    }
    
    private static File getenvAsFile(String name) {
        String filename = getenv(name);
        return (filename == null) ? null : new File(filename);
    }

}
