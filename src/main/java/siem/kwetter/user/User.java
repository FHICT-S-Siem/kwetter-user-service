package siem.kwetter.user;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Column;

@Entity(name = "UserEntity")
@NoArgsConstructor
@AllArgsConstructor
public class User extends PanacheEntity {

    @Column(nullable = false)
    public String userState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }
}
