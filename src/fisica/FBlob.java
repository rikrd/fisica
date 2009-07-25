package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

public class FBlob extends FBody {  
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

  public FConstantVolumeJoint m_joint;
  
  public FBlob() {
    super();
    
    m_vertices = new ArrayList();
    m_vertexBodies = new ArrayList();
  }

  public void addToWorld(FWorld world) {
    // Create the constant volume joint
    FConstantVolumeJoint m_joint = new FConstantVolumeJoint();
    m_joint.setFrequency(m_frequency);
    m_joint.setDamping(m_damping);
    m_joint.updateStyle(this);
    
    // Create bodies from the vertices and add them to the
    // constant volume joint that we just created
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
      
      Vec2 f = Fisica.worldToScreen(m_force);
      fb.addForce(f.x, f.y);
      fb.addTorque(m_torque);

      m_joint.addBody(fb);
    }

    world.add(m_joint);
  }

  public void removeFromWorld(FWorld world) {
    // Remove the constant volume joint
    m_joint.removeFromWorld();
    
    // Remove the vertex bodies
    for (int i=0; i<m_vertexBodies.size(); i++) {
      ((FBody)m_vertexBodies.get(i)).removeFromWorld();
    }
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
      m_joint.setDamping(damping);
    }

    m_damping = damping;
  }

  public void setFrequency(float frequency) {
    if ( m_joint != null ) {
      m_joint.setFrequency(frequency);
    }
    
    m_frequency = frequency;
  }

  public void addForce(float fx, float fy) {
    for (int i=0; i<m_vertexBodies.size(); i++) {
      ((FBody)m_vertexBodies.get(i)).addForce(fx, fy);
    }
    
    m_force.add(Fisica.screenToWorld(fx, fy));
  }

  public void addTorque(float t) {
    for (int i=0; i<m_vertexBodies.size(); i++) {
      ((FBody)m_vertexBodies.get(i)).addTorque(t);
    }
    
    m_torque += t;
  }

  public void setDensity(float d) {
    for (int i=0; i<m_vertexBodies.size(); i++) {
      ((FBody)m_vertexBodies.get(i)).setDensity(d);
    }
    
    m_density = d;
  }

  public void setFriction(float d) {
    for (int i=0; i<m_vertexBodies.size(); i++) {
      ((FBody)m_vertexBodies.get(i)).setFriction(d);
    }
    
    m_friction = d;
  }

  public void setRestitution(float d) {
    for (int i=0; i<m_vertexBodies.size(); i++) {
      ((FBody)m_vertexBodies.get(i)).setRestitution(d);
    }
    
    m_restitution = d;
  }

  public void setBullet(boolean d) {
    for (int i=0; i<m_vertexBodies.size(); i++) {
      ((FBody)m_vertexBodies.get(i)).setBullet(d);
    }
    
    m_bullet = d;
  }

  public void setNoStroke() {
    super.setNoStroke();

    if (m_joint != null) {
      m_joint.updateStyle(this);
    }
  }

  public void setNoFill() {
    super.setNoFill();
    
    if (m_joint != null) {
      m_joint.updateStyle(this);
    }
  }

  public void setFillColorInt(int col) {
    super.setFillColorInt(col);
    
    if (m_joint != null) {
      m_joint.updateStyle(this);
    }
  }

  public void setStrokeColorInt(int col) {
    super.setStrokeColorInt(col);
    
    if (m_joint != null) {
      m_joint.updateStyle(this);
    }
  }

  public void setStrokeWeight(float col) {
    super.setStrokeWeight(col);
    
    if (m_joint != null) {
      m_joint.updateStyle(this);
    }
  }

  public void setDrawable(boolean val) {
    super.setDrawable(val);
    
    if (m_joint != null) {
      m_joint.updateStyle(this);
    }
  }
}