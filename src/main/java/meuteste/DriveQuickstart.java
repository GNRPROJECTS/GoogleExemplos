package meuteste;



import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class DriveQuickstart {
  // nome da aplicação
  private static final String APPLICATION_NAME = "Drive API Java Quickstart";

  // diretório para armazenar as credênciais da aplicação
  private static final java.io.File DATA_STORE_DIR =
      new java.io.File(System.getProperty("user.home"), ".credentials/drive-java-quickstart");

  // instância global da classe FileDataStoreFactory
  private static FileDataStoreFactory DATA_STORE_FACTORY;

  // instância global da classe JsonFactory
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  private static HttpTransport HTTP_TRANSPORT;

  /**
   * Scopo de instâncias globais requeridas. Se modificado o scopo, deletar as credenciais
   * previamente salvas em ~/.credentials/drive-java-quickstart
   */
  private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);

  static {
    try {
      HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
      DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Cria um objeto Credential autorizado.
   * 
   * @return objeto Credential autorizado.
   * 
   */
  public static Credential authorize() throws IOException {
    // Carrega o cliente secreto.
    InputStream in = DriveQuickstart.class.getResourceAsStream("client_secret.json");

    // Cria um objeto GoogleClientSecrets para receber o json do cliente
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // ** Cria um fluxo e ativa solicitação de autorização do usuário.
    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("online").build();
    Credential credential =
        new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
    return credential;
  }

  /**
   * Build and return an authorized Drive client service.
   * 
   * @return an authorized Drive client service
   * @throws IOException
   */
  public static Drive getDriveService() throws IOException {
    Credential credential = authorize();
    return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
        .setApplicationName(APPLICATION_NAME).build();
  }

  public static void main(String[] args) throws IOException {
    // Build a new authorized API client service.
    Drive service = getDriveService();

    // Print the names and IDs for up to 10 files.
    FileList result = service.files().list().setPageSize(10)
        .setFields("nextPageToken, files(id, name)").execute();
    List<File> files = result.getFiles();
    if (files == null || files.size() == 0) {
      System.out.println("No files found.");
    } else {
      System.out.println("Files:");
      for (File file : files) {
        System.out.printf("%s (%s)\n", file.getName(), file.getId());
      }
    }
  }

}
