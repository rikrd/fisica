package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;

import processing.core.*;

public class FBody {
  public boolean m_drawable = true;
  
  // Body creation settings
  public float m_density = 1.0f;
  public float m_restitution = 0.5f;
  public float m_friction = 0.5f;
  public boolean m_bullet = false;
  public boolean m_sensor = false;
  public boolean m_static = false;
  public float m_linearDamping = 0.0f;
  public float m_angularDamping = 0.0f;
  public boolean m_rotatable = true;

  public boolean m_isSleeping = false;

  public Vec2 m_linearVelocity = new Vec2(0.0f, 0.0f);
  public float m_angularVelocity = 0.0f;
  public Vec2 m_force = new Vec2(0.0f, 0.0f);
  public float m_torque = 0.0f;
  public Vec2 m_position = new Vec2(0.0f, 0.0f);
  public float m_angle = 0.0f;
    
  public boolean m_fill = true;
  public int m_fillColor = 0xFFFFFFFF;
  public boolean m_stroke = true;
  public int m_strokeColor = 0xFFFFFFFF;
  public float m_strokeWeight = 1.0f;

  public PImage m_image = null;
  public float m_imageAlpha = 255.0f;
  public PImage m_mask = null;
  
  public Body m_body;
  
  public FBody() {
    m_body = null;
  }

  public void addToWorld(FWorld world) {
    BodyDef bd = new BodyDef();
    bd.isBullet = m_bullet;
    
    m_body = world.createBody(bd);
    m_body.createShape(getShapeDef());
    m_body.m_userData = this;
    m_body.setXForm(m_position, m_angle);
    m_body.setLinearVelocity(m_linearVelocity);
    m_body.setAngularVelocity(m_angularVelocity);

    m_body.setBullet(m_bullet);
    m_body.applyForce(m_force, m_body.getWorldCenter());
    //m_body.applyTorque(m_torque, m_body.getWorldCenter());
   
    updateMass();
    m_body.m_type = m_static ? m_body.e_staticType : m_body.e_dynamicType;

  }

  protected ShapeDef getShapeDef() {
    return new ShapeDef();
  }

  protected void appletStroke( PApplet applet, int argb ){
    final int a = (argb >> 24) & 0xFF;
    final int r = (argb >> 16) & 0xFF;  // Faster way of getting red(argb)
    final int g = (argb >> 8) & 0xFF;   // Faster way of getting green(argb)
    final int b = argb & 0xFF;          // Faster way of getting blue(argb)
    
    applet.stroke(r, g, b, a);
  }

  protected void appletFill( PApplet applet, int argb ){
    final int a = (argb >> 24) & 0xFF;
    final int r = (argb >> 16) & 0xFF;  // Faster way of getting red(argb)
    final int g = (argb >> 8) & 0xFF;   // Faster way of getting green(argb)
    final int b = argb & 0xFF;          // Faster way of getting blue(argb)
    
    applet.fill(r, g, b, a);
  }

  protected void appletFillStroke( PApplet applet ) {
    if (m_fill) {
      appletFill(applet, m_fillColor);
    } else {
      applet.noFill();
    }

    if (m_stroke) {
      appletStroke(applet, m_strokeColor);
    } else {
      applet.noStroke();
    }
    applet.strokeWeight(m_strokeWeight);
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

  protected void drawImage(PApplet applet) {
    applet.tint(255, 255, 255, m_imageAlpha);
    applet.image(m_image, 0-m_image.width/2, 0-m_image.height/2);
    applet.tint(255, 255, 255, 255);
  }
  
  public void draw(PApplet applet) {
    // Don't draw anything, each subclass will draw itself
  }

  public void setForce( float fx, float fy ){
    resetForces();
    addForce(fx, fy);
  }
  
  public void addForce( float fx, float fy ){
    // TODO: check if this is what it's supposed to do
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.applyForce(new Vec2(fx, fy), m_body.getWorldCenter());
    }

    m_force.x += fx;
    m_force.y += fy;
  }

  public void addForce( float fx, float fy, float px, float py ){
    // TODO: check if this is what it's supposed to do
    // TODO: w2s (world 2 screen)
    if (m_body == null) return;
    
    m_body.applyForce(new Vec2(fx, fy), new Vec2(px, py));
  }
  
  public void resetForces(){
    m_force.setZero();
    m_torque = 0f;

    if (m_body != null) {
      m_body.m_force.setZero();
      m_body.m_torque = 0f;
    }
  }

  public void applyMatrix(PApplet applet){
    applet.translate(getX(), getY());
    applet.rotate(getRotation());
  }
  
  public void attachImage( PImage img ) {
    m_image = img;
  }
  
  public void dettachImage() {
    m_image = null;
  }
  
  public float getImageAlpha() {
    return m_imageAlpha;
  }
  
  public void setImageAlpha(float alpha) {
    m_imageAlpha = alpha;
  }

  public float getVelocityX(){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      return m_body.getLinearVelocity().x;
    }

    return m_linearVelocity.x;
  }

  public float getVelocityY(){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      return m_body.getLinearVelocity().y;
    }

    return m_linearVelocity.y;
  }

  public void setVelocity( float vx, float vy){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.setLinearVelocity( new Vec2(vx, vy) );
      m_body.wakeUp();
    }    

    m_linearVelocity = new Vec2(vx, vy);
  }
  public void adjustVelocity( float dvx, float dvy ){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.setLinearVelocity( new Vec2(m_body.getLinearVelocity().x + dvx, m_body.getLinearVelocity().y + dvy) );  
      m_body.wakeUp();
    }

    m_linearVelocity = new Vec2(m_linearVelocity.x + dvx, m_linearVelocity.y + dvy);
  }

  public float getX(){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      return m_body.getMemberXForm().position.x;
    }
    
    return m_position.x;
  }
  
  public float getY(){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      return m_body.getMemberXForm().position.y;
    }
    
    return m_position.y;
  }

  public void setPosition( float x, float y ){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.setXForm(new Vec2(x, y), m_body.getAngle());
    }

    m_position = new Vec2(x, y);
  }

  public void adjustPosition( float dx, float dy ){
    // TODO: w2s (world 2 screen)
    if (m_body != null) {
      m_body.setXForm(new Vec2(m_body.getMemberXForm().position.x + dx, m_body.getMemberXForm().position.y + dy), m_body.getAngle());
    }
    
    m_position = new Vec2(m_position.x + dx, m_position.y + dy);
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
  
  public void setDrawable( boolean drawable ){
    m_drawable = drawable;
  }
  
  public boolean isDrawable(){
    return m_drawable;
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
  
  public void setNoFill() {
    m_fill = false;
  }

  public void setFillColorInt(int col) {
    m_fill = true;
    m_fillColor = col;
  }

  public void setFillColor(float g){
    setFillColorInt(Fisica.parent().color(g));
  }

  public void setFillColor(float g, float a){
    setFillColorInt(Fisica.parent().color(g, a));
  }

  public void setFillColor(float r, float g, float b){
    setFillColorInt(Fisica.parent().color(r, g, b));
  }

  public void setFillColor(float r, float g, float b, float a){
    setFillColorInt(Fisica.parent().color(r, g, b, a));
  }

  public void setNoStroke() {
    m_stroke = false;
  }

  public void setStrokeColorInt(int col) {
    m_stroke = true;
    m_strokeColor = col;
  }

  public void setStrokeColor(float g){
    setStrokeColorInt(Fisica.parent().color(g));
  }

  public void setStrokeColor(float g, float a){
    setStrokeColorInt(Fisica.parent().color(g, a));
  }

  public void setStrokeColor(float r, float g, float b){
    setStrokeColorInt(Fisica.parent().color(r, g, b));
  }

  public void setStrokeColor(float r, float g, float b, float a){
    setStrokeColorInt(Fisica.parent().color(r, g, b, a));
  }
  
  public void setStrokeWeight(float weight) {
    m_strokeWeight = weight;
  }

  public boolean isTouchingBody(FBody b){
    // TODO: implement this
    return false;
  }
}
