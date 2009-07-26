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

public class FJoint extends FDrawable {  
  // Joint creation settings
  public Joint m_joint;
  public FWorld m_world;

  public boolean m_collideConnected = true;
  
  public void processJoint(World world, JointDef jd){
    jd.collideConnected = m_collideConnected;
    m_joint = world.createJoint(jd);
  }

  public void addToWorld(FWorld world) {
    m_world = world;

    JointDef jd = getJointDef(world);
    processJoint(m_world, jd);
    m_joint.m_userData = this;
    
  }

  public void removeFromWorld() {
    if (m_joint == null) return;
    
    m_world.destroyJoint(this.m_joint);
    m_joint = null;
  }

  protected JointDef getJointDef(FWorld world) {
    return null;
  }

  protected void preDraw(PApplet applet) {
    applet.pushStyle();
    applet.pushMatrix();
    
    applet.ellipseMode(PConstants.CENTER);
    applet.rectMode(PConstants.CENTER);
    appletFillStroke(applet);
  }

  protected void postDraw(PApplet applet) {
    applet.popMatrix();
    applet.popStyle();
  }  

  /** Get the first body attached to this joint. */
  public FBody getBody1() {
    if (m_joint != null) {
      return (FBody)m_joint.m_body1.getUserData();
    }

    return null;
  }
  
  /** Get the second body attached to this joint. */
  public FBody getBody2() {
    if (m_joint != null) {
      return (FBody)m_joint.m_body2.getUserData();
    }

    return null;
  }

  public void setCollideConnected(boolean value) {
    if (m_joint != null) {
      ((Joint)m_joint).m_collideConnected = value;
    }
    
    m_collideConnected = value;
  }

  /** Get the reaction force on body2 at the joint anchor. */
  public float getReactionForceX() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getReactionForce()).x;
    }

    return 0.0f;
  }
  
  /** Get the reaction force on body2 at the joint anchor. */
  public float getReactionForceY() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getReactionForce()).y;
    }
    
    return 0.0f;
  }

  /** Get the reaction force on body2 at the joint anchor. */
  public float getReactionTorque() {
    if (m_joint != null) {
      return m_joint.getReactionTorque();
    }
    
    return 0.0f;
  }

}
