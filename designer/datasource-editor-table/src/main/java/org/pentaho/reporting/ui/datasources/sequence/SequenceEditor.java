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


package org.pentaho.reporting.ui.datasources.sequence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sequence.Sequence;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sequence.SequenceDescription;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sequence.SequenceRegistry;
import org.pentaho.reporting.libraries.designtime.swing.KeyedComboBoxModel;
import org.pentaho.reporting.libraries.designtime.swing.VerticalLayout;
import org.pentaho.reporting.libraries.designtime.swing.settings.LocaleSettings;
import org.pentaho.reporting.libraries.designtime.swing.table.PropertyTable;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.Locale;

public class SequenceEditor extends JComponent {
  private static Log logger = LogFactory.getLog( SequenceEditor.class );

  private class SelectGroupAction implements ListDataListener {
    private SelectGroupAction() {
    }

    public void intervalAdded( final ListDataEvent e ) {

    }

    public void intervalRemoved( final ListDataEvent e ) {

    }

    public void contentsChanged( final ListDataEvent e ) {
      if ( disableSequenceUpdate ) {
        return;
      }

      logger.debug( "start SequenceGroupContentChange: " + disableSequenceUpdate );
      disableSequenceUpdate = true;
      sequenceGroupSelected();
      sequenceSelected();
      disableSequenceUpdate = false;
      logger.debug( "end SequenceGroupContentChange: " + disableSequenceUpdate );
    }
  }

  private class SequenceSelectionHandler implements ListDataListener {
    public void intervalAdded( final ListDataEvent e ) {

    }

    public void intervalRemoved( final ListDataEvent e ) {

    }

    public void contentsChanged( final ListDataEvent e ) {
      if ( disableSequenceUpdate ) {
        return;
      }

      logger.debug( "start SequenceContentChange: " + disableSequenceUpdate );
      disableSequenceUpdate = true;
      sequenceSelected();
      disableSequenceUpdate = false;
      logger.debug( "end SequenceGroupContentChange: " + disableSequenceUpdate );
    }
  }

  private Sequence sequence;
  private JComboBox sequenceGroupBox;
  private JComboBox sequenceNameBox;
  private PropertyTable propertyTable;
  private SequenceRegistry registry;
  private KeyedComboBoxModel<Sequence, String> sequenceModel;
  private SequencePropertyTableModel propertyTableModel;
  private boolean disableSequenceUpdate;

  public SequenceEditor() {
    registry = new SequenceRegistry();
    setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );

    final DefaultComboBoxModel groupModel =
      new DefaultComboBoxModel( registry.getSequenceGroups( Locale.getDefault() ) );
    groupModel.addListDataListener( new SelectGroupAction() );
    sequenceGroupBox = new JComboBox();
    sequenceGroupBox.setModel( groupModel );

    sequenceModel = new KeyedComboBoxModel<Sequence, String>();
    sequenceModel.addListDataListener( new SequenceSelectionHandler() );
    sequenceNameBox = new JComboBox( sequenceModel );

    propertyTableModel = new SequencePropertyTableModel();
    propertyTable = new PropertyTable();
    propertyTable.setModel( propertyTableModel );

    final JPanel headerPanel = new JPanel();
    headerPanel.setLayout( new VerticalLayout( 5, VerticalLayout.BOTH ) );
    headerPanel.add( new JLabel( Messages.getString( "SequenceEditor.Group" ) ) );
    headerPanel.add( sequenceGroupBox );
    headerPanel.add( new JLabel( Messages.getString( "SequenceEditor.Sequence" ) ) );
    headerPanel.add( sequenceNameBox );

    setLayout( new BorderLayout() );
    add( new JScrollPane( propertyTable ), BorderLayout.CENTER );
    add( headerPanel, BorderLayout.NORTH );

    setSequence( null );
  }

  public void applyLocaleSettings( final LocaleSettings localeSettings ) {
    propertyTable.applyLocaleSettings( localeSettings );
  }

  public Sequence getSequence() {
    return sequence;
  }

  public void setSequence( final Sequence sequence ) {
    final Sequence oldSequence = this.sequence;

    final boolean oldDisableSequenceUpdateValue = disableSequenceUpdate;
    logger.debug( "start: setSequence: " + sequence );
    try {
      disableSequenceUpdate = true;

      this.sequence = sequence;
      this.propertyTableModel.setSequence( sequence );
      if ( sequence == null ) {
        sequenceGroupBox.setEnabled( false );
        sequenceNameBox.setEnabled( false );
        propertyTable.setEnabled( false );

        if ( sequenceGroupBox.getItemCount() > 0 ) {
          sequenceGroupBox.setSelectedIndex( 0 );
          logger.debug( "   : setSequence: Setting sequencegroup to first." );
          sequenceGroupSelected();
        }
        if ( sequenceNameBox.getItemCount() > 0 ) {
          sequenceNameBox.setSelectedIndex( 0 );
          logger.debug( "   : setSequence: Setting sequence to first in group." );
        }
        return;
      }

      sequenceGroupBox.setEnabled( true );
      sequenceNameBox.setEnabled( true );
      propertyTable.setEnabled( true );

      final SequenceDescription de = sequence.getSequenceDescription();
      final String group = de.getSequenceGroup( Locale.getDefault() );
      sequenceGroupBox.setSelectedItem( group );
      logger.debug( "   : setSequence: Setting sequencegroup to: " + group );
      sequenceGroupSelected();

      final String displayName = de.getDisplayName( Locale.getDefault() );
      sequenceModel.setSelectedValue( displayName );
      logger.debug( "   : setSequence: Setting sequence to: " + displayName );
    } finally {
      disableSequenceUpdate = oldDisableSequenceUpdateValue;
    }
    logger.debug( "end: setSequence: " + sequence );
    firePropertyChange( "sequence", oldSequence, this.sequence );
  }

  protected void sequenceGroupSelected() {
    logger.debug( "sequenceGroupSelected() called. Updating sequences" );
    final Object selectedItem = sequenceGroupBox.getSelectedItem();
    if ( selectedItem == null ) {
      return;
    }

    final String selectedGroup = String.valueOf( selectedItem );
    final SequenceDescription[] sequences = registry.getSequencesForGroup( selectedGroup, Locale.getDefault() );
    sequenceModel.clear();
    for ( final SequenceDescription description : sequences ) {
      final String displayName = description.getDisplayName( Locale.getDefault() );
      sequenceModel.add( description.newInstance(), displayName );
    }
    if ( sequences.length > 0 ) {
      sequenceModel.setSelectedKey( sequenceModel.getKeyAt( 0 ) );
    }
  }

  protected void sequenceSelected() {
    setSequence( sequenceModel.getSelectedKey() );
  }

  public void stopEditing() {
    final TableCellEditor cellEditor = propertyTable.getCellEditor();
    if ( cellEditor != null ) {
      cellEditor.stopCellEditing();
    }
  }
}
