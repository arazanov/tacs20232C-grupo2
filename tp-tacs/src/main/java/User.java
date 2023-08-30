import lombok.Getter;

@Getter
public class User {

    public User(String username) {
        this.username = username;
    }

    private String username;

}