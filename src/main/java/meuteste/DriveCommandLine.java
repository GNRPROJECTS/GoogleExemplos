/*
 * Copyright (c) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package meuteste;

/**
 * @author Adao@google.com (Your Name Here)
 *
 */
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;

public class DriveCommandLine {
  // adicione aqui o CLIENT_ID que nós criamos
  private static String CLIENT_ID =
      "544454124567-7514gcpfs1eipk7ed8kq5c0jasa7puie.apps.googleusercontent.com";
  // adicione aqui o CLIENT_SECRET que nós criamos
  private static String CLIENT_SECRET = "client_secret.json";
  // a REDIRECT_URI vai ser a mesma sempre (provavelmente)
  private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

  public static void main(String[] args) throws IOException {


    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();

    // gera um código de autorização
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
        jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
            .setAccessType("online").setApprovalPrompt("auto").build();

    String urlAuthorization = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();

    // abrir a uri automaticamente
    try {
      Desktop desktop = Desktop.getDesktop();
      URI uri = new URI(urlAuthorization);
      desktop.browse(uri);
    } catch (Exception ex) {
      System.err.println("Erro ao abrir página");
      ex.printStackTrace();

    }


    // aqui ele lê o código que retorna do site
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    String code = br.readLine();



    GoogleTokenResponse response =
        flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();

    GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response); // Cria um
                                                                                         // nova
                                                                                         // autorização
                                                                                         // do API
                                                                                         // client

    Drive service = new Drive.Builder(httpTransport, jsonFactory, credential).build(); // Insere um
                                                                                       // arquivo
    File body = new File();
    // título
    body.setTitle("My document");
    // descrição
    body.setDescription("A test document");
    body.setMimeType("text/plain");

    // local do arquivo, lembre-se de colocar um arquivo na pasta do projeto
    java.io.File fileContent = new java.io.File("document.txt");

    FileContent mediaContent = new FileContent("text/plain", fileContent);
    File file = service.files().insert(body, mediaContent).execute();
    System.out.println("File ID: " + file.getId());

  }
}// fim da classe DriveCommandLine

