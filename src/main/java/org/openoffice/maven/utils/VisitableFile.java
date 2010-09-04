/*************************************************************************
 *
 * $RCSfile: VisitableFile.java,v $
 *
 * $Revision: 1.1 $
 *
 * last change: $Author: cedricbosdo $ $Date: 2007/10/08 18:35:16 $
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
package org.openoffice.maven.utils;

import java.io.File;

/**
 * Class adding the support of the Visitor design pattern to files.
 * 
 * @author Cedric Bosdonnat
 * 
 * @see IVisitor for details on implementing a visitor
 * @see IVisitable for details on how to runs
 *
 */
public class VisitableFile extends File implements IVisitable {

    private static final long serialVersionUID = 1447247005481569493L;

    /**
     * Creates a new visitable file from the file path.
     * 
     * @param pPathname the file path.
     * 
     * @see File#File(String)
     */
    public VisitableFile(String pPathname) {
        super(pPathname);
    }

    /**
     * Creates a new visitable file from the parent and child paths.
     * 
     * @param pParent the parent path
     * @param pChild the child relative path
     * 
     * @see File#File(String, String)
     */
    public VisitableFile(String pParent, String pChild) {
        super(pParent, pChild);
    }

    /**
     * Creates a new visitable file from the parent file and the child
     * relative path.
     *
     * @param parent the parent
     * @param child the child
     * @see File#File(File, String)
     */
    public VisitableFile(File parent, String child) {
        super(parent, child);
    }

    /**
     * Creates a new visitable file from the parent file and the child 
     * relative path.
     * 
     * @param pParent the parent file
     * @param pChild the child relative path
     * 
     * @see File#File(File, String)
     */
    public VisitableFile(VisitableFile pParent, String pChild) {
        super(pParent, pChild);
    }

    /**
     * {@inheritDoc}
     */
    public void accept(IVisitor pVisitor) throws Exception {
        if (pVisitor != null) {
            
            // Visit this file
            boolean visitChildren = pVisitor.visit(this);
            
            // Visit children if needed and possible
            if (isDirectory() && visitChildren) {
                String[] children = list();
                for (int i = 0 ; i < children.length ; i++) {
                    VisitableFile child = new VisitableFile(this, children[i]);
                    child.accept(pVisitor);
                }
            }
        }
    }
}
