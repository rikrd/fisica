package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.util.nonconvex.*;

import processing.core.*;

import java.util.ArrayList;

public class FCompound extends FBody {
  ArrayList m_shapes;
  
  public FCompound(){
    super();
    
    m_shapes = new ArrayList<FBody>();
  }

  public void processBody(Body bd, ShapeDef sd){
    for (int i=0; i<m_shapes.size(); i++) {
      
      bd.createShape(m_shapes.at(i).getShapeDef());
    }
  }

  public void addBody(FBody body) {
    m_shapes.add(body);
  }
    
  public void draw(PApplet applet) {
    preDraw(applet);

    if (m_image != null ) {
      drawImage(applet);
    } else {
      applet.beginShape();
      for(int i = 0; i<m_vertices.size(); i++){
        Vec2 v = Fisica.worldToScreen((Vec2)m_vertices.get(i));
        applet.vertex(v.x, v.y);
      }
      applet.endShape(PConstants.CLOSE);
    }
    
    postDraw(applet);
  }
  
}
