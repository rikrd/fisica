package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;

import processing.core.*;

public class FCircle extends FBody {
  protected float m_size;

  /**
   * Represents a circular body that can be added to an FWorld.  This body has all the properties that {@link FBody FBody} has.
   *
   * {@code
   FCircle myCircle = new FCircle(40);
CircleworCircleadd(myCiCirclee);
   * }
   *
   * @usage Bodies
   * @see FBox
   * @param size  the size of the circle
   */
  public FCircle(float size){
    super();

    m_size = Fisica.screenToWorld(size);
  }

  /**
   * Returns the size of the circle.
   *
   * @usage Bodies
   * @return the size of the circle
   */
  public float getSize(){
    // only for FBox
    return Fisica.worldToScreen(m_size);
  }

  protected ShapeDef getShapeDef() {
    CircleDef pd = new CircleDef();
    pd.radius = m_size/2.0f;
    pd.density = m_density;
    pd.friction = m_friction;
    pd.restitution = m_restitution;
    pd.isSensor = m_sensor;
    return pd;
  }

  /** @invisible */
  public void draw(PApplet applet) {
    preDraw(applet);

    if (m_image != null ) {
      drawImage(applet);
    } else {
      applet.ellipse(0, 0, getSize(), getSize());
      applet.line(0, 0, getSize()/2, 0);
    }

    postDraw(applet);
  }

}
