/*************************************************************************
 * AbstractTest.java
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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

/**
 * This class is needed to set up the environment for OpenOffice.
 * 
 * @author oliver
 * @since 1.2 (31.07.2010)
 */
public class AbstractTest {

    private static Log log = new SystemStreamLog();
    
    static {
        setUpEnvironment();
    }

    public static void setUpEnvironment() {
        try {
            Environment.getOfficeHome();
            Environment.getOoSdkHome();
        } catch (IllegalStateException e) {
            if (SystemUtils.IS_OS_MAC) {
                log.info("OO environment not set, using my defaults...", e);
                Environment.setOfficeHome(new File("/opt/ooo/OpenOffice.org.app"));
                Environment.setOoSdkHome(new File("/opt/ooo/OpenOffice.org3.2_SDK"));
            }
        }
        if (ConfigurationManager.getIdlDir() == null) {
            setIdlDir();
        }
        if (ConfigurationManager.getOutput() == null) {
            ConfigurationManager.setOutput(new File("target"));
        }
    }
    
    private static void setIdlDir() {
        List<Resource> resources = new ArrayList<Resource>();
        Resource rsc = new Resource();
        rsc.setDirectory("src/main/resources/archetype-resources/src/main/resources");
        resources.add(rsc);
        ConfigurationManager.setResources(resources);
    }

}