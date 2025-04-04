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


package org.pentaho.reporting.engine.classic.core.metadata.parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.metadata.MaturityLevel;
import org.pentaho.reporting.engine.classic.core.metadata.builder.MetaDataBuilder;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.ReportParserUtil;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class AbstractMetaDataReadHandler extends AbstractXmlReadHandler {
  private static class DataHolderMetaDataBuilder extends MetaDataBuilder<DataHolderMetaDataBuilder> {
    protected DataHolderMetaDataBuilder self() {
      return this;
    }
  }

  private static final Log logger = LogFactory.getLog( AbstractMetaDataReadHandler.class );

  private String bundle;
  private MetaDataBuilder<?> builder;

  protected AbstractMetaDataReadHandler() {
  }

  protected AbstractMetaDataReadHandler( final String bundle ) {
    this.bundle = bundle;
  }

  /**
   * This method should be abstract, and will be in the near future and only exists as default implementation to honor
   * the strict requirements of preserving a stable API. Override it in your sub-classes.
   *
   * @return a meta-data builder.
   */
  protected MetaDataBuilder<?> getBuilder() {
    if ( builder == null ) {
      builder = new DataHolderMetaDataBuilder();
    }
    return builder;
  }

  protected String parseName( final Attributes attrs ) throws ParseException {
    String name = attrs.getValue( getUri(), "name" ); // NON-NLS
    if ( name == null ) {
      throw new ParseException( "Attribute 'name' is undefined", getLocator() );
    }
    return name;
  }

  protected void startParsing( final Attributes attrs ) throws SAXException {
    if ( isDerivedName() == false ) {
      getBuilder().name( parseName( attrs ) );
    }
    final String bundleFromAttributes = attrs.getValue( getUri(), "bundle-name" ); // NON-NLS
    if ( bundleFromAttributes != null ) {
      bundle = bundleFromAttributes;
    }

    boolean experimental = "true".equals( attrs.getValue( getUri(), "experimental" ) ); // NON-NLS
    getBuilder().maturity( parseMaturityLevel( attrs.getValue( getUri(), "maturity-level" ), experimental ) ); // NON-NLS
    getBuilder().expert( "true".equals( attrs.getValue( getUri(), "expert" ) ) ); // NON-NLS
    getBuilder().hidden( "true".equals( attrs.getValue( getUri(), "hidden" ) ) ); // NON-NLS
    getBuilder().preferred( "true".equals( attrs.getValue( getUri(), "preferred" ) ) ); // NON-NLS
    getBuilder().deprecated( "true".equals( attrs.getValue( getUri(), "deprecated" ) ) ); // NON-NLS
    getBuilder().since( ReportParserUtil.parseVersion( attrs.getValue( getUri(), "compatibility-level" ) ) ); // NON-NLS
  }

  private MaturityLevel parseMaturityLevel( String level, boolean experimentalAsDefault ) {
    try {
      if ( level != null ) {
        return MaturityLevel.valueOf( level );
      }
    } catch ( IllegalArgumentException e ) {
      if ( getLocator() != null ) {
        logger.debug( String.format(
            "Invalid attribute-value for maturity-level in metadata declaration [Line {0}, Column {1}]", getLocator()
                .getLineNumber(), getLocator().getColumnNumber() ) );
      } else {
        logger.debug( "Invalid attribute-value for maturity-level in metadata declaration" );
      }
    }
    if ( experimentalAsDefault ) {
      return MaturityLevel.Development;
    } else {
      return MaturityLevel.Production;
    }
  }

  protected boolean isDerivedName() {
    return false;
  }

  public int getCompatibilityLevel() {
    return getBuilder().getCompatibilityLevel();
  }

  public String getName() {
    return getBuilder().getName();
  }

  public boolean isPreferred() {
    return getBuilder().isPreferred();
  }

  public boolean isExpert() {
    return getBuilder().isExpert();
  }

  public boolean isHidden() {
    return getBuilder().isHidden();
  }

  public boolean isDeprecated() {
    return getBuilder().isDeprecated();
  }

  public String getBundle() {
    return bundle;
  }

  @Deprecated
  public boolean isExperimental() {
    return getMaturityLevel().isExperimental();
  }

  public MaturityLevel getMaturityLevel() {
    return getBuilder().getMaturityLevel();
  }
}
