class FSVG extends FPoly {
  RShape m_shape;
  
  float w = 100;
  float h = 100;  
  
  FSVG(String filename){
    super();
    
    RShape fullSvg = RG.loadShape(filename);
    m_shape = fullSvg.getChild("object");
    RShape outline = fullSvg.getChild("outline");
    
    if (m_shape == null || outline == null) {
      println("ERROR: Couldn't find the shapes called 'object' and 'outline' in the SVG file.");
      return;
    }
    
    // Make the shapes fit in a rectangle of size (w, h)
    // that is centered in 0
    m_shape.transform(-w/2, -h/2, w/2, h/2); 
    outline.transform(-w/2, -h/2, w/2, h/2); 
    
    RPoint[] points = outline.getPoints();

    if (points==null) return;
    
    for (int i=0; i<points.length; i++) {
      this.vertex(points[i].x, points[i].y);
    }

    this.setNoFill();
    this.setNoStroke();
  }
  
  void draw(PGraphics applet) {
    preDraw(applet);
    m_shape.draw(applet);
    postDraw(applet);
  }
  
}
