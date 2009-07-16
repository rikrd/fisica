package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.dynamics.*;

import processing.core.*;

public class FWorld extends World {
  PApplet m_parent;  
  
  public FWorld(PApplet parent) {
    super(new AABB(new Vec2(-parent.width/10.0f,   // 10.0f pixels per meter
                            -parent.height/10.0f),
                   new Vec2(parent.width/10.0f, 
                            parent.height/10.0f)),
          new Vec2(0.0f, -10.0f),                  // gravity vertical downwards 10 m/s^2
          true);                                   // allow sleeping bodies

    m_parent = parent;
  }
  
  public void draw( PApplet applet ) {
    for (FBody b = m_body.getShapeList(); s != null; s = s.m_next) {
      s.m_density = density;
    }
  }

  public void clear() {}

  public void remove( FBody body ) { }
  
  public void setDamping( float damping ) {}
  
  public void setEdges(PApplet applet, int col) {}

  public void setEdgesFriction( float friction ) {}
  
  public void setEdgesRestitution( float restitution ) {}
  
  public void setGravity( float gx, float gy ) {
    setGravity(new Vec2(gx, gy));
  }

  public void step( float dt ) {}

}
