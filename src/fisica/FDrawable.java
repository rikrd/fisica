package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;

import processing.core.*;

public class FDrawable {
  public boolean m_drawable = true;
  
  public boolean m_fill = true;
  public int m_fillColor = 0xFFFFFFFF;
  public boolean m_stroke = true;
  public int m_strokeColor = 0xFF000000;
  public float m_strokeWeight = 1.0f;

  public PImage m_image = null;
  public float m_imageAlpha = 255.0f;
  public PImage m_mask = null;  

  public void updateStyle( FDrawable other ){
    m_drawable = other.m_drawable;
    
    m_fill = other.m_fill;
    m_fillColor = other.m_fillColor;
    m_stroke = other.m_stroke;
    m_strokeColor = other.m_strokeColor;
    m_strokeWeight = other.m_strokeWeight;

    m_image = other.m_image;
    m_imageAlpha = other.m_imageAlpha;
    m_mask = other.m_mask;
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

  protected void drawImage(PApplet applet) {
    applet.tint(255, 255, 255, m_imageAlpha);
    applet.image(m_image, 0-m_image.width/2, 0-m_image.height/2);
    applet.tint(255, 255, 255, 255);
  }
  
  public void draw(PApplet applet) {
    // Don't draw anything, each subclass will draw itself
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

  public void setDrawable( boolean drawable ){
    m_drawable = drawable;
  }
  
  public boolean isDrawable(){
    return m_drawable;
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
}
