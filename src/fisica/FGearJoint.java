package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

public class FGearJoint extends FJoint {
  protected FJoint m_joint1;
  protected FJoint m_joint2;

  protected float m_ratio = 1.0f;   // in screen params because the units depends on the joints
  protected float m_worldRatio = 1.0f;   // in screen params because the units depends on the joints

  protected JointDef getJointDef(FWorld world) {
    GearJointDef md = new GearJointDef();
    md.joint1 = m_joint1.m_joint;
    md.joint2 = m_joint2.m_joint;
    md.ratio = m_worldRatio;
    return md;
  }

  public FGearJoint(FJoint joint1, FJoint joint2) {
    super();

    m_joint1 = joint1;
    m_joint2 = joint2;
    updateRatio();
  }

  public void updateRatio(){
    m_worldRatio = m_ratio;

    // TODO: check if it is this or the opposite
    if (m_joint1.m_joint.getType() == JointType.PRISMATIC_JOINT ) {
      m_worldRatio = Fisica.screenToWorld(m_worldRatio);
    }

    if (m_joint2.m_joint.getType() == JointType.PRISMATIC_JOINT ) {
      m_worldRatio = 1.0f / Fisica.screenToWorld(1.0f / m_worldRatio);
    }

    if (m_joint != null) {
      ((GearJoint)m_joint).m_ratio = m_worldRatio;
    }
  }

  public void setRatio(float ratio) {
    m_ratio = ratio;
    updateRatio();
  }

  public void draw(PApplet applet){
  }
}