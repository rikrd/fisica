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

  public FDistanceJoint(FBody body1, FBody body2) {
    super();
    m_body1 = body1;
    m_body2 = body2;
    m_anchor1 = new Vec2();
    m_anchor2 = new Vec2();
  }

  protected JointDef getJointDef() {
    
    DistanceJointDef md = new DistanceJointDef();
    md.body1 = m_body1.m_body;
    md.body2 = m_body2.m_body;
    md.localAnchor1 = m_anchor1.clone();
    md.localAnchor2 = m_anchor2.clone();
    md.length = m_body1.m_body.getPosition().sub(m_body2.m_body.getPosition()).length();
    md.frequencyHz = m_frequency;
    md.dampingRatio = m_damping;
    m_body1.m_body.wakeUp();
    m_body2.m_body.wakeUp();

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

  public void draw(PApplet applet){
    preDraw(applet);
 
    applet.line(getAnchor1X(), getAnchor1Y(), getAnchor2X(), getAnchor2Y());
    
    postDraw(applet);
  }
}