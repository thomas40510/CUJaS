package com.apogee.dev.CUJaS.UI;

import com.apogee.dev.CUJaS.UI.utils.ANSIColorConstants;
import com.apogee.dev.CUJaS.UI.utils.GUIConstants;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.Serial;

/**
 * <p>A {@code JTextPane} that supports ANSI colors. This pane adds an
 * {@link #append(String) append} method that will append a given string to the
 * pane; however, it will also understand certain ANSI color codes and
 * manipulate the pane to these colors. See the
 * {@link com.apogee.dev.CUJaS.UI.utils.ANSIColorConstants constants} class to see
 * what codes have been interpreted by this pane. This also makes the pane
 * ignore word wrap.</p>
 * <p>The method for which ANSI color codes can be derived was mainly received
 * by
 * <a href="https://stackoverflow.com/questions/6899282/ansi-colors-in-java-swing-text-fields">this</a>
 * stack overflow question. However, we have since improved on it.
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.1.5
 * @see com.apogee.dev.CUJaS.UI.utils.ANSIColorConstants
 */
public class ColoredTextPane extends JTextPane implements Printable
{
  @Serial
  private static final long serialVersionUID = 5972869629177225150L;

  /**
   * The current color we are using when we are scanning. This is an instance
   * variable because the {@link #append(String) append} method needs a variable
   * to store where it is at while scanning the string. This can be a background
   * color or a foreground color, as determined by {@link #isBackground}.
   */
  protected Color currentColor = ANSIColorConstants.COLOR_RESET;

  /**
   * Whether the current color is a background color. This is an instance
   * variable because the {@link #append(String) append} method needs a variable
   * to store whether the current color is a background color while scanning
   * the string.
   */
  protected boolean isBackground = false;

  /**
   * A history of what has been {@link #append(String) appended}.
   */
  protected StringBuffer typed = new StringBuffer();

  private String remaining = "";
  //enable colors or not.
  private boolean colorMode;

  /**
   * Constructs a {@code ColoredTextPane}. This pane is uneditable, and has the
   * default background and foreground as defined in the
   * {@link com.apogee.dev.CUJaS.UI.utils.ANSIColorConstants constants} class.
   * Moreover, it also uses the font as defined in the
   * {@link com.apogee.dev.CUJaS.UI.utils.GUIConstants#DEFAULT_FONT gui} constants
   * class.
   */
  public ColoredTextPane()
  {
    this.colorMode = true;
    this.setFont(GUIConstants.DEFAULT_FONT);
    this.setBackground(ANSIColorConstants.BACKGROUND_RESET);
    this.setForeground(ANSIColorConstants.COLOR_RESET);
    this.setEditable(false);
  }

  /**
   * Returns {@code true} if a viewport should always force the width of this
   * {@code Scrollable} to match the width of the viewport.
   * @return {@code false} if the {@code ComponentUI}'s preferred width is less
   *   than or equal to the parents width, {@code true} otherwise.
   */
  @Override
  public boolean getScrollableTracksViewportWidth()
  {
    Component parent = this.getParent();
    ComponentUI ui = this.getUI();

    return parent == null || (ui.getPreferredSize(this).width <=
            parent.getSize().width);
  }

  /**
   * Prints the given page. This will use the current graphics on the pane and
   * draw them on a buffered image, which will be sent to the printer service.
   * This <b>will not</b> print everything that has been appended to the colored
   * text pane; it will only print what is currently displayed.
   * @param g The graphics to draw on (what will be printed).
   * @param format The format to use when transforming the graphics.
   * @param pageIndex The page number (determines which page to print).
   */
  @Override
  public int print(Graphics g, PageFormat format, int pageIndex)
  {
    /*
     * User (0,0) is typically outside the imageable area, so we must
     * translate by the X and Y values in the PageFormat to avoid clipping
     */
    Graphics2D g2d = (Graphics2D)g;
    g2d.translate(format.getImageableX(), format.getImageableY());

    /*
     * Get the line height, lines per page, and number of lines
     */
    int lineHeight = (g.getFontMetrics(GUIConstants.DEFAULT_FONT)).getHeight();
    int linesPerPage = (int)format.getImageableHeight() / lineHeight;
    int textLines = ((this.typed.toString()).split("\n")).length;
    int documentHeight =
      (((lineHeight * textLines) / (int)format.getImageableHeight()) + 1) *
      (int)format.getImageableHeight();

    //TODO: this will only print lines to a certain point. This needs fixed.
    BufferedImage document = new BufferedImage((int)format.getImageableWidth(),
      documentHeight, BufferedImage.TYPE_INT_ARGB);
    this.paint(document.getGraphics());
    BufferedImage[] pages =
      new BufferedImage[((textLines - 1) / linesPerPage) + 1];
    for(int b = 0; b < pages.length; b++)
    {
      pages[b] = document.getSubimage(0, (int)(b * format.getImageableHeight()),
        (int)format.getImageableWidth(), (int)format.getImageableHeight());
    }

    if(pageIndex < 0 || pageIndex >= pages.length)
    {
      return NO_SUCH_PAGE;
    }
    else
    {
      g.drawImage(pages[pageIndex], 0, 0, null);
      /* tell the caller that this page is part of the printed document */
      return PAGE_EXISTS;
    }
  }

  /**
   * Sets the text of this {@code ColoredTextPane}, which is expected to be in
   * the format of the content type of this editor. To see the exact
   *  documentation, visit
   * {@link javax.swing.JEditorPane#setText(String) this} link. We needed to
   * override this method due to our coloring of the pane. With this in mind,
   * we do call {@code super.setText(text)} at the end of our {@code setText}
   * method. However, we have to ensure that the color state is only preserved
   * to our current state of color (not what our selection contains).
   * @param text The new text to be set; if {@code null} the old text will be
   *  deleted.
   */
  @Override
  public void setText(String text)
  {
    /*
     * We have to deselect the text we may have selected, as this will cause
     * the colored text pane to draw the next text with the selected color
     * attributes.
     */
    int end = this.getSelectionEnd();
    this.setSelectionStart(end);
    this.setSelectionEnd(end);
    //Okay do all the hard work for us.
    super.setText(text);
    this.typed.delete(0, this.typed.length());
    this.typed.append(text);
    //Send the reset symbol, just in case.
    this.append(ANSIColorConstants.ESCAPE_TEXT + "[0m");
  }

  /**
   * Appends the given text to the pane. This string may contain ANSI color
   * codes; we interpret them and change the pane accordingly.
   * @param s The string that may contain ANSI color codes.
   */
  public void append(String s)
  {
    this.setEditable(true);
    //Add what is going to be appended to the history of what was typed.
    this.typed.append(s);
    //currentColor char position in addString
    int aPos = 0;
    //index of next Escape sequence
    int aIndex;
    //index of "m" terminating Escape sequence
    int mIndex;
    String tmpString;
    //true until no more Escape sequences
    boolean stillSearching;
    String addString = this.remaining + s;
    this.remaining = "";

    if(!addString.isEmpty())
    {
      //find first escape
      aIndex = addString.indexOf(ANSIColorConstants.ESCAPE_TEXT);
      if(aIndex == -1)
      {
        //no escape code in this string, so just send it with currentColor color
        this.append(addString, this.currentColor, isBackground);
        return;
      }

      //otherwise there is an escape character in the string, so we process it
      if(aIndex > 0)
      {
        //Escape is not first char, so send text up to first escape
        tmpString = addString.substring(0, aIndex);
        this.append(tmpString, this.currentColor, isBackground);
        aPos = aIndex;
      }

      //aPos is now at the beginning of the first escape sequence
      stillSearching = true;
      while(stillSearching)
      {
        //find the end of the escape sequence
        mIndex = addString.indexOf(ANSIColorConstants.ESCAPE_TEXT_END, aPos);
        if(mIndex < 0)
        {
          //the buffer ends halfway through the ansi string!
          this.remaining = addString.substring(aPos);
          stillSearching = false;
          continue;
        }
        else
        {
          tmpString = addString.substring(aPos, mIndex + 1);
          if(ANSIColorConstants.isEscape(tmpString))
          {
            this.currentColor =
              ANSIColorConstants.isReset(tmpString) || !this.colorMode ?
              ANSIColorConstants.COLOR_RESET :
              ANSIColorConstants.getANSIColor(tmpString);
            isBackground = ANSIColorConstants.isBackgroundEscape(tmpString);

            if(ANSIColorConstants.isReset(tmpString) || !this.colorMode)
            {
              this.isBackground = false;
              this.append("", ANSIColorConstants.COLOR_RESET, false);
              this.append("", ANSIColorConstants.BACKGROUND_RESET, true);
            }
          }
          else
          {
            //The escape sequence was received, but did not have a valid code
            mIndex = aPos;
            //skip the escape text, but still process all the gobbldy gook
          }
        }

        aPos = mIndex + 1;
        //now we have the color, send text that is in that color
        aIndex = addString.indexOf(ANSIColorConstants.ESCAPE_TEXT, aPos);

        if(aIndex == -1)
        {
          //if that was the last sequence of the input, send remaining text
          tmpString = addString.substring(aPos);
          this.append(tmpString, this.currentColor, this.isBackground);
          stillSearching = false;
          //jump out of loop early, as the whole string has been sent
          continue;
        }

        //there is another escape sequence, so send part of the string
        tmpString = addString.substring(aPos, aIndex);
        aPos = aIndex;
        this.append(tmpString, this.currentColor, isBackground);
        //while there's text in the input buffer
      }
    }
    this.setEditable(false);
  }

  /**
   * Returns whether colors have been enabled with the text pane.
   * @return Whether colors have been enabled with the text pane.
   */
  public boolean getColorMode()
  {
    return this.colorMode;
  }

  /**
   * Returns all the text that has been appended to this pane. This is stored
   * within a {@code StringBuffer}, and text is added to this buffer whenever
   * the {@link #append(String) append} method is called. This will save any
   * ANSI escape codes sent to it.
   * @return All the text that has been appended to this pane.
   * @see java.lang.StringBuffer
   */
  public String getHistory()
  {
    return this.typed.toString();
  }

  /**
   * Sets the new color mode. If this is {@code false}, this will disable any
   * colors that have been drawn. It will also redraw the text as it came in the
   * pane (if the mode has been changed).
   * @param colorMode The new color mode ({@code true} for colors enabled).
   */
  public void setColorMode(boolean colorMode)
  {
    if(this.colorMode != colorMode)
    {
      this.colorMode = colorMode;
      String history = this.getHistory();
      this.setText("");
      this.append(history);
    }
  }

  /**
   * Appends the text to the pane using the given color. This is the main
   * backbone to the {@link #append(String) append} method. This will use the
   * given color (and whether it is a background color) and the current
   * {@code AttributeSet} to draw the given text.
   * @param s The text to draw.
   * @param c The color of the text.
   * @param background Whether this is a background or foreground color.
   */
  protected void append(String s, Color c, boolean background)
  {
    StyleContext sc = StyleContext.getDefaultStyleContext();
    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
      background ? StyleConstants.Background : StyleConstants.Foreground, c);

    int len = this.getDocument().getLength();//same value as getText().length();
    this.setCaretPosition(len); //place caret at the end (with no selection)
    this.setCharacterAttributes(aset, false);
    this.replaceSelection(s); //there is no selection, so inserts at caret
  }
}