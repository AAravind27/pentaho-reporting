/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.reporting.engine.classic.core.modules.parser.extwriter.sql;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.ConnectionProvider;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.SQLReportDataFactory;
import org.pentaho.reporting.engine.classic.core.modules.parser.data.sql.SQLDataFactoryModule;
import org.pentaho.reporting.engine.classic.core.modules.parser.extwriter.DataFactoryWriteHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.extwriter.ReportWriterContext;
import org.pentaho.reporting.engine.classic.core.modules.parser.extwriter.ReportWriterException;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;
import org.pentaho.reporting.libraries.xmlns.common.AttributeList;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriter;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriterSupport;

import java.io.IOException;

/**
 * Creation-Date: Jan 19, 2007, 4:44:05 PM
 *
 * @author Thomas Morgner
 */
public class SQLDataFactoryWriteHandler implements DataFactoryWriteHandler {
  public static final String PREFIX =
      "org.pentaho.reporting.engine.classic.core.modules.parser.extwriter.handler.sql-connection-provider.";

  public SQLDataFactoryWriteHandler() {
  }

  /**
   * Writes a data-source into a XML-stream.
   *
   * @param reportWriter
   *          the writer context that holds all factories.
   * @param xmlWriter
   *          the XML writer that will receive the generated XML data.
   * @param dataFactory
   *          the data factory that should be written.
   * @throws IOException
   *           if any error occured
   * @throws ReportWriterException
   *           if the data factory cannot be written.
   */
  public void write( final ReportWriterContext reportWriter, final XmlWriter xmlWriter, final DataFactory dataFactory )
    throws IOException, ReportWriterException {
    if ( reportWriter == null ) {
      throw new NullPointerException();
    }
    if ( dataFactory == null ) {
      throw new NullPointerException();
    }
    if ( xmlWriter == null ) {
      throw new NullPointerException();
    }

    final SQLReportDataFactory sqlDataFactory = (SQLReportDataFactory) dataFactory;

    final AttributeList rootAttrs = new AttributeList();
    if ( xmlWriter.isNamespaceDefined( SQLDataFactoryModule.NAMESPACE ) == false ) {
      rootAttrs.addNamespaceDeclaration( "data", SQLDataFactoryModule.NAMESPACE );
    }
    xmlWriter.writeTag( SQLDataFactoryModule.NAMESPACE, "sql-datasource", rootAttrs, XmlWriterSupport.OPEN );

    writeConnectionInfo( reportWriter, xmlWriter, sqlDataFactory.getConnectionProvider() );

    final String[] queryNames = sqlDataFactory.getQueryNames();
    for ( int i = 0; i < queryNames.length; i++ ) {
      final String queryName = queryNames[i];
      final String query = sqlDataFactory.getQuery( queryName );
      xmlWriter.writeTag( SQLDataFactoryModule.NAMESPACE, "query", "name", queryName, XmlWriterSupport.OPEN );
      xmlWriter.writeTextNormalized( query, false );
      xmlWriter.writeCloseTag();
    }
    xmlWriter.writeCloseTag();
  }

  private void writeConnectionInfo( final ReportWriterContext reportWriter, final XmlWriter xmlWriter,
      final ConnectionProvider connectionProvider ) throws IOException, ReportWriterException {
    final String configKey = SQLDataFactoryWriteHandler.PREFIX + connectionProvider.getClass().getName();
    final Configuration globalConfig = ClassicEngineBoot.getInstance().getGlobalConfig();
    final String value = globalConfig.getConfigProperty( configKey );
    if ( value != null ) {
      final ConnectionProviderWriteHandler handler =
          ObjectUtilities.loadAndInstantiate( value, SQLReportDataFactory.class, ConnectionProviderWriteHandler.class );
      if ( handler != null ) {
        handler.write( reportWriter, xmlWriter, connectionProvider );
      }
    }

  }
}
