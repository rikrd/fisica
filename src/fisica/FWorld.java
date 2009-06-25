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
    
  }

  public void add( FBody body ) {
    // TODO: add an existing body to a world
    /*
    assert(m_lock == false);
    if (m_lock == true) {
      return null;
    }
    
    // Add to world doubly linked list.
    body.m_prev = null;
    body.m_next = m_bodyList;
    if (m_bodyList != null) {
      m_bodyList.m_prev = body;
    }
    m_bodyList = body;
    ++m_bodyCount;
    */
    return;
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
