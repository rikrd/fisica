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

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

/**
 * Represents a revolute joint that restricts the movement of one body with respect to another to rotation around a given anchor.  The rotation can be further limited given a lower and un upper angles.  Additionally the user can enable a motor in order to apply a constant rotation force (torque) to the joint in order to reach the desired rotation speed.
 *
 */
public class FRevoluteJoint extends FJoint {
  protected FBody m_body1;
  protected FBody m_body2;

  protected Vec2 m_anchor;

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
   *  The lower angle for the joint limit (radians).
   */
  protected float m_lowerAngle = 0.0f;

  /**
   *  The upper angle for the joint limit (radians).
   */
  protected float m_upperAngle = 0.0f;

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

  protected void updateLocalAnchors() {
    m_localAnchor1 = m_body1.getLocalWorldPoint(Fisica.screenToWorld(getAnchorX(), getAnchorY())); //Fisica.screenToWorld(getAnchorX() - m_body1.getX(), getAnchorY() - m_body1.getY());
    m_localAnchor2 = m_body2.getLocalWorldPoint(Fisica.screenToWorld(getAnchorX(), getAnchorY())); //Fisica.screenToWorld(getAnchorX() - m_body2.getX(), getAnchorY() - m_body2.getY());
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
    
    if (m_body1.m_body != null) {
      m_body1.m_body.wakeUp();
    }
    
    if (m_body2.m_body != null) {
      m_body2.m_body.wakeUp();
    }

    return md;
  }
  
  /**
   * Construct a revolute joint between two bodies given an anchor position.
   *
   * @param body1  first body of the joint
   * @param body2  second body of the joint
   * @param x  horizontal coordinate of the anchor given in global coordinates, relative to the canvas' center
   * @param y  vertical coordinate of the anchor given in global coordinates, relative to the canvas' center
   */
  public FRevoluteJoint(FBody body1, FBody body2, float x, float y) {
    super();

    m_body1 = body1;
    m_body2 = body2;
    
    m_anchor = Fisica.screenToWorld(x, y);
    updateLocalAnchors();

    m_referenceAngle = m_body2.getRotation() - m_body1.getRotation();
  }

  /**
   * Construct a revolute joint between two bodies.
   *
   * @param body1  first body of the joint
   * @param body2  second body of the joint
   */
  public FRevoluteJoint(FBody body1, FBody body2) {
    this(body1, body2, (body1.getX() + body2.getX())/2, (body1.getY() + body2.getY())/2);
  }

  /**
   * Set the lowest angle allowed.  This property only has effect if the {@code enableLimit} has been set to {@code true} using {@link #setEnableLimit(boolean)}.
   *
   * @param a  lowest angle allowed in radians
   */
  public void setLowerAngle(float a) {
    if (m_joint != null) {
      ((RevoluteJoint)m_joint).m_lowerAngle = a;
    }

    m_lowerAngle = a;
  }

  /**
   * Set the highest angle allowed.  This property only has effect if the {@code enableLimit} has been set to {@code true} using {@link #setEnableLimit(boolean)}.
   *
   * @param a  highest angle allowed in radians
   */
  public void setUpperAngle(float a) {
    if (m_joint != null) {
      ((RevoluteJoint)m_joint).m_upperAngle = a;
    }

    m_upperAngle = a;
  }

  /**
   * Set limits to the allowed rotation of one body respect to the other.  If set to {@code true} the limits imposed using {@link #setLowerAngle(float) setLowerAngle} and {@link #setUpperAngle(float) setLowerAngle} are enforced.
   *
   * @param value  if {@code true} the bodies will be able to rotate around the anchor only between certain limits
   */
  public void setEnableLimit(boolean value) {
    if (m_joint != null) {
      ((RevoluteJoint)m_joint).m_enableLimit = value;
    }

    m_enableLimit = value;
  }

  /**
   * Set the desired rotation speed of the joint.  This property only has effect if the {@code enableMotor} has been set to {@code true} using {@link #setEnableMotor(boolean)}.  The speed is given in radians per second.
   *
   * @param a  the desired speed in radians per second
   */
  public void setMotorSpeed(float a) {
    if (m_joint != null) {
      ((RevoluteJoint)m_joint).m_motorSpeed = a;
    }

    m_motorSpeed = a;
  }

  /**
   * Set the maximum torque that the joint's motor can apply in order to acheive the desired speed.  This property only has effect if the {@code enableMotor} has been set to {@code true} using {@link #setEnableMotor(boolean)}.
   *
   * @param a  the maximum torque of the joint's motor
   */
  public void setMaxMotorTorque(float a) {
    if (m_joint != null) {
      ((RevoluteJoint)m_joint).m_maxMotorTorque = a;
    }

    m_maxMotorTorque = a;
  }

  /**
   * Set the state of the motor in order to generate a rotation force (torque) on the joint.  If set to {@code true} the desired motor speed, set using {@link #setMotorSpeed(float) setMotorSpeed}, will try to be matched using a motor with a maximum rotation force (torque) set using {@link #setMaxMotorTorque(float) setMaxMotorTorque}.
   *
   * @param value  if {@code true} the joint will receive the rotation force (torque) of a motor
   */
  public void setEnableMotor(boolean value) {
    if (m_joint != null) {
      ((RevoluteJoint)m_joint).m_enableMotor = value;
    }

    m_enableMotor = value;
  }

  /**
   * Sets the position of the anchor of the joint around which the bodies rotate.  This position is given global coordinates, relative to the center of the canvas.
   *
   * @param x  the horizontal coordinate of the anchor in global coordinates, relative to the center of the canvas
   * @param y  the vertical coordinate of the anchor in global coordinates, relative to the center of the canvas
   */
  public void setAnchor(float x, float y) {
    if (m_joint != null) {
      ((RevoluteJoint)m_joint).getAnchor2().set(Fisica.screenToWorld(x), Fisica.screenToWorld(y));
    }

    m_anchor = Fisica.screenToWorld(x, y);
    updateLocalAnchors();
  }

  /**
   * Get the horizontal coordinate of the anchor of the joint around which the bodies can rotate.  This position is given global coordinates, relative to the center of the canvas.
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
   * Get the vertical coordinate of the anchor of the joint around which the bodies can rotate.  This position is given global coordinates, relative to the center of the canvas.
   *
   * @return  the vertical coordinate of the anchor in global coordinates, relative to the center of the canvas
   */
  public float getAnchorY() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor2()).y;
    }

    return Fisica.worldToScreen(m_anchor.y);
  }

  public void setReferenceAngle(float ang) {
    m_referenceAngle = ang;
  }

  public void draw(PGraphics applet){
    preDraw(applet);

    applet.line(getAnchorX(), getAnchorY(), getBody1().getX(), getBody1().getY());
    applet.line(getAnchorX(), getAnchorY(), getBody2().getX(), getBody2().getY());
    applet.ellipse(getAnchorX(), getAnchorY(), 10, 10);

    postDraw(applet);
  }
  
  public void drawDebug(PGraphics applet){
    preDrawDebug(applet);
    
    applet.line(getAnchorX(), getAnchorY(), getBody1().getX(), getBody1().getY());
    applet.line(getAnchorX(), getAnchorY(), getBody2().getX(), getBody2().getY());
    applet.ellipse(getAnchorX(), getAnchorY(), 10, 10);
    
    postDrawDebug(applet);
  }

}