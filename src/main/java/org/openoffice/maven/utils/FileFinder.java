/*************************************************************************
 * FileFinder.java
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

package org.openoffice.maven.utils;

import java.io.File;

/**
 * Some utilites if you try to find some directories or files.
 *
 * @author boehm (oliver.boehm@agentes.de)
 * @since 1.1.1 (03.09.2010)
 */
public class FileFinder {
    
    /** Utility class, no need to instantiate it. */
    private FileFinder() {
    }
    
    public static File tryDirs(final File... dirs) {
        for (int i = 0; i < dirs.length; i++) {
            if (dirs[i].isDirectory()) {
                return dirs[i];
            }
        }
        return null;
    }

    public static File tryFiles(final File... files) {
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                return files[i];
            }
        }
        return null;
    }

}
