package siem.kwetter.user;

import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
public class UserReceiver {
    @Inject UserService userService;
    @Incoming("user-channel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Blocking
    public void process(JsonObject u) {
        // log
        Log.info(u.toString());

        // JsonObject to User
        User user = u.mapTo(User.class);

        // entity detached workaround >:(
        User user2 = new User();
        user2.userState = user.userState;
        user2.name = user.name;
        user2.email = user.email;
        user2.picture = user.picture;
        user2.nickname = user.nickname;
        user2.sub = user.sub;
        user2.updated_at = user.updated_at;
        user2.role = user.role;

        // post
        userService.create(user2);
    }
}
