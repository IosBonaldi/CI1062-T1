import java.time.LocalDateTime;

public class Score implements Comparable<Score> {
  private Integer score;
  private LocalDateTime date;

  public Score(Integer score, LocalDateTime date) {
    this.setScore(score);
    this.setDate(date);
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  /**
   * Sobreposição do método compareTo para possibilitar a inserção
   * ordenada decrescente na árvore.
   */
  @Override
  public int compareTo(Score arg0) {
    if (this.getScore() < arg0.getScore())
      return 1;
    if (this.getScore() > arg0.getScore())
      return -1;
    return 0;
  }
}