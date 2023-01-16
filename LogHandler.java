import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;


public class LogHandler {
  String filePath;
  ArrayList<Score> scores;

  public LogHandler(String filePath) {
    this.filePath = filePath;
    this.scores = new ArrayList<>();
  }

  public String getFilePath() {
    return filePath;
  }

  public ArrayList<Score> getScores() {
    return scores;
  }

  // Verifica existência do arquivo de log;
  public boolean logFileExist(){
    File file = new File(this.filePath);
    return file.exists();
  }

  // Caso o arquivo de log não exista, cria um novo;
  public void createLogFile(){
    if(!logFileExist()){
      File file = new File(this.filePath);
      try {
        file.createNewFile();
        try (FileWriter fw = new FileWriter(file)) {
          fw.write("***** ANTIVÍRUS GAME LOG *****");
          fw.write("\n\n");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // Atualiza arquivo de log com score e data de finalização da partida;
  public void logFileManipulation(Integer score){
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

  // Recupera os 10 maiores scores do arquivo de log (caso houver);
  public void topTenScores(){
    try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
      String line;
      Integer score = null;
      LocalDateTime date = null;

      // Looping para buscar os valores de score e data no arquivo;
      while((line = br.readLine()) != null){
        if(line.contains("Score:")){
          score = Integer.valueOf(line.split(": ")[1]);
          continue;
        }

        if(line.contains("Date:")){
          date = LocalDateTime.parse(line.split(": ")[1]);
          if(score != null && date != null){
            this.scores.add(new Score(score, date));
            score = null;
            date = null;
          }
        }
      }

      // Ordenação decrescente;
      Collections.sort(this.scores, Collections.reverseOrder());

      // Busca dos 10 maiores scores (caso houver);
      for (int i = 0; i < this.scores.size(); i++){
        if(i > 9) break;
        this.scores.get(i).printScore();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
