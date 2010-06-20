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
 * Represents the result of the contact between two bodies.  Objects of this type are not created by the users.  Contact results are passed to the user when they implement the {@code contactResult(FContactResult){ }} method in the applet:
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
 * }
 *
 * void contactResult(FContactResult result) {
 *   // Draw an ellipse where the contact took place and as big as the normal impulse of the contact
 *   ellipse(result.getX(), result.getY(), result.getNormalImpulse(), result.getNormalImpulse());
 *
 *   // Trigger your sound here
 *   // ...
 * }
 *}
 * </pre>
 *
 * To know if the contact is the beggining, the continuation or the end of a contact it is better to use the other methods {@code contactStarted(FContact){ }}, {@code contactPersisted(FContact){ }} and {@code contactEnded(FContact){ }}.
 *
 * @usage Contacts
 * @see FContact
 */
public class FContactResult {
  protected Vec2 m_position;
  protected Vec2 m_normal;

  protected FBody m_body1;
  protected FBody m_body2;

  protected float m_normalImpulse;
  protected float m_tangentImpulse;

  protected FContactID m_id;

  protected FContactResult(ContactResult contactResult) {
    m_position = new Vec2(contactResult.position);
    m_normal = new Vec2(contactResult.normal);

    m_body1 = (FBody)contactResult.shape1.getBody().getUserData();
    m_body2 = (FBody)contactResult.shape2.getBody().getUserData();

    m_normalImpulse = contactResult.normalImpulse;
    m_tangentImpulse = contactResult.tangentImpulse;

    m_id = new FContactID(new ContactID(contactResult.id), m_body1, m_body2);
  }

  /**
   * Returns the first body involved in the contact.
   * @return first of the bodies involved in the contact
   */
  public FBody getBody1() {
    return m_body1;
  }

  /**
   * Returns the second body involved in the contact.
   * @return second of the bodies involved in the contact
   */
  public FBody getBody2() {
    return m_body2;
  }

  /**
   * Returns the horizontal position of the contact point.
   *
   * @return the horizontal position of the contact point in pixels
   * @see #getY
   */
  public float getX() {
    return Fisica.worldToScreen(m_position).x;
  }

  /**
   * Returns the vertical position of the contact point.
   *
   * @return the vertical position of the contact point in pixels
   * @see #getX
   */
  public float getY() {
    return Fisica.worldToScreen(m_position).y;
  }

  /**
   * Returns the horizontal component of the contact normal.
   *
   * @return the horizontal component of the contact normal
   * @see #getNormalY
   */
  public float getNormalX() {
    return Fisica.worldToScreen(m_normal).x;
  }

  /**
   * Returns the vertical component of the contact normal.
   *
   * @return the vertical component of the contact normal
   * @see #getNormalX
   */
  public float getNormalY() {
    return Fisica.worldToScreen(m_normal).y;
  }

  /**
   * Returns the normal component of the impulse of the contact.  This gives an idea of the strength of the collision that took place.
   *
   * @return the normal component of the contact's impulse
   * @see #getTangentImpulse
   */
  public float getNormalImpulse() {
    return Fisica.worldToScreen(m_normalImpulse);
  }

  /**
   * Returns the tangential component of the impulse of the contact.  This gives an idea of the strength of the friction between the bodies that took place.
   *
   * @return the tangent component of the contact's impulse
   * @see #getNormalImpulse
   */
  public float getTangentImpulse() {
    return Fisica.worldToScreen(m_tangentImpulse);
  }

  /**
   * Get the identifier of the contact.  This value is useful in order to uniquely identify a contact.  A new contact ID is created whenever to bodies enter into contact at a given point.  If the bodies slide against each other the contact ID is maintained even if the point of contact is modified due to the slide.  As soon as the two bodies separate the contact is considered ended.
   *
   * @return a unique identifier representing the contact
   */
  public FContactID getId(){
    return m_id;
  }
}
