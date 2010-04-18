/*************************************************************************
 *
 * $RCSfile: IVisitor.java,v $
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

/**
 * Executes an operation on the nodes of a tree-like data structure.
 * 
 * <p>The treatment is executed by the {@link #visit(IVisitable)} method. 
 * Any visitor implementation have to implement and customize this method.</p>
 * 
 * @author Cedric Bosdonnat
 * 
 * @see IVisitable for a code snippet showing the use of the visitor 
 *      design pattern
 *
 */
public interface IVisitor {

    /**
     * Runs some treatment on the visited node.
     * 
     * @param pNode the visited node
     * @return <code>true</code> if the node's children have to be visited,
     *             <code>false</code> otherwise. 
     * @throws Exception is thrown if anything wrong happens during the visit
     */
    public boolean visit(IVisitable pNode) throws Exception;
}
