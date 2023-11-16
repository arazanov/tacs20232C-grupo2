package com.example.telegrambot.api;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiOrders {



    String myUrl ="http://backend:8080";



    public boolean mySharePatch(String id,String body){
        try {
            // URL de la API a la que deseas hacer la solicitud PATCH
            String apiUrl = myUrl+"/orders/"+id+"/"; // Reemplaza 123 con el valor de ID correcto

            // Cuerpo de la solicitud PATCH como una cadena JSON
            String jsonBody = body;

            // Tipo de contenido del cuerpo de la solicitud
            MediaType mediaType = MediaType.parse("application/json");

            // Crear un cliente OkHttpClient
            OkHttpClient client = new OkHttpClient();

            // Construir la solicitud PATCH con el cuerpo
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .patch(RequestBody.create(mediaType, jsonBody))
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
    private boolean orderPatch(String token,String orderId,String body){
        try {
            // URL de la API a la que deseas hacer la solicitud PATCH
            String url = myUrl+"/orders/" +orderId;
            String apiUrl = url; // Reemplaza 123 con el valor de ID correcto

            System.out.println(url);

            // Cuerpo de la solicitud PATCH como una cadena JSON
            String jsonBody = body;

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

    public boolean patchOrderName(String userToken,String orderId,String name){
        return orderPatch(userToken,orderId,"{\"description\": \""+name+"\"}");
    }


    public boolean closeOrderApi(String orderId,String token){
        return orderPatch(token,orderId,"{\"closed\": true}");
    }

    public boolean shareOrderApi(String orderId,String token,String username){
        return orderPatch(token,orderId,"{\"description\": \""+username+"\"}");
    }



    public JsonNode createOrdersByUserId(String token) {
        try {
            // Crear una URL para la solicitud HTTP
            URL url = new URL(myUrl+"/orders");

            // Abrir una conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Configurar el método de solicitud (GET, POST, etc.)

            connection.setRequestMethod("POST");

            connection.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = connection.getResponseCode();
            // Leer la respuesta

            System.out.println("Código de respuesta: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Convertir la respuesta a un JsonNode
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonResponse = mapper.readTree(response.toString());

                // Realizar acciones con el JsonNode según sea necesario
                // Por ejemplo, mostrar la respuesta
                System.out.println("Respuesta JSON: " + jsonResponse);

                return jsonResponse;
                }else {
                System.out.println("La solicitud HTTP no fue exitosa.");
                return null;
            }
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
    }




    public JsonNode getOrderById(String id,String token) {
        try {
            // Crear una URL para la solicitud HTTP
            String url = myUrl+"/orders/" + id;
            URL apiUrl = new URL(url);

            System.out.println(url);

            // Abrir una conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
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



    public JsonNode getOrdersByUserId(String token) {
        try {
            // Crear una URL para la solicitud HTTP
            URL url = new URL(myUrl+"/orders");

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
