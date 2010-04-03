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
import processing.core.*;

import org.jbox2d.common.*;

/**
 * Represents the library Fisica.  It is mainly used to initialize it with the PApplet:
 * <pre>
 * {@code
 * Fisica.init(this);
 * }
 * </pre>
 * It also allows to modify global properties of the simulation such as the scale of meters to pixels:
 * <pre>
 * {@code
 * Fisica.setScale(40);
 * }
 * </pre>
 * In the future it may contain helping methods to create the objects.
 *
 */
public class Fisica implements PConstants{
  protected static class FViewport {
    float m_scale;

    public FViewport(){
      m_scale = 1.0f;
    }

    public void setScaleTransform(float a) {
      m_scale = a;
    }

    public float getScreenToWorld(float a) {
      return a/m_scale;
    }

    public Vec2 getScreenToWorld(float x, float y) {
      return new Vec2(x/m_scale, y/m_scale);
    }

    public Vec2 getScreenToWorld(Vec2 p) {
      return new Vec2(p.x/m_scale, p.y/m_scale);
    }

    public float getWorldToScreen(float a) {
      return a*m_scale;
    }

    public Vec2 getWorldToScreen(float x, float y) {
      return new Vec2(x*m_scale, y*m_scale);
    }

    public Vec2 getWorldToScreen(Vec2 p) {
      return new Vec2(p.x*m_scale, p.y*m_scale);
    }
  }

  private static boolean m_initialized = false;
  private static PApplet m_parent;
  private static FViewport m_viewport;

  public static Vec2 screenToWorld( Vec2 m_in ) {
    return m_viewport.getScreenToWorld(m_in);
  }

  public static Vec2 screenToWorld( float x, float y ) {
    return m_viewport.getScreenToWorld(x, y);
  }

  public static float screenToWorld( float a ) {
    return m_viewport.getScreenToWorld(a);
  }

  public static Vec2 worldToScreen( Vec2 m_in ) {
    return m_viewport.getWorldToScreen(m_in);
  }

  public static Vec2 worldToScreen( float x, float y ) {
    return m_viewport.getWorldToScreen(x, y);
  }

  public static float worldToScreen( float a ) {
    return m_viewport.getWorldToScreen(a);
  }

  /**
   * Exception thrown when the library has not been initialized.  The method {@link Fisica#init(PApplet)} must be called before any use of the library.
   *
   * @param applet  The applet on which to use the library.  This library can only be used with one applet
   */
  public static class LibraryNotInitializedException extends NullPointerException{
    private static final long serialVersionUID = -3710605630786298674L;

    LibraryNotInitializedException(){
      super("Must call Fisica.init(this); before using this library.");
    }
  }

  protected static boolean initialized() {
    return m_initialized;
  }

  protected static PApplet parent(){
    if(m_parent == null){
      throw new LibraryNotInitializedException();
    }

    return m_parent;
  }

  /**
   * Initialize the library.  Must be called before any use of the library.  Must be called by passing the PApplet.  e.g. {@code Fisica.init(this)}
   *
   * @param applet  The applet on which to use the library.  This library can only be used with one applet
   */
  public static void init(PApplet applet){
    m_parent = applet;
    m_initialized = true;

    m_viewport = new FViewport();
    m_viewport.setScaleTransform(20);
  }

  /**
   * Set the scale from screen units to world units.  By setting the scale to 20 we are stating that 20 pixels is equivalent to 1 meter in the simulated world.
   *
   * @param scale the number of pixels that are equivalent to 1 meter in the simulated world.
   *
   */
  public static void setScale(float scale){
    m_viewport.m_scale = scale;
  }

}
