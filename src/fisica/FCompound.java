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
    
    m_shapes = new ArrayList();
  }

  public ArrayList getShapeDefs() {
    ArrayList result = new ArrayList();
    for (int i=0; i<m_shapes.size(); i++) {
      ShapeDef sd = (ShapeDef)(((FBody)m_shapes.get(i)).getTransformedShapeDef());
      sd = ((FBody)m_shapes.get(i)).processShapeDef(sd);
      result.add(sd);
    }
    return result;
  }
  
  public ArrayList getBodies() {
    return m_shapes;
  }

  public void addBody(FBody body) {
    m_shapes.add(body);
  }
    
  public void draw(PGraphics applet) {
    preDraw(applet);

    if (m_image != null ) {
      drawImage(applet);
    } else {
      for(int i = 0; i<m_shapes.size(); i++){
        ((FBody)m_shapes.get(i)).draw(applet);
      }
    }
    
    postDraw(applet);
  }
  
  public void drawDebug(PGraphics applet) {
    preDrawDebug(applet);
    
    for(int i = 0; i<m_shapes.size(); i++){
        ((FBody)m_shapes.get(i)).drawDebug(applet);
    }
      
    postDrawDebug(applet);
  }
  
}
