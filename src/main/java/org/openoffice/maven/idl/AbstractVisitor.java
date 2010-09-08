package org.openoffice.maven.idl;

import org.apache.maven.plugin.logging.Log;
import org.openoffice.maven.ConfigurationManager;
import org.openoffice.maven.utils.IVisitor;

/**
 * The Class AbstractVisitor provides a getLog() method for logging.
 * 
 * @author oliver (oliver.boehm@agentes.de)
 * @since 1.1.1 (08.09.2010)
 */
public abstract class AbstractVisitor implements IVisitor {

    /**
     * Gets the log.
     *
     * @return the log
     */
    protected static final Log getLog() {
        return ConfigurationManager.getLog();
    }

}