package com.example.telegrambot.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.stream.Collectors;

public class UserApi {

    public String userLogin(String username,String password) {
        try {
            // Crear una URL para la solicitud HTTP
            URL url = new URL("http://backend:8080/");

            String body = "{\"username\":\""+username+"\", \"password\":"+password+"}";

            MediaType mediaType = MediaType.parse("application/json");

            // Crear un cliente OkHttpClient
            OkHttpClient client = new OkHttpClient();

            // Construir la solicitud PATCH con el cuerpo
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(mediaType, body))
                    .build();

            // Realizar la solicitud PATCH
            Response response = client.newCall(request).execute();

            // Verificar el código de respuesta
            if (response.isSuccessful()) {
                // La solicitud PATCH fue exitosa
                System.out.println("Solicitud POST exitosa.");

                String responseBody = response.body().string();



                ObjectMapper objectMapper = new ObjectMapper();
                String token = objectMapper.readTree(responseBody).get("token").asText();

                // Imprimir el token obtenido
                System.out.println("Token obtenido: " + token);


                return token;
            } else {
                // La solicitud PATCH no fue exitosa
                System.out.println("La solicitud POST no fue exitosa. Código de respuesta: " + response.code());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String userSignUp(String username,String email,String password) {
        try {
            // Crear una URL para la solicitud HTTP
            URL url = new URL("http://backend:8080/users");

            String body = "{" +
                    "\"username\":\""+username+"\"," +
                    "\"email\":\""+email+"\"," +
                    " \"password\":"+password+"" +
                    "}";

            MediaType mediaType = MediaType.parse("application/json");

            // Crear un cliente OkHttpClient
            OkHttpClient client = new OkHttpClient();

            // Construir la solicitud PATCH con el cuerpo
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(mediaType, body))
                    .build();

            // Realizar la solicitud PATCH
            Response response = client.newCall(request).execute();

            // Verificar el código de respuesta
            if (response.isSuccessful()) {
                // La solicitud PATCH fue exitosa
                System.out.println("Solicitud POST exitosa.");

                String responseBody = response.body().string();



                ObjectMapper objectMapper = new ObjectMapper();
                String token = objectMapper.readTree(responseBody).get("token").asText();

                // Imprimir el token obtenido
                System.out.println("Token obtenido: " + token);


                return token;
            } else {
                // La solicitud PATCH no fue exitosa
                System.out.println("La solicitud POST no fue exitosa. Código de respuesta: " + response.code());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public JsonNode getUserById(String id) {
        try {
            // Crear una URL para la solicitud HTTP
            URL url = new URL("http://backend:8080/users/" + id);

            // Abrir una conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Configurar el método de solicitud (GET, POST, etc.)

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            // Leer la respuesta

            System.out.println("Código de respuesta: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Obtener el cuerpo de la respuesta como un InputStream
                try (InputStream inputStream = connection.getInputStream()) {
                    // Lee la respuesta de la API
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String linea;

                    while ((linea = reader.readLine()) != null) {
                        response.append(linea);
                    }

                    // Cierra la conexión y el lector
                    reader.close();
                    connection.disconnect();

                    // Parsea el cuerpo de la respuesta como JSON utilizando Jackson
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonResponse = objectMapper.readTree(response.toString());

                    return jsonResponse;

                }
            } else {
                System.out.println("La solicitud HTTP no fue exitosa.");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
