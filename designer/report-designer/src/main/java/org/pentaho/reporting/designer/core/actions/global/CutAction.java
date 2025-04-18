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


package org.pentaho.reporting.designer.core.actions.global;

import org.pentaho.reporting.designer.core.actions.AbstractElementSelectionAction;
import org.pentaho.reporting.designer.core.actions.ActionMessages;
import org.pentaho.reporting.designer.core.editor.ReportDocumentContext;
import org.pentaho.reporting.designer.core.model.selection.DocumentContextSelectionModel;
import org.pentaho.reporting.designer.core.util.IconLoader;
import org.pentaho.reporting.designer.core.util.dnd.ClipboardManager;
import org.pentaho.reporting.designer.core.util.dnd.InsertationUtil;
import org.pentaho.reporting.designer.core.util.undo.CompoundUndoEntry;
import org.pentaho.reporting.designer.core.util.undo.UndoEntry;
import org.pentaho.reporting.engine.classic.core.event.ReportModelEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class CutAction extends AbstractElementSelectionAction {
  public CutAction() {
    putValue( Action.NAME, ActionMessages.getString( "CutAction.Text" ) );
    putValue( Action.SHORT_DESCRIPTION, ActionMessages.getString( "CutAction.Description" ) );
    putValue( Action.MNEMONIC_KEY, ActionMessages.getOptionalMnemonic( "CutAction.Mnemonic" ) );
    putValue( Action.SMALL_ICON, IconLoader.getInstance().getCutIcon() );
    putValue( Action.ACCELERATOR_KEY, ActionMessages.getOptionalKeyStroke( "CutAction.Accelerator" ) );
  }

  protected void selectedElementPropertiesChanged( final ReportModelEvent event ) {
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed( final ActionEvent e ) {
    final DocumentContextSelectionModel selectionModel1 = getSelectionModel();
    if ( selectionModel1 == null ) {
      return;
    }

    final ReportDocumentContext activeContext = getActiveContext();
    final Object[] selectedElements = selectionModel1.getSelectedElements();
    if ( selectedElements.length == 0 ) {
      return;
    }

    final ArrayList<Object> preparedElements = new ArrayList<Object>( selectedElements.length );
    final ArrayList<UndoEntry> undoEntries = new ArrayList<UndoEntry>( selectedElements.length );
    try {
      for ( int i = 0; i < selectedElements.length; i++ ) {
        final Object selectedElement = selectedElements[ i ];
        final Object preparedElement = InsertationUtil.prepareForCopy( activeContext, selectedElement );
        if ( preparedElement == null ) {
          continue;
        }

        final UndoEntry undoEntry = InsertationUtil.delete( activeContext, selectedElement );
        if ( undoEntry != null ) {
          preparedElements.add( preparedElement );
          undoEntries.add( undoEntry );
        }
      }

      ClipboardManager.getManager().setContents( preparedElements.toArray() );
    } finally {
      activeContext.getUndo().addChange( ActionMessages.getString( "CutAction.Text" ),
        new CompoundUndoEntry( (UndoEntry[]) undoEntries.toArray( new UndoEntry[ undoEntries.size() ] ) ) );
    }
  }
}
