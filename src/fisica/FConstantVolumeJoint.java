package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

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

  public FConstantVolumeJoint() {
    super();

    m_bodies = new ArrayList();
  }

  public void addBody(FBody b) {
    m_bodies.add(b);
  }

  public ArrayList getBodies() {
    return m_bodies;
  }

  public void setDamping(float damping) {
    m_damping = damping;
  }

  public void setFrequency(float frequency) {
    m_frequency = frequency;
  }

  public void draw(PApplet applet){
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