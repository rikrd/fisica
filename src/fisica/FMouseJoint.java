package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

public class FMouseJoint extends FJoint {  
  FBody m_fbody;
  Vec2 m_target;
  float m_damping = 0.9f;
  float m_frequency = 20.0f;

  public FMouseJoint(FBody body, float x, float y) {
    super();
    m_fbody = body;
    m_target = Fisica.screenToWorld(x, y);
  }

  protected JointDef getJointDef() {
    Body body = m_fbody.m_body;
    
    MouseJointDef md = new MouseJointDef();
    md.body1 = m_world.getGroundBody();
    md.body2 = body;
    md.target.set(m_target);
    md.maxForce = 10000.0f * body.m_mass;
    md.frequencyHz = m_frequency;
    md.dampingRatio = m_damping;
    body.wakeUp();

    return md;
  }

  public void setDamping(float damping) {
    // TODO: handle the fact that this is not updated when already added to the world
    m_damping = damping;
  }

  public void setFrequency(float frequency) {
    // TODO: handle the fact that this is not updated when already added to the world
    m_frequency = frequency;
  }

  public void setTarget(float x, float y) {
    if (m_joint != null) {
      ((MouseJoint)m_joint).setTarget(Fisica.screenToWorld(x, y));
    }
    
    m_target = Fisica.screenToWorld(x, y);
  }

  public float getTargetX(){
    if (m_joint != null) {
      return Fisica.worldToScreen(((MouseJoint)m_joint).m_target).x;
    }

    return Fisica.worldToScreen(m_target).x;
  }

  public float getTargetY(){
    if (m_joint != null) {
      return Fisica.worldToScreen(((MouseJoint)m_joint).m_target).y;
    }

    return Fisica.worldToScreen(m_target).y;
  }

  public void setGrabbedBodyAndTarget(FBody body, float x, float y) {
    if (m_joint != null) {
      m_joint.m_body2 = body.m_body;
      ((MouseJoint)m_joint).m_target.set(Fisica.screenToWorld(x, y));
    }
    
    m_fbody = body;
    m_target = Fisica.screenToWorld(x, y);
  }

  public void releaseGrabbedBody() {
    if (m_joint != null) {
      m_joint.m_body2 = null;
      ((MouseJoint)m_joint).m_target.set(0.0f, 0.0f);
    }

    m_fbody = null;
    m_target = null;    
  }

  public FBody getGrabbedBody() {
    if (m_joint != null) {
      return (FBody)m_joint.m_body2.getUserData();
    }
    
    return m_fbody;
  }

  public void draw(PApplet applet){
    preDraw(applet);
    
    applet.line(getAnchor2X(), getAnchor2Y(), getTargetX(), getTargetY());
    
    postDraw(applet);
  }
}