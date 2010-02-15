package fisica;
import processing.core.*;

import org.jbox2d.common.*;


public class Fisica implements PConstants{
  protected static class FViewport {
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

  private static boolean m_initialized = false;
  private static PApplet m_parent;
  private static FViewport m_viewport;

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

  public static class LibraryNotInitializedException extends NullPointerException{
    private static final long serialVersionUID = -3710605630786298674L;

    LibraryNotInitializedException(){
      super("Must call Fisica.init(this); before using this library.");
    }
  }

  protected static boolean initialized() {
    return m_initialized;
  }

  protected static PApplet parent(){
    if(m_parent == null){
      throw new LibraryNotInitializedException();
    }

    return m_parent;
  }

  /**
   * Initialize the library.  Must be called before any call to Geomerative methods.  Must be called by passing the PApplet.  e.g. {@code Fisica.init(this)}
   *
   * @param applet  The applet on which to use the library.  This library can only be used with one applet
   */
  public static void init(PApplet applet){
    m_parent = applet;
    m_initialized = true;

    m_viewport = new FViewport();
    m_viewport.setScaleTransform(20);
  }

  /**
   * Set the scale from screen units to world units.  By setting the scale to 20 we are stating that 20 pixels is equivalent to 1 meter in the simulated world.
   *
   * @param scale the number of pixels that are equivalent to 1 meter in the simulated world.
   *
   */
  public static void setScale(float scale){
    m_viewport.m_scale = scale;
  }

}
