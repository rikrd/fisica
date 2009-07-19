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
  public Body m_bodyA;
  public Body m_bodyB;

  public Joint m_joint;
  public FWorld m_world;

  public void processJoint(World world, JointDef jd){
    m_joint = world.createJoint(jd);
  }

  public void addToWorld(FWorld world) {
    m_world = world;

    JointDef jd = getJointDef();
    processJoint(m_world, jd);
    
  }

  protected JointDef getJointDef() {
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
  public FBody getBodyA() {
    if (m_joint != null) {
      return (FBody)m_joint.m_body1.getUserData();
    }

    return null;
  }
  
  /** Get the second body attached to this joint. */
  public FBody getBodyB() {
    if (m_joint != null) {
      return (FBody)m_joint.m_body2.getUserData();
    }

    return null;
  }
  
  /** Get the anchor point on body1 in world coordinates. */
  public float getAnchor1X() {
    if (m_joint != null) {
      return m_joint.getAnchor1().x;
    }

    return 0.0f;
  }

  /** Get the anchor point on body1 in world coordinates. */
  public float getAnchor1Y() {
    if (m_joint != null) {
      return m_joint.getAnchor1().y;
    }

    return 0.0f;
  }

  /** Get the anchor point on body1 in world coordinates. */
  public float getAnchor2X() {
    if (m_joint != null) {
      return m_joint.getAnchor2().x;
    }

    return 0.0f;
  }

  /** Get the anchor point on body1 in world coordinates. */
  public float getAnchor2Y() {
    if (m_joint != null) {
      return m_joint.getAnchor2().y;
    }

    return 0.0f;
  }
  
  /** Get the reaction force on body2 at the joint anchor. */
  public float getReactionForceX() {
    if (m_joint != null) {
      return m_joint.getReactionForce().x;
    }

    return 0.0f;
  }
  
  /** Get the reaction force on body2 at the joint anchor. */
  public float getReactionForceY() {
    if (m_joint != null) {
      return m_joint.getReactionForce().y;
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
