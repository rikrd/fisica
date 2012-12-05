/*
  Part of the Fisica library - http://www.ricardmarxer.com/fisica

  Copyright (c) 2009 - 2010 Ricard Marxer

  Fisica is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.
  
  You should have received a copy of the GNU Lesser General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.util.nonconvex.*;

import processing.core.*;

import java.util.ArrayList;

/**
 * Represents a polygonal body that can be added to a world.
 * Polygons can be created by adding vertices using the {@link #vertex(float,float) vertex} method in a similar way to {@link FPoly FPoly}:
 * <pre>
 * {@code
 * FPoly myPoly = new FPoly();
 * myBlob.vertex(40, 10);
 * myBlob.vertex(50, 20);
 * myBlob.vertex(60, 30);
 * myBlob.vertex(60, 40);
 * myBlob.vertex(50, 50);
 * myBlob.vertex(40, 60);
 * myBlob.vertex(30, 70);
 * myBlob.vertex(20, 60);
 * myBlob.vertex(10, 50);
 * myBlob.vertex(10, 40);
 * myBlob.vertex(20, 30);
 * myBlob.vertex(30, 20);
 * myBlob.vertex(40, 10);
 * world.add(myPoly);
 * }
 * </pre>
 *
 * @usage Bodies
 * @see FBox
 * @see FCircle
 * @see FBlob
 * @see FLine
 */
public class FPoly extends FBody {
  protected Polygon m_polygon;
  protected boolean m_closed;
  protected ArrayList m_vertices;
  
  /**
   * Constructs a polygonal body that can be added to a world.  It creates an empty polygon, before adding the blob to the world use {@link #vertex(float,float) vertex} to define the shape of the polygon.
   */
  public FPoly(){
    super();
    m_closed = false;
    m_vertices = new ArrayList();
  }

  /**
   * Adds vertices to the shape of the poly.  This method must called before adding the body to the world.
   *
   * @param x  x coordinate of the vertex to be added
   * @param y  y coordinate of the vertex to be added
   */
  public void vertex(float x, float y){
    /*
    if (m_vertices.size() >= Settings.maxPolygonVertices ) {
      throw new IllegalArgumentException("The maximum number of vertices allowed for polygon bodies is: " + Settings.maxPolygonVertices);
    }
    */
    m_vertices.add(Fisica.screenToWorld(x, y));
  }

  protected void processBody(Body bd, ShapeDef sd){
    Polygon.decomposeConvexAndAddTo(m_polygon, bd, (PolygonDef)sd);
  }

  protected ShapeDef getShapeDef() {
    PolygonDef pd = new PolygonDef();
    
    m_vertices.add(new Vec2((Vec2)m_vertices.get(m_vertices.size()-1)));
    m_closed = true;

    Vec2[] vertices = new Vec2[m_vertices.size()];
    m_vertices.toArray(vertices);
    m_polygon = new Polygon(vertices);
    
    pd.density = m_density;
    pd.friction = m_friction;
    pd.restitution = m_restitution;
    pd.isSensor = m_sensor;
    return pd;
  }
  
  protected ShapeDef getTransformedShapeDef() {
    PolygonDef pd = (PolygonDef)getShapeDef();

    XForm xf = new XForm();
    xf.R.set(-m_angle);
    xf.position = Mat22.mul(xf.R, m_position.negate());
    
    for (int i=0; i<pd.vertices.size(); i++) {
        Vec2 ver = (Vec2)pd.vertices.get(i);
        XForm.mulTransToOut(xf, ver, ver);
    }

    return pd;
  }

  public void draw(PGraphics applet) {
    preDraw(applet);

    if (m_image != null ) {
      drawImage(applet);
    } else {
      applet.beginShape();
      for(int i = 0; i<m_vertices.size(); i++){
        Vec2 v = Fisica.worldToScreen((Vec2)m_vertices.get(i));
        applet.vertex(v.x, v.y);
      }
      if (m_closed) {
        applet.endShape(PConstants.CLOSE);
      } else {
        applet.endShape();
      }
    }
    
    postDraw(applet);
  }
  
  public void drawDebug(PGraphics applet) {
    preDrawDebug(applet);        

    Body b = getBox2dBody();
    if (b != null) {
        applet.pushStyle();
        applet.stroke(120, 100);
        applet.fill(120, 30);
        Shape ss = b.getShapeList();
        
        while (ss != null) {        
            PolygonShape ps = (PolygonShape)ss;
            Vec2[] vecs = ps.getVertices();
            applet.beginShape();
            for (int j=0; j<ps.getVertexCount(); j++) {
            Vec2 v = Fisica.worldToScreen(vecs[j]);
            applet.vertex(v.x, v.y);
            }
            applet.endShape(applet.CLOSE);
            
            Vec2 c = Fisica.worldToScreen(ps.getCentroid());
            applet.ellipse(c.x, c.y, 2, 2);
            
            ss = ss.getNext();
        }
        applet.popStyle();
    }
    
    applet.beginShape();
    for(int i = 0; i<m_vertices.size(); i++){
      Vec2 v = Fisica.worldToScreen((Vec2)m_vertices.get(i));
      applet.vertex(v.x, v.y);
    }
      
    if (m_closed) {
      applet.endShape(PConstants.CLOSE);
    } else {
      applet.endShape();
    }
    
    postDrawDebug(applet);
  }

  
}
