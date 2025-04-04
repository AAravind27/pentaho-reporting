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


package org.pentaho.reporting.engine.classic.core.modules.output.table.base;

public class SheetLayoutTableCellDefinition {
  protected static final int LINE_HINT_NONE = 0;
  protected static final int LINE_HINT_VERTICAL = 1;
  protected static final int LINE_HINT_HORIZONTAL = 2;

  private int lineType; // 0 none, 1 horizontal, 2 vertical
  private long coordinate;

  public SheetLayoutTableCellDefinition() {
    this.lineType = LINE_HINT_NONE;
  }

  public SheetLayoutTableCellDefinition( final int lineType, final long coordinate ) {
    this.lineType = lineType;
    this.coordinate = coordinate;
  }

  public int getLineType() {
    return lineType;
  }

  public long getCoordinate() {
    return coordinate;
  }
}
