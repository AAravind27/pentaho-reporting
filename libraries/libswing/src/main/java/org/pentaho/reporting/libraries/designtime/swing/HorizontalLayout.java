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


package org.pentaho.reporting.libraries.designtime.swing;

import java.awt.*;

public class HorizontalLayout implements LayoutManager {

  public static final int CENTER = 0;
  public static final int RIGHT = 1;
  public static final int LEFT = 2;
  public static final int BOTH = 3;
  public static final int TOP = 1;
  public static final int BOTTOM = 2;

  private int hgap; //the horizontal hgap between components...defaults to 5
  private int alignment; //LEFT, RIGHT, CENTER or BOTH...how the components are justified
  private int anchor; //TOP, BOTTOM or CENTER ...where are the components positioned in an overlarge space

  public HorizontalLayout() {
    this( 5, CENTER, TOP );
  }

  public HorizontalLayout( final int hgap ) {
    this( hgap, CENTER, TOP );
  }

  public HorizontalLayout( final int hgap, final int alignment ) {
    this( hgap, alignment, TOP );
  }

  public HorizontalLayout( final int hgap, final int alignment, final int anchor ) {
    this.hgap = hgap;
    this.alignment = alignment;
    this.anchor = anchor;
  }

  private Dimension layoutSize( final Container parent, final boolean minimum ) {
    final Dimension dim = new Dimension( 0, 0 );
    synchronized( parent.getTreeLock() ) {
      final int n = parent.getComponentCount();
      for ( int i = 0; i < n; i++ ) {
        final Component c = parent.getComponent( i );
        if ( c.isVisible() ) {
          final Dimension d = minimum ? c.getMinimumSize() : c.getPreferredSize();
          dim.height = Math.max( dim.height, d.height );
          dim.width += d.width;
          if ( i > 0 ) {
            dim.width += hgap;
          }
        }
      }
    }
    final Insets insets = parent.getInsets();
    dim.width += insets.left + insets.right + hgap + hgap;
    dim.height += insets.top + insets.bottom;
    return dim;
  }

  public void layoutContainer( final Container parent ) {
    final Insets insets = parent.getInsets();
    synchronized( parent.getTreeLock() ) {
      final int n = parent.getComponentCount();
      final Dimension pd = parent.getSize();
      int x = 0;
      //work out the total size
      for ( int i = 0; i < n; i++ ) {
        final Component c = parent.getComponent( i );
        final Dimension d = c.getPreferredSize();
        x += d.width + hgap;
      }
      x -= hgap; //otherwise there's a hgap too many
      //Work out the anchor paint
      if ( alignment == LEFT ) {
        x = insets.left;
      } else if ( alignment == CENTER ) {
        x = ( pd.width - x ) / 2;
      } else {
        x = pd.width - x - insets.right;
      }
      //do layout
      for ( int i = 0; i < n; i++ ) {
        final Component c = parent.getComponent( i );
        final Dimension d = c.getPreferredSize();
        int y = insets.top;
        int height = d.height;
        if ( anchor == CENTER ) {
          y = ( pd.height - d.height ) / 2;
        } else if ( anchor == BOTTOM ) {
          y = pd.height - d.height - insets.bottom;
        } else if ( anchor == BOTH ) {
          height = pd.height - insets.top - insets.bottom;
        }
        c.setBounds( x, y, d.width, height );
        x += d.width + hgap;
      }
    }
  }

  public Dimension minimumLayoutSize( final Container parent ) {
    return layoutSize( parent, true );
  }

  public Dimension preferredLayoutSize( final Container parent ) {
    return layoutSize( parent, false );
  }

  public void addLayoutComponent( final String name, final Component comp ) {
  }

  public void removeLayoutComponent( final Component comp ) {
  }

  public String toString() {
    return getClass().getName() + "[hgap=" + hgap + " align=" + alignment + " anchor=" + anchor + ']';
  }
}
