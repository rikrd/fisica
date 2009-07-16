package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;

import processing.core.*;

public class FBody extends Body {
  public boolean m_drawable = true;
  
  public boolean m_fill;
  public int m_fillColor;
  public boolean m_stroke;
  public int m_strokeColor;
  public float m_strokeWeight;

  public PImage m_image;
  public PImage m_mask;
  
  public PApplet m_parent;
  public Body m_body;
  public BodyDef m_bodydef;
  
  public FBody() {
    m_body = null;
    m_bodydef = new BodyDef();
  }

  /*
  public float getHeight(){ 
    // only for FBox
  }
  
  public float getWidth(){
    // only for FBox
  }
  
  public float setSize(){
    // only for FBox
  }
  
  public float getSize(){
    // only for FCircle
  }
  
  public float setSize(){
    // only for FCircle 
  }
  */
  
  public void draw(PApplet applet) {
    // Don't draw anything, each subclass will draw itself
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
  
  public PImage getImageAlpha() {
    return m_mask;
  }
  
  public void setImageAlpha(PImage mask) {
    m_mask = mask;
  }

  public float getVelocityX(){
    // TODO: w2s (world 2 screen)
    if (m_body == null) return 0.0f;

    return m_body.getLinearVelocity().x;
  }

  public float getVelocityY(){
    // TODO: w2s (world 2 screen)
    if (m_body == null) return 0.0f;

    return m_body.getLinearVelocity().y;
  }

  public void setVelocity( float vx, float vy){
    // TODO: w2s (world 2 screen)
    if (m_body == null) return;
    
    m_body.setLinearVelocity( new Vec2(vx, vy) );
  }
  public void adjustVelocity( float dvx, float dvy ){
    // TODO: w2s (world 2 screen)
    if (m_body == null) return;

    m_body.setLinearVelocity( new Vec2(m_body.getLinearVelocity().x + dvx, m_body.getLinearVelocity().y + dvy) );  
  }

  public float getX(){
    // TODO: w2s (world 2 screen)
    if (m_body == null) return m_bodydef.position.x;
    
    return m_body.getXForm().position.x;
  }
  
  public float getY(){
    // TODO: w2s (world 2 screen)
    if (m_body == null) return m_bodydef.position.x;
    
    return m_body.getXForm().position.y;
  }

  public void setPosition( float x, float y ){
    // TODO: w2s (world 2 screen)
    if (m_body == null) return;
    
    m_body.setXForm(new Vec2(x, y), m_body.getAngle());
  }

  public void adjustPosition( float dx, float dy ){
    // TODO: w2s (world 2 screen)
    if (m_body == null) {
      return;
    }
    
    m_body.setXForm(new Vec2(m_body.getXForm().position.x + dx, m_body.getXForm().position.y + dy), m_body.getAngle());
  }

  public float getRotation(){
    if (m_body == null) {
      return m_bodydef.angle;
    }
    
    return m_body.getAngle();
  }
  
  public void setRotation( float w ){
    if (m_body == null) {
      m_bodydef.angle = w;
      return;
    }

    m_body.setXForm(getXForm().position, w);
  }
  
  public void adjustRotation( float dw ){
    if (m_body == null) {
      m_bodydef.angle += dw;
      return;
    }
    
    m_body.setXForm(m_body.getXForm().position, m_body.getAngle() + dw);
  }

  public boolean isStatic(){
    if (m_body == null) return false;
    
    return m_body.isStatic();
  }
  
  public void setStatic( boolean isStatic ){
    if (m_body == null) {
      return;
    }

    m_body.m_type = e_staticType;
  }

  public boolean isResting(){
    if (m_body == null) {
      return m_bodydef.isSleeping;
    }
    
    return m_body.isSleeping();
  }
  
  //public boolean isTouchingBody( FBody other ){}

  public float getAngularVelocity(){
    if (m_body == null) {
      return 0.0f;
    }

    return m_body.getAngularVelocity();
  }
  
  public void setAngularVelocity( float w ){
    if (m_body == null) {
      return;
    }
    
    m_body.setAngularVelocity( w );
  }
  public void adjustAngularVelocity( float w ){
    if (m_body == null) {
      return;
    }

    m_body.setAngularVelocity( m_body.getAngularVelocity() + w );
  }

  public void setAngularDamping( float damping ){
    if (m_body == null) {
      m_bodydef.angularDamping = damping;
      return;
    }

    m_angularDamping = damping;
  }

  public void setDamping( float damping ){
    if (m_body == null) {
      m_bodydef.linearDamping = damping;
      return;
    }

    m_linearDamping = damping;
  }
  
  public void setDrawable( boolean drawable ){
    m_drawable = drawable;
  }

  public void setDensity( float density ){
    if ( m_body == null ) {
      return;
    }
    
    // Set the density of shapes
    for (Shape s = m_body.getShapeList(); s != null; s = s.m_next) {
      s.m_density = density;
    }
    
    // Recalculate the body's mass
    setMassFromShapes();
  }

  public void setRestitution( float restitution ){
    if ( m_body == null ) {
      return;
    }
    
    for (Shape s = m_body.getShapeList(); s != null; s = s.m_next) {
      s.setRestitution( restitution );
    }
  }

  public void setFriction( float friction ){
    if ( m_body == null ) {
      return;
    }
    
    for (Shape s = m_body.getShapeList(); s != null; s = s.m_next) {
      s.setFriction( friction );
    }
  }
  
  public void setRotatable( boolean rotatable ){
    if ( m_body == null ) {
      return;
    }
    
    // TODO: check this
    if (rotatable) {
      m_body.m_flags &= ~e_fixedRotationFlag;
    }else{
      m_body.m_flags |= e_fixedRotationFlag;
    }
  }
  
  public void noFill() {
    m_fill = false;
  }

  public void fill(int col) {
    m_fill = true;
    m_fillColor = col;
  }

  public void fill(float g){
    fill(m_parent.color(g));
  }

  public void fill(float g, float a){
    fill(m_parent.color(g, a));
  }

  public void fill(float r, float g, float b){
    fill(m_parent.color(r, g, b));
  }

  public void fill(float r, float g, float b, float a){
    fill(m_parent.color(r, g, b, a));
  }

  public void noStroke() {
    m_stroke = false;
  }

  public void stroke(int col) {
    m_stroke = true;
    m_strokeColor = col;
  }

  public void stroke(float g){
    stroke(m_parent.color(g));
  }

  public void stroke(float g, float a){
    stroke(m_parent.color(g, a));
  }

  public void stroke(float r, float g, float b){
    stroke(m_parent.color(r, g, b));
  }

  public void stroke(float r, float g, float b, float a){
    stroke(m_parent.color(r, g, b, a));
  }
  
  public void strokeWeight(float weight) {
    m_strokeWeight = weight;
  }
}
