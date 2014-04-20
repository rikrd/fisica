package fisica;

public interface FContactListener {
  public void contactStarted(FContact contact);

  public void contactPersisted(FContact contact);

  public void contactEnded(FContact contact);

  public void contactResult(FContactResult result);
}
