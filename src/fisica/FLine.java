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

import java.util.ArrayList;

/**
 * Represents a line body that can be added to a world.
 *
 * <pre>
 * {@code
 * FLine myLine = new FLine(40, 20, 100, 40);
 * world.add(myLine);
 * }
 * </pre>
 *
 * @usage Bodies
 * @see FBox
 * @see FCircle
 * @see FBlob
 * @see FPoly
 */
public class FLine extends FBody {
  protected Vec2 m_start;
  protected Vec2 m_end;

  protected ShapeDef getShapeDef() {
    EdgeChainDef pd = new EdgeChainDef();

    pd.addVertex(m_start);
    pd.addVertex(m_end);

    pd.setIsLoop(false);

    pd.density = m_density;
    pd.friction = m_friction;
    pd.restitution = m_restitution;
    pd.isSensor = m_sensor;
    return pd;
  }

  /**
   * Constructs a line body that can be added to a world.
   *
   * @param x1  horizontal position of the first point of the line
   * @param y1  vertical position of the first point of the line
   * @param x2  horizontal position of the second point of the line
   * @param y2  vertical position of the second point of the line
   */
  public FLine(float x1, float y1, float x2, float y2){
    super();

    m_start = Fisica.screenToWorld(x1, y1);
    m_end = Fisica.screenToWorld(x2, y2);
  }

  public void draw(PGraphics applet) {
    preDraw(applet);

    if (m_image != null ) {
      drawImage(applet);
    } else {
      Vec2 tempStart = Fisica.worldToScreen(m_start);
      Vec2 tempEnd = Fisica.worldToScreen(m_end);
      applet.line(tempStart.x, tempStart.y, tempEnd.x, tempEnd.y);
    }

    postDraw(applet);
  }

}
