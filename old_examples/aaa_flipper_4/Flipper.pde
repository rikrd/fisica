class Flipper {
  float x;
  float y;

  FPoly palo;
  FRevoluteJoint junta;

  Flipper(float _x, float _y, float _refAngle, int _direction, FWorld _mundo) {
    x = _x;
    y = _y;
    
    float flipperWidth = 50;
    flipperIzq = new FPoly();
    flipperIzq.vertex(-flipperWidth/5, 0);
    flipperIzq.vertex(0, flipperWidth/5);
    flipperIzq.vertex(flipperWidth*9/10, flipperWidth/10);
    flipperIzq.vertex(flipperWidth, 0);
    flipperIzq.vertex(flipperWidth*9/10, -flipperWidth/10);
    flipperIzq.vertex(0, -flipperWidth/5);
    flipperIzq.setPosition(20, height-20);
    flipperIzq.setFillColor(255);
    flipperIzq.setBullet(true);
    mundo.add(flipperIzq);

    junta = new FRevoluteJoint(flipperIzq, mundo.left, 20, height-20);
    junta.setEnableLimit(true);
    junta.setReferenceAngle(-PI);
    junta.setLowerAngle(0.0);
    junta.setUpperAngle(PI/3*_direction);
    junta.setDrawable(false);
    mundo.add(junta);
  }

}

