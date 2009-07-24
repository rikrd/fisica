package fisica;
import processing.core.*;

import org.jbox2d.common.*;


public class Fisica implements PConstants{
  public static class FViewport {
    float m_scale;
    
    public FViewport(){
      m_scale = 1.0f;
    }
    
    public void setScaleTransform(float a) {
      m_scale = a;
    }
    
    public float getScreenToWorld(float a) {
      return a/m_scale;
    }
    
    public Vec2 getScreenToWorld(float x, float y) {
      return new Vec2(x/m_scale, y/m_scale);
    }
    
    public Vec2 getScreenToWorld(Vec2 p) {
      return new Vec2(p.x/m_scale, p.y/m_scale);
    }
    
    public float getWorldToScreen(float a) {
      return a*m_scale;
    }
    
    public Vec2 getWorldToScreen(float x, float y) {
      return new Vec2(x*m_scale, y*m_scale);
    }
    
    public Vec2 getWorldToScreen(Vec2 p) {
      return new Vec2(p.x*m_scale, p.y*m_scale);
    }
  }
  

  /**
   * @invisible
   */
  private static boolean m_initialized = false;
  
  /**
   * @invisible
   */  
  private static PApplet m_parent;
  private static FViewport m_viewport;

  protected static Vec2 m_temp = new Vec2();

  protected static Vec2 screenToWorld( Vec2 m_in ) {
    return m_viewport.getScreenToWorld(m_in);
  }

  protected static Vec2 screenToWorld( float x, float y ) {
    return m_viewport.getScreenToWorld(x, y);
  }

  protected static float screenToWorld( float a ) {
    return m_viewport.getScreenToWorld(a);
  }

  protected static Vec2 worldToScreen( Vec2 m_in ) {
    return m_viewport.getWorldToScreen(m_in);
  }

  protected static Vec2 worldToScreen( float x, float y ) {
    return m_viewport.getWorldToScreen(x, y);
  }
  
  protected static float worldToScreen( float a ) {
    return m_viewport.getWorldToScreen(a);
  }
  /*
  protected static void screenToWorldToOut( Vec2 m_in, Vec2 m_out ) {
    m_viewport.getScreenToWorldToOut(m_in, m_out);
  }

  protected static void worldToScreenToOut( Vec2 m_in, Vec2 m_out ) {
    m_viewport.getWorldToScreenToOut(m_in, m_out);
  }
  */
  public static class LibraryNotInitializedException extends NullPointerException{
    private static final long serialVersionUID = -3710605630786298674L;

    LibraryNotInitializedException(){
      super("Must call Fisica.init(this); before using this library.");
    }
  }

  /**
   * Initialize the library.  Must be called before any call to Geomerative methods.  Must be called by passing the PApplet.  e.g. Fisica.init(this)
   */
  public static void init(PApplet applet){
    m_parent = applet;
    m_initialized = true;

    /*
    m_viewport = new ViewportTransform();
    m_viewport.setTransform( Mat22.createScaleTransform( 20 ) );
    m_viewport.setCenter( m_parent.width/2, m_parent.height/2 );
    m_viewport.setExtents( m_parent.width/2, m_parent.height/2 );
    m_viewport.yFlip = true;
    */
    
    m_viewport = new FViewport();
    m_viewport.setScaleTransform(20);
  }
  
  /**
   * @invisible
   */
  public static boolean initialized() {
    return m_initialized;
  }

  /**
   * @invisible
   */
  protected static PApplet parent(){
    if(m_parent == null){
      throw new LibraryNotInitializedException();
    }
    
    return m_parent;
  }

  /**
   * @invisible
   */
  public static void setScale(float scale){
    m_viewport.m_scale = scale;
  }
  
}
