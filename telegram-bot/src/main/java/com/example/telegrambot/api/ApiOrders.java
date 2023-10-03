package com.example.telegrambot.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiOrders {

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


    public boolean shareOrder(String id,String userBody) {
        try {
            // Crear una URL para la solicitud HTTP
            URL url = new URL("http://localhost:8080/orders/" + id);


            // Abrir una conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Configurar el método de solicitud (GET, POST, etc.)


            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            connection.setRequestMethod("POST");

            // Establecer el tipo de contenido del cuerpo de la solicitud
            connection.setRequestProperty("Content-Type", "application/json");

            // Habilitar la escritura en el cuerpo de la solicitud
            connection.setDoOutput(true);

            // Obtener el flujo de salida para escribir el cuerpo de la solicitud
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(userBody);
                outputStream.flush();
            }


            int responseCode = connection.getResponseCode();
            // Leer la respuesta

            System.out.println("Código de respuesta: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                return true;

            } else {
                System.out.println("La solicitud HTTP no fue exitosa.");
                return false;
            }
        } catch (IOException e) {

            e.printStackTrace();
            return false;
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
