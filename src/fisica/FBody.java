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
  public Vec2 m_linearVelocity = new Vec2(0.0f, 0.0f);
  public float m_angularVelocity = 0.0f;
  public Vec2 m_force = new Vec2(0.0f, 0.0f);
  public float m_torque = 0.0f;
  public float m_linearDamping = 0.0f;
  public float m_angularDamping = 0.0f;

  public Vec2 m_position = new Vec2(0.0f, 0.0f);
  public float m_angle = 0.0f;
  public boolean m_isSleeping = false;
  
  public boolean m_rotatable = true;
  
  public boolean m_fill = true;
  public int m_fillColor = 0xFFFFFFFF;
  public boolean m_stroke = true;
  public int m_strokeColor = 0xFFFFFFFF;
  public float m_strokeWeight = 1.0f;

  public PImage m_image = null;
  public float m_imageAlpha = 255.0f;
  public PImage m_mask = null;
  
  public PApplet m_parent;
  public Body m_body;
  
  public FBody() {
    m_body = null;
  }

  protected void setParent(PApplet parent){
    m_parent = parent;
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
    if (m_body == null) {
      m_force.setZero();
      m_torque = 0f;
    }

    m_body.m_force.setZero();
    m_body.m_torque = 0f;
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
    if (m_body == null) {
      return m_position.x;
    }
    
    return m_body.getMemberXForm().position.x;
  }
  
  public float getY(){
    // TODO: w2s (world 2 screen)
    if (m_body == null) {
      return m_position.y;
    }
    
    return m_body.getMemberXForm().position.y;
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
    
    m_body.setXForm(new Vec2(m_body.getMemberXForm().position.x + dx, m_body.getMemberXForm().position.y + dy), m_body.getAngle());
  }

  public float getRotation(){
    if (m_body == null) {
      return m_angle;
    }
    
    return m_body.getAngle();
  }
  
  public void setRotation( float w ){
    if (m_body == null) {
      m_angle = w;
      return;
    }

    m_body.setXForm(m_body.getMemberXForm().position, w);
  }
  
  public void adjustRotation( float dw ){
    if (m_body == null) {
      m_angle += dw;
      return;
    }
    
    m_body.setXForm(m_body.getMemberXForm().position, m_body.getAngle() + dw);
  }

  public boolean isStatic(){
    if (m_body == null) return false;
    
    return m_body.isStatic();
  }
  
  public void setStatic( boolean isStatic ){
    if (m_body == null) {
      return;
    }

    m_body.m_type = m_body.e_staticType;
  }

  public boolean isResting(){
    if (m_body == null) {
      return m_isSleeping;
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
      m_angularDamping = damping;
      return;
    }

    m_angularDamping = damping;
  }

  public void setDamping( float damping ){
    if (m_body == null) {
      m_linearDamping = damping;
      return;
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
    if ( m_body == null ) {
      return;
    }
    
    // Set the density of shapes
    for (Shape s = m_body.getShapeList(); s != null; s = s.m_next) {
      s.m_density = density;
    }
    
    // Recalculate the body's mass
    m_body.setMassFromShapes();
  }

  public void setStaticBody( boolean value ) {
    setDensity(0.0f);
  }

  public void setRestitution( float restitution ){
    if ( m_body == null ) {
      m_restitution = restitution;
      return;
    }
    
    for (Shape s = m_body.getShapeList(); s != null; s = s.m_next) {
      s.setRestitution( restitution );
    }
  }

  public void setFriction( float friction ){
    if ( m_body == null ) {
      m_friction = friction;
      return;
    }
    
    for (Shape s = m_body.getShapeList(); s != null; s = s.m_next) {
      s.setFriction( friction );
    }
  }
  
  public void setRotatable( boolean rotatable ){
    if ( m_body == null ) {
      m_rotatable = rotatable;
      return;
    }
    
    // TODO: check this
    if (rotatable) {
      m_body.m_flags &= ~m_body.e_fixedRotationFlag;
    }else{
      m_body.m_flags |= m_body.e_fixedRotationFlag;
    }
  }
  
  public void setNoFill() {
    m_fill = false;
  }

  public void setFillColorInt(int col) {
    m_fill = true;
    m_fillColor = col;
  }

  public void setFillColor(float g){
    setFillColorInt(m_parent.color(g));
  }

  public void setFillColor(float g, float a){
    setFillColorInt(m_parent.color(g, a));
  }

  public void setFillColor(float r, float g, float b){
    setFillColorInt(m_parent.color(r, g, b));
  }

  public void setFillColor(float r, float g, float b, float a){
    setFillColorInt(m_parent.color(r, g, b, a));
  }

  public void setNoStroke() {
    m_stroke = false;
  }

  public void setStrokeColorInt(int col) {
    m_stroke = true;
    m_strokeColor = col;
  }

  public void setStrokeColor(float g){
    setStrokeColorInt(m_parent.color(g));
  }

  public void setStrokeColor(float g, float a){
    setStrokeColorInt(m_parent.color(g, a));
  }

  public void setStrokeColor(float r, float g, float b){
    setStrokeColorInt(m_parent.color(r, g, b));
  }

  public void setStrokeColor(float r, float g, float b, float a){
    setStrokeColorInt(m_parent.color(r, g, b, a));
  }
  
  public void setStrokeWeight(float weight) {
    m_strokeWeight = weight;
  }
}
