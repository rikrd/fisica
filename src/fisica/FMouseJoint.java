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
 * Represents a mouse joint that tries to keep a body at a constant distance from the target.  This joint is similar to connecting a spring from from the body to a target whose position can be changed programatically using {@link #setTarget}.
 *
 */
public class FMouseJoint extends FJoint {
  protected FBody m_fbody;

  protected Vec2 m_anchor;
  protected Vec2 m_target;

  protected float m_damping = 0.9f;
  protected float m_frequency = 20.0f;

  protected JointDef getJointDef(FWorld world) {
    //if (m_fbody == null) return null;
    Body body = m_fbody.m_body;

    MouseJointDef md = new MouseJointDef();
    md.body1 = m_world.getGroundBody();
    md.body2 = body;
    md.target.set(m_anchor);
    md.maxForce = 10000.0f * body.m_mass;
    md.frequencyHz = m_frequency;
    md.dampingRatio = m_damping;
    body.wakeUp();

    return md;
  }

  /**
   * Construct a mouse joint between a body and a target.  The constructor automatically sets the anchors of the joint to the center of the body.
   *
   * @param body  the body to be grabbed by the joint
   * @param x  horizontal coordinate of the initial target of the joint
   * @param y  vertical coordinate of the initial target of the joint
   */
  public FMouseJoint(FBody body, float x, float y) {
    super();
    m_fbody = body;
    m_target = Fisica.screenToWorld(x, y);
    m_anchor = Fisica.screenToWorld(x, y);
  }

  /**
   * Sets the damping of the spring used to maintain the body and the target together.  This property must be set before adding the joint to the world.
   *
   * @param damping  the damping of the spring
   */
  public void setDamping(float damping) {
    // TODO: handle the fact that this is not updated when already added to the world
    m_damping = damping;
  }

  /**
   * Sets the frequency of the spring used to maintain the body and the target together.  This property must be set before adding the joint to the world.
   *
   * @param frequency  the frequency of the spring
   */
  public void setFrequency(float frequency) {
    // TODO: handle the fact that this is not updated when already added to the world
    m_frequency = frequency;
  }

  /**
   * Sets the target position of the joint.  By setting this property everytime the mouse is used we are able to make the body of this joint follow mouse.
   *
   * @param x  horizontal coordinate of the target of the joint
   * @param y  vertical coordinate of the target of the joint
   */
  public void setTarget(float x, float y) {
    if (m_joint != null) {
      ((MouseJoint)m_joint).setTarget(Fisica.screenToWorld(x, y));
    }

    m_target = Fisica.screenToWorld(x, y);
  }

  /**
   * Returns the horizontal target position of the joint.
   *
   * @return  horizontal coordinate of the target of the joint
   */
  public float getTargetX(){
    if (m_joint != null) {
      return Fisica.worldToScreen(((MouseJoint)m_joint).m_target).x;
    }

    return Fisica.worldToScreen(m_target).x;
  }

  /**
   * Returns the vertical target position of the joint.
   *
   * @return  vertical coordinate of the target of the joint
   */
  public float getTargetY(){
    if (m_joint != null) {
      return Fisica.worldToScreen(((MouseJoint)m_joint).m_target).y;
    }

    return Fisica.worldToScreen(m_target).y;
  }

  /**
   * Sets the body grabbed by this joint and the target position.
   *
   * @param body  the body to be grabbed by the joint
   * @param x  horizontal coordinate of the target of the joint
   * @param y  vertical coordinate of the target of the joint
   */
  public void setGrabbedBodyAndTarget(FBody body, float x, float y) {
    if (m_joint != null) {
      m_joint.m_body2 = body.m_body;
      ((MouseJoint)m_joint).m_target.set(Fisica.screenToWorld(x, y));
      ((MouseJoint)m_joint).getAnchor2().set(Fisica.screenToWorld(x), Fisica.screenToWorld(y));
    }

    m_fbody = body;
    m_target = Fisica.screenToWorld(x, y);
    m_anchor = Fisica.screenToWorld(x, y);
  }

  /**
   * Releases the body grabbed by this joint.
   *
   */
  public void releaseGrabbedBody() {
    if (m_joint != null) {
      m_joint.m_body2 = null;
      ((MouseJoint)m_joint).m_target.set(0.0f, 0.0f);
    }

    m_fbody = null;
    m_target = null;
  }

  /**
   * Returns the body grabbed by this joint.
   *
   * @return the body grabbed by this joint
   */
  public FBody getGrabbedBody() {
    if (m_joint != null) {
      return (FBody)m_joint.m_body2.getUserData();
    }

    return m_fbody;
  }

  /**
   * Sets the anchor position at which the joint grabs the body.  The anchor point is the point used to apply forces in order to move the body.
   *
   * @param x  the horizontal coordinate of the anchor relative to the center of the body
   * @param y  the vertical coordinate of the anchor relative to the center of the body
   */
  public void setAnchor(float x, float y) {
    if (m_joint != null) {
      ((MouseJoint)m_joint).getAnchor2().set(Fisica.screenToWorld(x), Fisica.screenToWorld(y));
    }

    m_anchor = Fisica.screenToWorld(x, y);
  }

  /**
   * Get the horizontal coordinate of the anchor point on the body.  This position is given relative to the center of the body.
   *
   * @return  the horizontal coordinate of the anchor relative to the center of the first body
   */
  public float getAnchorX() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor2()).x;
    }

    return Fisica.worldToScreen(m_anchor.x);
  }

  /**
   * Get the vertical coordinate of the anchor point on the body.  This position is given relative to the center of the body.
   *
   * @return  the vertical coordinate of the anchor relative to the center of the first body
   */
  public float getAnchorY() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor2()).y;
    }

    return Fisica.worldToScreen(m_anchor.y);
  }

  public void draw(PGraphics applet){
    preDraw(applet);

    applet.line(getAnchorX(), getAnchorY(), getTargetX(), getTargetY());

    postDraw(applet);
  }
  
  public void drawDebug(PGraphics applet){
    preDrawDebug(applet);
    
    applet.line(getAnchorX(), getAnchorY(), getTargetX(), getTargetY());
    applet.ellipse(getAnchorX(), getAnchorY(), 5, 5);
    applet.ellipse(getTargetX(), getTargetY(), 10, 10);
    
    postDrawDebug(applet);
  }

}