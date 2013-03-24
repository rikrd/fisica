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

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

import java.text.DecimalFormat;

import processing.core.*;

/**
 * Represents a body in the world.
 *
 * A body is an object which may collide and react to forces in the world.  The bodies have many properties such as density, velocity, position, etc... with which we can control their behavior.  This class cannot be be instantiated, instead use one of the derived classes.
 *
 */
public abstract class FBody extends FDrawable {
  // Body creation settings
  protected float m_density = 1.0f;
  protected float m_restitution = 0.1f;
  protected float m_friction = 0.1f;
  protected boolean m_bullet = false;
  protected boolean m_sensor = false;
  protected boolean m_static = false;
  protected float m_linearDamping = 0.5f;
  protected float m_angularDamping = 0.5f;
  protected boolean m_rotatable = true;
  protected boolean m_allowSleep = true;

  protected boolean m_isSleeping = false;
  protected int m_groupIndex = 0;
  protected int m_filterBits = 0xffff;
  protected int m_categoryBits = 0x0001;

  protected Vec2 m_linearVelocity = new Vec2(0.0f, 0.0f);
  protected float m_angularVelocity = 0.0f;
  protected Vec2 m_force = new Vec2(0.0f, 0.0f);
  protected float m_torque = 0.0f;
  protected Vec2 m_position = new Vec2(0.0f, 0.0f);
  protected float m_angle = 0.0f;

  protected String m_name;

  protected Body m_body;
  protected FWorld m_world;

  protected FBody m_parent;

  protected boolean m_grabbable = true;

  protected void processBody(Body bd, ShapeDef sd){
    bd.createShape(sd);
  }

  /**
   * WARNING: This method is internal only and may change someday.  If you are using this method please contact the developer since there should be a better way or we may add something to the library.
   *
   * @return the internal JBox2D body
   *
   */
  public Body getBox2dBody() {
    return m_body;
  }

  /**
   * Get the group to which this body belongs.  Groups allow to select the bodies that may collide together or with others.  If the group index is negative then they will not collide with each other but they will collide with all the bodies of the other groups.
   *
   * @return the index of the group
   *
   */
  public int getGroupIndex() {
    return m_groupIndex;
  }

  public void addToWorld(FWorld world) {
    BodyDef bd = new BodyDef();
    bd.isBullet = m_bullet;

    m_world = world;
    m_body = world.createBody(bd);

    ShapeDef sd = getProcessedShapeDef();
    if (sd != null) {
        processBody(m_body, sd);
    }

    ArrayList bodies = getBodies();
    ArrayList sds = getShapeDefs();
    // HACK: fix the compounds of polygon bodies for now, this should change with the new version of jBox2d
    if (sds.size() != bodies.size()) {
        for (int i=0; i<sds.size(); i++) {
            sd = (ShapeDef)(sds.get(i));
            if (sd != null) {
                processBody(m_body, sd);
            }
        }
    } else {
        for (int i=0; i<sds.size(); i++) {
            FBody b = (FBody)(bodies.get(i));
            sd = (ShapeDef)(sds.get(i));
            if (sd != null) {
                b.processBody(m_body, sd);
            }
        }    
    }

    m_body.m_userData = this;
    m_body.setXForm(m_position, m_angle);
    m_body.setLinearVelocity(m_linearVelocity);
    m_body.setAngularVelocity(m_angularVelocity);

    m_body.m_linearDamping = m_linearDamping;
    m_body.m_angularDamping = m_angularDamping;

    if (m_rotatable) {
      m_body.m_flags &= ~m_body.e_fixedRotationFlag;
    }else{
      m_body.m_flags |= m_body.e_fixedRotationFlag;
    }
    
    if (m_allowSleep) {
      m_body.m_flags |= m_body.e_allowSleepFlag;
    }else{
      m_body.m_flags &= ~m_body.e_allowSleepFlag;
    }

    m_body.setBullet(m_bullet);

    m_body.applyForce(m_force, m_body.getWorldCenter());
    m_body.applyTorque(m_torque);

    m_body.m_type = m_static ? m_body.e_staticType : m_body.e_dynamicType;
    updateMass();

  }

  public void setState(FBody b) {
    if (b == null || b.m_body == null) {
      return;
    }

    // save the properties in case we must add the body again
    m_linearVelocity = b.m_body.getLinearVelocity();
    m_angularVelocity = b.m_body.getAngularVelocity();
    m_position = b.m_body.getMemberXForm().position;
    m_angle = b.m_body.getAngle();    

    m_force = b.m_body.m_force;
    m_torque = b.m_body.m_torque;
  }  

  public void setStateFromWorld() {
    if (m_body == null) {
      return;
    }

    // save the properties in case we must add the body again
    m_linearVelocity = m_body.getLinearVelocity();
    m_angularVelocity = m_body.getAngularVelocity();
    m_position = m_body.getMemberXForm().position;
    m_angle = m_body.getAngle();    

    m_force = m_body.m_force;
    m_torque = m_body.m_torque;
  }

  public void recreateInWorld() {
    if (m_body == null) return;
    
    this.setStateFromWorld();
    m_world.remove(this);
    m_world.add(this);
  }

  public void removeFromWorld() {
    if (m_body == null) return;

    m_world.destroyBody(this.m_body);

    m_body = null;
    m_world = null;
  }

  protected ShapeDef getShapeDef() {
    return null;
  }

  protected ShapeDef getTransformedShapeDef() {
    return getShapeDef();
  }

  protected ShapeDef getProcessedShapeDef() {
    ShapeDef sd = getShapeDef();
    if (sd != null) {
        sd.isSensor = m_sensor;
        sd.filter.groupIndex = m_groupIndex;
        sd.filter.maskBits = m_filterBits;
        sd.filter.categoryBits = m_categoryBits;
    }
    return sd;
  }

  protected ArrayList getShapeDefs() {
    return new ArrayList();
  }

  protected ArrayList getBodies() {
    return new ArrayList();
  }

  protected ShapeDef processShapeDef(ShapeDef sd) {
    if (sd != null) {
        sd.isSensor = m_sensor;
        sd.filter.groupIndex = m_groupIndex;
        sd.filter.maskBits = m_filterBits;
        sd.filter.categoryBits = m_categoryBits;
    }
    return sd;
  }
  
  protected void preDraw(PGraphics applet) {
    applet.pushStyle();
    applet.pushMatrix();

    applyMatrix(applet);
    applet.ellipseMode(PConstants.CENTER);
    applet.rectMode(PConstants.CENTER);
    appletFillStroke(applet);
  }

  protected void postDraw(PGraphics applet) {
    applet.popMatrix();
    applet.popStyle();
  }

  protected void preDrawDebug(PGraphics applet) {
    applet.pushStyle();
    applet.pushMatrix();

    applyMatrix(applet);
    applet.ellipseMode(PConstants.CENTER);
    applet.rectMode(PConstants.CENTER);
    
    applet.strokeWeight(1);
    
    if (m_body != null) {
        applet.fill(0, 200, 0, 50);
        applet.stroke(0, 200, 0, 150);
    }
        
    if (isSleeping()) {
        applet.fill(200, 0, 0, 50);
        applet.stroke(200, 0, 0, 150);            
    }
    
    if (isStatic()) {
        applet.fill(0, 0, 200, 50);
        applet.stroke(0, 0, 200, 150);
    } 

    if (isSensor()) {
        applet.noStroke();
    }
  }

  protected void postDrawDebug(PGraphics applet) {
    if (m_body != null) {
        applet.fill(0, 200, 0, 150);
    }
    
    if (isSleeping()) {
        applet.fill(200, 0, 0, 150);
    }
    
    if (isStatic()) {
        applet.fill(0, 0, 200, 150);
    }
        
    // Draw the origin (the point 0, 0) of the body
    applet.line(-3, 0, 3, 0);
    applet.line(0, -3, 0, 3);
        
    applet.popMatrix();

    if (getBox2dBody() != null) {
        // Draw the AABB
        applet.pushStyle();
        applet.stroke(120, 40);
        applet.noFill();
        applet.rectMode(applet.CORNERS);
        
        AABB aabb = getAABB();
        Vec2 lower = Fisica.worldToScreen(aabb.lowerBound);
        Vec2 upper = Fisica.worldToScreen(aabb.upperBound);
        applet.rect(lower.x, lower.y, upper.x, upper.y);
        applet.popStyle();

        
        applet.pushMatrix();

        Vec2 cent = Fisica.worldToScreen(getBox2dBody().getWorldCenter());
        applet.translate(cent.x, cent.y);
        
        // Draw the mass centroid of the body
        applet.pushStyle();
        applet.noStroke();
        applet.rect(0, 0, 3, 3);
        applet.popStyle();

        applet.popMatrix();

        
        // Draw the infobox (mass, dimensions)
        String infobox = "";
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        
        // Dimensions
        AABB bb = getBB();
        Vec2 dim = new Vec2(bb.upperBound);
        dim = dim.sub(bb.lowerBound);
        
        float width = dim.x;
        float height = dim.y;
        
        infobox += "w: "; 
        if ( width <= 0.001 ) {
            infobox += df.format(width*100.0) + "cm";
        } else {
            infobox += df.format(width) + "m";
        }
        infobox += "\n";
        
        infobox += "h: ";
        if ( height <= 0.001 ) {
            infobox += df.format(height*100.0) + "cm";
        } else {
            infobox += df.format(height) + "m";
        }
        infobox += "\n";

        if (!isStatic()) {
            // Mass
            float m = getMass();
            if ( m <= 1000 ) {
                infobox += "m: " + df.format(getMass()) + "g\n";
            } else {
                infobox += "m: " + df.format(getMass() / 1000.0) + "Kg\n";    
            }
        }
        
        applet.text(infobox, upper.x+4, lower.y-4);
    }
    
    applet.popStyle();
  }

  
  protected AABB getAABB() {
    AABB result = new AABB();
    boolean first = true;
    Body b = getBox2dBody();
    
    if (b == null) {
        return result;
    }
    
    AABB temp = new AABB();
    XForm tempXForm = b.getXForm();
    if (b != null) {
        Shape ss = b.getShapeList();
        
        while (ss != null) {        
            ss.computeAABB(temp, tempXForm);
            
            if (first) {
                result = new AABB(temp);
                first = false;
            } else {
                result = new AABB(Vec2.min(result.lowerBound, temp.lowerBound), Vec2.max(result.upperBound, temp.upperBound));
            }
            
            ss = ss.getNext();
        }
    }
    
    return result;
  }
  
  protected AABB getBB() {
    AABB result = new AABB();
    boolean first = true;
    Body b = getBox2dBody();

    if (b == null) {
        return result;
    }

    AABB temp = new AABB();
    XForm tempXForm = b.getXForm();
    tempXForm.setIdentity();
    if (b != null) {
        Shape ss = b.getShapeList();
        
        while (ss != null) {        
            ss.computeAABB(temp, tempXForm);
            
            if (first) {
                result = new AABB(temp);
                first = false;
            } else {
                result = new AABB(Vec2.min(result.lowerBound, temp.lowerBound), Vec2.max(result.upperBound, temp.upperBound));
            }
            
            ss = ss.getNext();
        }
    }
    
    return result;
  }
  
  protected void applyMatrix(PGraphics applet){
    applet.translate(getX(), getY());
    applet.rotate(getRotation());
  }

  /**
   * Control the group to which this body belongs.  Groups allow to select the bodies that may collide together or with others.  If the group index is negative then they will not collide with each other but they will collide with all the bodies of the other groups.
   *
   * @param index  the index of the group
   *
   */
  public void setGroupIndex(int index) {
    m_groupIndex = index;

    this.recreateInWorld();
  }

  public void setFilterBits(int mask) {
    m_filterBits = mask;

    this.recreateInWorld();
  }

  public void setCategoryBits(int mask) {
    m_categoryBits = mask;

    this.recreateInWorld();
  }

  public int getCategoryBits() {
    return m_categoryBits;
  }

  public int getFilterBits() {
    return m_filterBits;
  }

  
  public void setParent(FBody b) {
    m_parent = b;
  }

  public FBody getParent() {
    return m_parent;
  }


  /**
   * Control if this body can be grabbed by the mouse, when clicked on.  This property only has effect if the world is grabbable. If a body is grabbable, then it can be dragged around by the mouse.
   *
   * @see FWorld#setGrabbable(boolean)
   *
   * @param value if {@code true} and the world it belongs to is grabbable, then the body is grabbable by the mouse
   *
   */
  public void setGrabbable(boolean value) {
    m_grabbable = value;
  }

  /**
   * Set the force applied to the center of the body.
   *
   * @see #addForce(float,float)
   *
   * @param fx the x coordinate of the force
   * @param fy the y coordinate of the force
   *
   */
  public void setForce( float fx, float fy ){
    resetForces();
    addForce(Fisica.screenToWorld(fx), Fisica.screenToWorld(fy));
  }

  /**
   * Get the x coordinate of the force applied to the center of the body.
   *
   * @see #addForce(float,float)
   * @see #getForceY()
   *
   * @return the x coordinate of the force
   *
   */
  public float getForceX(){
    return Fisica.worldToScreen(m_force).x;
  }

  /**
   * Get the y coordinate of the force applied to the center of the body.
   *
   * @see #addForce(float,float)
   * @see #getForceX()
   *
   * @return the y coordinate of the force
   *
   */
  public float getForceY(){
    return Fisica.worldToScreen(m_force).y;
  }

  /**
   * Add a rotation force (a torque) to the body.
   *
   * @see #addForce(float,float)
   * @see #addForce(float,float,float,float)
   *
   * @param torque the value of the torque
   *
   */
  public void addTorque( float torque ){
    // TODO: check if this is what it's supposed to do
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.applyTorque(torque);
    }

    m_torque += torque;
  }

  /**
   * Aply a force on the center of the body.
   *
   * @see #addTorque(float)
   * @see #addForce(float,float,float,float)
   *
   * @param fx the x coordinate of the force
   * @param fy the y coordinate of the force
   *
   */
  public void addForce( float fx, float fy ){
    // TODO: check if this is what it's supposed to do
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.applyForce(Fisica.screenToWorld(fx, fy), m_body.getWorldCenter());
    }

    m_force.x += Fisica.screenToWorld(fx);
    m_force.y += Fisica.screenToWorld(fy);
  }
  
  /**
   * Apply an impulse on the center of the body.
   *
   * @see #addTorque(float)
   * @see #addForce(float,float,float,float)
   *
   * @param fx the x coordinate of the force
   * @param fy the y coordinate of the force
   *
   */
  public void addImpulse( float fx, float fy ){
    // TODO: check if this is what it's supposed to do
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.applyImpulse(Fisica.screenToWorld(fx, fy), m_body.getWorldCenter());
    }

    m_force.x += Fisica.screenToWorld(fx);
    m_force.y += Fisica.screenToWorld(fy);
  }


  /**
   * Apply a force to a given point of the body.  If the force is not applied on the center of the body this force might induce a rotation change.  It would be as applying a force on the center of the body and a torque.
   *
   * @see #addTorque(float)
   * @see #addImpulse(float,float,float,float)
   *
   * @param fx the x coordinate of the force
   * @param fy the y coordinate of the force
   * @param px the x position relative to the body's center, where to apply the force
   * @param py the y position relative to the body's center, where to apply the force
   *
   */
  public void addForce( float fx, float fy, float px, float py ){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.applyForce(Fisica.screenToWorld(fx, fy), m_body.getWorldCenter().add(Fisica.screenToWorld(px, py)));
    }

    // FIXME: we must calculate the force and torque this force produces
    //m_force.x += Fisica.screenToWorld(fx);
    //m_force.y += Fisica.screenToWorld(fy);
  }

  /**
   * Apply an impulse to a given point of the body.  If the impulse is not applied on the center of the body this force might induce a rotation change.  It would be as applying a force on the center of the body and a torque.
   *
   * @see #addTorque(float)
   * @see #addForce(float,float,float,float)
   *
   * @param fx the x coordinate of the force
   * @param fy the y coordinate of the force
   * @param px the x position relative to the body's center, where to apply the force
   * @param py the y position relative to the body's center, where to apply the force
   *
   */
  public void addImpulse( float fx, float fy, float px, float py ){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.applyImpulse(Fisica.screenToWorld(fx, fy), m_body.getWorldCenter().add(Fisica.screenToWorld(px, py)));
    }

    // FIXME: we must calculate the force and torque this force produces
    //m_force.x += Fisica.screenToWorld(fx);
    //m_force.y += Fisica.screenToWorld(fy);
  }

  
  /**
   * Remove all the forces that are applied to the body.
   */
  public void resetForces(){
    if (m_body != null) {
      m_body.m_force.setZero();
      m_body.m_torque = 0f;
    }

    m_force.setZero();
    m_torque = 0f;
  }

  /**
   * Returns the horizontal velocity of the body.
   *
   * @return the horizontal velocity of the body in pixels per second
   */
  public float getVelocityX(){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      return Fisica.worldToScreen(m_body.getLinearVelocity()).x;
    }

    return Fisica.worldToScreen(m_linearVelocity).x;
  }

  /**
   * Returns the vertical velocity of the body.
   *
   * @return the vertical velocity of the body in pixels per second
   */
  public float getVelocityY(){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      return Fisica.worldToScreen(m_body.getLinearVelocity()).y;
    }

    return Fisica.worldToScreen(m_linearVelocity).y;
  }

  /**
   * Set the velocity of the body.
   *
   * @param vx  the horizontal velocity of the body in pixels per second
   * @param vy  the vertical velocity of the body in pixels per second
   */
  public void setVelocity( float vx, float vy){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.setLinearVelocity( Fisica.screenToWorld(vx, vy) );
      m_body.wakeUp();
    }

    m_linearVelocity = Fisica.screenToWorld(vx, vy);
  }

  /**
   * Adjust the velocity of the body.
   *
   * @param dvx  the horizontal velocity to be added to the body in pixels per second
   * @param dvy  the vertical velocity to be added to the body in pixels per second
   */
  public void adjustVelocity( float dvx, float dvy ){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.setLinearVelocity( Fisica.screenToWorld(getVelocityX() + dvx, getVelocityY() + dvy) );
      m_body.wakeUp();
    }

    m_linearVelocity = Fisica.screenToWorld(getVelocityX() + dvx, getVelocityY() + dvy);
  }

  /**
   * Returns the horizontal position of the body.
   *
   * @return the horizontal position of the body in pixels
   * @see #getY
   * @see #setPosition(float,float)
   */
  public float getX(){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      return Fisica.worldToScreen(m_body.getMemberXForm().position).x;
    }

    return Fisica.worldToScreen(m_position).x;
  }

  /**
   * Returns the vertical position of the body.
   *
   * @return the vertical position of the body in pixels
   * @see #getX
   * @see #setPosition(float,float)
   */
  public float getY(){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      return Fisica.worldToScreen(m_body.getMemberXForm().position).y;
    }

    return Fisica.worldToScreen(m_position).y;
  }

  /**
   * Set the position of the body.
   *
   * @param x  the horizontal position of the body in pixels
   * @param y  the vertical position of the body in pixels
   */
  public void setPosition( float x, float y ){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.setXForm(Fisica.screenToWorld(x, y), m_body.getAngle());
    }

    m_position = Fisica.screenToWorld(x, y);
  }

  protected void setPosition( Vec2 position ){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.setXForm(position, m_body.getAngle());
    }

    m_position = Fisica.screenToWorld(position);
  }

  /**
   * Adjust the position of the body.
   *
   * @param dx  the horizontal position to be added to the body in pixels
   * @param dy  the vertical position to be added to the body in pixels
   */
  public void adjustPosition( float dx, float dy ){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.setXForm(Fisica.screenToWorld(getX() + dx, getY() + dy), m_body.getAngle());
    }

    m_position = Fisica.screenToWorld(getX() + dx, getY() + dy);
  }

  /**
   * Returns the rotation of the body.
   *
   * @return the rotation of the body in radians
   * @see #setRotation(float)
   */
  public float getRotation(){
    if (m_body != null) {
      return m_body.getAngle();
    }

    return m_angle;
  }

  /**
   * Set the rotation of the body.
   *
   * @param w  the rotation of the body in radians
   * @see #getRotation()
   */
  public void setRotation( float w ){
    if (m_body != null) {
      m_body.setXForm(m_body.getMemberXForm().position, w);
    }

    m_angle = w;
  }

  /**
   * Adjust the rotation of the body.
   *
   * @param dw  the rotation to be added to the body in radians
   * @see #getRotation()
   * @see #setRotation(float)
   */
  public void adjustRotation( float dw ){
    if (m_body != null) {
      m_body.setXForm(m_body.getMemberXForm().position, m_body.getAngle() + dw);
    }

    m_angle += dw;
  }

  /**
   * Deprecated. Please use isSleeping().
   *
   * @return true if the body is resting
   */
  public boolean isResting(){
    return isSleeping();
  }
  
  /**
   * Indicates whether the body is in a sleeping state.
   *
   * The sleeping state of a body is reached when it has not moved or has not received any forces nor collisions for some time.
   *
   * @return true if the body is sleeping
   * @see #wakeUp()
   * @see #setAllowSleeping(boolean)
   */
  public boolean isSleeping(){
    if (m_body != null) {
      return m_body.isSleeping();
    }

    return m_isSleeping;
  }

  /**
   * Wake up the body from a sleeping state.
   *
   * @see #isSleeping()
   * @see #setAllowSleeping(boolean)
   */
  public void wakeUp() {
    if (m_body == null) {
        return;
    }
    
    m_body.wakeUp();
  }

  /**
   * Returns the rotation velocity of the body.
   *
   * @return the rotation velocity of the body in radians per second
   * @see #setAngularVelocity(float)
   * @see #adjustAngularVelocity(float)
   */
  public float getAngularVelocity(){
    if (m_body != null) {
      return m_body.getAngularVelocity();
    }

    return m_angularVelocity;
  }

  /**
   * Set the rotation velocity of the body.
   *
   * @param w   the rotation velocity of the body in radians per second
   */
  public void setAngularVelocity( float w ){
    if (m_body != null) {
      m_body.setAngularVelocity( w );
      m_body.wakeUp();
    }

    m_angularVelocity = w;
  }

  /**
   * Adjust the rotation velocity of the body.
   *
   * @param dw   the rotation velocity to be added to the body in radians per second
   * @see #getAngularVelocity()
   * @see #setAngularVelocity(float)
   */
  public void adjustAngularVelocity( float dw ){
    if (m_body != null) {
      m_body.setAngularVelocity( m_body.getAngularVelocity() + dw );
      m_body.wakeUp();
    }

    m_angularVelocity += dw;
  }

  /**
   * Set the damping of the rotation movement of the body.  The damping constantly reduces the rotation velocity of the body.
   *
   * @param damping   the damping of the rotation movement of the body
   * @see #setDamping(float)
   */
  public void setAngularDamping( float damping ){
    if (m_body != null) {
      m_body.m_angularDamping = damping;
    }

    m_angularDamping = damping;
  }

  /**
   * Set the damping of the translation movement of the body.  The damping constantly reduces the translation velocity of the body.
   *
   * @param damping   the damping of the translation movement of the body
   * @see #setAngularDamping(float)
   */
  public void setDamping( float damping ){
    if (m_body != null) {
      m_body.m_linearDamping = damping;
    }

    m_linearDamping = damping;
  }

  /**
   * Set the name of the body.
   *
   * @param name   the name of the body
   */
  public void setName( String name ){
    m_name = name;
  }

  /**
   * Get the name of the body.
   *
   * @return name    the name of the body
   */
  public String getName( ){
    return m_name;
  }

  /**
   * Set the density of the body.  The density will determine the total mass of the body and thus it's behavior with respect to collisions, bounces, inertia, joints,...  When the density is set, the mass of the body is recalculated automatically given it's area.
   *
   * Note that a density of 0.0 corresponds to a mass of 0.0 independently of the area and the body will be considered static.
   *
   * @param density   the density of the body
   */
  public void setDensity( float density ){
    m_density = density;
    updateMass();
  }

  /**
   * Get the density of the body.  The density determines the total mass of the body and thus it's behavior with respect to collisions, bounces, inertia, joints,...
   *
   * Note that a density of 0.0 corresponds to a mass of 0.0 independently of the area and the body will be considered static.
   *
   * @return density   the density of the body
   */
  public float getDensity( ){
    return m_density;
  }

  protected void updateMass() {
    if (m_body == null) {
      return;
    }

    // Set the density of shapes
    for (Shape s = m_body.getShapeList(); s != null; s = s.m_next) {
      s.m_density = m_static ? 0.0f : m_density;
    }

    // Recalculate the body's mass
    m_body.setMassFromShapes();
  }

  /**
   * Set whether the body is a sensor.  Sensor bodies act as normal bodies in the sense that they notify about contacts, however they do not collide with other bodies (they act like ghost bodies).
   *
   * @param value   if {@code true} the body will be a sensor.  It will not collide when enters contact with other bodies
   */
  public void setSensor( boolean value ){
    if( m_body != null ) {
      // Set the density of shapes
      for (Shape s = m_body.getShapeList(); s != null; s = s.m_next) {
        s.m_isSensor = value;
      }
    }

    m_sensor = value;
  }

  /**
   * Returns whether the body is a sensor.  Sensor bodies act as normal bodies in the sense that they notify about contacts, however they do not collide with other bodies (they act like ghost bodies).
   *
   * @return   if {@code true} the body is a sensor.  It will not collide when enters contact with other bodies
   */
  public boolean isSensor(){
    return m_sensor;
  }

  /**
   * Set whether the body is static.  Static bodies do not move or rotate, unless done manually using {@link #setPosition(float,float) setPosition} or {@link #setRotation(float) setRotation}.
   *
   * @param value   if {@code true} the body will be static
   */
  public void setStaticBody( boolean value ) {
    setStatic(value);
  }

  /**
   * Set whether the body is static.  Static bodies do not move or rotate, unless done manually using {@link #setPosition(float,float) setPosition} or {@link #setRotation(float) setRotation}.
   *
   * @param value   if {@code true} the body will be static
   */
  public void setStatic( boolean value ){
    if( m_body != null ) {
      m_body.m_type = value ? m_body.e_staticType : m_body.e_dynamicType;
    }

    m_static = value;

    updateMass();
  }

  /**
   * Returns the mass of the body.  Static bodies or bodies not added to the world return 0.
   *
   * @return   the mass of the body or 0 if static
   */
  public float getMass(){
    if (m_body != null) {
      return m_body.getMass();
    }

    return 0.0f;
  }

  /**
   * Returns whether the body is static.  Static bodies do not move or rotate, unless done manually using {@link #setPosition(float,float) setPosition} or {@link #setRotation(float) setRotation}.
   *
   * @return   if {@code true} the body is static
   */
  public boolean isStatic(){
    if (m_body != null) {
      return m_body.isStatic();
    }

    return m_static;
  }

  /**
   * Set whether the body is a bullet.  Bullet bodies are computationally more expensive but more accurate in their movement.  Use this only with fast objects.
   *
   * @param value   if {@code true} the body will be a bullet
   */
  public void setBullet( boolean value ){
    if( m_body != null ) {
      m_body.setBullet(value);
    }

    m_bullet = value;
  }

  /**
   * Set the restitution of the body.  The restitution determines the ratio of the reaction force normal to a contact, when the body collides with another body.  Basically it can be seen as a coefficient that will control the strength with which the body bounces back from a collision.  The resititution of a contact of two bodies in a collision is calculated as the maximum of the restitution values of the 2 bodies involved.
   *
   * @param restitution   a positive value.  A value of 0 means no bounce after a collision, and a value of 1 means bounce with it's full speed from a collision
   */
  public void setRestitution( float restitution ){
    if ( m_body != null ) {
      for (Shape s = m_body.getShapeList(); s != null; s = s.m_next) {
        s.setRestitution( restitution );
      }
    }

    m_restitution = restitution;
  }

  /**
   * Set the friction of the body.  The friction determines the ratio of the reaction force tangent to a contact, when the body collides with another body.  Basically it can be seen as a coefficient that will control how the body gets slown down when the body slides against another body.  The friction of a contact of two bodies in a collision is calculated from the friction values of the 2 bodies involved.
   *
   * @param friction   a positive value.  A value of 0 means no friction and thus the body will not be slown down if no other forces are applied
   */
  public void setFriction( float friction ){
    if ( m_body != null ) {
      for (Shape s = m_body.getShapeList(); s != null; s = s.m_next) {
        s.setFriction( friction );
      }
    }

    m_friction = friction;
  }

  /**
   * Set whether the body can rotate.
   *
   * @param rotatable   if {@code true} the body will not rotate
   */
  public void setRotatable( boolean rotatable ){
    if ( m_body != null ) {
      // TODO: check this
      if (rotatable) {
        m_body.m_flags &= ~m_body.e_fixedRotationFlag;
      }else{
        m_body.m_flags |= m_body.e_fixedRotationFlag;
      }
    }

    m_rotatable = rotatable;
  }

  /**
   * Set whether the body can sleep.
   *
   * @param allowSleep if {@code true} the body will be able to sleep
   */
  public void setAllowSleeping( boolean allowSleep ){
    if ( m_body != null ) {
      // TODO: check this
      if (allowSleep) {
        m_body.m_flags |= m_body.e_allowSleepFlag;
      }else{
        m_body.m_flags &= ~m_body.e_allowSleepFlag;
        m_body.wakeUp();
      }
    }

    m_allowSleep = allowSleep;
  }
  
  /**
   * Return a list of bodies currently touching the body.
   *
   * @return   list of bodies (ArrayList of FBody) touching the body
   */
  public ArrayList getTouching() {
    ArrayList result = new ArrayList();
    
    if (m_world == null) {
      return result;
    }

    Collection contacts = m_world.m_contacts.values();
    Iterator iter = contacts.iterator();
    while (iter.hasNext()) {
      FContact contact = (FContact)iter.next();
      if (this == contact.getBody1()) {
        result.add(contact.getBody2());
      } else if (this == contact.getBody2()) {
        result.add(contact.getBody1());
      }
    }

    return result;
  }

  /**
   * Return a list of contacts currently involving the body.
   *
   * @return   list of contacts (ArrayList of FContact) touching the body
   */
  public ArrayList getContacts() {
    ArrayList result = new ArrayList();

    if (m_world == null) {
      return result;
    }
    
    Collection contacts = m_world.m_contacts.values();    
    Iterator iter = contacts.iterator();
    while (iter.hasNext()) {
      FContact contact = (FContact)iter.next();
      
      if (this == contact.getBody1() ||
          this == contact.getBody2()) {
        result.add(contact);
      }
    }

    return result;
  }

  /**
   * Returns a list with all the joints with a connection to the body
   *
   * @return    an ArrayList (of FJoint) connected to the body
   */
  public ArrayList getJoints() {
    ArrayList result = new ArrayList();
      
    if ( m_body != null ) {
      for (JointEdge jn = m_body.getJointList(); jn != null; jn = jn.next) {
        FJoint j = (FJoint)(jn.joint.m_userData);
        if (j != null) {
          result.add(j);
        }
      }
    }

    return result;
  }

  /**
   * Returns true if the body is joint to the body passed as argument
   *
   * @param other  the other body
   * @return     if {@code true} the body is connected to other
   */
  public boolean isConnected(FBody other) {
    if ( m_body != null ) {
      for (JointEdge jn = m_body.getJointList(); jn != null; jn = jn.next) {
        FBody b = (FBody)(jn.other.m_userData);
        if (jn.other.m_userData == other) {
          return (jn.joint.m_collideConnected == false);
        }
      }
    }

    return false;
  }

  /**
   * Return whether the body is currently touching the body passed as argument.
   *
   * @param b  the body for which we want to know if there is contact
   * @return   if {@code true} the body is touching b
   */
  public boolean isTouchingBody(FBody b){
    return getTouching().contains(b);
  }

  protected Vec2 getLocalWorldPoint(Vec2 p) {
    if (m_body != null) {
      Vec2 v = m_body.getLocalPoint(p);
      return v;
    }

    XForm xf = new XForm();
    xf.position.set(Fisica.screenToWorld(getX(), getY()));
    xf.R.set(getRotation());
    return XForm.mulTrans(xf, p);
  }
}
