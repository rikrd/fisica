package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

public class FGearJoint extends FJoint {    
  public FJoint m_joint1;
  public FJoint m_joint2;

  public float m_ratio = 1.0f;
  
  public FGearJoint(FJoint joint1, FJoint joint2) {
    super();
    
    m_joint1 = joint1;
    m_joint2 = joint2;
  }

  public void setRatio(float ratio) {
    // TODO:  check if ratio can be changed during simulation
    if (m_joint != null) {
      ((GearJoint)m_joint).m_ratio = ratio;
    }

    m_ratio = ratio;
  }

  protected JointDef getJointDef(FWorld world) {
    GearJointDef md = new GearJointDef();
    md.joint1 = m_joint1.m_joint;
    md.joint2 = m_joint2.m_joint;
    md.ratio = m_ratio;
    return md;
  }


  public void draw(PApplet applet){
  }
}