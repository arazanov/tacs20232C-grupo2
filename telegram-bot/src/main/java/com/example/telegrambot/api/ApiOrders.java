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

    public boolean addItemApi(String pedidoId,String userId,String body){
        try {
            // URL de la API a la que deseas hacer la solicitud PATCH
            String apiUrl = "http://localhost:8080/orders/"+pedidoId+"/"+userId; // Reemplaza 123 con el valor de ID correcto

            // Cuerpo de la solicitud PATCH como una cadena JSON
            String jsonBody = body;

            // Tipo de contenido del cuerpo de la solicitud
            MediaType mediaType = MediaType.parse("application/json");

            // Crear un cliente OkHttpClient
            OkHttpClient client = new OkHttpClient();

            // Construir la solicitud PATCH con el cuerpo
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(RequestBody.create(mediaType, jsonBody))
                    .build();

            // Realizar la solicitud PATCH
            Response response = client.newCall(request).execute();

            // Verificar el código de respuesta
            if (response.isSuccessful()) {
                // La solicitud PATCH fue exitosa
                System.out.println("Solicitud POST exitosa.");
                return true;
            } else {
                // La solicitud PATCH no fue exitosa
                System.out.println("La solicitud POST no fue exitosa. Código de respuesta: " + response.code());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean mySharePatch(String id,String body){
        try {
            // URL de la API a la que deseas hacer la solicitud PATCH
            String apiUrl = "http://localhost:8080/orders/"+id+"/"; // Reemplaza 123 con el valor de ID correcto

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





    public boolean closeOrderApi(String orderId,String userId,String body){
        try {
            // URL de la API a la que deseas hacer la solicitud PATCH
            String myUrl = "http://localhost:8080/orders/" +orderId+"/"+userId;
            String apiUrl = myUrl; // Reemplaza 123 con el valor de ID correcto

            System.out.println(myUrl);

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



    public boolean createOrdersByUserId(String id) {
        try {
            // Crear una URL para la solicitud HTTP
            URL url = new URL("http://localhost:8080/orders/" + id);

            // Abrir una conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Configurar el método de solicitud (GET, POST, etc.)

            connection.setRequestMethod("POST");

            int responseCode = connection.getResponseCode();
            // Leer la respuesta

            System.out.println("Código de respuesta: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                return true;
                }else {
                System.out.println("La solicitud HTTP no fue exitosa.");
                return false;
            }
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
    }




    public JsonNode getOrderById(String id) {
        try {
            // Crear una URL para la solicitud HTTP
            String myUrl = "http://localhost:8080/orders/" + id;
            URL url = new URL(myUrl);

            System.out.println(myUrl);

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



    public JsonNode getOrdersByUserId(String id) {
        try {
            // Crear una URL para la solicitud HTTP
            URL url = new URL("http://localhost:8080/user/" + id + "/orders");

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
