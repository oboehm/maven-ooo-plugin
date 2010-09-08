/*************************************************************************
 *
 * $RCSfile: RegmergeVisitor.java,v $
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

import org.codehaus.plexus.util.cli.CommandLineException;
import org.openoffice.maven.ConfigurationManager;
import org.openoffice.maven.utils.IVisitable;
import org.openoffice.maven.utils.VisitableFile;

/**
 * Visits the URD files and merge them with the <code>types.rdb</code> file.
 * 
 * @author Cedric Bosdonnat
 *
 */
public class RegmergeVisitor extends AbstractVisitor {
    
    /**
     * {@inheritDoc}
     */
    public boolean visit(IVisitable pNode) throws Exception {
        boolean visitChildren = false;
        VisitableFile file = (VisitableFile)pNode;

        if (file.isFile() && file.canRead()) {
            // Try to merge the file with types.rdb if it is a URD file
            if (file.getName().endsWith(".urd")) {
                runRegmergeOnFile(file);
            }
        } else if (file.isDirectory()) {

            visitChildren = true;
        }
        return visitChildren;
    }

    /**
     * Merges the file into the <code>types.rdb</code> of the project.
     * 
     * @param pFile the URD file to merge with <code>types.rdb</code>
     * @throws Exception if anything wrong happens.
     */
    private static void runRegmergeOnFile(VisitableFile pFile) 
        throws Exception {
        
        getLog().info("Merging file: " + pFile.getPath());
        
        // Compute the command line
//        String commandPattern = "regmerge {0} /UCR {0} \"{1}\"";
//        
//        String projectTypes = "";
//        projectTypes = ConfigurationManager.getTypesFile();
//        
//        String[] args = {projectTypes, pFile.getPath()};
//        
//        String command = MessageFormat.format(commandPattern, (Object[]) args);
        
        // Run regmerge
//        ConfigurationManager.runTool(command);
        int n = ConfigurationManager.runCommand("regmerge", ConfigurationManager.getTypesFile(), "/UCR",
                pFile.getPath());
        if (n != 0) {
            throw new CommandLineException("regmerge returned with " + n);
        }
    }
}
