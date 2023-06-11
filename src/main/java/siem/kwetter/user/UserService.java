package siem.kwetter.user;

import io.quarkus.logging.Log;
import lombok.AllArgsConstructor;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@AllArgsConstructor
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    EntityManager entityManager;
    public List<User> getUsers() {
        return userRepository.listAll();
    }

    public Optional<User> findProfileById(long id) {
        return userRepository.findByIdOptional(id);
    }

    @Transactional
    public void createOrUpdate(User user) {
        Optional<User> existingUser = userRepository.findByIdOptional(user.getId());
        if (existingUser.isPresent()) {
            User persistedUser = existingUser.get();
            persistedUser.setUserState(user.getUserState());
            persistedUser.setName(user.getName());
            persistedUser.setEmail(user.getEmail());
            persistedUser.setPicture(user.getPicture());
            persistedUser.setNickname(user.getNickname());
            persistedUser.setSub(user.getSub());
            persistedUser.setUpdated_at(user.getUpdated_at());

            entityManager.merge(persistedUser); // Use merge to update the detached entity
        } else {
            entityManager.persist(user); // Use persist for new entities
        }
    }

    @Transactional
    public Boolean create(User user) {
        userRepository.persist(user);
        return user.isPersistent();
    }

    @Transactional
    public User update(User user) {
        return userRepository.update(user).orElseThrow(() -> new InvalidParameterException("Profile not found"));
    }

    @Transactional
    public boolean delete(long id) {
        return userRepository.deleteById(id);
    }
}
