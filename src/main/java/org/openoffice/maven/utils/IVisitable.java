/*************************************************************************
 *
 * $RCSfile: IVisitable.java,v $
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
 * This interfaces adds the support of the Visitor design pattern to
 * tree-like data structures.
 * 
 * <p>To use the visitor pattern, simply implements a visitor performing the
 * actions to repeat on all the nodes, and accept it on the highest directory.
 * The following code snippet shows how to easily display each node's string 
 * representation.</p>
 * 
 * <pre>
 * IVisitable topNode = ...;
 * topNode.accept(new IVisitor() {
 * 
 *   public boolean visit(IVisitable pNode) {
 *     System.out.println(pNode.toString());
 *             
 *     // Always visit the children
 *     return true;
 *   }    
 * });
 * </pre>
 * 
 * @author Cedric Bosdonnat
 *
 */
public interface IVisitable {

    /**
     * Visits all the current node and its sub-tree.
     * 
     * @param pVisitor the visitor which will be visiting all the hierarchy
     * @throws Exception is thrown if anything wrong happens during the visit
     */
    public void accept(IVisitor pVisitor) throws Exception;
}
