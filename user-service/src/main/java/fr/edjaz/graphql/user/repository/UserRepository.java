package fr.edjaz.graphql.user.repository;

import fr.edjaz.graphql.user.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class UserRepository {

    private static List<User> userList;

    static {
        userList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            userList.add(new User(String.valueOf(i), "person" + i, "book" + i));
        }
    }

    public List<User> getUserList() {
        return userList;
    }

    public User getUser(String id) {
        return userList.stream().filter(user -> Objects.equals(user.getId(), id)).findFirst().orElse(null);
    }


}
