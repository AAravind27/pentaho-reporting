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


package org.pentaho.reporting.designer.core.editor.styles.styleeditor;

import org.pentaho.reporting.designer.core.actions.AbstractReportContextAction;
import org.pentaho.reporting.designer.core.editor.styles.Messages;
import org.pentaho.reporting.designer.core.util.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SaveAction extends AbstractReportContextAction {
  private StyleDefinitionEditorContext editorContext;

  public SaveAction( final StyleDefinitionEditorContext editorContext ) {
    this.editorContext = editorContext;
    putValue( Action.NAME, Messages.getString( "StyleDefinitionEditorDialog.Save.Text" ) );
    putValue( Action.SHORT_DESCRIPTION, Messages.getString( "StyleDefinitionEditorDialog.Save.Description" ) );
    putValue( Action.MNEMONIC_KEY, Messages.getOptionalMnemonic( "StyleDefinitionEditorDialog.Save.Mnemonic" ) );
    putValue( Action.ACCELERATOR_KEY, Messages.getOptionalKeyStroke( "StyleDefinitionEditorDialog.Save.Accelerator" ) );
    putValue( Action.SMALL_ICON, IconLoader.getInstance().getSaveIcon() );

    setReportDesignerContext( editorContext.getDesignerContext() );
    setEnabled( true );
  }

  public void actionPerformed( final ActionEvent e ) {
    StyleDefinitionUtilities.saveStyleDefinition( editorContext, editorContext.getParent() );
  }
}
