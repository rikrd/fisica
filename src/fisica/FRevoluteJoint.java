package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

public class FRevoluteJoint extends FJoint {    
  public FBody m_body1;
  public FBody m_body2;

  public Vec2 m_anchor;

  /**
   *  The local anchor point relative to body1's origin.
   */
  public Vec2 m_localAnchor1 = new Vec2(0.0f, 0.0f);
  
  /**
   *  The local anchor point relative to body2's origin.
   */
  public Vec2 m_localAnchor2 = new Vec2(0.0f, 0.0f);
  
  /**
   *  The body2 angle minus body1 angle in the reference state (radians).
   */
  public float m_referenceAngle = 0.0f;
  
  /**
   *  A flag to enable joint limits.
   */
  public boolean m_enableLimit = false;
  
  /**
   *  The lower angle for the joint limit (radians).
   */
  public float m_lowerAngle = 0.0f;
  
  /**
   *  The upper angle for the joint limit (radians).
   */
  public float m_upperAngle = 0.0f;
  
  /**
   *  A flag to enable the joint motor.
   */
  public boolean m_enableMotor = false;
  
  /**
   *  The desired motor speed. Usually in radians per second.
   */
  public float m_motorSpeed = 0.0f;
  
  /**
   *  The maximum motor torque used to achieve the desired motor speed.
   *  Usually in N-m.
   */
  public float m_maxMotorTorque = 0.0f;
  
  public FRevoluteJoint(FBody body1, FBody body2, float x, float y) {
    super();
    
    m_body1 = body1;
    m_body2 = body2;

    m_anchor = Fisica.screenToWorld(x, y);
    updateLocalAnchors();

    m_referenceAngle = m_body2.getRotation() - m_body1.getRotation();
  }

  protected void updateLocalAnchors(){
    m_localAnchor1 = Fisica.screenToWorld(getAnchorX() - m_body1.getX(), getAnchorY() - m_body1.getY());
    m_localAnchor2 = Fisica.screenToWorld(getAnchorX() - m_body2.getX(), getAnchorY() - m_body2.getY());
  }

  protected JointDef getJointDef(FWorld world) {
    RevoluteJointDef md = new RevoluteJointDef();
    md.body1 = m_body1.m_body;
    md.body2 = m_body2.m_body;
    md.localAnchor1 = m_localAnchor1.clone();
    md.localAnchor2 = m_localAnchor2.clone();
    md.referenceAngle = m_referenceAngle;
    md.lowerAngle = m_lowerAngle;
    md.upperAngle = m_upperAngle;
    md.enableMotor = m_enableMotor;
    md.enableLimit = m_enableLimit;
    md.motorSpeed = m_motorSpeed;
    md.maxMotorTorque = m_maxMotorTorque;
    m_body1.m_body.wakeUp();
    m_body2.m_body.wakeUp();

    return md;
  }

  public void setLowerAngle(float a) {
    if (m_joint != null) {
      ((RevoluteJoint)m_joint).m_lowerAngle = a;
    }
    
    m_lowerAngle = a;
  }

  public void setUpperAngle(float a) {
    if (m_joint != null) {
      ((RevoluteJoint)m_joint).m_upperAngle = a;
    }
    
    m_upperAngle = a;
  }

  public void setEnableLimit(boolean v) {
    if (m_joint != null) {
      ((RevoluteJoint)m_joint).m_enableLimit = v;
    }
    
    m_enableLimit = v;
  }

  public void setAnchor(float x, float y) {
    if (m_joint != null) {
      ((RevoluteJoint)m_joint).getAnchor2().set(Fisica.screenToWorld(x), Fisica.screenToWorld(y));
    }
    
    m_anchor = Fisica.screenToWorld(x, y);
  }
  
  public float getAnchorX() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor2()).x;
    }

    return Fisica.worldToScreen(m_anchor.x);
  }

  public float getAnchorY() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor2()).y;
    }

    return Fisica.worldToScreen(m_anchor.y);
  }

  public void draw(PApplet applet){
    preDraw(applet);
 
    applet.line(getAnchorX(), getAnchorY(), getBody1().getX(), getBody1().getY());
    applet.line(getAnchorX(), getAnchorY(), getBody2().getX(), getBody2().getY());
    applet.ellipse(getAnchorX(), getAnchorY(), 10, 10);

    postDraw(applet);
  }
}