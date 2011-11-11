class FChar extends FPoly {
  RShape m_shape;
  RShape m_poly;
  boolean m_bodyCreated;
    
  FChar(char chr){
    super();
    
    String txt = "";
    txt += chr;
    
    RG.textFont(font, (int)random(72, 120));
    m_shape = RG.getText(txt);
    m_poly = RG.polygonize(m_shape);
    
    if (m_poly.countChildren() < 1) return;
    m_poly = m_poly.children[0];    
    
    // Find the longest contour of our letter
    float maxLength = 0.0;
    int maxIndex = -1;
    for (int i = 0; i < m_poly.countPaths(); i++) {
      float currentLength = m_poly.paths[i].getCurveLength();
      if (currentLength > maxLength) {
        maxLength = currentLength;
        maxIndex = i;
      }
    }
  
    if (maxIndex == -1) return;
    
    RPoint[] points = m_poly.paths[maxIndex].getPoints();

    for (int i=0; i<points.length; i++) {
      this.vertex(points[i].x, points[i].y);
    }

    this.setFill(0, 100, 150);
    this.setStroke(0);
    this.setStrokeWeight(2);
    
    this.setDamping(0);
    this.setRestitution(0.5);
    this.setBullet(true);
    this.setPosition(posX+10, height/5);
    
    posX = (posX + m_poly.getWidth()) % (width-100);
  
    m_bodyCreated = true;
  }
  
  boolean bodyCreated(){
    return m_bodyCreated;  
  }
  
  void draw(PGraphics applet){
    preDraw(applet);
    m_shape.draw(applet);
    postDraw(applet);
  }
}
