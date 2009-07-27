package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

public class FContactResult {
  public ContactResult m_contactResult;
  
  public FContactResult(ContactResult contactResult) {
    m_contactResult = contactResult;
    /*
    m_contactResult = new ContactResult();
    m_contactResult.shape1 = contactResult.shape1;
    m_contactResult.shape2 = contactResult.shape2;
    m_contactResult.position.set(contactResult.position);
    m_contactResult.normal.set(contactResult.normal);
    m_contactResult.normalImpulse = contactResult.normalImpulse;
    m_contactResult.tangentImpulse = contactResult.tangentImpulse;
    m_contactResult.id = new ContactID(contactResult.id);
    */  
  }

  public FBody getBody1() {
    return (FBody)m_contactResult.shape1.getBody().getUserData();
  }

  public FBody getBody2() {
    return (FBody)m_contactResult.shape2.getBody().getUserData();
  }

  public float getX() {
    return Fisica.worldToScreen(m_contactResult.position).x;
  }

  public float getY() {
    return Fisica.worldToScreen(m_contactResult.position).y;
  }

  public float getNormalX() {
    return Fisica.worldToScreen(m_contactResult.normal).x;
  }

  public float getNormalY() {
    return Fisica.worldToScreen(m_contactResult.normal).y;
  }

  public float getNormalImpulse() {
    return Fisica.worldToScreen(m_contactResult.normalImpulse);
  }
  
  public float getTangentImpulse() {
    return Fisica.worldToScreen(m_contactResult.tangentImpulse);
  }
  
  public ContactID getId(){
    return m_contactResult.id;
  }
}
