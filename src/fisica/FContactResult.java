package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

/** @invisible */
public class FContactResult {
  public Vec2 m_position;
  public Vec2 m_normal;

  public FBody m_body1;
  public FBody m_body2;

  public float m_normalImpulse;
  public float m_tangentImpulse;

  public FContactID m_id;

  public FContactResult(ContactResult contactResult) {
    m_position = new Vec2(contactResult.position);
    m_normal = new Vec2(contactResult.normal);
    
    m_body1 = (FBody)contactResult.shape1.getBody().getUserData();
    m_body2 = (FBody)contactResult.shape2.getBody().getUserData();

    m_normalImpulse = contactResult.normalImpulse;
    m_tangentImpulse = contactResult.tangentImpulse;

    m_id = new FContactID(new ContactID(contactResult.id));
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

  public float getNormalX() {
    return Fisica.worldToScreen(m_normal).x;
  }

  public float getNormalY() {
    return Fisica.worldToScreen(m_normal).y;
  }

  public float getNormalImpulse() {
    return Fisica.worldToScreen(m_normalImpulse);
  }
  
  public float getTangentImpulse() {
    return Fisica.worldToScreen(m_tangentImpulse);
  }
  
  public FContactID getId(){
    return m_id;
  }
}
