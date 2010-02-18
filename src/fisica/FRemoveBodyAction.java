package fisica;

class FRemoveBodyAction extends FWorldAction {
  protected FBody m_body;
  
  FRemoveBodyAction(FBody body) {
    m_body = body;
  }
  
  protected void apply(FWorld world) {
    world.removeBody(m_body);
  }
}