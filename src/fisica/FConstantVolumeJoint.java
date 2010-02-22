package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

/**
 * Represents constant volume joint that tries to keep the volume enclosed by a group of bodies constant.  This joint is similar to connecting multiple springs between the bodies in order to maintain the volume inside their position constant. This is the joint used to create the {@link FBlob} body.
 *
 */
public class FConstantVolumeJoint extends FJoint {
  protected ArrayList m_bodies;
  protected float m_damping = 0.0f;
  protected float m_frequency = 0.0f;

  protected JointDef getJointDef(FWorld world) {
    ConstantVolumeJointDef md = new ConstantVolumeJointDef();
    md.frequencyHz = m_frequency;
    md.dampingRatio = m_damping;

    for (int i=0; i<m_bodies.size(); i++) {
      Body b = ((FBody)m_bodies.get(i)).m_body;
      if (b != null) {
        md.addBody(b);
      }
    }

    return md;
  }

  /**
   * Constructs an constant volume joint.  It creates an empty joint, before adding the joint to the world use {@link #addBody(FBody) addBody} to add bodies to the joint and to define the initial and target volume of the joint.
   */
  public FConstantVolumeJoint() {
    super();

    m_bodies = new ArrayList();
  }

  /**
   * Adds a body to the joint.  This method must be called before adding the joint to the world.
   *
   * @param b  body to be added
   */
  public void addBody(FBody b) {
    m_bodies.add(b);
  }

  /**
   * Return the group of bodies that are connected by this joint.
   *
   * @return   list of bodies (ArrayList of FBody) connected by the joint
   *
   */
  public ArrayList getBodies() {
    return m_bodies;
  }

  /**
   * Sets the damping of the springs used to maintain the volume defined by the vertices constant.  This method must be called before adding the joint to the world.
   *
   * @param damping  the damping of the springs of the constant volume joint
   */
  public void setDamping(float damping) {
    m_damping = damping;
  }

  /**
   * Sets the frequency of the springs used to maintain the volume defined by the vertices constant.  This method must be called before adding the joint to the world.
   *
   * @param frequency  the frequency of the springs of the constant volume joint
   */
  public void setFrequency(float frequency) {
    m_frequency = frequency;
  }

  public void draw(PGraphics applet){
    preDraw(applet);

    if (m_bodies.size()>0) {
      applet.beginShape();
      for (int i=0; i<m_bodies.size(); i++) {
        applet.vertex(((FBody)m_bodies.get(i)).getX(), ((FBody)m_bodies.get(i)).getY());
      }
      applet.endShape(applet.CLOSE);
    }

    postDraw(applet);
  }
}