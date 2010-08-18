/*************************************************************************
 * IdlcVisitorTest.java
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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openoffice.maven.AbstractTest;
import org.openoffice.maven.utils.VisitableFile;

/**
 * @author oliver
 * @since 1.2 (31.07.2010)
 */
public class IdlcVisitorTest extends AbstractTest {

    /**
     * Test method for {@link org.openoffice.maven.idl.IdlcVisitor#runIdlcOnFile(org.openoffice.maven.utils.VisitableFile)}.
     * @throws Exception if idlc compiler fails
     */
    @Test
    public void testRunIdlcOnFile() throws Exception {
        VisitableFile idlFile = new VisitableFile(
                "src/main/resources/archetype-resources/src/main/idl/hello/WorldInterface.idl");
        assertTrue(idlFile.getAbsoluteFile() + " not found", idlFile.exists());
        IdlcVisitor.runIdlcOnFile(idlFile);
    }

}
