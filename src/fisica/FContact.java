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
 * Represents a contact between two bodies.  Objects of this type are not created by the users.  Contacts are passed to the user when they implement the {@code contactStarted(FContact){ }}, {@code contactPersisted(FContact){ }} and {@code contactEnded(FContact){ }} methods in the applet:
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
 * void contactStarted(FContact contact) {
 *   // Draw in green an ellipse where the contact took place
 *   fill(0, 170, 0);
 *   ellipse(contact.getX(), contact.getY(), 20, 20);
 * }
 *
 * void contactPersisted(FContact contact) {
 *   // Draw in blue an ellipse where the contact took place
 *   fill(0, 0, 170);
 *   ellipse(contact.getX(), contact.getY(), 10, 10);
 * }
 *
 * void contactStarted(FContact contact) {
 *   // Draw in red an ellipse where the contact took place
 *   fill(170, 0, 0);
 *   ellipse(contact.getX(), result.getY(), 20, 20);
 * }
 *
 *}
 * </pre>
 *
 * To know if the contact is the beggining, the continuation or the end of a contact it is better to use the other method {@code contactResult(FContactResult){ }}.
 *
 * @usage Contacts
 * @see FContactResult
 *
 */
public class FContact {
  protected FBody m_body1;
  protected FBody m_body2;
  protected Vec2 m_position;
  protected Vec2 m_velocity;
  protected Vec2 m_normal;
  protected float m_separation;
  protected float m_friction;
  protected float m_restitution;

  protected FContactID m_id;

  protected FContact(ContactPoint contactPoint) {
    m_position = new Vec2(contactPoint.position);
    m_velocity = new Vec2(contactPoint.velocity);
    m_normal = new Vec2(contactPoint.normal);

    m_separation = contactPoint.separation;
    m_friction = contactPoint.friction;
    m_restitution = contactPoint.restitution;

    m_body1 = (FBody)contactPoint.shape1.getBody().getUserData();
    m_body2 = (FBody)contactPoint.shape2.getBody().getUserData();

    m_id = new FContactID(new ContactID(contactPoint.id), m_body1, m_body2);
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
   * Returns the horizontal component of the contact velocity.
   *
   * @return the horizontal component of the contact velocity
   * @see #getVelocityY
   */
  public float getVelocityX() {
    return Fisica.worldToScreen(m_velocity).x;
  }

  /**
   * Returns the vertical component of the contact velocity.
   *
   * @return the vertical component of the contact velocity
   * @see #getVelocityX
   */
  public float getVelocityY() {
    return Fisica.worldToScreen(m_velocity).y;
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
   * Get the separation between the bodies.
   *
   * @return a positive value means that the bodies have space between them, negative values means that the bodies have penetrated each other
   */
  public float getSeparation() {
    return Fisica.worldToScreen(m_separation);
  }

  /**
   * Get the friction coefficient of the contact.  The friction determines the ratio of the reaction force tangent to a contact that the two bodies will recieve.  Basically it can be seen as a coefficient that will control how the bodies get slown down when they slide against each other.  This value depends on the friction coefficients of the two bodies involved in the contact.
   *
   * @return a positive value.  A value of 0 means no friction and thus the body will not be slown down if no other forces are applied
   */
  public float getFriction() {
    return m_friction;
  }

  /**
   * Get the restitution coefficient of the contact.  The restitution determines the ratio of the reaction force normal to a contact that the two bodies will recieve.  Basically it can be seen as a coefficient that will control the strength with which the bodies bounce back from the collision.  This value depends on the resititution coefficients of the two bodies involved in the contact.
   *
   * @return a positive value.  A value of 0 means no bounce after a collision, and a value of 1 means bounce with it's full speed from a collision
   */
  public float getRestitution() {
    return m_restitution;
  }

  /**
   * Get the identifier of the contact.  This value is useful in order to uniquely identify a contact.  A new contact ID is created whenever to bodies enter into contact at a given point.  If the bodies slide against each other the contact ID is maintained even if the point of contact is modified due to the slide.  As soon as the two bodies separate the contact is considered ended.
   *
   * @return a unique identifier representing the contact
   */
  public FContactID getId() {
    return m_id;
  }
  
  /**
   * Returns true if the contact contains the two bodies.  If one of the bodies does not have a name this function returns false.
   *
   * @param n1 the name of one of the bodies
   * @param n2 the name of another one of the bodies
   * @return true if the contact bodies have the names given by the parameters
   * @see FBody#setName
   */
  public boolean contains(String n1, String n2) {
    if (this.getBody1() == null || this.getBody2() == null) {
        return false;
    }
    
    if (this.getBody1().getName() == null || this.getBody2().getName() == null) {
        return false;
    }
    
    return ((this.getBody1().getName().equals(n1) && this.getBody2().getName().equals(n2)) ||
        (this.getBody1().getName().equals(n2) && this.getBody2().getName().equals(n1)));
  }

  /**
   * Returns true if the contact contains the two bodies.
   *
   * @param n1 one of the bodies
   * @param n2 another one of the bodies
   * @return true if the contact bodies are the ones given by the parameters
   */
  public boolean contains(FBody n1, FBody n2) {
    if (this.getBody1() == null || this.getBody2() == null) {
        return false;
    }
    
    return ((this.getBody1() == n1 && this.getBody2() == n2) ||
        (this.getBody1() == n2 && this.getBody2() == n1));
  }

  
  /**
   * Returns true if the contact contains the body.
   *
   * @return true if one of the contact bodies has the name given by the parameters
   * @see FBody#setName
   */
  public boolean contains(String n1) {
    if ((this.getBody1() != null) && (this.getBody1().getName() != null) && (this.getBody1().getName().equals(n1))) {
        return true;
    }
    
    if ((this.getBody2() != null) && (this.getBody2().getName() != null) && (this.getBody2().getName().equals(n1))) {
        return true;
    }
    
    return false;
  }
  
  /**
   * Returns true if the contact contains the body.
   *
   * @return true if one of the contact bodies has the name given by the parameters
   */
  public boolean contains(FBody n1) {
    if ((this.getBody1() != null) && (this.getBody1() == n1)) {
        return true;
    }
    
    if ((this.getBody2() != null) && (this.getBody2() == n1)) {
        return true;
    }
    
    return false;
  }

}
