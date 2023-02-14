import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class LogHandler {
  private String filePath;
  private TreeSet<Score> scores;

  public LogHandler(String filePath) {
    this.setFilePath(filePath);
    this.setScores(new TreeSet<Score>());
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public void setScores(TreeSet<Score> scores) {
    this.scores = scores;
  }

  public TreeSet<Score> getScores() {
    return scores;
  }

  /**
   * Verifica existência do arquivo de log.
   */
  public boolean logFileExist() {
    File file = new File(this.filePath);
    return file.exists();
  }

  /**
   * Cria um novo arquivo em caso de não existência.
   */
  public void createLogFile() {
    if (!logFileExist()) {
      File file = new File(this.filePath);
      try {
        file.createNewFile();
        try (FileWriter fw = new FileWriter(file)) {
          fw.write("***** ANTIVIRUS GAME LOG *****");
          fw.write("\n\n");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Atualiza arquivo de log com score e data de finalização da partida.
   * 
   * @param score
   */
  public void logFileManipulation(Integer score) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.filePath, true))) {
      bw.write("Score: " + score);
      bw.newLine();
      bw.write("Date: " + LocalDateTime.now());
      bw.newLine();
      bw.newLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Recupera os dez maiores scores do arquivo de log.
   */
  private void recoverTopTenScores() {
    try (BufferedReader br = new BufferedReader(new FileReader(this.getFilePath()))) {
      String line;
      Integer score = null;
      LocalDateTime date = null;

      while ((line = br.readLine()) != null) {
        if (line.contains("Score:")) {
          score = Integer.valueOf(line.split(": ")[1]);
          continue;
        }

        if (line.contains("Date:")) {
          date = LocalDateTime.parse(line.split(": ")[1]);
          if (score != null && date != null) {
            this.scores.add(new Score(score, date));
            score = null;
            date = null;
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Função que retorna uma string contendo as dez maiores pontuações
   * ou todas pontuações as caso a árvore possua dez ou menos registros.
   */
  public String topTenToString() {
    recoverTopTenScores();
    DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm:ss");

    String str = this.scores.stream()
        .limit(10)
        .map(e -> String.format("%-15s", "Score: " + e.getPoints()) + " Date: " +
            e.getDate().format(dtFormatter))
        .collect(Collectors.joining("\n"));

    return str;
  }
}
