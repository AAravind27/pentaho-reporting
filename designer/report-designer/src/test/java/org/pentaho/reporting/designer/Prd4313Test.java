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


package org.pentaho.reporting.designer;

import junit.framework.TestCase;
import org.pentaho.reporting.designer.core.editor.ReportRenderContext;
import org.pentaho.reporting.designer.core.editor.report.layouting.SharedElementRenderer;
import org.pentaho.reporting.designer.testsupport.TableTestUtil;
import org.pentaho.reporting.designer.testsupport.TestReportDesignerContext;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.testsupport.DebugJndiContextFactoryBuilder;
import org.pentaho.reporting.libraries.base.util.DebugLog;
import org.pentaho.reporting.libraries.base.util.StopWatch;

import java.io.PrintStream;

import javax.naming.spi.NamingManager;

public class Prd4313Test extends TestCase {
  public void setUp() throws Exception {
    final PrintStream out = System.out;
    final PrintStream err = System.err;
    ClassicEngineBoot.getInstance().start();
    if ( NamingManager.hasInitialContextFactoryBuilder() == false ) {
      NamingManager.setInitialContextFactoryBuilder( new DebugJndiContextFactoryBuilder() );
    }
    System.setOut( out );
    System.setErr( err );
  }

  public void testEventNotification() {
    final MasterReport report = new MasterReport();
    final Element mrLabel = TableTestUtil.createDataItem( "Label" );
    report.getPageHeader().addElement( mrLabel );

    final Element mrLabel2 = TableTestUtil.createDataItem( "Label2" );
    report.getPageHeader().addElement( mrLabel2 );

    final TestReportDesignerContext designerContext = new TestReportDesignerContext();
    final int idx = designerContext.addMasterReport( report );
    final ReportRenderContext masterContext = designerContext.getReportRenderContext( idx );

    final SharedElementRenderer sharedRenderer = masterContext.getSharedRenderer();
    assertTrue( sharedRenderer.performLayouting() );

    // we should have conflicts ..
    assertFalse( sharedRenderer.getConflicts().isEmpty() );
  }

  public void testPerformance() {
    runPerformanceTestInternal();
  }

  private void runPerformanceTestInternal() {
    final MasterReport report = new MasterReport();
    report.getReportConfiguration().setConfigProperty
      ( "org.pentaho.reporting.engine.classic.core.modules.output.table.base.ReportCellConflicts", "false" );
    final Element mrLabel = TableTestUtil.createDataItem( "Label" );
    report.getPageHeader().addElement( mrLabel );

    final Element mrLabel2 = TableTestUtil.createDataItem( "Label2" );
    report.getPageHeader().addElement( mrLabel2 );

    final TestReportDesignerContext designerContext = new TestReportDesignerContext();
    final int idx = designerContext.addMasterReport( report );
    final ReportRenderContext masterContext = designerContext.getReportRenderContext( idx );

    final SharedElementRenderer sharedRenderer = masterContext.getSharedRenderer();
    final StopWatch w = StopWatch.startNew();
    run( mrLabel2, sharedRenderer );
    DebugLog.log( w );
  }

  private void run( final Element mrLabel2, final SharedElementRenderer sharedRenderer ) {
    for ( int i = 0; i < 1; i += 1 ) {
      mrLabel2.getStyle().setStyleProperty( ElementStyleKeys.ANCHOR_NAME, String.valueOf( Math.random() ) );
      //      mrLabel2.getStyle().setStyleProperty(ElementStyleKeys.POS_Y, new Float(100f * Math.random()));
      //   mrLabel2.getStyle().setStyleProperty(ElementStyleKeys.POS_X, new Float(100f * Math.random()));
      assertTrue( sharedRenderer.performLayouting() );
    }
  }
}
