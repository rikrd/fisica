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
 * Represents a blob body that can be added to a world.
 * Blobs are soft bodies that are composed of vertices and tries to maintain constant the volume the vertices enclose.
 * Blobs can be created by adding vertices using the {@link #vertex(float,float) vertex} method in a similar way to {@link FPoly FPoly}:
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
 * @see FPoly
 * @see FLine
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

  public void addToWorld(FWorld world) {
    // Create the constant volume joint
    m_joint = new FConstantVolumeJoint();
    m_joint.setFrequency(m_frequency);
    m_joint.setDamping(m_damping);
    m_joint.updateStyle(this);

    // Create bodies from the vertices and add them to the
    // constant volume joint that we just created
    for (int i=0; i<m_vertices.size(); i++) {
      Vec2 p = Fisica.worldToScreen((Vec2)m_vertices.get(i));
      FBody fb = new FCircle(getVertexSize());
      fb.setPosition(p.x, p.y);
      fb.setDensity(m_density);
      fb.setRestitution(m_restitution);
      fb.setFriction(m_friction);
      fb.setGroupIndex(m_groupIndex);
      fb.setFilterBits(m_filterBits);
      fb.setCategoryBits(m_categoryBits);
      fb.setState(this);
      m_vertexBodies.add(fb);
    }

    for (int i=0; i<m_vertexBodies.size(); i++) {
      FBody fb = (FBody)m_vertexBodies.get(i);
      fb.setDrawable(false);
      fb.setParent(this);
      fb.setRotatable(false);
      world.add(fb);

      Vec2 f = Fisica.worldToScreen(m_force);
      fb.addForce(f.x, f.y);
      fb.addTorque(m_torque);

      m_joint.addBody(fb);
    }

    m_joint.setCollideConnected(false);
    world.add(m_joint);
  }

  public void removeFromWorld() {
    // Remove the constant volume joint
    m_joint.removeFromWorld();

    // Remove the vertex bodies
    for (int i=0; i<m_vertexBodies.size(); i++) {
      ((FBody)(m_vertexBodies.get(i))).removeFromWorld();
    }
  }

  /**
   * Adds a vertex body to the initial shape of the blob.  This method must be called before adding the body to the world.
   *
   * @param b  b the body to be added
   */
  public void addVertexBody(FBody b){
    m_vertexBodies.add(b);
  }

  /**
   * Adds a vertex to the initial shape of the blob.  This method must be called before adding the body to the world.
   *
   * @param x  x coordinate of the vertex to be added
   * @param y  y coordinate of the vertex to be added
   */
  public void vertex(float x, float y){
    m_vertices.add(Fisica.screenToWorld(x, y));
  }

  /**
   * Gets the x coordinate of the ith vertex of the initial shape of the blob.
   *
   * @param i  index of the vertex to retrieve
   * @return  the x coordinate of the vertex to retrieve
   */
  public float getVertexX(int i){
    return Fisica.worldToScreen((Vec2)m_vertices.get(i)).x;
  }

  /**
   * Gets the y coordinate of the ith vertex of the initial shape of the blob.
   *
   * @param i  index of the vertex to retrieve
   * @return  the y coordinate of the vertex to retrieve
   */
  public float getVertexY(int i){
    return Fisica.worldToScreen((Vec2)m_vertices.get(i)).y;
  }

  /**
   * Sets the initial shape of the blob to a circle.  This method removes all the previous vertices tha may have been added by the use of the {@link #vertex(float,float) vertex}.  This method must be called before adding the body to the world.
   *
   * @param x  x coordinate of the position of the circle
   * @param y  y coordinate of the position of the circle
   * @param size  size of the circle
   * @param vertexCount  number of vertices of the circle
   */
  public void setAsCircle(float x, float y,
                          float size, int vertexCount) {
    m_vertices.clear();

    for (int i=0; i<vertexCount; i++) {
      float angle = Fisica.parent().map(i, 0, vertexCount, 0, Fisica.parent().TWO_PI);
      float vx = x + size/2 * Fisica.parent().sin(angle);
      float vy = y + size/2 * Fisica.parent().cos(angle);

      this.vertex(vx, vy);
    }
  }

  /**
   * Sets the initial shape of the blob to a circle.  This method removes all the previous vertices tha may have been added by the use of the {@link #vertex(float,float) vertex}.  This method must be called before adding the body to the world.
   *
   * @param x  x coordinate of the position of the circle
   * @param y  y coordinate of the position of the circle
   * @param size  size of the circle
   */
  public void setAsCircle(float x, float y, float size) {
      setAsCircle(x, y, size, 30);
  }

  /**
   * Sets the initial shape of the blob to a circle.  This method removes all the previous vertices tha may have been added by the use of the {@link #vertex(float,float) vertex}.  This method must be called before adding the body to the world.
   *
   * @param size  size of the circle
   */
  public void setAsCircle(float size) {
    setAsCircle(0, 0, size, 30);
  }

  /**
   * Sets the initial shape of the blob to a circle.  This method removes all the previous vertices tha may have been added by the use of the {@link #vertex(float,float) vertex}.  This method must be called before adding the body to the world.
   *
   * @param size  size of the circle
   * @param vertexCount  number of vertices of the circle
   */
  public void setAsCircle(float size, int vertexCount) {
    setAsCircle(0, 0, size, vertexCount);
  }

  /**
   * Returns the size of the circular vertices of the blob.  This method must be called before the body is added to the world.
   *
   * @return size of the circular vertices of the blob
   */
  public float getVertexSize(){
    return Fisica.worldToScreen(m_vertexSize);
  }

  /**
   * Sets the size of the circular vertices of the blob.  This method must be called before the body is added to the world.
   *
   * @param size  size of the circular vertices of the blob
   */
  public void setVertexSize(float size){
    m_vertexSize = Fisica.screenToWorld(size);
  }

  /**
   * Returns vertices of the blob.
   *
   * @return list of vertex bodies
   */
  public ArrayList getVertexBodies() {
    return m_vertexBodies;
  }       

  /**
   * Sets the frequency of the springs used to maintain the volume defined by the vertices constant.
   *
   * @param frequency  the frequency of the springs of the constant volume joint
   */
  public void setFrequency(float frequency) {
    if ( m_joint != null ) {
      m_joint.setFrequency(frequency);
    }

    m_frequency = frequency;
  }

  /**
   * Sets the damping of the springs used to maintain the volume defined by the vertices constant.
   *
   * @param damping  the damping of the springs of the constant volume joint
   */
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
  
  public void attachImage( PImage img ) {
    super.attachImage(img);

    if (m_joint != null) {
      m_joint.updateStyle(this);
    }
  }

  public void dettachImage() {
    super.dettachImage();

    if (m_joint != null) {
      m_joint.updateStyle(this);
    }
  }
}
