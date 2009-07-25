package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

public class FBlob extends FJoint {  
  public ArrayList m_bodies;
  public ArrayList m_vertices;  // in world coords
  public ArrayList m_vertexBodies;  // in world coords
  float m_damping = 0.0f;
  float m_frequency = 0.0f;
  float m_vertexSize = 0.4f;  // in world coords
  
  Vec2 m_force = new Vec2();
  float m_torque = 0.0f;
  float m_density = 1.0f;
  float m_restitution = 0.5f;
  float m_friction = 0.5f;
  boolean m_bullet = false;
  
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
      fb.setDensity(m_density);
      fb.setRestitution(m_restitution);
      fb.setFriction(m_friction);
      world.add(fb);
      
      m_vertexBodies.add(fb);
      
      if (fb.m_body != null) {
        Vec2 f = Fisica.worldToScreen(m_force);
        fb.addForce(f.x, f.y);
        fb.addTorque(m_torque);
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

  public void setVertexSize(float size){
    m_vertexSize = Fisica.screenToWorld(size);
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

  public void addForce(float fx, float fy) {
    if ( m_joint != null ) {
      for (int i=0; i<m_bodies.size(); i++) {
        ((FBody)m_bodies.get(i)).addForce(fx, fy);
      }

      for (int i=0; i<m_vertexBodies.size(); i++) {
        ((FBody)m_vertexBodies.get(i)).addForce(fx, fy);
      }
    }
    
    m_force.add(Fisica.screenToWorld(fx, fy));
  }

  public void addTorque(float t) {
    if ( m_joint != null ) {
      for (int i=0; i<m_bodies.size(); i++) {
        ((FBody)m_bodies.get(i)).addTorque(t);
      }

      for (int i=0; i<m_vertexBodies.size(); i++) {
        ((FBody)m_vertexBodies.get(i)).addTorque(t);
      }
    }
    
    m_torque += t;
  }

  public void setDensity(float d) {
    if ( m_joint != null ) {
      for (int i=0; i<m_bodies.size(); i++) {
        ((FBody)m_bodies.get(i)).setDensity(d);
      }

      for (int i=0; i<m_vertexBodies.size(); i++) {
        ((FBody)m_vertexBodies.get(i)).setDensity(d);
      }
    }
    
    m_density = d;
  }

  public void setFriction(float d) {
    if ( m_joint != null ) {
      for (int i=0; i<m_bodies.size(); i++) {
        ((FBody)m_bodies.get(i)).setFriction(d);
      }

      for (int i=0; i<m_vertexBodies.size(); i++) {
        ((FBody)m_vertexBodies.get(i)).setFriction(d);
      }
    }
    
    m_friction = d;
  }

  public void setRestitution(float d) {
    if ( m_joint != null ) {
      for (int i=0; i<m_bodies.size(); i++) {
        ((FBody)m_bodies.get(i)).setRestitution(d);
      }

      for (int i=0; i<m_vertexBodies.size(); i++) {
        ((FBody)m_vertexBodies.get(i)).setRestitution(d);
      }
    }
    
    m_restitution = d;
  }

  public void setBullet(boolean d) {
    if ( m_joint != null ) {
      for (int i=0; i<m_bodies.size(); i++) {
        ((FBody)m_bodies.get(i)).setBullet(d);
      }

      for (int i=0; i<m_vertexBodies.size(); i++) {
        ((FBody)m_vertexBodies.get(i)).setBullet(d);
      }
    }
    
    m_bullet = d;
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

    if (m_vertexBodies.size()>0) {
      applet.beginShape();
      for (int i=0; i<m_vertexBodies.size(); i++) {
        applet.vertex(((FBody)m_vertexBodies.get(i)).getX(), ((FBody)m_vertexBodies.get(i)).getY());
      }
      applet.endShape(applet.CLOSE);
    }
    
    postDraw(applet);
  }
}