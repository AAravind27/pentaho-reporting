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


package org.pentaho.reporting.engine.classic.extensions.parsers.reportdesigner.datasets;

import org.pentaho.reporting.engine.classic.core.CompoundDataFactory;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.IgnoreAnyChildReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;

public class DataSetsReadHandler extends AbstractXmlReadHandler {
  private ArrayList dataSets;
  private CompoundDataFactory dataFactory;

  public DataSetsReadHandler() {
    dataSets = new ArrayList();

  }

  /**
   * Returns the handler for a child element.
   *
   * @param uri     the URI of the namespace of the current element.
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild( final String uri, final String tagName, final Attributes atts )
    throws SAXException {
    if ( isSameNamespace( uri ) == false ) {
      return null;
    }
    if ( "child".equals( tagName ) ) {
      final String type = atts.getValue( uri, "type" );
      if ( "org.pentaho.reportdesigner.crm.report.datasetplugin.properties.PropertiesDataSetReportElement"
        .equals( type ) ) {
        return new PropertiesDataSetReadHandler();
      }
      if ( "org.pentaho.reportdesigner.crm.report.datasetplugin.sampledb.SampleDataSetReportElement".equals( type ) ||
        "org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCDataSetReportElement".equals( type ) ) {
        final XmlReadHandler readHandler = new JdbcDataSetReadHandler();
        dataSets.add( readHandler );
        return readHandler;
      }
      if ( "org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement"
        .equals( type ) ) {
        final XmlReadHandler readHandler = new MultiDataSetReadHandler();
        dataSets.add( readHandler );
        return readHandler;
      }
      if ( "org.pentaho.reportdesigner.crm.report.datasetplugin.staticfactory.StaticFactoryDataSetReportElement"
        .equals( type ) ) {
        final XmlReadHandler readHandler = new StaticDataSetReadHandler();
        dataSets.add( readHandler );
        return readHandler;
      }
    }
    if ( "padding".equals( tagName ) ) {
      return new IgnoreAnyChildReadHandler();
    }
    if ( "property".equals( tagName ) ) {
      return new IgnoreAnyChildReadHandler();
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws SAXException if there is a parsing error.
   */
  protected void doneParsing() throws SAXException {
    dataFactory = new CompoundDataFactory();
    for ( int i = 0; i < dataSets.size(); i++ ) {
      final XmlReadHandler handler = (XmlReadHandler) dataSets.get( i );
      dataFactory.add( (DataFactory) handler.getObject() );
    }
  }

  /**
   * Returns the object for this element or null, if this element does not create an object.
   *
   * @return the object.
   * @throws SAXException if an parser error occured.
   */
  public Object getObject() throws SAXException {
    return dataFactory;
  }
}
