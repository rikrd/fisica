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
 * Represents a gear joint between joints that transfers the movement of one joint to the other.  This type of joint can only be used on joints of types {@link FRevoluteJoint} and {@link FPrismaticJoint}.
 *
 */
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

  protected void updateRatio(){
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

  /**
   * Construct a gear joint between two joints.
   *
   * @param joint1  first joint of the gear
   * @param joint2  second joint of the gear
   *
   */
  public FGearJoint(FJoint joint1, FJoint joint2) {
    super();

    m_joint1 = joint1;
    m_joint2 = joint2;
    updateRatio();
  }

  /**
   * Sets the ratio of movement transfer from one joint to the other of the gear.
   *
   * @param ratio  the ratio of movement that is transfered between the first and the second joints of the gear
   *
   */
  public void setRatio(float ratio) {
    m_ratio = ratio;
    updateRatio();
  }

  public void draw(PGraphics applet){
  }
}