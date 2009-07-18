package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;

import processing.core.*;

public class FBox extends FBody {
  float m_height;
  float m_width;

  public FBox(PApplet applet, float width, float height){
    super(applet);
    
    m_height = height;
    m_width = width;
  }

  public ShapeDef getShapeDef() {
    PolygonDef pd = new PolygonDef();
    pd.setAsBox(m_width/2.0f, m_height/2.0f);
    pd.density = m_density;
    pd.friction = m_friction;
    pd.restitution = m_restitution;
    pd.isSensor = m_sensor;
    return pd;
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
    preDraw(applet);

    if (m_image != null ) {
      drawImage(applet);
    } else {
      applet.rect(0, 0, getWidth(), getHeight());
    }
    
    postDraw(applet);
  }
  
}
