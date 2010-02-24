package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;

import processing.core.*;

import java.util.ArrayList;

/**
 * Represents a line body that can be added to a world.
 *
 * <pre>
 * {@code
 * FLine myLine = new FLine(40, 20, 100, 40);
 * world.add(myLine);
 * }
 * </pre>
 *
 * @usage Bodies
 * @see FBox
 * @see FCircle
 * @see FBlob
 * @see FPoly
 */
public class FLine extends FBody {
  protected Vec2 m_start;
  protected Vec2 m_end;

  protected ShapeDef getShapeDef() {
    EdgeChainDef pd = new EdgeChainDef();

    pd.addVertex(m_start);
    pd.addVertex(m_end);

    pd.setIsLoop(false);

    pd.density = m_density;
    pd.friction = m_friction;
    pd.restitution = m_restitution;
    pd.isSensor = m_sensor;
    return pd;
  }

  /**
   * Constructs a line body that can be added to a world.
   *
   * @param x1  horizontal position of the first point of the line
   * @param y1  vertical position of the first point of the line
   * @param x2  horizontal position of the second point of the line
   * @param y2  vertical position of the second point of the line
   */
  public FLine(float x1, float y1, float x2, float y2){
    super();

    m_start = Fisica.screenToWorld(x1, y1);
    m_end = Fisica.screenToWorld(x2, y2);
  }

  public void draw(PGraphics applet) {
    preDraw(applet);

    if (m_image != null ) {
      drawImage(applet);
    } else {
      Vec2 tempStart = Fisica.worldToScreen(m_start);
      Vec2 tempEnd = Fisica.worldToScreen(m_end);
      applet.line(tempStart.x, tempStart.y, tempEnd.x, tempEnd.y);
    }

    postDraw(applet);
  }

}
