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

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.elementfactory.TextFieldElementFactory;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.StaticDataFactory;
import org.pentaho.reporting.engine.classic.core.style.FontDefinition;
import org.pentaho.reporting.engine.classic.core.testsupport.DebugReportRunner;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.net.URL;

/**
 * Creation-Date: 02.08.2007, 09:23:19
 *
 * @author Thomas Morgner
 */
public class SubReportIT extends TestCase {
  private static final Log logger = LogFactory.getLog( SubReportIT.class );

  public SubReportIT() {
  }

  public SubReportIT( final String string ) {
    super( string );
  }

  protected void setUp() throws Exception {
    ClassicEngineBoot.getInstance().start();
  }

  public static TableModel createMainTableModel() {
    logger.debug( "TestDataFactory.createTableModel" );
    return new DefaultTableModel( new String[][] { { "1.1", "1.2" }, { "2.1", "2.2" } }, new String[] { "c1", "c2" } );
  }

  public static TableModel createSubReportTableModel( final String param1 ) {
    logger.debug( "TestDataFactory.createTableModel(" + param1 + ")" );
    return new DefaultTableModel( new String[][] { { "1.1:" + param1, "1.2:" + param1 },
      { "2.1:" + param1, "2.2:" + param1 } }, new String[] { "t1", "t2" } );
  }

  public String getDemoName() {
    return "Test-Case: Reports with subreports crash under certain conditions.";
  }

  public MasterReport createReport() {
    final MasterReport report = new MasterReport();
    final StaticDataFactory staticDataFactory = new StaticDataFactory();
    report.setDataFactory( staticDataFactory );
    report.setQuery( SubReportIT.class.getName() + "#createMainTableModel()" );

    final Element textElement =
        TextFieldElementFactory.createStringElement( "reportField1", new Rectangle( 0, 0, 100, 20 ), Color.BLACK,
            ElementAlignment.LEFT, ElementAlignment.TOP, new FontDefinition( "Arial", 12 ), "-", "c1" );
    report.getItemBand().addElement( textElement );

    final SubReport subReport = new SubReport();
    subReport.addInputParameter( "c1", "c1" );

    subReport.setQuery( SubReportIT.class.getName() + "#createSubReportTableModel(c1)" );
    final Element subReportTextElement =
        TextFieldElementFactory.createStringElement( "subreportField1", new Rectangle( 20, 0, 100, 20 ), Color.RED,
            ElementAlignment.LEFT, ElementAlignment.TOP, new FontDefinition( "Arial", 12 ), "-", "t1" );
    subReport.getItemBand().addElement( subReportTextElement );
    report.getItemBand().addSubReport( subReport );

    final Element textElementT1 =
        TextFieldElementFactory.createStringElement( "reportFieldT1", new Rectangle( 0, 20, 100, 20 ), Color.BLACK,
            ElementAlignment.LEFT, ElementAlignment.TOP, new FontDefinition( "Arial", 12 ), "-", "t2" );
    report.getItemBand().addElement( textElementT1 );

    final ParameterMapping[] parameterMappings = subReport.getExportMappings();

    for ( int i = 0; i < parameterMappings.length; i++ ) {
      final ParameterMapping parameterMapping = parameterMappings[i];
      logger.debug( "parameterMapping.getAlias() = " + parameterMapping.getAlias() );
      logger.debug( "parameterMapping.getName() = " + parameterMapping.getName() );
    }

    return report;
  }

  public void testXmlSubReport() throws Exception {
    final URL url = getClass().getResource( "subreport-test-master.xml" );
    assertNotNull( url );
    final ResourceManager resourceManager = new ResourceManager();
    final Resource directly = resourceManager.createDirectly( url, MasterReport.class );
    final MasterReport resource = (MasterReport) directly.getResource();

    final TableDataFactory tableDataFactory = new TableDataFactory();
    tableDataFactory.addTable( "fruit", createFruitTableModel() );
    tableDataFactory.addTable( "color", createColorTableModel() );
    tableDataFactory.addTable( "default", new DefaultTableModel() );
    resource.setDataFactory( tableDataFactory );
    DebugReportRunner.executeAll( resource );
  }

  private TableModel createFruitTableModel() {
    final String[] names = new String[] { "Id Number", "Cat", "Fruit" };
    final Object[][] data =
        new Object[][] { { "I1", "A", "Apple" }, { "I2", "A", "Orange" }, { "I2", "A", "Orange" },
          { "I2", "A", "Orange" }, { "I2", "A", "Orange" }, { "I2", "A", "Orange" }, { "I2", "A", "Orange" },
          { "I2", "A", "Orange" }, { "I2", "A", "Orange" }, { "I3", "B", "Water melon" }, { "I3", "B", "Water melon" },
          { "I3", "B", "Water melon" }, { "I3", "B", "Water melon" }, { "I3", "B", "Water melon" },
          { "I3", "B", "Water melon" }, { "I3", "B", "Water melon" }, { "I3", "B", "Water melon" },
          { "I4", "B", "Strawberry" }, };
    return new DefaultTableModel( data, names );
  }

  private TableModel createColorTableModel() {
    final String[] names = new String[] { "Number", "Group", "Color" };
    final Object[][] data =
        new Object[][] { { new Integer( 1 ), "X", "Red" }, { new Integer( 2 ), "X", "Green" },
          { new Integer( 3 ), "Y", "Yellow" }, { new Integer( 3 ), "Y", "Yellow" }, { new Integer( 4 ), "Y", "Blue" },
          { new Integer( 4 ), "Y", "Blue" }, { new Integer( 5 ), "Z", "Orange" }, { new Integer( 5 ), "Z", "Orange" },
          { new Integer( 5 ), "Z", "Orange" }, { new Integer( 6 ), "Z", "White" }, { new Integer( 6 ), "Z", "White" },
          { new Integer( 6 ), "Z", "White" }, };
    return new DefaultTableModel( data, names );
  }

  public void testAPISubReport() throws Exception {
    final MasterReport report = createReport();
    DebugReportRunner.executeAll( report );
  }

  public void testNestedEmptySubReports() throws Exception {
    final SubReport sr = new SubReport();
    sr.getReportHeader().addSubReport( new SubReport() );
    sr.getReportHeader().addSubReport( new SubReport() );

    final MasterReport report = new MasterReport();
    report.getReportHeader().addSubReport( sr );
    report.getReportHeader().addSubReport( new SubReport() );
    report.getReportHeader().addSubReport( new SubReport() );

    DebugReportRunner.executeAll( report );
  }
}
