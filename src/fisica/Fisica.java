package fisica;
import processing.core.*;

public class Fisica implements PConstants{
  /**
   * @invisible
   */
  private static boolean m_initialized = false;
  
  /**
   * @invisible
   */  
  private static PApplet m_parent;

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
