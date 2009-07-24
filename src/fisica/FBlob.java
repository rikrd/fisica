package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

public class FBlob extends FJoint {  
  ArrayList m_bodies;
  ArrayList m_vertices;  // in world coords
  ArrayList m_vertexBodies;  // in world coords
  float m_damping = 0.0f;
  float m_frequency = 0.0f;
  float m_vertexSize = 0.4f;  // in world coords

  public FBlob() {
    super();
    
    m_bodies = new ArrayList();
    m_vertices = new ArrayList();
    m_vertexBodies = new ArrayList();
  }

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

    m_vertexBodies = new ArrayList();
    for (int i=0; i<m_vertices.size(); i++) {
      Vec2 p = Fisica.worldToScreen((Vec2)m_vertices.get(i));
      FBody fb = new FCircle(getVertexSize());
      fb.setPosition(p.x, p.y);
      fb.setGroupIndex(-2);
      fb.setDrawable(false);
      world.add(fb);
      
      m_vertexBodies.add(fb);
      
      if (fb.m_body != null) {
        md.addBody(fb.m_body);
      }
    }
    
    return md;
  }

  public void vertex(float x, float y){
    m_vertices.add(Fisica.screenToWorld(x, y));
  }

  public float getVertexSize(){
    return Fisica.worldToScreen(m_vertexSize);
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

  public void draw(PApplet applet){
    preDraw(applet);

    applet.beginShape();
    for (int i=0; i<m_vertexBodies.size(); i++) {
      applet.vertex(((FBody)m_vertexBodies.get(i)).getX(), ((FBody)m_vertexBodies.get(i)).getY());
    }
    applet.endShape(applet.CLOSE);
    
    postDraw(applet);
  }
}