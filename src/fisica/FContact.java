package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

public class FContact {
  public FBody m_body1;
  public FBody m_body2;
  public Vec2 m_position;
  public Vec2 m_velocity;
  public Vec2 m_normal;
  public float m_separation;
  public float m_friction;
  public float m_restitution;
  
  public FContactID m_id;
  
  public FContact(ContactPoint contactPoint) {
    m_position = new Vec2(contactPoint.position);
    m_velocity = new Vec2(contactPoint.velocity);
    m_normal = new Vec2(contactPoint.normal);

    m_separation = contactPoint.separation;
    m_friction = contactPoint.friction;
    m_restitution = contactPoint.restitution;
    
    m_body1 = (FBody)contactPoint.shape1.getBody().getUserData();
    m_body2 = (FBody)contactPoint.shape2.getBody().getUserData();
    
    m_id = new FContactID(new ContactID(contactPoint.id));
  }

  public FBody getBody1() {
    return m_body1;
  }

  public FBody getBody2() {
    return m_body2;
  }

  public float getX() {
    return Fisica.worldToScreen(m_position).x;
  }

  public float getY() {
    return Fisica.worldToScreen(m_position).y;
  }

  public float getVelocityX() {
    return Fisica.worldToScreen(m_velocity).x;
  }

  public float getVelocityY() {
    return Fisica.worldToScreen(m_velocity).y;
  }

  public float getNormalX() {
    return Fisica.worldToScreen(m_normal).x;
  }

  public float getNormalY() {
    return Fisica.worldToScreen(m_normal).y;
  }

  public float getSeparation() {
    return Fisica.worldToScreen(m_separation);
  }
  
  public float getFriction() {
    return m_friction;
  }

  public float getRestitution() {
    return m_restitution;
  }

  public FContactID getId() {
    return m_id;
  }
}
