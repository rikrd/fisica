package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.dynamics.*;

import processing.core.*;

public class FWorld extends World {
  PApplet m_parent;
  
  public FWorld(PApplet parent) {
    super(new AABB(new Vec2(-parent.width,   // 10.0f pixels per meter
                            -parent.height),
                   new Vec2(parent.width, 
                            parent.height)),
          new Vec2(0.0f, -10.0f),                  // gravity vertical downwards 10 m/s^2
          true);                                   // allow sleeping bodies
    
    m_parent = parent;
  }
  

  public void draw( PApplet applet ) {
    for (Body b = getBodyList(); b != null; b = b.m_next) {
      FBody fb = (FBody)(b.m_userData);
      if (fb != null) fb.draw(applet);
    }
  }

  public void add( FBody body ) { }

  public void clear() { }

  public void remove( FBody body ) { }
  
  public void setDamping( float damping ) { }
  
  public void setEdges(PApplet applet, int col) { }

  public void setEdgesFriction( float friction ) { }
  
  public void setEdgesRestitution( float restitution ) { }
  
  public void setGravity( float gx, float gy ) {
    setGravity(new Vec2(gx, gy));
  }

  public void step() {
    step(1.0f/60.0f);
  }
  
  public void step( float dt ) {
    step(dt, 10);
  }

  public void step( float dt, int iterationCount) {
    super.setWarmStarting( true );
    super.setPositionCorrection( true );
    super.setContinuousPhysics( true );

    super.step( dt, iterationCount );
  }

}
