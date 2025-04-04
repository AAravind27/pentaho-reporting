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


package gui;

import java.io.PrintWriter;

public class PropertyHelper {
  /**
   * A constant defining that text should be escaped in a way which is suitable for property keys.
   */
  public static final int ESCAPE_KEY = 0;
  /**
   * A constant defining that text should be escaped in a way which is suitable for property values.
   */
  public static final int ESCAPE_VALUE = 1;
  /**
   * A constant defining that text should be escaped in a way which is suitable for property comments.
   */
  public static final int ESCAPE_COMMENT = 2;


  /**
   * Performs the necessary conversion of an java string into a property escaped string.
   *
   * @param text       the text to be escaped
   * @param escapeMode the mode that should be applied.
   * @param writer     the writer that should receive the content.
   */
  public static void saveConvert( final String text,
                                  final int escapeMode,
                                  final PrintWriter writer ) {
    if ( text == null ) {
      return;
    }

    final char[] string = text.toCharArray();
    final char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7',
      '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    for ( int x = 0; x < string.length; x++ ) {
      final char aChar = string[ x ];
      switch( aChar ) {
        case ' ': {
          if ( ( escapeMode != ESCAPE_COMMENT ) &&
            ( x == 0 || escapeMode == ESCAPE_KEY ) ) {
            writer.print( '\\' );
          }
          writer.print( ' ' );
          break;
        }
        case '\\': {
          writer.print( '\\' );
          writer.print( '\\' );
          break;
        }
        case '\t': {
          if ( escapeMode == ESCAPE_COMMENT ) {
            writer.print( aChar );
          } else {
            writer.print( '\\' );
            writer.print( 't' );
          }
          break;
        }
        case '\n': {
          writer.print( '\\' );
          writer.print( 'n' );
          break;
        }
        case '\r': {
          writer.print( '\\' );
          writer.print( 'r' );
          break;
        }
        case '\f': {
          if ( escapeMode == ESCAPE_COMMENT ) {
            writer.print( aChar );
          } else {
            writer.print( '\\' );
            writer.print( 'f' );
          }
          break;
        }
        case '#':
        case '"':
        case '!':
        case '=':
        case ':': {
          if ( escapeMode == ESCAPE_COMMENT ) {
            writer.print( aChar );
          } else {
            writer.print( '\\' );
            writer.print( aChar );
          }
          break;
        }
        default:
          if ( ( aChar < 0x0020 ) || ( aChar > 0x007e ) ) {
            writer.print( '\\' );
            writer.print( 'u' );
            writer.print( hexChars[ ( aChar >> 12 ) & 0xF ] );
            writer.print( hexChars[ ( aChar >> 8 ) & 0xF ] );
            writer.print( hexChars[ ( aChar >> 4 ) & 0xF ] );
            writer.print( hexChars[ aChar & 0xF ] );
          } else {
            writer.print( aChar );
          }
      }
    }
  }
}
