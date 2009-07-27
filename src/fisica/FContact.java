package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

public class FContact {
  public ContactPoint m_contactPoint;
  public final int m_type;
  
  public static final int START = 0;
  public static final int PERSIST = 1;
  public static final int END = 2;
  
  public FContact(ContactPoint contactPoint, int type) {
    m_type = type;
    
    m_contactPoint = new ContactPoint();
    m_contactPoint.shape1 = contactPoint.shape1;
    m_contactPoint.shape2 = contactPoint.shape2;
    m_contactPoint.position.set(contactPoint.position);
    m_contactPoint.velocity.set(contactPoint.velocity);
    m_contactPoint.normal.set(contactPoint.normal);
    m_contactPoint.separation = contactPoint.separation;
    m_contactPoint.friction = contactPoint.friction;
    m_contactPoint.restitution = contactPoint.restitution;
    m_contactPoint.id = new ContactID(contactPoint.id);
  }

  public FBody getBody1() {
    return (FBody)m_contactPoint.shape1.getBody().getUserData();
  }

  public FBody getBody2() {
    return (FBody)m_contactPoint.shape2.getBody().getUserData();
  }

  public float getX() {
    return Fisica.worldToScreen(m_contactPoint.position).x;
  }

  public float getY() {
    return Fisica.worldToScreen(m_contactPoint.position).y;
  }

  public float getVelocityX() {
    return Fisica.worldToScreen(m_contactPoint.velocity).x;
  }

  public float getVelocityY() {
    return Fisica.worldToScreen(m_contactPoint.velocity).y;
  }

  public float getNormalX() {
    return Fisica.worldToScreen(m_contactPoint.normal).x;
  }

  public float getNormalY() {
    return Fisica.worldToScreen(m_contactPoint.normal).y;
  }

  public float getSeparation() {
    return Fisica.worldToScreen(m_contactPoint.separation);
  }
  
  public float getFriction() {
    return m_contactPoint.friction;
  }

  public float getRestitution() {
    return m_contactPoint.restitution;
  }

  public ContactID getId(){
    return m_contactPoint.id;
  }
}
