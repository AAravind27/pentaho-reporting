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


package org.pentaho.reporting.engine.classic.core.modules.parser.simple;

import org.pentaho.reporting.engine.classic.core.modules.parser.base.DataFactoryReadHandlerFactory;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.MasterReportXmlResourceFactory;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.ReportElementReadHandlerFactory;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.SubReportReadHandlerFactory;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.SubReportXmlResourceFactory;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.common.DataFactoryRefReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.AnchorFieldReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.BandReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.ComponentFieldReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.DateFieldReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.DrawableFieldReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.DrawableRefReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.DrawableURLFieldReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.EllipseReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.ImageFieldReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.ImageRefReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.ImageURLFieldReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.LabelReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.LineReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.MessageFieldReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.NumberFieldReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.RectangleReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.ResourceFieldReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.ResourceLabelReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.ResourceMessageReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.RoundRectangleReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.ShapeFieldReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.SimpleSubReportReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers.StringFieldReadHandler;
import org.pentaho.reporting.libraries.base.boot.AbstractModule;
import org.pentaho.reporting.libraries.base.boot.ModuleInitializeException;
import org.pentaho.reporting.libraries.base.boot.SubSystem;

/**
 * The Module specification for the simple parser module. This module handles the simple report definition format.
 *
 * @author Thomas Morgner
 */
public class SimpleParserModule extends AbstractModule {
  public static final String NAMESPACE = "http://jfreereport.sourceforge.net/namespaces/reports/legacy/simple";

  /**
   * Loads the module information from the module.properties file.
   *
   * @throws ModuleInitializeException
   *           if loading the specifications failed.
   */
  public SimpleParserModule() throws ModuleInitializeException {
    loadModuleInfo();
  }

  /**
   * Initializes the module.
   *
   * @param subSystem
   *          the subsystem which this module belongs to.
   * @throws ModuleInitializeException
   *           if initialisation fails.
   */
  public void initialize( final SubSystem subSystem ) throws ModuleInitializeException {
    final ReportElementReadHandlerFactory factory = ReportElementReadHandlerFactory.getInstance();
    factory.setDefaultNamespace( NAMESPACE );
    factory.setElementHandler( "string-field", StringFieldReadHandler.class );
    factory.setElementHandler( "anchor-field", AnchorFieldReadHandler.class );
    factory.setElementHandler( "band", BandReadHandler.class );
    factory.setElementHandler( "component-field", ComponentFieldReadHandler.class );
    factory.setElementHandler( "date-field", DateFieldReadHandler.class );
    factory.setElementHandler( "drawable-field", DrawableFieldReadHandler.class );
    factory.setElementHandler( "drawable-url-field", DrawableURLFieldReadHandler.class );
    factory.setElementHandler( "drawableref", DrawableRefReadHandler.class );
    factory.setElementHandler( "ellipse", EllipseReadHandler.class );
    factory.setElementHandler( "image-field", ImageFieldReadHandler.class );
    factory.setElementHandler( "imageref", ImageRefReadHandler.class );
    factory.setElementHandler( "imageurl-field", ImageURLFieldReadHandler.class );
    factory.setElementHandler( "label", LabelReadHandler.class );
    factory.setElementHandler( "line", LineReadHandler.class );
    factory.setElementHandler( "message-field", MessageFieldReadHandler.class );
    factory.setElementHandler( "number-field", NumberFieldReadHandler.class );
    factory.setElementHandler( "rectangle", RectangleReadHandler.class );
    factory.setElementHandler( "resource-field", ResourceFieldReadHandler.class );
    factory.setElementHandler( "resource-label", ResourceLabelReadHandler.class );
    factory.setElementHandler( "resource-message", ResourceMessageReadHandler.class );
    factory.setElementHandler( "round-rectangle", RoundRectangleReadHandler.class );
    factory.setElementHandler( "shape-field", ShapeFieldReadHandler.class );
    factory.setElementHandler( "string-field", StringFieldReadHandler.class );

    MasterReportXmlResourceFactory.register( SimpleReportXmlFactoryModule.class );
    SubReportXmlResourceFactory.register( SimpleSubReportXmlFactoryModule.class );

    SubReportReadHandlerFactory.getInstance().setElementHandler( NAMESPACE, "sub-report",
        SimpleSubReportReadHandler.class );
    DataFactoryReadHandlerFactory.getInstance().setElementHandler( NAMESPACE, "data-factory",
        DataFactoryRefReadHandler.class );

    performExternalInitialize( SimpleParserModuleInit.class.getName(), SimpleParserModule.class );
  }
}
