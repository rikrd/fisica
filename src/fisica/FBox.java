package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;

import processing.core.*;

public class FBox extends FBody {
  float m_height;
  float m_width;

  public FBox(FWorld world, float width, float height){
    PolygonDef pd = new PolygonDef();
    pd.setAsBox(width/2.0f, height/2.0f);
    /*
    pd.density = m_body.m_density;
    pd.friction = m_body.m_friction;
    pd.restitution = m_body.m_restitution;
    */
    pd.isSensor = false;
 
    BodyDef bd = new BodyDef();
    bd.isBullet = false;
    
    m_body = world.createBody(bd);
    m_body.createShape(pd);
    m_body.m_userData = this;
    
    m_height = height;
    m_width = width;
  }
  
  public float getHeight(){ 
    // only for FBox
    return m_height;
  }
  
  public float getWidth(){
    // only for FBox
    return m_width;
  }  
  
  public void draw(PApplet applet) {
    // Don't draw anything, each subclass will draw itself
    applet.pushMatrix();
    applet.translate(getX(), getY());
    applet.rotate(getRotation());
    applet.rect(0, 0, getHeight(), getWidth());
    applet.popMatrix();
  }
  
}
