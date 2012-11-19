/**
 *  Canica
 *
 *  by Ricard Marxer
 *
 *  This example shows how to create an Android app using Fisica.
 *
 *  Wood image from http://j-owen.deviantart.com/art/Wood-Pack-1-88580807
 *                  http://zygat3r.deviantart.com/art/Dark-Wood-58266349
 *  Ball image from http://www.swithenbanks.co.uk/Solar_Photovoltaic_Fixings/12669/Schletter_Secufix_Steel_Ball_For_Socket_Head_M8.html
 *
 */


import ketai.sensors.*;
import fisica.*;
import android.view.WindowManager;
import android.view.View;
import android.os.Bundle;

FWorld w;
KetaiSensor sensor;
float accelerometerX, accelerometerY, accelerometerZ;

FCircle c;
FCircle t;

int winCount = 0;
int loseCount = 0;

float cx;
float cy;

PImage bg;
PImage ball;

boolean mustReset = false;
boolean mustNext = false;

void onCreate(Bundle bundle) 
{
  super.onCreate(bundle);
  // fix so screen doesn't go to sleep when app is active
  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
}

void setup() {
  size(displayWidth, displayHeight);
  smooth();
  orientation(LANDSCAPE);
  textSize(36);
  textAlign(CENTER, CENTER);
  
  sensor = new KetaiSensor(this);
  sensor.start();

  Fisica.init(this);

  w = new FWorld();
  w.setEdges();

  bg = loadImage("woodbg.png");
  ball = loadImage("ball.png");

  next();
}

void mousePressed() {
  loseCount++;
  next();
}

void next() {
  w.clear();
  w.setEdges();

  c = new FCircle(50);
  c.attachImage(ball);

  cx = random(c.getSize(), width-c.getSize());
  cy = random(c.getSize(), height-c.getSize());

  c.setPosition(cx, cy);
  c.setFill(255, 0, 0);
  c.setNoStroke();
  c.setName("ball");
  c.setRotatable(false);
  c.setAllowSleeping(false);
  w.add(c);

  t = new FCircle(50);
  t.setPosition(random(t.getSize(), width-t.getSize()), random(t.getSize(), height-t.getSize()));
  t.setFill(0);
  t.setNoStroke();
  t.setSensor(true);
  t.setStatic(true);
  t.setName("hole");
  w.add(t);

  for (int i=0; i<10; i++) {
    FCircle o = new FCircle(20);
    o.setPosition(random(o.getSize(), width-o.getSize()), random(o.getSize(), height-o.getSize()));
    o.setFill(0);
    o.setNoStroke();
    o.setStatic(true);
    o.setName("obstacle");
    w.add(o);
  }

  for (int i=0; i<20; i++) {
    FCircle o = new FCircle(20);
    o.setPosition(random(o.getSize(), width-o.getSize()), random(o.getSize(), height-o.getSize()));
    o.setFill(128);
    o.setNoStroke();
    o.setSensor(true);
    o.setStatic(true);
    o.setName("badhole");
    w.add(o);
  }
  
  mustNext = false;
}

void reset() {
  c.setPosition(cx, cy);
  c.setVelocity(0, 0);
  mustReset = false;
}

void draw() {
  image(bg, 0, 0, width, height);

  float gy = constrain(accelerometerY*100, -300, 300);
  float gx = constrain(accelerometerX*100, -300, 300);

  w.setGravity(gy, gx);

  w.step();
  w.draw();

  if (mustReset) {
    reset();
  }
  
  if (mustNext) {
    next();
  }

  printScore();
}

void printScore() {
  fill(#0070FF);
  textAlign(RIGHT, TOP);
  text(""+winCount, width-200, 0, 200, 200);

  fill(#FF9E00);
  textAlign(LEFT, TOP);
  text(""+loseCount, 0, 0, 200, 200);
}


void win() {
  winCount++;
  mustNext = true;
}

void lose() {
  loseCount++;
  mustReset = true;
}

void contactStarted(FContact cont) {
  if (cont == null) {
    return;
  }
  
  if (cont.contains("ball", "hole")) {
    win();
  } 
  else if (cont.contains("ball", "badhole")) {
    lose();
  }
}

void onAccelerometerEvent(float x, float y, float z)
{
  accelerometerX = x;
  accelerometerY = y;
  accelerometerZ = z;
}

