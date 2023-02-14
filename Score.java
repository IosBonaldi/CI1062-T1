import java.time.LocalDateTime;

public class Score implements Comparable<Score> {
  private int points;
  private LocalDateTime date;

  public Score(int points, LocalDateTime date) {
    this.setPoints(points);
    this.setDate(date);
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int score) {
    if (0 > score || score < this.points)
      throw new IllegalArgumentException();
    this.points = score;
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
    if (this.getPoints() < arg0.getPoints())
      return 1;
    if (this.getPoints() > arg0.getPoints())
      return -1;
    return 0;
  }
}