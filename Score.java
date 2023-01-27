import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Score implements Comparable<Score> {
  Integer score;
  LocalDateTime date;

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

  @Override
  public int compareTo(Score arg0) {
    if (this.getScore() > arg0.getScore())
      return 1;
    if (this.getScore() < arg0.getScore())
      return -1;
    return 0;
  }

  public void printScore() {
    DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    String s = "Score: " + this.getScore();
    System.out.println(String.format("%-15s", s) + " Date: " + this.getDate().format(dtFormatter));
  }
}