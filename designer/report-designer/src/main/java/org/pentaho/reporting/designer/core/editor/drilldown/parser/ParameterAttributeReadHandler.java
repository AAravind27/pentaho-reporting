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


package org.pentaho.reporting.designer.core.editor.drilldown.parser;

import org.pentaho.reporting.engine.classic.core.parameters.ParameterAttributeNames;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Todo: Document me!
 * <p/>
 * Date: 13.08.2010 Time: 17:34:04
 *
 * @author Thomas Morgner.
 */
public class ParameterAttributeReadHandler extends AbstractXmlReadHandler {
  private String namespace;
  private String name;
  private String value;

  public ParameterAttributeReadHandler() {
  }

  public String getNamespace() {
    return namespace;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing( final Attributes attrs ) throws SAXException {
    name = attrs.getValue( getUri(), "name" );
    if ( StringUtils.isEmpty( name ) ) {
      throw new ParseException( "Required attribute 'name' is missing", getLocator() );
    }
    namespace = attrs.getValue( getUri(), "namespace" );
    if ( StringUtils.isEmpty( namespace ) ) {
      namespace = ParameterAttributeNames.Core.NAMESPACE;
    }
    value = attrs.getValue( getUri(), "value" );
    if ( StringUtils.isEmpty( value ) ) {
      throw new ParseException( "Required attribute 'value' is missing", getLocator() );
    }
  }

  /**
   * Returns the object for this element or null, if this element does not create an object.
   *
   * @return the object.
   * @throws org.xml.sax.SAXException if an parser error occured.
   */
  public Object getObject() throws SAXException {
    return null;
  }
}
