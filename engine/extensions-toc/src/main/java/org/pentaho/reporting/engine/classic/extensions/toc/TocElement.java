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


package org.pentaho.reporting.engine.classic.extensions.toc;

import org.pentaho.reporting.engine.classic.core.CompoundDataFactory;
import org.pentaho.reporting.engine.classic.core.SubReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

public class TocElement extends SubReport {
  /**
   * Creates a new subreport instance.
   */
  public TocElement() {
    setElementType( new TocElementType() );

    final Class[] columnTypes = new Class[ 4 + 10 ];
    final String[] columnNames = new String[ 4 + 10 ];
    columnNames[ 0 ] = "item-title";
    columnNames[ 1 ] = "item-page";
    columnNames[ 2 ] = "item-index";
    columnNames[ 3 ] = "item-index-array";
    columnTypes[ 0 ] = Object.class;
    columnTypes[ 1 ] = Integer.class;
    columnTypes[ 2 ] = String.class;
    columnTypes[ 3 ] = Integer[].class;
    for ( int i = 0; i < 10; i++ ) {
      columnNames[ i + 4 ] = "group-value-" + i;
      columnTypes[ i + 4 ] = Object.class;
    }
    final TypedTableModel sampleModel = new TypedTableModel( columnNames, columnTypes );

    final CompoundDataFactory compoundDataFactory = new CompoundDataFactory();
    compoundDataFactory.add( new TableDataFactory( "design-time-data", sampleModel ) );
    setQuery( "design-time-data" );
    setDataFactory( compoundDataFactory );
  }


}
