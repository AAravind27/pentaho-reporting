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


package org.pentaho.reporting.designer.core.actions.elements.format;

import org.pentaho.reporting.designer.core.actions.AbstractElementSelectionAction;
import org.pentaho.reporting.designer.core.actions.ActionMessages;
import org.pentaho.reporting.designer.core.actions.ToggleStateAction;
import org.pentaho.reporting.designer.core.model.selection.DocumentContextSelectionModel;
import org.pentaho.reporting.designer.core.util.undo.CompoundUndoEntry;
import org.pentaho.reporting.designer.core.util.undo.StyleEditUndoEntry;
import org.pentaho.reporting.designer.core.util.undo.UndoEntry;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.designtime.ReportModelEventFilter;
import org.pentaho.reporting.engine.classic.core.designtime.ReportModelEventFilterFactory;
import org.pentaho.reporting.engine.classic.core.event.ReportModelEvent;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleSheet;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public final class KeepAspectRatioAction extends AbstractElementSelectionAction implements ToggleStateAction {
  private ReportModelEventFilter eventFilter;

  public KeepAspectRatioAction() {
    putValue( Action.SELECTED_KEY, Boolean.FALSE );
    putValue( Action.NAME, ActionMessages.getString( "KeepAspectRatioAction.Text" ) );
    putValue( Action.SHORT_DESCRIPTION, ActionMessages.getString( "KeepAspectRatioAction.Description" ) );
    putValue( Action.MNEMONIC_KEY, ActionMessages.getOptionalMnemonic( "KeepAspectRatioAction.Mnemonic" ) );
    putValue( Action.ACCELERATOR_KEY, ActionMessages.getOptionalKeyStroke( "KeepAspectRatioAction.Accelerator" ) );

    eventFilter = new ReportModelEventFilterFactory().createStyleFilter( ElementStyleKeys.KEEP_ASPECT_RATIO );
  }

  protected void selectedElementPropertiesChanged( final ReportModelEvent event ) {
    if ( eventFilter.isFilteredEvent( event ) ) {
      updateSelection();
    }
  }

  public boolean isSelected() {
    return Boolean.TRUE.equals( getValue( Action.SELECTED_KEY ) );
  }

  public void setSelected( final boolean selected ) {
    putValue( Action.SELECTED_KEY, selected );
  }

  protected void updateSelection() {
    super.updateSelection();

    final DocumentContextSelectionModel model = getSelectionModel();
    if ( model == null ) {
      return;
    }
    final List<Element> visualElements = model.getSelectedElementsOfType( Element.class );
    if ( visualElements.isEmpty() ) {
      setSelected( false );
      return;
    }

    final Element element = visualElements.get( 0 );
    final ElementStyleSheet styleSheet = element.getStyle();
    setSelected( styleSheet.getBooleanStyleProperty( ElementStyleKeys.KEEP_ASPECT_RATIO ) );
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed( final ActionEvent e ) {
    final DocumentContextSelectionModel model = getSelectionModel();
    if ( model == null ) {
      return;
    }
    final List<Element> visualElements = model.getSelectedElementsOfType( Element.class );
    final ArrayList<UndoEntry> undos = new ArrayList<UndoEntry>();

    Boolean value = null;
    for ( Element element : visualElements ) {
      final ElementStyleSheet styleSheet = element.getStyle();
      if ( value == null ) {
        if ( styleSheet.getBooleanStyleProperty( ElementStyleKeys.KEEP_ASPECT_RATIO ) ) {
          value = Boolean.FALSE;
        } else {
          value = Boolean.TRUE;
        }
      }
      undos.add( StyleEditUndoEntry.createConditional( element, ElementStyleKeys.KEEP_ASPECT_RATIO, value ) );
      styleSheet.setStyleProperty( ElementStyleKeys.KEEP_ASPECT_RATIO, value );
    }
    getActiveContext().getUndo().addChange( ActionMessages.getString( "KeepAspectRatioAction.UndoName" ),
      new CompoundUndoEntry( undos.toArray( new UndoEntry[ undos.size() ] ) ) );
  }
}
