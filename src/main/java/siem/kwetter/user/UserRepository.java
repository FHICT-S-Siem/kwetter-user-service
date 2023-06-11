package siem.kwetter.user;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;
import org.apache.commons.text.WordUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    public void persist(User user) {
        var message = WordUtils.capitalize(user.getUserState());
        user.setUserState(message);
        Log.info("In UserRepository voor persist");
        PanacheRepository.super.persist(user);
        Log.info("In UserRepository na persist");
    }

    @Override
    public User findById(Long id) {
        return PanacheRepository.super.findById(id);
    }

    public List<User> findByMessage(String userState) {
        return this.list("userState", WordUtils.capitalize(userState));
    }

    public Optional<User> update(User user) {
        final var id = user.getId();
        var savedOpt = this.findByIdOptional(id);
        if (savedOpt.isEmpty()) {
            return Optional.empty();
        }

        var saved = savedOpt.get();
        saved.setUserState(user.getUserState());

        return Optional.of(saved);
    }
}
