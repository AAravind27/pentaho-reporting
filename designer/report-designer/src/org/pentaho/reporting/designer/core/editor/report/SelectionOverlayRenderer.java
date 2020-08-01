/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2009 Pentaho Corporation.  All rights reserved.
 */

package org.pentaho.reporting.designer.core.editor.report;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;

import org.pentaho.reporting.designer.core.ReportDesignerBoot;
import org.pentaho.reporting.designer.core.editor.ReportRenderContext;
import org.pentaho.reporting.designer.core.model.ModelUtility;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.Section;

/**
 * Todo: Document me!
 * <p/>
 * Date: 26.04.2009
 * Time: 20:51:09
 *
 * @author Thomas Morgner.
 */
public class SelectionOverlayRenderer implements OverlayRenderer
{
  private Section rootElement;
  private ReportRenderContext context;
  private double zoomFactor;

  public SelectionOverlayRenderer(final Element defaultElement)
  {
    if (defaultElement instanceof Section)
    {
      this.rootElement = (Section) defaultElement;
    }
  }

  public void validate(final ReportRenderContext context, final double zoomFactor)
  {
    this.context = context;
    this.zoomFactor = zoomFactor;
  }

  public void draw(final Graphics2D graphics, final Rectangle2D bounds, final ImageObserver obs)
  {
    if (context == null || rootElement == null)
    {
      return;
    }

    graphics.translate(bounds.getX(), bounds.getY());
    
    final Element[] visualElements = context.getSelectionModel().getSelectedVisualElements();
    for (int i = 0; i < visualElements.length; i++)
    {
      final Element visualElement = visualElements[i];
      if (ModelUtility.isDescendant(rootElement, visualElement) == false)
      {
        continue;
      }
      final Object o = visualElement.getAttribute(ReportDesignerBoot.DESIGNER_NAMESPACE, ReportDesignerBoot.SELECTION_OVERLAY_INFORMATION);
      if (o instanceof SelectionOverlayInformation == false)
      {
        continue;
      }
      
      final SelectionOverlayInformation information = (SelectionOverlayInformation) o;
      information.validate(zoomFactor);
      information.draw(graphics, obs);
    }
  }
}