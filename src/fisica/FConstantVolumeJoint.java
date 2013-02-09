/*
  Part of the Fisica library - http://www.ricardmarxer.com/fisica

  Copyright (c) 2009 - 2010 Ricard Marxer

  Fisica is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.
  
  You should have received a copy of the GNU Lesser General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

/**
 * Represents constant volume joint that tries to keep the volume enclosed by a group of bodies constant.  This joint is similar to connecting multiple springs between the bodies in order to maintain the volume inside their position constant. This is the joint used to create the {@link FBlob} body.
 *
 */
public class FConstantVolumeJoint extends FJoint {
  protected ArrayList m_bodies;
  protected float m_damping = 0.0f;
  protected float m_frequency = 0.0f;

  protected JointDef getJointDef(FWorld world) {
    ConstantVolumeJointDef md = new ConstantVolumeJointDef();
    md.frequencyHz = m_frequency;
    md.dampingRatio = m_damping;

    for (int i=0; i<m_bodies.size(); i++) {
      Body b = ((FBody)m_bodies.get(i)).m_body;
      if (b != null) {
        md.addBody(b);
      }
    }

    return md;
  }

  /**
   * Constructs an constant volume joint.  It creates an empty joint, before adding the joint to the world use {@link #addBody(FBody) addBody} to add bodies to the joint and to define the initial and target volume of the joint.
   */
  public FConstantVolumeJoint() {
    super();

    m_bodies = new ArrayList();
  }

  /**
   * Adds a body to the joint.  This method must be called before adding the joint to the world.
   *
   * @param b  body to be added
   */
  public void addBody(FBody b) {
    m_bodies.add(b);
  }

  /**
   * Return the group of bodies that are connected by this joint.
   *
   * @return   list of bodies (ArrayList of FBody) connected by the joint
   *
   */
  public ArrayList getBodies() {
    return m_bodies;
  }

  /**
   * Sets the damping of the springs used to maintain the volume defined by the vertices constant.  This method must be called before adding the joint to the world.
   *
   * @param damping  the damping of the springs of the constant volume joint
   */
  public void setDamping(float damping) {
    m_damping = damping;
  }

  /**
   * Sets the frequency of the springs used to maintain the volume defined by the vertices constant.  This method must be called before adding the joint to the world.
   *
   * @param frequency  the frequency of the springs of the constant volume joint
   */
  public void setFrequency(float frequency) {
    m_frequency = frequency;
  }

  public PVector getCentroid() {
    PVector centroid = new PVector(0, 0);
    float signedArea = 0.0f;
    float x0 = 0.0f; // Current vertex X
    float y0 = 0.0f; // Current vertex Y
    float x1 = 0.0f; // Next vertex X
    float y1 = 0.0f; // Next vertex Y
    float a = 0.0f;  // Partial signed area

    // For all vertices except last
    int i;
    for (i=0; i<m_bodies.size()-1; ++i)
    {
        x0 = ((FBody)m_bodies.get(i)).getX();
        y0 = ((FBody)m_bodies.get(i)).getY();
        x1 = ((FBody)m_bodies.get(i+1)).getX();
        y1 = ((FBody)m_bodies.get(i+1)).getY();
        a = x0*y1 - x1*y0;
        signedArea += a;
        centroid.x += (x0 + x1)*a;
        centroid.y += (y0 + y1)*a;
    }

    // Do last vertex
    x0 = ((FBody)m_bodies.get(i)).getX();
    y0 = ((FBody)m_bodies.get(i)).getY();
    x1 = ((FBody)m_bodies.get(0)).getX();
    y1 = ((FBody)m_bodies.get(0)).getY();
    a = x0*y1 - x1*y0;
    signedArea += a;
    centroid.x += (x0 + x1)*a;
    centroid.y += (y0 + y1)*a;

    signedArea *= 0.5;
    centroid.x /= (6.0*signedArea);
    centroid.y /= (6.0*signedArea);

    return centroid;
  }
  
  public void draw(PGraphics applet){
    preDraw(applet);

    if (m_image != null ) {
      applet.pushMatrix();
      PVector c = getCentroid();
      applet.translate(c.x, c.y);
      drawImage(applet);
      applet.popMatrix();
    } else {
      if (m_bodies.size()>0) {
        applet.beginShape();
        for (int i=0; i<m_bodies.size(); i++) {
          applet.vertex(((FBody)m_bodies.get(i)).getX(), ((FBody)m_bodies.get(i)).getY());
        }
        applet.endShape(applet.CLOSE);
      }
    }

    postDraw(applet);
  }
  
  public void drawDebug(PGraphics applet){
    preDrawDebug(applet);

    if (m_bodies.size()>0) {
      applet.beginShape();
      for (int i=0; i<m_bodies.size(); i++) {
        applet.vertex(((FBody)m_bodies.get(i)).getX(), ((FBody)m_bodies.get(i)).getY());
      }
      applet.endShape(applet.CLOSE);
      
      for (int i=0; i<m_bodies.size(); i++) {
        applet.ellipse(((FBody)m_bodies.get(i)).getX(), ((FBody)m_bodies.get(i)).getY(), 5, 5);
      }
    }

    postDrawDebug(applet);
  }
}