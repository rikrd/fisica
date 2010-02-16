import fisica.*;

FBody[] steps = new FBody[10];
FWorld world;

int boxWidth = 30;

void setup() {
  size(400, 400, P3D);
  smooth();

  Fisica.init(this);
  Fisica.setScale(100);

  world = new FWorld();
  world.setEdges();
  world.setGravity(0, 200);

  FCircle bola = new FCircle(40);
  bola.setPosition(width/3, height/4);
  world.add(bola);

  for (int i=0; i<steps.length; i++) {
    steps[i] = new FBox(boxWidth, 5);
    steps[i].setPosition(map(i, 0, steps.length-1, boxWidth, width-boxWidth), height/2);
    world.add(steps[i]);
  }

  for (int i=1; i<steps.length; i++) {
    FDistanceJoint junta = new FDistanceJoint(steps[i-1], steps[i]);
    junta.setAnchor1(boxWidth/2, 0);
    junta.setAnchor2(-boxWidth/2, 0);
    junta.calculateLength();
    world.add(junta);
  }

  FDistanceJoint juntaPrincipio = new FDistanceJoint(steps[0], world.left);
  juntaPrincipio.setAnchor1(-boxWidth/2, 0);
  juntaPrincipio.calculateLength();
  world.add(juntaPrincipio);

  FDistanceJoint juntaFinal = new FDistanceJoint(steps[steps.length-1], world.right);
  juntaFinal.setAnchor1(boxWidth/2, 0);
  juntaFinal.calculateLength();
  world.add(juntaFinal);
}

void draw() {
  background(255);

  world.step();
  world.draw();
}

