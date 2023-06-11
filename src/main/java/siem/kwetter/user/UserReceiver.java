package siem.kwetter.user;

import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class UserReceiver {
    @Inject UserService userService;

    List<User> users = new CopyOnWriteArrayList<>();
    @Incoming("user-channel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Blocking
    public void process(JsonObject u) {
        // log
        Log.info(u.toString());

        // JsonObject to User
        User user = u.mapTo(User.class);

        users.add(user);

        // post
        userService.create(user);
    }
}
