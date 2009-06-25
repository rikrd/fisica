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

  public FBody() {
    super();
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
    super.applyForce(new Vec2(fx, fy), new Vec2(px, py));
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
    return super.getLinearVelocity().x;
  }

  public float getVelocityY(){
    // TODO: w2s (world 2 screen)
    return super.getLinearVelocity().y;
  }

  public void setVelocity( float vx, float vy){
    // TODO: w2s (world 2 screen)
    setLinearVelocity( new Vec2(vx, vy) );
  }
  public void adjustVelocity( float dvx, float dvy ){
    // TODO: w2s (world 2 screen)
    setLinearVelocity( new Vec2(super.getLinearVelocity().x + dvx, super.getLinearVelocity().y + dvy) );  
  }

  public float getX(){
    // TODO: w2s (world 2 screen)
    return getXForm().position.x;
  }
  
  public float getY(){
    // TODO: w2s (world 2 screen)
    return getXForm().position.y;
  }

  public void setPosition( float x, float y ){
    // TODO: w2s (world 2 screen)
    setXForm(new Vec2(x, y), getAngle());
  }

  public void adjustPosition( float dx, float dy ){
    // TODO: w2s (world 2 screen)
    setXForm(new Vec2(getXForm().position.x + dx, getXForm().position.y + dy), getAngle());
  }

  public float getRotation(){
    return getAngle();
  }
  
  public void setRotation( float w ){
    setXForm(getXForm().position, w);
  }
  
  public void adjustRotation( float dw ){
    setXForm(getXForm().position, getAngle() + dw);
  }

  public boolean isStatic(){
    return isStatic();
  }
  
  public void setStatic( boolean isStatic ){
    m_type = e_staticType;
  }

  public boolean isResting(){
    return isSleeping();
  }
  
  //public boolean isTouchingBody( FBody other ){}

  public float getAngularVelocity(){
    return super.getAngularVelocity();
  }
  
  public void setAngularVelocity( float w ){
    super.setAngularVelocity( w );
  }
  public void adjustAngularVelocity( float w ){
    super.setAngularVelocity( super.getAngularVelocity() + w );
  }

  public void setAngularDamping( float damping ){
    m_angularDamping = damping;
  }

  public void setDamping( float damping ){
    m_linearDamping = damping;
  }
  
  public void setDrawable( boolean drawable ){
    m_drawable = drawable;
  }

  public void setDensity( float density ){
    // Set the density of shapes
    for (Shape s = m_shapeList; s != null; s = s.m_next) {
      s.m_density = density;
    }
    
    // Recalculate the body's mass
    setMassFromShapes();
  }

  public void setRestitution( float restitution ){
    for (Shape s = m_shapeList; s != null; s = s.m_next) {
      s.setRestitution( restitution );
    }
  }

  public void setFriction( float friction ){
    for (Shape s = m_shapeList; s != null; s = s.m_next) {
      s.setFriction( friction );
    }
  }
  
  public void setRotatable( boolean rotatable ){
    // TODO: check this
    if (rotatable) {
      m_flags &= ~e_fixedRotationFlag;
    }else{
      m_flags |= e_fixedRotationFlag;
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
