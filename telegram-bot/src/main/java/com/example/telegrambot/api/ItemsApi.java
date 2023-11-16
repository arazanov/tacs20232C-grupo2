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

public class ItemsApi extends apiCalls{

    String myUrl ="http://backend:8080";

    public boolean patchItemDescription(String token,String itemId,String description){return true;}

    public boolean patchItemQuantity(String token,String itemId,Integer qty){
        return true;
        /*
        try {
            // URL de la API a la que deseas hacer la solicitud PATCH
            String url = myUrl+"/orders/" +orderId;
            String apiUrl = url; // Reemplaza 123 con el valor de ID correcto

            System.out.println(url);

            // Cuerpo de la solicitud PATCH como una cadena JSON
            String jsonBody = "{\"closed\": true}";

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
        */

    }







    public JsonNode addItemApi(String pedidoId, String token){
        return super.post(token,"/orders/"+pedidoId+"/items");
    }


    public JsonNode getItemById(String id,String token) {
        return super.get(token,"/items/" + id);
    }


    public JsonNode getOrderItems(String pedidoId,String token){
        return super.get(token,"/orders/"+pedidoId+"/items");
    }

}
