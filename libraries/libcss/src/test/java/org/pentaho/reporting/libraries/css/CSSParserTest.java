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


package org.pentaho.reporting.libraries.css;

import junit.framework.TestCase;
import org.pentaho.reporting.libraries.css.keys.border.BorderStyleKeys;
import org.pentaho.reporting.libraries.css.keys.box.BoxStyleKeys;
import org.pentaho.reporting.libraries.css.model.CSSStyleRule;
import org.pentaho.reporting.libraries.css.model.StyleKey;
import org.pentaho.reporting.libraries.css.model.StyleKeyRegistry;
import org.pentaho.reporting.libraries.css.model.StyleSheet;
import org.pentaho.reporting.libraries.css.parser.StyleSheetParserUtil;
import org.pentaho.reporting.libraries.css.values.CSSFunctionValue;
import org.pentaho.reporting.libraries.css.values.CSSValue;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

public class CSSParserTest extends TestCase {
  public CSSParserTest() {
  }

  public CSSParserTest( final String s ) {
    super( s );
  }

  protected void setUp() throws Exception {
    LibCssBoot.getInstance().start();
  }

  public void testParseColorFunction() {
    StyleSheet s = new StyleSheet();
    CSSStyleRule rule = new CSSStyleRule( s, null );
    rule.setPropertyValueAsString( BorderStyleKeys.BACKGROUND_COLOR, "rgb(0,255,0)" );
    final CSSFunctionValue value = (CSSFunctionValue) rule.getPropertyCSSValue( BorderStyleKeys.BACKGROUND_COLOR );
    assertEquals( "rgb", value.getFunctionName() );
    final CSSValue[] cssValues = value.getParameters();
    assertEquals( "Parameter-count", 3, cssValues.length );
    assertEquals( "Parameter1", "0", cssValues[ 0 ].toString() );
    assertEquals( "Parameter2", "255", cssValues[ 1 ].toString() );
    assertEquals( "Parameter3", "0", cssValues[ 2 ].toString() );
  }

  public void testParseInitialStyleSheet() throws Exception {
    final ResourceManager resourceManager = new ResourceManager();
    resourceManager.registerDefaults();

    final Resource directly = resourceManager.createDirectly
      ( "res://org/pentaho/reporting/libraries/css/initial.css", StyleSheet.class );
    assertNotNull( directly.getResource() );
  }


  /**
   * Tests the style resolution to make sure the <code>StyleResolver</code> class is initialized correctly
   */
  public void testStyleResolver() throws ResourceException {
    final StyleKey fontFamily = StyleKeyRegistry.getRegistry().findKeyByName( "font-family" );
    final StyleKey fontSize = StyleKeyRegistry.getRegistry().findKeyByName( "font-size" );
    final StyleKey fontWeight = StyleKeyRegistry.getRegistry().findKeyByName( "font-weight" );
    final StyleKey fontStyle = StyleKeyRegistry.getRegistry().findKeyByName( "font-style" );
    final StyleKey textAlign = StyleKeyRegistry.getRegistry().findKeyByName( "text-align" );
    final StyleKey color = StyleKeyRegistry.getRegistry().findKeyByName( "color" );
    final StyleKey backgroundColor = StyleKeyRegistry.getRegistry().findKeyByName( "background-color" );
    assertNotNull( "Could not retrieve the StyleKey for [font-family]", fontFamily );
    assertNotNull( "Could not retrieve the StyleKey for [font-size]", fontSize );
    assertNotNull( "Could not retrieve the StyleKey for [font-weight]", fontWeight );
    assertNotNull( "Could not retrieve the StyleKey for [font-style]", fontStyle );
    assertNotNull( "Could not retrieve the StyleKey for [text-align]", textAlign );
    assertNotNull( "Could not retrieve the StyleKey for [color]", color );
    assertNotNull( "Could not retrieve the StyleKey for [background-color]", backgroundColor );
  }

  public void testParseSingleStyle() throws Exception {
    StyleSheet s = new StyleSheet();
    CSSStyleRule rule = new CSSStyleRule( s, null );
    rule.setPropertyValueAsString( BoxStyleKeys.HEIGHT, "10%" );
    assertEquals( "10%", rule.getPropertyCSSValue( BoxStyleKeys.HEIGHT ).toString() );
    rule.setPropertyValueAsString( BoxStyleKeys.HEIGHT, "10pt" );
    assertEquals( "10pt", rule.getPropertyCSSValue( BoxStyleKeys.HEIGHT ).toString() );
    rule.setPropertyValueAsString( BoxStyleKeys.HEIGHT, "10px" );
    assertEquals( "10px", rule.getPropertyCSSValue( BoxStyleKeys.HEIGHT ).toString() );
  }

  public void testParseStyleRule() throws Exception {
    final ResourceManager resourceManager = new ResourceManager();
    resourceManager.registerDefaults();

    StyleSheetParserUtil.getInstance().parseStyleRule
      ( null, "* { width: 10% }", null, null, resourceManager, StyleKeyRegistry.getRegistry() );
  }
}
