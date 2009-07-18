package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;

import processing.core.*;

import java.util.ArrayList;

public class FPoly extends FBody {
  ArrayList m_vertices;
  
  public FPoly(PApplet applet){
    super(applet);
    
    m_vertices = new ArrayList<Vec2>();
  }

  public void vertex(float x, float y){
    if (m_vertices.size() >= Settings.maxPolygonVertices ) {
      throw new IllegalArgumentException("The maximum number of vertices allowed for polygon bodies is: " + Settings.maxPolygonVertices);
    }
    
    m_vertices.add(new Vec2(x, y));
  }

  public ShapeDef getShapeDef() {
    PolygonDef pd = new PolygonDef();
    
    // TODO: force counter-clockwiseness
    for(int i = 0; i<m_vertices.size(); i++){
      pd.addVertex((Vec2)m_vertices.get(i));
    }

    pd.density = m_density;
    pd.friction = m_friction;
    pd.restitution = m_restitution;
    pd.isSensor = m_sensor;
    return pd;
  }
    
  public void draw(PApplet applet) {
    preDraw(applet);

    if (m_image != null ) {
      drawImage(applet);
    } else {
      applet.beginShape();
      for(int i = 0; i<m_vertices.size(); i++){
        Vec2 v = (Vec2)m_vertices.get(i);
        applet.vertex(v.x, v.y);
      }
      applet.endShape(PConstants.CLOSE);
    }
    
    postDraw(applet);
  }
  
}
