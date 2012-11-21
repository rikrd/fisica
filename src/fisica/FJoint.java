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

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

import processing.core.*;

/**
 * Represents a joint between two or more bodies.
 *
 * A joint establishes some kind of relation between two or more bodies.  Depending on the specific joint the relation might be a distance relation, a rotation relation or even a volume conservation relation.  This class cannot be be instantiated, instead use one of the derived classes.
 *
 */
public abstract class FJoint extends FDrawable {
  // Joint creation settings
  protected Joint m_joint;
  protected FWorld m_world;

  protected boolean m_collideConnected = true;

  /**
   * WARNING: This method is internal only and may change someday.  If you are using this method please contact the developer since there should be a better way or we may add something to the library.
   *
   * @return the internal JBox2D joint
   *
   */
  public Joint getBox2dJoint() {
    return m_joint;
  }
  
  protected void processJoint(World world, JointDef jd){
    jd.collideConnected = m_collideConnected;
    m_joint = world.createJoint(jd);
  }

  public void addToWorld(FWorld world) {
    m_world = world;

    JointDef jd = getJointDef(world);
    if (jd == null) return;

    processJoint(m_world, jd);
    m_joint.m_userData = this;

  }

  public void removeFromWorld() {
    if (m_joint == null) return;

    m_world.destroyJoint(this.m_joint);
    
    m_joint = null;
    m_world = null;
  }

  protected JointDef getJointDef(FWorld world) {
    return null;
  }

  protected void preDraw(PGraphics applet) {
    applet.pushStyle();
    applet.pushMatrix();

    applet.ellipseMode(PConstants.CENTER);
    applet.rectMode(PConstants.CENTER);
    appletFillStroke(applet);
  }

  protected void postDraw(PGraphics applet) {
    applet.popMatrix();
    applet.popStyle();
  }

  protected void preDrawDebug(PGraphics applet) {
    applet.pushStyle();
    applet.pushMatrix();

    applet.ellipseMode(PConstants.CENTER);
    applet.rectMode(PConstants.CENTER);
    
    applet.strokeWeight(1);
    
    applet.fill(80, 50);
    applet.stroke(80, 150);
  }

  protected void postDrawDebug(PGraphics applet) {    
    applet.popMatrix();
    applet.popStyle();
  }

  
  /** 
   * Returns the first body attached to this joint. 
   * @return first of the bodies connected by this joint
   */
  public FBody getBody1() {
    if (m_joint != null) {
      return (FBody)m_joint.m_body1.getUserData();
    }

    return null;
  }

  /** 
   * Returns the second body attached to this joint. 
   * @return second of the bodies connected by this joint
   */
  public FBody getBody2() {
    if (m_joint != null) {
      return (FBody)m_joint.m_body2.getUserData();
    }

    return null;
  }

  /** 
   * Sets whether the bodies connected by the joint should collide with each other. 
   *
   * @param value  if {@code true} the bodies connected by this joint will be allowed to collide with each other
   */
  public void setCollideConnected(boolean value) {
    if (m_joint != null) {
      ((Joint)m_joint).m_collideConnected = value;
    }

    m_collideConnected = value;
  }

  /**
   * Returns the horizontal component of the reaction force on the second body at the joint anchor.
   * @return horizontal component of the reaction force
   */
  public float getReactionForceX() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getReactionForce()).x;
    }

    return 0.0f;
  }

  /** 
   * Returns the vertical component of the reaction force on the second body at the joint anchor.
   * @return vertical component of the reaction force
   */
  public float getReactionForceY() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getReactionForce()).y;
    }

    return 0.0f;
  }

  /** 
   * Returns the reaction torque on the second body at the joint anchor.
   * @return reaction torque
   */
  public float getReactionTorque() {
    if (m_joint != null) {
      return m_joint.getReactionTorque();
    }

    return 0.0f;
  }

}
