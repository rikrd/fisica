class Texto extends FBox {
  
  String texto;
  float textOffset;

  Texto(String _texto){
    super(textWidth(_texto), textAscent() + textDescent());
    texto = _texto;
    textOffset = textAscent() - getHeight()/2;
  }
  
  void draw(PGraphics applet) {
    super.draw(applet);

    preDraw(applet);
    fill(0);
    stroke(0);
    textAlign(CENTER);
    text(texto, 0, textOffset);
    postDraw(applet);
  }
  
}
