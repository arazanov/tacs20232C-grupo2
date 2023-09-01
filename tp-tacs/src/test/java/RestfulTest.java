import com.fasterxml.jackson.databind.ObjectMapper;
import model.ItemType;
import model.User;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repositories.ItemTypeRepository;
import repositories.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Comparator;

public class RestfulTest {

    private final String BASE_URL = "http://localhost:8080/";
    private CloseableHttpClient client;

    @Before
    public void createClient() {
        client = HttpClients.createDefault();
    }

    @After
    public void closeClient() throws IOException {
        client.close();
    }

    private ItemType getItemType(int id) throws IOException, URISyntaxException {
        URL url = new URI(BASE_URL + "itemTypes/" + id).toURL();
        InputStream input = url.openStream();
        return new ObjectMapper().readValue(new InputStreamReader(input), ItemType.class);
    }

    private User getUser(int id) throws IOException, URISyntaxException {
        URL url = new URI(BASE_URL + "users/" + id).toURL();
        InputStream input = url.openStream();
        return new ObjectMapper().readValue(new InputStreamReader(input), User.class);
    }

    @Test
    public void getItemTypeMethod() throws IOException, URISyntaxException {
        Comparator<ItemType> itemTypeComparator = Comparator
                .comparingInt(ItemType::getId)
                .thenComparing(ItemType::getName)
                .thenComparing(ItemType::getDescription)
                .thenComparingDouble(ItemType::getPrice);
        Assert.assertEquals(0, itemTypeComparator.compare(new ItemTypeRepository().get(1), getItemType(1)));
    }

    @Test
    public void getUserMethod() throws IOException, URISyntaxException {
        Comparator<User> UserComparator = Comparator
                .comparing(User::getUsername);
        Assert.assertEquals(0, UserComparator.compare(new UserRepository().get(2), getUser(2)));
    }
}
