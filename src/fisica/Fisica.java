package fisica;
import processing.core.*;

import org.jbox2d.common.*;

public class Fisica implements PConstants{
  /**
   * @invisible
   */
  private static boolean m_initialized = false;
  
  /**
   * @invisible
   */  
  private static PApplet m_parent;
  private static ViewportTransform m_viewport;

  protected static Vec2 m_temp = new Vec2();

  protected static Vec2 screenToWorld( Vec2 m_in ) {
    return m_viewport.getScreenToWorld(m_in);
  }

  protected static Vec2 screenToWorld( float x, float y ) {
    return m_viewport.getScreenToWorld(new Vec2(x, y));
  }

  protected static Vec2 worldToScreen( Vec2 m_in ) {
    return m_viewport.getWorldToScreen(m_in);
  }

  protected static Vec2 worldToScreen( float x, float y ) {
    return m_viewport.getWorldToScreen(new Vec2(x, y));
  }

  protected static void screenToWorldToOut( Vec2 m_in, Vec2 m_out ) {
    m_viewport.getScreenToWorldToOut(m_in, m_out);
  }

  protected static void worldToScreenToOut( Vec2 m_in, Vec2 m_out ) {
    m_viewport.getWorldToScreenToOut(m_in, m_out);
  }

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

    m_viewport = new ViewportTransform();
    m_viewport.setTransform( Mat22.createScaleTransform( 20));
    m_viewport.setCenter( 0, 0 );
    m_viewport.setExtents( m_parent.width/2, m_parent.height/2 );
    m_viewport.yFlip = true;

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
}
