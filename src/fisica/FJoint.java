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

  public void processJoint(World world, JointDef jd){
    m_joint = world.createJoint(jd);
  }

  public void addToWorld(FWorld world) {
    m_world = world;

    JointDef jd = getJointDef();
    processJoint(m_world, jd);

    /*
    m_joint.m_userData = this;
    m_joint.setXForm(m_position, m_angle);
    m_joint.setLinearVelocity(m_linearVelocity);
    m_joint.setAngularVelocity(m_angularVelocity);
    
    m_joint.m_linearDamping = m_linearDamping;
    m_joint.m_angularDamping = m_angularDamping;
    
    if (m_rotatable) {
      m_joint.m_flags &= ~m_joint.e_fixedRotationFlag;
    }else{
      m_joint.m_flags |= m_joint.e_fixedRotationFlag;
    }
    
    m_joint.setBullet(m_bullet);
    
    m_joint.applyForce(m_force, m_joint.getWorldCenter());
    m_joint.applyTorque(m_torque);
    
    m_joint.m_type = m_static ? m_joint.e_staticType : m_joint.e_dynamicType;   
    updateMass();
    */
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
}
