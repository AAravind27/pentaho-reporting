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


package org.pentaho.plugin.jfreereport.reportcharts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.pentaho.plugin.jfreereport.reportcharts.backport.XYDotRenderer;

public class ScatterPlotChartExpression extends XYChartExpression {
  private static final long serialVersionUID = 7822813481960064738L;
  private int dotWidth;
  private int dotHeight;

  public ScatterPlotChartExpression() {
    dotHeight = 5;
    dotWidth = 5;
  }

  protected JFreeChart computeXYChart( final XYDataset xyDataset ) {
    final JFreeChart chart;
    if ( xyDataset instanceof TimeSeriesCollection ) {
      chart =
        ChartFactory.createTimeSeriesChart( computeTitle(), getDomainTitle(), getRangeTitle(), xyDataset,
          isShowLegend(), false, false );
      final XYPlot xyPlot = chart.getXYPlot();
      final XYLineAndShapeRenderer itemRenderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
      final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( false, true );
      renderer.setBaseToolTipGenerator( itemRenderer.getBaseToolTipGenerator() );
      renderer.setURLGenerator( itemRenderer.getURLGenerator() );
      xyPlot.setRenderer( renderer );

    } else {
      final PlotOrientation orientation = computePlotOrientation();
      chart = ChartFactory.createScatterPlot( computeTitle(), getDomainTitle(), getRangeTitle(),
        xyDataset, orientation, isShowLegend(), false, false );
    }

    chart.getXYPlot().setRenderer( new XYDotRenderer() );
    configureLogarithmicAxis( chart.getXYPlot() );
    return chart;
  }

  protected void configureChart( final JFreeChart chart ) {
    super.configureChart( chart );

    final XYPlot xypl = chart.getXYPlot();
    final XYItemRenderer renderer = xypl.getRenderer();
    if ( renderer instanceof XYDotRenderer ) {
      final XYDotRenderer renderer1 = (XYDotRenderer) renderer;
      renderer1.setDotHeight( getDotHeight() );
      renderer1.setDotWidth( getDotWidth() );
    }

  }

  /**
   * @return Returns the dot height.
   */
  public int getDotHeight() {
    return dotHeight;
  }

  /**
   * @param height The dot height to set.
   */
  public void setDotHeight( final int height ) {
    this.dotHeight = height;
  }

  /**
   * @return Returns the dot width.
   */
  public int getDotWidth() {
    return dotWidth;
  }

  /**
   * @param width The dot width to set.
   */
  public void setDotWidth( final int width ) {
    this.dotWidth = width;
  }

}
