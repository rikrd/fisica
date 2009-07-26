package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

public class FPrismaticJoint extends FJoint {    
  public FBody m_body1;
  public FBody m_body2;

  public Vec2 m_anchor;
  public Vec2 m_axis;

  public Vec2 m_localAxis1 = new Vec2();

  public float m_force;  
  public float m_torque;
  
  public float m_motorForce;
  public float m_limitForce;
  public float m_limitPositionImpulse;
  
  public float m_maxMotorForce;
    
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
   *  The lower translation for the joint limit (world coords).
   */
  public float m_lowerTranslation = 0.0f;
  
  /**
   *  The upper translation for the joint limit (world coords).
   */
  public float m_upperTranslation = 0.0f;
  
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
  
  public FPrismaticJoint(FBody body1, FBody body2,
                        float x, float y,
                        float axisX, float axisY) {
    super();
    
    m_body1 = body1;
    m_body2 = body2;

    m_anchor = Fisica.screenToWorld(x, y);
    updateLocalAnchors();

    m_axis = Fisica.screenToWorld(axisX, axisY);
    updateLocalAxis();
    
    m_referenceAngle = m_body2.getRotation() - m_body1.getRotation();
  }

  protected void updateLocalAnchors(){
    if (m_body1.m_body != null) {
      m_body1.m_body.getLocalPointToOut(m_anchor, m_localAnchor1);
      m_body2.m_body.getLocalPointToOut(m_anchor, m_localAnchor2);
    }
  }

  protected void updateLocalAxis(){
    // TODO: find out how to calculate this on our own
    if (m_body1.m_body != null) {
      m_body1.m_body.getLocalVectorToOut(m_axis, m_localAxis1);
    }
  }

  protected JointDef getJointDef(FWorld world) {
    PrismaticJointDef md = new PrismaticJointDef();
    md.body1 = m_body1.m_body;
    md.body2 = m_body2.m_body;
    md.localAnchor1 = m_localAnchor1;
    md.localAnchor2 = m_localAnchor2;
    md.referenceAngle = m_referenceAngle;
    md.lowerTranslation = m_lowerTranslation;
    md.upperTranslation = m_upperTranslation;
    md.enableMotor = m_enableMotor;
    md.enableLimit = m_enableLimit;
    md.motorSpeed = m_motorSpeed;
    md.maxMotorForce = m_maxMotorForce;
    m_body1.m_body.wakeUp();
    m_body2.m_body.wakeUp();

    return md;
  }

  public void setAxis(float x, float y) {
    // TODO: cannot change axis once it has been created    
    m_axis = Fisica.screenToWorld(x, y);
  }

  public void setAnchor(float x, float y) {
    if (m_joint != null) {
      ((PrismaticJoint)m_joint).getAnchor2().set(Fisica.screenToWorld(x), Fisica.screenToWorld(y));
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

  public void setLowerTranslation(float translation) {
    if (m_joint != null) {
      ((PrismaticJoint)m_joint).m_lowerTranslation = Fisica.screenToWorld(translation);
    }

    m_lowerTranslation = Fisica.screenToWorld(translation);
  }

  public void setUpperTranslation(float translation) {
    if (m_joint != null) {
      ((PrismaticJoint)m_joint).m_upperTranslation = Fisica.screenToWorld(translation);
    }

    m_upperTranslation = Fisica.screenToWorld(translation);
  }

  public void setEnableLimit(boolean value) {
    if (m_joint != null) {
      ((PrismaticJoint)m_joint).m_enableLimit = value;
    }

    m_enableLimit = value;
  }

  public void draw(PApplet applet){
    preDraw(applet);
 
    applet.line(getAnchorX(), getAnchorY(), getBody1().getX(), getBody1().getY());
    applet.line(getAnchorX(), getAnchorY(), getBody2().getX(), getBody2().getY());
    applet.rect(getAnchorX(), getAnchorY(), 10, 10);

    postDraw(applet);
  }
}