package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

/**
 * Represents a prismatic joint that restricts the movement of one body with respect to another to a translation along a given axis.  Often this joint is used with one of the bodies being static in order to only allow translation of the other body along a given axis.  This translation can also be bounded given lower and upper translation limits.
 *
 */
public class FPrismaticJoint extends FJoint {
  protected FBody m_body1;
  protected FBody m_body2;

  protected Vec2 m_anchor;
  protected Vec2 m_axis;

  protected Vec2 m_localAxis1 = new Vec2();

  protected float m_force;
  protected float m_torque;

  protected float m_motorForce;
  protected float m_limitForce;
  protected float m_limitPositionImpulse;

  protected float m_maxMotorForce;

  /**
   *  The local anchor point relative to body1's origin.
   */
  protected Vec2 m_localAnchor1 = new Vec2(0.0f, 0.0f);

  /**
   *  The local anchor point relative to body2's origin.
   */
  protected Vec2 m_localAnchor2 = new Vec2(0.0f, 0.0f);

  /**
   *  The body2 angle minus body1 angle in the reference state (radians).
   */
  protected float m_referenceAngle = 0.0f;

  /**
   *  A flag to enable joint limits.
   */
  protected boolean m_enableLimit = false;

  /**
   *  The lower translation for the joint limit (world coords).
   */
  protected float m_lowerTranslation = 0.0f;

  /**
   *  The upper translation for the joint limit (world coords).
   */
  protected float m_upperTranslation = 0.0f;

  /**
   *  A flag to enable the joint motor.
   */
  protected boolean m_enableMotor = false;

  /**
   *  The desired motor speed. Usually in radians per second.
   */
  protected float m_motorSpeed = 0.0f;

  /**
   *  The maximum motor torque used to achieve the desired motor speed.
   *  Usually in N-m.
   */
  protected float m_maxMotorTorque = 0.0f;

  protected void updateLocalAnchors(){
    if (m_body1.m_body != null) {
      m_body1.m_body.getLocalPointToOut(m_anchor, m_localAnchor1);
    }

    if (m_body2.m_body != null) {
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

  /**
   * Construct a prismatic joint between two bodies.  The constructor automatically sets the anchor of the joint to the center of each body, and the length of the joint to the current distance between the bodies.
   *
   * @param body1  first body of the joint
   * @param body2  second body of the joint
   */
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

  /**
   * Sets the axis of movement of the joint.  This is only axis alog which the bodies can translate with relation to each other.  The axis is given global coordinates, relative to the center of the canvas.  This property must be set before adding the joint to the world.
   *
   * @param x  the horizontal component of the axis in global coordinates, relative to the center of the canvas
   * @param y  the vertical coordinate of the axis in global coordinates, relative to the center of the canvas
   */
  public void setAxis(float x, float y) {
    // TODO: cannot change axis once it has been created
    m_axis = Fisica.screenToWorld(x, y);
    updateLocalAxis();
  }

  /**
   * Sets the position of the anchor of the joint.  This position is given global coordinates, relative to the center of the canvas.
   *
   * @param x  the horizontal coordinate of the anchor in global coordinates, relative to the center of the canvas
   * @param y  the vertical coordinate of the anchor in global coordinates, relative to the center of the canvas
   */
  public void setAnchor(float x, float y) {
    m_anchor = Fisica.screenToWorld(x, y);
    updateLocalAnchors();
  }

  /**
   * Get the horizontal coordinate of the anchor of the joint.  This position is given global coordinates, relative to the center of the canvas.
   *
   * @return  the horizontal coordinate of the anchor in global coordinates, relative to the center of the canvas
   */
  public float getAnchorX() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor2()).x;
    }

    return Fisica.worldToScreen(m_anchor.x);
  }

  /**
   * Get the vertical coordinate of the anchor of the joint.  This position is given global coordinates, relative to the center of the canvas.
   *
   * @return  the vertical coordinate of the anchor in global coordinates, relative to the center of the canvas
   */
  public float getAnchorY() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor2()).y;
    }

    return Fisica.worldToScreen(m_anchor.y);
  }

  /**
   * Set the lowest translation allowed.  This property only has effect if the {@code enableLimit} has been set to {@code true} using {@link #setEnableLimit(boolean)}.
   *
   * @param translation  lowest translation position in pixels
   */
  public void setLowerTranslation(float translation) {
    if (m_joint != null) {
      ((PrismaticJoint)m_joint).m_lowerTranslation = Fisica.screenToWorld(translation);
    }

    m_lowerTranslation = Fisica.screenToWorld(translation);
  }

  /**
   * Set the highest translation allowed.  This property only has effect if the {@code enableLimit} has been set to {@code true} using {@link #setEnableLimit(boolean)}.
   *
   * @param translation  highest translation position in pixels
   */
  public void setUpperTranslation(float translation) {
    if (m_joint != null) {
      ((PrismaticJoint)m_joint).m_upperTranslation = Fisica.screenToWorld(translation);
    }

    m_upperTranslation = Fisica.screenToWorld(translation);
  }

  /**
   * Set limits to the allowed translation of one body respect to the other.  If set to {@code true} the limits imposed using {@link #setLowerTranslation(float) setLowerTranslation} and {@link #setUpperTranslation(float) setLowerTranslation} are enforced.
   *
   * @param value  if {@code true} the bodies will be able to translate along the axis only between certain limits
   */
  public void setEnableLimit(boolean value) {
    if (m_joint != null) {
      ((PrismaticJoint)m_joint).m_enableLimit = value;
    }

    m_enableLimit = value;
  }

  public void draw(PGraphics applet){
    preDraw(applet);

    applet.line(getAnchorX(), getAnchorY(), getBody1().getX(), getBody1().getY());
    applet.line(getAnchorX(), getAnchorY(), getBody2().getX(), getBody2().getY());
    applet.rect(getAnchorX(), getAnchorY(), 10, 10);

    postDraw(applet);
  }
}