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
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

/**
 * Represents the result of a casted ray.
 *
 *
 * <pre>
 * {@code
 * FWorld world;
 *
 * void setup() {
 *   Fisica.init(this);
 *
 *   world = new FWorld();
 *   world.setEdges();
 *
 *   // Create and add bodies to the world here
 *   // ...
 * }
 *
 * void draw() {
 *   world.step();
 *   world.draw();
 *
 *   FRaycastResult result = null;
 *   FBody b = world.raycastOne(width/2, height/2, mouseX, mouseY, result, true);
 * }
 *
 *}
 * </pre>
 *
 *
 * @usage Contacts
 * @see FContact
 */
public class FRaycastResult {
  protected float m_lambda = 0.0f;
  protected Vec2 m_normal = new Vec2();
  protected float m_x1, m_x2, m_y1, m_y2;

  protected FRaycastResult set(float x1, float y1, float x2, float y2, RaycastResult raycastResult) {
    if (raycastResult != null) {
      m_lambda = raycastResult.lambda;
      m_normal.set(raycastResult.normal);
    }
    
    m_x1 = x1;
    m_x2 = x2;
    m_y1 = y1;
    m_y2 = y2;
    
    return this;
  }

  /**
   * Returns the lambda of the raycast result.
   *
   * @return the lambda of the raycast result
   */
  public float getLambda() {
    return m_lambda;
  }

  /**
   * Returns the horizontal component of the ray cast contact normal.
   *
   * @return the horizontal component of the ray cast contact normal
   * @see #getNormalY
   */
  public float getNormalX() {
    return Fisica.worldToScreen(m_normal).x;
  }

  /**
   * Returns the vertical component of the ray cast contact normal.
   *
   * @return the vertical component of the ray cast contact normal
   * @see #getNormalX
   */
  public float getNormalY() {
    return Fisica.worldToScreen(m_normal).y;
  }

  /**
   * Returns the horizontal component of the ray cast contact normal.
   *
   * @return the horizontal component of the ray cast contact normal
   * @see #getY
   * @see #getNormalX
   * @see #getNormalY
   */
  public float getX() {
    return Fisica.parent().lerp(m_x1, m_x2, m_lambda);
  }

  /**
   * Returns the vertical component of the contact ray cast normal.
   *
   * @return the vertical component of the contact ray cast normal
   * @see #getX
   * @see #getNormalX
   * @see #getNormalY
   */
  public float getY() {
    return Fisica.parent().lerp(m_y1, m_y2, m_lambda);
  }
}
