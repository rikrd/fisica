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
 * Represents a rectangular body that can be added to a world.
 *
 * <pre>
 * {@code
 * FBox myBox = new FBox(40, 20);
 * world.add(myBox);
 * }
 * </pre>
 *
 * @usage Bodies
 * @see FCircle
 * @see FBlob
 * @see FPoly
 * @see FLine
 */
public class FBox extends FBody {
  protected float m_height;
  protected float m_width;

  protected ShapeDef getShapeDef() {
    PolygonDef pd = new PolygonDef();
    pd.setAsBox(m_width/2.0f, m_height/2.0f);
    pd.density = m_density;
    pd.friction = m_friction;
    pd.restitution = m_restitution;
    pd.isSensor = m_sensor;
    return pd;
  }

  protected ShapeDef getTransformedShapeDef() {
    PolygonDef pd = (PolygonDef)getShapeDef();

    XForm xf = new XForm();
    xf.R.set(-m_angle);
    xf.position = Mat22.mul(xf.R, m_position.negate());
    
    for (int i=0; i<pd.vertices.size(); i++) {
        Vec2 ver = (Vec2)pd.vertices.get(i);
        XForm.mulTransToOut(xf, ver, ver);
    }

    return pd;
  }

  /**
   * Constructs a rectangular body that can be added to a world.
   *
   * @param width  the width of the rectangle
   * @param height  the height of the rectangle
   */
  public FBox(float width, float height){
    super();

    m_height = Fisica.screenToWorld(height);
    m_width = Fisica.screenToWorld(width);
  }

  /**
   * Returns the height of the rectangle.
   *
   * @usage Bodies
   * @see #getWidth()
   * @return the height of the rectangle
   */
  public float getHeight(){
    // only for FBox
    return Fisica.worldToScreen(m_height);
  }

  /**
   * Returns the width of the rectangle.
   *
   * @usage Bodies
   * @see #getHeight()
   * @return the width of the rectangle
   */
  public float getWidth(){
    // only for FBox
    return Fisica.worldToScreen(m_width);
  }

  /**
   * Sets the height of the rectangle.  
   * Under the hood the body is removed and readded to the world.
   *
   * @usage Bodies
   * @see #getWidth()
   * @return the height of the rectangle
   */
  public void setHeight(float height){
    m_height = Fisica.screenToWorld(height);
    
    this.recreateInWorld();
  }
  
  /**
   * Sets the width of the rectangle.  
   * Under the hood the body is removed and readded to the world.
   *
   * @usage Bodies
   * @see #getWidth()
   * @return the width of the rectangle
   */
  public void setWidth(float width){
    m_width = Fisica.screenToWorld(width);

    this.recreateInWorld();
  }

  public void draw(PGraphics applet) {
    preDraw(applet);

    if (m_image != null ) {
      drawImage(applet);
    } else {
      applet.rect(0, 0, getWidth(), getHeight());
    }

    postDraw(applet);
  }
  
  public void drawDebug(PGraphics applet) {
    preDrawDebug(applet);

    applet.rect(0, 0, getWidth(), getHeight());

    postDrawDebug(applet);
  }


}
