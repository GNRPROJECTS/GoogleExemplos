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

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.DriveScopes;

import java.awt.Desktop;
import java.net.URI;
import java.util.Arrays;

/**
 * @author Adao@google.com (Your Name Here)
 *
 */
public class autorizacao {

  // CLiente ID da API Google Drive
  private static String CLIENT_ID =
      "544454124567-7514gcpfs1eipk7ed8kq5c0jasa7puie.apps.googleusercontent.com";

  // arquivo CLIENT_SECRET json do cliente da API Google Drive
  private static String CLIENT_SECRET = "client_secret.json";

  // a REDIRECT_URI vai ser a mesma sempre (provavelmente)
  private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

  public static void main(String[] args) {


    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();

    // gera um código de autorização
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
        jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
            .setAccessType("online").setApprovalPrompt("auto").build();

    String urlAuthorization = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();


    try {
      Desktop desktop = Desktop.getDesktop();
      URI uri = new URI(urlAuthorization);
      desktop.browse(uri);

    } catch (Exception ex) {
      System.err.println("Erro ao abrir página");
      ex.printStackTrace();

    }
  }

}
