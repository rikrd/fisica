package fisica;

import java.lang.reflect.Method;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

import processing.core.*;

public class FWorld extends World {
  FBox left, right, top, bottom;
  float m_edgesFriction = 0.5f;
  float m_edgesRestitution = 0.5f;


  /**
   * Forward the contact events to the contactStarted(ContactPoint point),
   * contactPersisted(ContactPoint point) and contactStopped(ContactPoint point)
   * which may be implemented in the sketch.
   *
   */
  class ConcreteContactListener implements ContactListener {
    public void add(ContactPoint point) {
      if (m_world.m_contactStartedMethod == null) {
        return;
      }
      
      try {
        m_world.m_contactStartedMethod.invoke(Fisica.parent(),
                                              new Object[] { new FContact(point) });
      } catch (Exception e) {
        System.err.println("Disabling contactStarted(ContactPoint point) because of an error.");
        e.printStackTrace();
        m_world.m_contactStartedMethod = null;
      }
    }
    
    public void persist(ContactPoint point) {
      if (m_world.m_contactPersistedMethod == null) {
        return;
      }
      
      try {
        m_world.m_contactPersistedMethod.invoke(Fisica.parent(),
                                                new Object[] { new FContact(point) });
      } catch (Exception e) {
        System.err.println("Disabling contactPersisted(ContactPoint point) because of an error.");
        e.printStackTrace();
        m_world.m_contactPersistedMethod = null;
      }
    }
    
    public void remove(ContactPoint point) {
      if (m_world.m_contactEndedMethod == null) {
        return;
      }
      
      try {
        m_world.m_contactEndedMethod.invoke(Fisica.parent(),
                                            new Object[] { new FContact(point) });
      } catch (Exception e) {
        System.err.println("Disabling contactEnded(ContactPoint point) because of an error.");
        e.printStackTrace();
        m_world.m_contactEndedMethod = null;
      }
    }
    
    public FWorld m_world;
    
    public void result(ContactResult point) {
      //TODO
    }
  }

  private ConcreteContactListener m_contactListener;
  private Method m_contactStartedMethod;
  private Method m_contactPersistedMethod;
  private Method m_contactEndedMethod;
  
  public FWorld() {
    super(new AABB(new Vec2(-Fisica.parent().width,   // 10.0f pixels per meter
                            -Fisica.parent().height),
                   new Vec2(Fisica.parent().width, 
                            Fisica.parent().height)),
          new Vec2(0.0f, -10.0f),                  // gravity vertical downwards 10 m/s^2
          true);                                   // allow sleeping bodies

    // Get the contactStarted(), contactPersisted() and contactEnded()
    // methods from the sketch
    try {
      m_contactStartedMethod =
        Fisica.parent().getClass().getMethod("contactStarted",
                                             new Class[] { FContact.class });
    } catch (Exception e) {
      // no such method, or an error.. which is fine, just ignore
    }
    
    try {
      m_contactPersistedMethod =
        Fisica.parent().getClass().getMethod("contactPersisted",
                                             new Class[] { FContact.class });
    } catch (Exception e) {
      // no such method, or an error.. which is fine, just ignore
    }
    
    try {
      m_contactEndedMethod =
        Fisica.parent().getClass().getMethod("contactEnded",
                                             new Class[] { FContact.class });
    } catch (Exception e) {
      // no such method, or an error.. which is fine, just ignore
    }
    
    m_contactListener = new ConcreteContactListener();
    m_contactListener.m_world = this;
    setContactListener(m_contactListener);
  }
  

  public void draw( PApplet applet ) {
    for (Body b = getBodyList(); b != null; b = b.m_next) {
      FBody fb = (FBody)(b.m_userData);
      if (fb != null && fb.isDrawable()) fb.draw(applet);
    }
  }

  public void add( FBody body ) {
    body.addToWorld(this);
  }

  public void clear() { }

  public void remove( FBody body ) { }
  
  public void setDamping( float damping ) { }
  
  public void setEdges(PApplet applet, int color) {
    left = new FBox(20, applet.height);
    left.setStaticBody(true);
    left.setRestitution(0.5f);
    left.setFillColorInt(color);
    left.setStrokeColorInt(color);	
    left.setPosition(-5, applet.height/2);
    add(left);
    
    right = new FBox(20, applet.height);
    right.setStaticBody(true);
    right.setRestitution(0.5f);
    right.setPosition(applet.width+5, applet.height/2);
    right.setFillColorInt(color);
    right.setStrokeColorInt(color);	
    add(right);
    
    top = new FBox(applet.width, 20);
    top.setStaticBody(true);
    top.setRestitution(0.5f);
    top.setPosition(applet.width/2, -5);
    top.setFillColorInt(color);
    top.setStrokeColorInt(color);	
    add(top);
		
    bottom = new FBox(applet.width, 20);
    bottom.setStaticBody(true);
    bottom.setRestitution(0.5f);
    bottom.setPosition(applet.width/2, applet.height+5);
    bottom.setFillColorInt(color);
    bottom.setStrokeColorInt(color);	
    add(bottom);

    setEdgesFriction(m_edgesFriction);
    setEdgesRestitution(m_edgesRestitution);
  }

  public void setEdgesFriction( float friction ) {
    if (left != null) {
      left.setFriction(friction);
    }
    
    if (right != null) {    
      right.setFriction(friction);
    }
    
    if (top != null) {  
      top.setFriction(friction);
    }
    
    if (bottom != null) {  
      bottom.setFriction(friction);
    }

    m_edgesFriction = friction;
  }
  
  public void setEdgesRestitution( float restitution ) {
    if (left != null) {
      left.setRestitution(restitution);
    }
    
    if (right != null) {    
      right.setRestitution(restitution);
    }
    
    if (top != null) {  
      top.setRestitution(restitution);
    }
    
    if (bottom != null) {  
      bottom.setRestitution(restitution);
    }

    m_edgesRestitution = restitution;
  }
  
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
