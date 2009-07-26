package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

public class FDistanceJoint extends FJoint {  
  FBody m_body1;
  FBody m_body2;
  float m_damping = 0.0f;
  float m_frequency = 0.0f;
  float m_length;

  public Vec2 m_anchor1;
  public Vec2 m_anchor2;

  public FDistanceJoint(FBody body1, FBody body2) {
    super();
    m_body1 = body1;
    m_body2 = body2;
    
    // These local positions ( relative to the bodys' positions )
    m_anchor1 = new Vec2(0.0f, 0.0f);
    m_anchor2 = new Vec2(0.0f, 0.0f);
    
    m_length = m_body1.m_body.getPosition().sub(m_body2.m_body.getPosition()).length();
  }

  protected JointDef getJointDef(FWorld world) {
    
    DistanceJointDef md = new DistanceJointDef();
    md.body1 = m_body1.m_body;
    md.body2 = m_body2.m_body;
    md.localAnchor1 = m_anchor1.clone();
    md.localAnchor2 = m_anchor2.clone();
    md.length = m_length;
    md.frequencyHz = m_frequency;
    md.dampingRatio = m_damping;
    m_body1.m_body.wakeUp();
    m_body2.m_body.wakeUp();

    return md;
  }

  public void setDamping(float damping) {
    if ( m_joint != null ) {
      ((DistanceJoint)m_joint).m_dampingRatio = damping;
    }

    m_damping = damping;
  }

  public void setFrequency(float frequency) {
    if ( m_joint != null ) {
      ((DistanceJoint)m_joint).m_frequencyHz = frequency;
    }
    
    m_frequency = frequency;
  }

  public void setLength(float length) {
    if ( m_joint != null ) {
      ((DistanceJoint)m_joint).m_length = Fisica.screenToWorld(length);
    }
    
    m_length = Fisica.screenToWorld(length);
  }


  public void setAnchor2(float x, float y) {
    if (m_joint != null) {
      ((DistanceJoint)m_joint).getAnchor2().set(Fisica.screenToWorld(x), Fisica.screenToWorld(y));
    }
    
    m_anchor2 = Fisica.screenToWorld(x, y);
  }

  public void setAnchor1(float x, float y) {
    if (m_joint != null) {
      ((DistanceJoint)m_joint).getAnchor1().set(Fisica.screenToWorld(x), Fisica.screenToWorld(y));
    }
    
    m_anchor1 = Fisica.screenToWorld(x, y);
  }
  
  /** Get the anchor point on body1 in world coordinates. */
  public float getAnchor1X() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor1()).x;
    }

    return Fisica.worldToScreen(m_anchor1.x);
  }

  /** Get the anchor point on body1 in world coordinates. */
  public float getAnchor1Y() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor1()).y;
    }

    return Fisica.worldToScreen(m_anchor1.y);
  }

  /** Get the anchor point on body1 in world coordinates. */
  public float getAnchor2X() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor2()).x;
    }

    return Fisica.worldToScreen(m_anchor2.x);
  }

  /** Get the anchor point on body1 in world coordinates. */
  public float getAnchor2Y() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor2()).y;
    }

    return Fisica.worldToScreen(m_anchor2.y);
  }
  
  public void draw(PApplet applet){
    preDraw(applet);
 
    applet.line(getAnchor1X(), getAnchor1Y(), getAnchor2X(), getAnchor2Y());
    
    postDraw(applet);
  }
}