package com.example.telegrambot.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class apiCalls {

    String myUrl ="http://backend:8080";

    public boolean put(String token,String path,String body){
        try {
            // URL de la API a la que deseas hacer la solicitud PATCH
            String apiUrl = myUrl+path; // Reemplaza 123 con el valor de ID correcto

            // Tipo de contenido del cuerpo de la solicitud
            MediaType mediaType = MediaType.parse("application/json");

            // Crear un cliente OkHttpClient
            OkHttpClient client = new OkHttpClient();

            // Construir la solicitud PATCH con el cuerpo
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .put(RequestBody.create(mediaType, body))
                    .addHeader("Authorization", "Bearer "+token)
                    .build();

            // Realizar la solicitud PATCH
            Response response = client.newCall(request).execute();

            // Verificar el código de respuesta
            if (response.isSuccessful()) {
                // La solicitud PATCH fue exitosa
                System.out.println("Solicitud PUT exitosa.");

                return true;
            } else {
                // La solicitud PATCH no fue exitosa
                System.out.println("La solicitud PUT no fue exitosa. Código de respuesta: " + response.code());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean patch(String token,String path,String body){
        try {
            // URL de la API a la que deseas hacer la solicitud PATCH
            String url = myUrl+path;
            String apiUrl = url; // Reemplaza 123 con el valor de ID correcto

            System.out.println(url);

            // Cuerpo de la solicitud PATCH como una cadena JSON
            String jsonBody = body;
            System.out.println(body);

            // Tipo de contenido del cuerpo de la solicitud
            MediaType mediaType = MediaType.parse("application/json");

            // Crear un cliente OkHttpClient
            OkHttpClient client = new OkHttpClient();

            // Construir la solicitud PATCH con el cuerpo
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .patch(RequestBody.create(mediaType, jsonBody))
                    .addHeader("Authorization", "Bearer "+token)
                    .build();

            // Realizar la solicitud PATCH
            Response response = client.newCall(request).execute();

            // Verificar el código de respuesta
            if (response.isSuccessful()) {
                // La solicitud PATCH fue exitosa
                System.out.println("Solicitud PATCH exitosa.");
                return true;
            } else {
                // La solicitud PATCH no fue exitosa
                System.out.println("La solicitud PATCH no fue exitosa. Código de respuesta: " + response.code());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public JsonNode post(String token,String path){
        return post(token,path,"");
    }
    public JsonNode post(String token,String path,String body){
        try {
            // URL de la API a la que deseas hacer la solicitud PATCH
            String apiUrl = myUrl+path; // Reemplaza 123 con el valor de ID correcto

            // Tipo de contenido del cuerpo de la solicitud
            MediaType mediaType = MediaType.parse("application/json");

            // Crear un cliente OkHttpClient
            OkHttpClient client = new OkHttpClient();

            // Construir la solicitud PATCH con el cuerpo
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(RequestBody.create(mediaType, body))
                    .addHeader("Authorization", "Bearer "+token)
                    .build();

            // Realizar la solicitud PATCH
            Response response = client.newCall(request).execute();

            // Verificar el código de respuesta
            if (response.isSuccessful()) {
                // La solicitud PATCH fue exitosa
                System.out.println("Solicitud POST exitosa.");

                // Leer el cuerpo de la respuesta como String
                String responseBody = response.body().string();

                // Convertir el cuerpo de la respuesta a un JsonNode
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonResponse = mapper.readTree(responseBody);

                // Realizar acciones con el JsonNode según sea necesario
                // Por ejemplo, mostrar la respuesta
                System.out.println("Respuesta JSON: " + jsonResponse);

                return jsonResponse;
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

    public JsonNode get(String token,String path){
        try {
            // Crear una URL para la solicitud HTTP
            URL url = new URL(myUrl+path);

            // Abrir una conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Configurar el método de solicitud (GET, POST, etc.)

            connection.setRequestMethod("GET");

            connection.setRequestProperty("Authorization", "Bearer " + token);

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

                    System.out.println(response.toString());

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
