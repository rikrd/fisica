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

  public void draw(PApplet applet){
    preDraw(applet);
 
    applet.line(getAnchor1X(), getAnchor1Y(), getAnchor2X(), getAnchor2Y());
    
    postDraw(applet);
  }
}