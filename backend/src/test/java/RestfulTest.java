import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.Order;
import com.model.User;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.repositories.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RestfulTest {

    private final String BASE_URL = "http://localhost:8080/";
    private CloseableHttpClient client;
    private Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().setPrettyPrinting().create();

    @Before
    public void createClient() {
        client = HttpClients.createDefault();
    }

    @After
    public void closeClient() throws IOException {
        client.close();
    }

    private User getUser(int id) {
        try {
            URL url = new URI(BASE_URL + "users/" + id).toURL();
            InputStream input = url.openStream();
            return new ObjectMapper().readValue(new InputStreamReader(input), User.class);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Order getOrder(int id) {
        try {
            URL url = new URI(BASE_URL + "orders/" + id).toURL();
            InputStream input = url.openStream();
            return new ObjectMapper().readValue(new InputStreamReader(input), Order.class);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpResponse post(String path, String json) {
        HttpPost httpPost = new HttpPost(BASE_URL + path);
        if(json != null) httpPost.setEntity(new StringEntity(json));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        try {
            return client.execute(httpPost);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpResponse patch(String path, String json) {
        HttpPatch httpPatch = new HttpPatch(BASE_URL + path);
        if(json != null) httpPatch.setEntity(new StringEntity(json));
        httpPatch.setHeader("Accept", "application/json");
        httpPatch.setHeader("Content-type", "application/json");
        try {
            return client.execute(httpPatch);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpResponse postOrder(String username) {
        String path = "orders";
        String json = "{\"username\":\"" + username + "\"}";
        return post(path, json);
    }

    private HttpResponse shareOrder(int id) {
        String path = "orders/" + id + "/share";
        String json = "{\"username\":\"carla\"}";
        return post(path, json);
    }

    private HttpResponse postItem(int id, int userId) {
        String path = "orders/" + id + "/addItems/" + userId;
        String json = "{\"description\":\"pizza napolitana\",\"quantity\":2}";
        return post(path, json);
    }

    private HttpResponse closeOrder(int id, int userId) {
        String path = "orders/" + id + "/close/" + userId;
        String json = null;
        return patch(path, json);
    }

    @Test
    public void createOrder() {
        List<HttpResponse> responseList = new ArrayList<>();

        responseList.add(postOrder("pepe"));
        responseList.add(postItem(2, 1));
        responseList.add(shareOrder(2));
        responseList.add(postItem(2, 2));
        responseList.add(closeOrder(2, 1));

        int sum = responseList.stream().mapToInt(HttpResponse::getCode).reduce(0, Integer::sum);
        System.out.println(gson.toJson(getOrder(2)));
        Assert.assertEquals(200 * 5, sum);
    }

    @Test
    public void getUserMethod() {
        Assert.assertEquals(new UserRepository().findById(2).map(User::getUsername).orElse(""), getUser(2).getUsername());
    }
}
