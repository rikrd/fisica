package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.util.nonconvex.*;

import processing.core.*;

import java.util.ArrayList;

public class FPoly extends FBody {
  Polygon m_polygon;
  ArrayList m_vertices;
  
  public FPoly(){
    super();
    
    m_vertices = new ArrayList<Vec2>();
  }

  public void vertex(float x, float y){
    if (m_vertices.size() >= Settings.maxPolygonVertices ) {
      throw new IllegalArgumentException("The maximum number of vertices allowed for polygon bodies is: " + Settings.maxPolygonVertices);
    }
    
    m_vertices.add(new Vec2(x, y));
  }

  public void processBody(Body bd, ShapeDef sd){
    Polygon.decomposeConvexAndAddTo(m_polygon, bd, (PolygonDef)sd);
  }

  public ShapeDef getShapeDef() {
    PolygonDef pd = new PolygonDef();
    
    Vec2[] vertices = new Vec2[m_vertices.size()];
    m_vertices.toArray(vertices);
    m_polygon = new Polygon(vertices);
    
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
