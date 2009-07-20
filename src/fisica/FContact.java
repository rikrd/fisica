package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

public class FContact {
  public ContactPoint m_contactPoint;
  
  public FContact(ContactPoint contactPoint) {
    m_contactPoint = contactPoint;
  }

  public FBody getBodyA() {
    return (FBody)m_contactPoint.shape1.getBody().getUserData();
  }

  public FBody getBodyB() {
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
