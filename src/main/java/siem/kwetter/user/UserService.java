package siem.kwetter.user;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.directory.InvalidAttributesException;
import javax.transaction.Transactional;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@AllArgsConstructor
public class UserService {

    @Inject
    UserRepository userRepository;

    public List<User> getKweets(String userState) {
        if(StringUtils.isNotBlank(userState)) {
            return userRepository.findByMessage(userState);
        }

        return userRepository.listAll();
    }

    public Optional<User> findProfileById(long id) {
        return userRepository.findByIdOptional(id);
    }

    @Transactional
    public void create(User user) throws InvalidAttributesException {
        if (user.getId() != null) {
            throw new InvalidAttributesException("Id must be filled");
        }
        Validate.notNull(user, "Profile can not be null");
        Validate.notBlank(user.getUserState(), "Userstate can not be empty");

        userRepository.persist(user);
    }

    @Transactional
    public User replace(long id, User user) {
        user.setId(id);
        return userRepository.update(user).orElseThrow(() -> new InvalidParameterException("Profile not found"));
    }

    @Transactional
    public boolean delete(long id) {
        return userRepository.deleteById(id);
    }
}
