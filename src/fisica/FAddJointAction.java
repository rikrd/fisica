package fisica;

class FAddJointAction extends FWorldAction {
  protected FJoint m_joint;
  
  FAddJointAction(FJoint joint) {
    m_joint = joint;
  }
  
  protected void apply(FWorld world) {
    world.addJoint(m_joint);
  }
}