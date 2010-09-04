/*************************************************************************
 * RegmergeVisitorTest.java
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

package org.openoffice.maven.idl;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.openoffice.maven.AbstractTest;
import org.openoffice.maven.utils.IVisitor;
import org.openoffice.maven.utils.VisitableFile;

/**
 * JUnit test for RegmergeVisitor.
 * 
 * @author oliver (oliver.boehm@agentes.de)
 * @since 1.1.1 (04.09.2010)
 */
public class RegmergeVisitorTest extends AbstractTest {
    
    private static final Log log = LogFactory.getLog(RegmergeVisitorTest.class);
    private final IVisitor visitor = new RegmergeVisitor();

    /**
     * Test method for {@link RegmergeVisitor#visit(org.openoffice.maven.utils.IVisitable)}.
     * We want to test the call of the regmerge command.
     *
     * @throws Exception the exception
     */
    @Test
    public void testVisit() throws Exception {
        File typesFile = new File(targetDir, "types.rdb");
        typesFile.delete();
        assertFalse(typesFile + " can't be deleted", typesFile.exists());
        VisitableFile urdFile = new VisitableFile(urdDir, "hello/WorldInterface.urd");
        if (!urdFile.exists()) {
            log.info(urdFile + " must be created first...");
            new IdlcVisitorTest().testRunIdlcOnFile();
        }
        assertTrue(urdFile + " was not created", urdFile.exists());
        visitor.visit(urdFile);
        assertTrue(typesFile + " was not merged", typesFile.exists());
    }

}
