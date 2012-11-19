/*
  Part of the Fisica library - http://www.ricardmarxer.com/fisica

  Copyright (c) 2009 - 2010 Ricard Marxer

  Fisica is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.
  
  You should have received a copy of the GNU Lesser General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;

import processing.core.*;

/**
 * This is a class from which all drawable objects of a world inherit.
 *
 * It contains most helping methods that handles stroke and fill colors and styles, as well as image attachments, etc.
 *
 */
abstract class FDrawable {
  protected boolean m_drawable = true;

  protected boolean m_fill = true;
  protected int m_fillColor = 0xFFFFFFFF;
  protected boolean m_stroke = true;
  protected int m_strokeColor = 0xFF000000;
  protected float m_strokeWeight = 1.0f;

  protected PImage m_image = null;
  protected float m_imageAlpha = 255.0f;
  protected PImage m_mask = null;

  protected void updateStyle( FDrawable other ){
    m_drawable = other.m_drawable;

    m_fill = other.m_fill;
    m_fillColor = other.m_fillColor;
    m_stroke = other.m_stroke;
    m_strokeColor = other.m_strokeColor;
    m_strokeWeight = other.m_strokeWeight;

    m_image = other.m_image;
    m_imageAlpha = other.m_imageAlpha;
    m_mask = other.m_mask;
  }

  protected void appletStroke( PGraphics applet, int argb ){
    final int a = (argb >> 24) & 0xFF;
    final int r = (argb >> 16) & 0xFF;  // Faster way of getting red(argb)
    final int g = (argb >> 8) & 0xFF;   // Faster way of getting green(argb)
    final int b = argb & 0xFF;          // Faster way of getting blue(argb)

    applet.stroke(r, g, b, a);
  }

  protected void appletFill( PGraphics applet, int argb ){
    final int a = (argb >> 24) & 0xFF;
    final int r = (argb >> 16) & 0xFF;  // Faster way of getting red(argb)
    final int g = (argb >> 8) & 0xFF;   // Faster way of getting green(argb)
    final int b = argb & 0xFF;          // Faster way of getting blue(argb)

    applet.fill(r, g, b, a);
  }

  protected void appletFillStroke( PGraphics applet ) {
    if (m_fill) {
      appletFill(applet, m_fillColor);
    } else {
      applet.noFill();
    }

    if (m_stroke) {
      appletStroke(applet, m_strokeColor);
    } else {
      applet.noStroke();
    }
    applet.strokeWeight(m_strokeWeight);
  }

  protected void drawImage(PGraphics applet) {
    applet.tint(255, 255, 255, m_imageAlpha);
    applet.image(m_image, 0-m_image.width/2, 0-m_image.height/2);
    applet.tint(255, 255, 255, 255);
  }

  /**
   * This method is called when calling {@code world.draw()}.
   * This method may be overriden to allow custom drawing of the object.
   *
   * @param graphics  the graphics onto which the object must be drawn.
   */
  public void draw(PGraphics graphics) {
    // Don't draw anything, each subclass will draw itself
  }

    /**
   * This method is called when calling {@code world.draw()}.
   * This method may be overriden to allow custom drawing of the object.
   *
   * @param applet  the applet onto which the object must be drawn.
   */
   public void draw(PApplet applet) {
    // Don't draw anything, each subclass will draw itself
    draw(applet.g);
  }

  /**
   * This method is called when calling {@code world.drawDebug()}.
   * This method may be overriden to allow custom debug drawing of the object.
   *
   * @param graphics  the graphics onto which the object must be drawn.
   */
  public void drawDebug(PGraphics graphics) {
    // Don't draw anything, each subclass will draw itself
  }

  /**
   * This method is called when calling {@code world.drawDebug()}.
   * This method may be overriden to allow custom debug drawing of the object.
   *
   * @param applet  the applet onto which the object must be drawn.
   */
  public void drawDebug(PApplet applet) {
    // Don't draw anything, each subclass will draw itself
    drawDebug(applet.g);
  }

  
  /**
   * Attach an image to the object.
   * This method allows to draw an image onto the screen instead of calling the {@link #draw(PApplet)} method.
   *
   * @param img  the PImage to attach to the object.
   */
  public void attachImage( PImage img ) {
    m_image = img;
  }

  /**
   * Dettach any image that has been attached to the object.
   *
   * @see #attachImage(PImage)
   */
  public void dettachImage() {
    m_image = null;
  }

  /**
   * Get the opacity with which to draw the attached image.
   *
   * @return the opacity, a value from 0.0 to 1.0 with which to draw the attached image
   * @see #attachImage(PImage)
   * @see #setImageAlpha(float)
   */
  public float getImageAlpha() {
    return m_imageAlpha;
  }

  /**
   * Set the opacity with which to draw the attached image.
   *
   * @param alpha   the opacity, a value from 0.0 to 1.0 with which to draw the attached image
   * @see #attachImage(PImage)
   * @see #getImageAlpha()
   */
  public void setImageAlpha(float alpha) {
    m_imageAlpha = alpha;
  }

  /**
   * Set whether the object must be drawn or not.
   *
   * @param drawable  if {@code true} the object will be drawn, else it will not
   * @see #isDrawable()
   */
  public void setDrawable( boolean drawable ){
    m_drawable = drawable;
  }

  /**
   * Get whether the object must be drawn or not.
   *
   * @return drawable  if {@code true} the object will be drawn, else it will not
   * @see #setDrawable(boolean)
   */
  public boolean isDrawable(){
    return m_drawable;
  }

  /**
   * Returns the fill color of the object.
   *
   * @return a Processing color type. e.g.  {@code myBody.setFillColor(color(20,100,30,90));}
   * @see #setNoFill()
   * @see #setFill(float)
   * @see #setFill(float,float)
   * @see #setFill(float,float,float)
   * @see #setFill(float,float,float,float)
   */
  public int getFillColor() {
    return m_fillColor;
  }

  /**
   * Set the fill color of the object.  This method must be used in conjunction with Processing's color().  In most cases users will find it more convenient to use the versions of {@link #setFill(float)}, {@link #setFill(float,float)}, {@link #setFill(float,float,float)} or {@link #setFill(float,float,float,float)}
   *
   * @param col  a Processing color type. e.g.  {@code myBody.setFillColor(color(20,100,30,90));}
   * @see #setNoFill()
   * @see #setFill(float)
   * @see #setFill(float,float)
   * @see #setFill(float,float,float)
   * @see #setFill(float,float,float,float)
   */
  public void setFillColor(int col) {
    m_fill = true;
    m_fillColor = col;
  }

  /**
   * Set that the object must  be drawn without fill.
   *
   * @see #setFill(float)
   * @see #setFill(float,float)
   * @see #setFill(float,float,float)
   * @see #setFill(float,float,float,float)
   */
  public void setNoFill() {
    m_fill = false;
  }

  /**
   * Set the fill color of the object.
   *
   * @param g   gray value
   * @see #setFill(float)
   * @see #setFill(float,float)
   * @see #setFill(float,float,float)
   * @see #setFill(float,float,float,float)
   */
  public void setFill(float g){
    setFillColor(Fisica.parent().color(g));
  }

  /**
   * Set the fill color of the object.
   *
   * @param g   gray value
   * @param a   alpha (opacity) value
   * @see #setFill(float)
   * @see #setFill(float,float)
   * @see #setFill(float,float,float)
   * @see #setFill(float,float,float,float)
   */
  public void setFill(float g, float a){
    setFillColor(Fisica.parent().color(g, a));
  }

  /**
   * Set the fill color of the object.
   *
   * @param r   red value
   * @param g   green value
   * @param b   blue value
   * @see #setFill(float)
   * @see #setFill(float,float)
   * @see #setFill(float,float,float)
   * @see #setFill(float,float,float,float)
   */
  public void setFill(float r, float g, float b){
    setFillColor(Fisica.parent().color(r, g, b));
  }

  /**
   * Set the fill color of the object.
   *
   * @param r   red value
   * @param g   green value
   * @param b   blue value
   * @param a   alpha (opacity) value
   * @see #setFill(float)
   * @see #setFill(float,float)
   * @see #setFill(float,float,float)
   * @see #setFill(float,float,float,float)
   */
  public void setFill(float r, float g, float b, float a){
    setFillColor(Fisica.parent().color(r, g, b, a));
  }


  /**
   * Set the stroke color of the object.  This method must be used in conjunction with Processing's color().  In most cases users will find it more convenient to use the versions of {@link #setStroke(float)}, {@link #setStroke(float,float)}, {@link #setStroke(float,float,float)} or {@link #setStroke(float,float,float,float)}
   *
   * @param col  a Processing color type. e.g.  {@code myBody.setStrokeColor(color(20,100,30,90));}
   * @see #setNoStroke()
   * @see #setStroke(float)
   * @see #setStroke(float,float)
   * @see #setStroke(float,float,float)
   * @see #setStroke(float,float,float,float)
   */
  public void setStrokeColor(int col) {
    m_stroke = true;
    m_strokeColor = col;
  }

  /**
   * Set that the object must  be drawn without stroke.
   *
   * @see #setStroke(float)
   * @see #setStroke(float,float)
   * @see #setStroke(float,float,float)
   * @see #setStroke(float,float,float,float)
   */
  public void setNoStroke() {
    m_stroke = false;
  }

  /**
   * Set the stroke color of the object.
   *
   * @param g   gray value
   * @see #setStroke(float)
   * @see #setStroke(float,float)
   * @see #setStroke(float,float,float)
   * @see #setStroke(float,float,float,float)
   */
  public void setStroke(float g){
    setStrokeColor(Fisica.parent().color(g));
  }

  /**
   * Set the stroke color of the object.
   *
   * @param g   gray value
   * @param a   alpha (opacity) value
   * @see #setStroke(float)
   * @see #setStroke(float,float)
   * @see #setStroke(float,float,float)
   * @see #setStroke(float,float,float,float)
   */
  public void setStroke(float g, float a){
    setStrokeColor(Fisica.parent().color(g, a));
  }

  /**
   * Set the stroke color of the object.
   *
   * @param r   red value
   * @param g   green value
   * @param b   blue value
   * @see #setStroke(float)
   * @see #setStroke(float,float)
   * @see #setStroke(float,float,float)
   * @see #setStroke(float,float,float,float)
   */
  public void setStroke(float r, float g, float b){
    setStrokeColor(Fisica.parent().color(r, g, b));
  }

  /**
   * Set the stroke color of the object.
   *
   * @param r   red value
   * @param g   green value
   * @param b   blue value
   * @param a   alpha (opacity) value
   * @see #setStroke(float)
   * @see #setStroke(float,float)
   * @see #setStroke(float,float,float)
   * @see #setStroke(float,float,float,float)
   */
  public void setStroke(float r, float g, float b, float a){
    setStrokeColor(Fisica.parent().color(r, g, b, a));
  }

  /**
   * Set the stroke weight of the object.
   *
   * @param weight   weight value in pixels
   * @see #setStroke(float)
   * @see #setStroke(float,float)
   * @see #setStroke(float,float,float)
   * @see #setStroke(float,float,float,float)
   */
  public void setStrokeWeight(float weight) {
    m_strokeWeight = weight;
  }
}
