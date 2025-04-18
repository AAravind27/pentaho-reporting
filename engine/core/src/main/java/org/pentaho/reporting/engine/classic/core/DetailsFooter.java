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


package org.pentaho.reporting.engine.classic.core;

import org.pentaho.reporting.engine.classic.core.filter.types.bands.DetailsFooterType;
import org.pentaho.reporting.engine.classic.core.style.BandStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleSheet;
import org.pentaho.reporting.engine.classic.core.style.RootLevelBandDefaultStyleSheet;

/**
 * A details footer is printed between the last itemband and the first group-footer. The footer is printed on the
 * itemsFinished(..) event. A details-footer cannot carry subreports. The details footer will be printed even if there
 * is no data available.
 * <p/>
 * This behavior can be easily changed with a style-expression on the visible-style-property. ("=NOT(ISEMPTYDATA())"
 *
 * @author Thomas Morgner
 */
public class DetailsFooter extends Band implements RootLevelBand {
  /**
   * A helper array to prevent unnecessary object creation.
   */
  private static final SubReport[] EMPTY_SUB_REPORTS = new SubReport[0];

  /**
   * Constructs a new band (initially empty).
   */
  public DetailsFooter() {
    setElementType( new DetailsFooterType() );
    getStyle().setStyleProperty( ElementStyleKeys.AVOID_PAGEBREAK_INSIDE, Boolean.TRUE );
  }

  /**
   * Returns the number of subreports on this band. This returns zero, as page-bands cannot have subreports.
   *
   * @return the subreport count.
   */
  public final int getSubReportCount() {
    return 0;
  }

  /**
   * Throws an IndexOutOfBoundsException as page-footer cannot have sub-reports.
   *
   * @param index
   *          the index.
   * @return nothing, as an exception is thrown instead.
   */
  public final SubReport getSubReport( final int index ) {
    throw new IndexOutOfBoundsException( "DetailsFooter cannot have subreports" );
  }

  /**
   * Returns an empty array, as page-footer cannot have subreports.
   *
   * @return the sub-reports as array.
   */
  public SubReport[] getSubReports() {
    return DetailsFooter.EMPTY_SUB_REPORTS;
  }

  /**
   * Checks whether this group header should be repeated on new pages.
   *
   * @return true, if the header will be repeated, false otherwise
   */
  public boolean isRepeat() {
    return getStyle().getBooleanStyleProperty( BandStyleKeys.REPEAT_HEADER );
  }

  /**
   * Defines, whether this group header should be repeated on new pages.
   *
   * @param repeat
   *          true, if the header will be repeated, false otherwise
   */
  public void setRepeat( final boolean repeat ) {
    getStyle().setBooleanStyleProperty( BandStyleKeys.REPEAT_HEADER, repeat );
    notifyNodePropertiesChanged();
  }

  /**
   * Returns true if the footer should be shown on all subreports.
   *
   * @return true or false.
   */
  public boolean isSticky() {
    return getStyle().getBooleanStyleProperty( BandStyleKeys.STICKY, false );
  }

  /**
   * Defines whether the footer should be shown on all subreports.
   *
   * @param b
   *          a flag indicating whether or not the footer is shown on the first page.
   */
  public void setSticky( final boolean b ) {
    getStyle().setBooleanStyleProperty( BandStyleKeys.STICKY, b );
    notifyNodePropertiesChanged();
  }

  public ElementStyleSheet getDefaultStyleSheet() {
    return RootLevelBandDefaultStyleSheet.getRootLevelBandDefaultStyle();
  }
}
