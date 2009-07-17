package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;

import processing.core.*;

public class FCircle extends FBody {
  float m_size;

  public FCircle(FWorld world, float size){
    super();
    
    CircleDef pd = new CircleDef();
    pd.radius = size/2.0f;
    pd.density = m_density;
    pd.friction = m_friction;
    pd.restitution = m_restitution;
    pd.isSensor = m_sensor;
 
    BodyDef bd = new BodyDef();
    bd.isBullet = m_bullet;
    
    m_body = world.createBody(bd);
    m_body.createShape(pd);
    m_body.m_userData = this;
    
    m_body.setXForm(m_position, m_angle);

    m_size = size;
    
    setParent(world.m_parent);
  }
  
  public float getSize(){ 
    // only for FBox
    return m_size;
  }

  public void draw(PApplet applet) {
    if (!isDrawable()) {
      return;
    }

    applet.pushStyle();
    applet.pushMatrix();

    applyMatrix(applet);
    applet.ellipseMode(PConstants.CENTER);
    appletFillStroke(applet);
    
    if (m_image != null ) {
      applet.tint(255, 255, 255, m_imageAlpha);
      applet.image(m_image, 0-m_image.width/2, 0-m_image.height/2);
      applet.tint(255, 255, 255, 255);
    } else {
      applet.ellipse(0, 0, getSize(), getSize());
    }
    
    applet.popMatrix();
    applet.popStyle();

  }
  
}
