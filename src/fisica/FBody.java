package fisica;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;

import processing.core.*;

public class FBody extends FDrawable {
  // Body creation settings
  public float m_density = 5.0f;
  public float m_restitution = 0.0f;
  public float m_friction = 0.1f;
  public boolean m_bullet = false;
  public boolean m_sensor = false;
  public boolean m_static = false;
  public float m_linearDamping = 1.0f;
  public float m_angularDamping = 1.0f;
  public boolean m_rotatable = true;

  public boolean m_isSleeping = false;

  public Vec2 m_linearVelocity = new Vec2(0.0f, 0.0f);
  public float m_angularVelocity = 0.0f;
  public Vec2 m_force = new Vec2(0.0f, 0.0f);
  public float m_torque = 0.0f;
  public Vec2 m_position = new Vec2(0.0f, 0.0f);
  public float m_angle = 0.0f;
  
  public Body m_body;
  public FWorld m_world;

  public FBody() {
    m_body = null;
  }

  public void processBody(Body bd, ShapeDef sd){
    bd.createShape(sd);
  }

  public void addToWorld(FWorld world) {
    BodyDef bd = new BodyDef();
    bd.isBullet = m_bullet;

    m_world = world;
    m_body = world.createBody(bd);

    ShapeDef sd = getShapeDef();
    processBody(m_body, sd);

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
    
    m_body.setBullet(m_bullet);
    
    m_body.applyForce(m_force, m_body.getWorldCenter());
    m_body.applyTorque(m_torque);
    
    m_body.m_type = m_static ? m_body.e_staticType : m_body.e_dynamicType;   
    updateMass();

  }

  public void removeFromWorld(FWorld world) {
    if (m_body == null) return;

    world.destroyBody(this.m_body);
  }

  protected ShapeDef getShapeDef() {
    return new ShapeDef();
  }

  protected void preDraw(PApplet applet) {
    applet.pushStyle();
    applet.pushMatrix();
    
    applyMatrix(applet);
    applet.ellipseMode(PConstants.CENTER);
    applet.rectMode(PConstants.CENTER);
    appletFillStroke(applet);
  }

  protected void postDraw(PApplet applet) {
    applet.popMatrix();
    applet.popStyle();
  }
  
  public void setForce( float fx, float fy ){
    resetForces();
    addForce(Fisica.screenToWorld(fx), Fisica.screenToWorld(fy));
  }
  
  public void addForce( float fx, float fy ){
    // TODO: check if this is what it's supposed to do
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.applyForce(Fisica.screenToWorld(fx, fy), m_body.getWorldCenter());
    }

    m_force.x += Fisica.screenToWorld(fx);
    m_force.y += Fisica.screenToWorld(fy);
  }

  public void addForce( float fx, float fy, float px, float py ){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.applyForce(Fisica.screenToWorld(fx, fy), m_body.getWorldCenter());
    }

    // FIXME: we must calculate the force and torque this force produces    
    //m_force.x += Fisica.screenToWorld(fx);
    //m_force.y += Fisica.screenToWorld(fy);
  }
  
  public void resetForces(){
    if (m_body != null) {
      m_body.m_force.setZero();
      m_body.m_torque = 0f;
    }

    m_force.setZero();
    m_torque = 0f;
  }

  public void applyMatrix(PApplet applet){
    applet.translate(getX(), getY());
    applet.rotate(getRotation());
  }
  
  public float getVelocityX(){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      return Fisica.worldToScreen(m_body.getLinearVelocity()).x;
    }

    return m_linearVelocity.x;
  }

  public float getVelocityY(){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      return Fisica.worldToScreen(m_body.getLinearVelocity()).y;
    }

    return m_linearVelocity.y;
  }

  public void setVelocity( float vx, float vy){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.setLinearVelocity( Fisica.screenToWorld(vx, vy) );
      m_body.wakeUp();
    }    

    m_linearVelocity = Fisica.screenToWorld(vx, vy);
  }
  public void adjustVelocity( float dvx, float dvy ){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.setLinearVelocity( Fisica.screenToWorld(getVelocityX() + dvx, getVelocityY() + dvy) );  
      m_body.wakeUp();
    }

    m_linearVelocity = Fisica.screenToWorld(getVelocityX() + dvx, getVelocityY() + dvy);
  }

  public float getX(){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      return Fisica.worldToScreen(m_body.getMemberXForm().position).x;
    }
    
    return Fisica.worldToScreen(m_position).x;
  }
  
  public float getY(){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      return Fisica.worldToScreen(m_body.getMemberXForm().position).y;
    }
    
    return Fisica.worldToScreen(m_position).y;
  }

  public void setPosition( float x, float y ){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.setXForm(Fisica.screenToWorld(x, y), m_body.getAngle());
    }

    m_position = Fisica.screenToWorld(x, y);
  }

  public void setPosition( Vec2 position ){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.setXForm(position, m_body.getAngle());
    }

    m_position = Fisica.screenToWorld(position);
  }

  public void adjustPosition( float dx, float dy ){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.setXForm(Fisica.screenToWorld(getX() + dx, getY() + dy), m_body.getAngle());
    }
    
    m_position = Fisica.screenToWorld(getX() + dx, getY() + dy);
  }

  public float getRotation(){
    if (m_body != null) {
      return m_body.getAngle();
    }
    
    return m_angle;    
  }
  
  public void setRotation( float w ){
    if (m_body != null) {
      m_body.setXForm(m_body.getMemberXForm().position, w);
    }
    
    m_angle = w;
  }
  
  public void adjustRotation( float dw ){
    if (m_body != null) {
      m_body.setXForm(m_body.getMemberXForm().position, m_body.getAngle() + dw);
    }
    
    m_angle += dw;
  }

  public boolean isResting(){
    if (m_body != null) {
      return m_body.isSleeping();
    }

    return m_isSleeping;
  }
  
  //public boolean isTouchingBody( FBody other ){}

  public float getAngularVelocity(){
    if (m_body != null) {
      return m_body.getAngularVelocity();
    }

    return m_angularVelocity;
  }
  
  public void setAngularVelocity( float w ){
    if (m_body != null) {
      m_body.setAngularVelocity( w );
      m_body.wakeUp();
    }
    
    m_angularVelocity = w;
  }

  public void adjustAngularVelocity( float dw ){
    if (m_body != null) {
      m_body.setAngularVelocity( m_body.getAngularVelocity() + dw );
      m_body.wakeUp();
    }

    m_angularVelocity += dw;
  }

  public void setAngularDamping( float damping ){
    if (m_body != null) {
      m_body.m_angularDamping = damping;
    }

    m_angularDamping = damping;
  }

  public void setDamping( float damping ){
    if (m_body != null) {
      m_body.m_linearDamping = damping;
    }

    m_linearDamping = damping;
  }

  public void setDensity( float density ){
    m_density = density;
    updateMass();
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

  public void setStaticBody( boolean value ) {
    if( m_body != null ) {
      m_body.m_type = value ? m_body.e_staticType : m_body.e_dynamicType;
    }

    m_static = value;

    updateMass();
  }

  public void setStatic( boolean value ){
    if( m_body != null ) {
      m_body.m_type = value ? m_body.e_staticType : m_body.e_dynamicType;
    }

    m_static = value;
    
    updateMass();
  }

  public boolean isStatic(){
    if (m_body != null) {
      return m_body.isStatic();
    }
    
    return m_static;
  }  

  public void setBullet( boolean value ){
    if( m_body != null ) {
      m_body.setBullet(value);
    }

    m_bullet = value;
  }

  public void setRestitution( float restitution ){
    if ( m_body != null ) {
      for (Shape s = m_body.getShapeList(); s != null; s = s.m_next) {
        s.setRestitution( restitution );
      }
    }

    m_restitution = restitution;
  }

  public void setFriction( float friction ){
    if ( m_body != null ) {
      for (Shape s = m_body.getShapeList(); s != null; s = s.m_next) {
        s.setFriction( friction );
      }
    }

    m_friction = friction;
  }
  
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

  public ArrayList getTouching() {
    ArrayList result = new ArrayList();
    
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

  public boolean isTouchingBody(FBody b){
    return getTouching().contains(b);
  }
}
