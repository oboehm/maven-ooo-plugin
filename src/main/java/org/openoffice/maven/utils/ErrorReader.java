/*************************************************************************
 *
 * $RCSfile: ErrorReader.java,v $
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
package org.openoffice.maven.utils;

import java.io.*;

/**
 * Utility class for reading errors from a process output.
 * 
 * @author Cedric Bosdonnat
 */
public class ErrorReader {

    /**
     * Reads the error stream and throw and throw an exception
     * with the message read.
     * 
     * @param pErrorStream the error stream to read.
     * @throws Exception the exception with the error message.
     */
    public static void readErrors(InputStream pErrorStream) throws Exception {
        String message = "";
        
        BufferedReader buffer = new BufferedReader(
                new InputStreamReader(pErrorStream));
        String line = buffer.readLine();
        while (null != line) {
            message += line + "\n";
            line = buffer.readLine();
        }
        
        if (!message.equals("")) {
            throw new Exception(message);
        }        
    }

}
