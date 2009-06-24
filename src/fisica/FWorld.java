package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.dynamics.*;

public class FWorld extends World {
  PApplet m_parent;  
  
  public FWorld(PApplet parent) {
    m_parent = parent;
    Vec2 gravity = new Vec2(0.0, -10.0);
    float screenAABBWidth = m_parent.width * 2;
    float screenAABBHeight = m_parent.height * 2;
    float pixelsPerMeter = 10.0;
    
    Vec2 minWorldAABB = new Vec2(-screenAABBWidth*.5f/pixelsPerMeter, -screenAABBHeight*.5f/pixelsPerMeter);
    Vec2 maxWorldAABB = minWorldAABB.mul(-1.0f);
    boolean doSleep = true;

    super(new AABB(minWorldAABB,maxWorldAABB),gravity,doSleep);
    
    setDebugDraw(m_draw);
    setDrawDebugData(false);
  }
  
  public void draw( PApplet applet ) {
    
  }

  public void add( FBody body ) {
    assert(m_lock == false);
    if (m_lock == true) {
      return null;
    }
    
    // Add to world doubly linked list.
    b.m_prev = null;
    b.m_next = m_bodyList;
    if (m_bodyList != null) {
      m_bodyList.m_prev = body;
    }
    m_bodyList = body;
    ++m_bodyCount;
    
    return;
  }

  public void clear() {}

  public void remove( FBody body ) { }
  
  public void setDamping( float damping ) {}
  
  public void setEdges(PApplet applet, color col) {}

  public void setEdgesFriction( float friction ) {}
  
  public void setEdgesRestitution( float restitution ) {}
  
  public void setGravity( float gx, float gy ) {
    setGravity(new Vec2(gravX, gravY));
  }

  public void step( float dt ) {}

}
