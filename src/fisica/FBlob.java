package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

/**
 * Represents a blob body that can be added to a world.
 * Blobs are soft bodies that are composed of vertices and tries to maintain constant the volume the vertices enclose.
 * Blobs can be created by adding vertices in a similar way to {@link FPoly FPoly}:
 * <pre>
 * {@code
 * FBlob myBlob = new FBlob();
 * myBlob.vertex(40, 10);
 * myBlob.vertex(50, 20);
 * myBlob.vertex(60, 30);
 * myBlob.vertex(60, 40);
 * myBlob.vertex(50, 50);
 * myBlob.vertex(40, 60);
 * myBlob.vertex(30, 70);
 * myBlob.vertex(20, 60);
 * myBlob.vertex(10, 50);
 * myBlob.vertex(10, 40);
 * myBlob.vertex(20, 30);
 * myBlob.vertex(30, 20);
 * myBlob.vertex(40, 10);
 * world.add(myBlob);
 * }
 * </pre>
 *
 * or it may be initialized using the method {@link #setAsCircle(float) setAsCircle} to set the initial shape as a circle:
 * <pre>
 * {@code
 * FBlob myBlob = new FBlob();
 * myBlob.setAsCircle(40);
 * world.add(myBlob);
 * }
 * </pre>
 *
 * @usage Bodies
 * @see FBox
 * @see FCircle
 */
public class FBlob extends FBody {  
  protected ArrayList m_vertices;  // in world coords
  protected ArrayList m_vertexBodies;  // in world coords
  protected float m_damping = 0.0f;
  protected float m_frequency = 0.0f;
  protected float m_vertexSize = 0.4f;  // in world coords
  
  protected Vec2 m_force = new Vec2();
  protected float m_torque = 0.0f;
  protected float m_density = 1.0f;
  protected float m_restitution = 0.5f;
  protected float m_friction = 0.5f;
  protected boolean m_bullet = false;

  protected FConstantVolumeJoint m_joint;
  
  /**
   * Constructs a blob body that can be added to a world.  It creates an empty blob, before adding the blob to the world use {@link #vertex(float,float) vertex} or {@link #setAsCircle(float) setAsCircle} to define the initial shape of the blob.
   */
  public FBlob() {
    super();
    
    m_vertices = new ArrayList();
    m_vertexBodies = new ArrayList();
  }

  protected void addToWorld(FWorld world) {
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

  protected void removeFromWorld(FWorld world) {
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

  public void setAsCircle(float x, float y, 
                          float size, int vertexCount) {
    for (int i=0; i<vertexCount; i++) {
      float angle = Fisica.parent().map(i, 0, vertexCount-1, 0, Fisica.parent().TWO_PI);
      float vx = x + size/2 * Fisica.parent().sin(angle);
      float vy = y + size/2 * Fisica.parent().cos(angle);
      
      this.vertex(vx, vy);
    }
  }

  public void setAsCircle(float x, float y, float size) {
    setAsCircle(x, y, size);
  }

  public void setAsCircle(float size) {
    setAsCircle(0, 0, size, 30);
  }

  public void setAsCircle(float size, int vertexCount) {
    setAsCircle(0, 0, size, vertexCount);
  }

  public void setVertexSize(float size){
    m_vertexSize = Fisica.screenToWorld(size);
  }

  public void setFrequency(float frequency) {
    if ( m_joint != null ) {
      m_joint.setFrequency(frequency);
    }
    
    m_frequency = frequency;
  }

  public void setDamping(float damping) {
    if ( m_joint != null ) {
      m_joint.setDamping(damping);
    }

    m_damping = damping;
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

  public void setFillColor(int col) {
    super.setFillColor(col);
    
    if (m_joint != null) {
      m_joint.updateStyle(this);
    }
  }

  public void setStrokeColor(int col) {
    super.setStrokeColor(col);
    
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