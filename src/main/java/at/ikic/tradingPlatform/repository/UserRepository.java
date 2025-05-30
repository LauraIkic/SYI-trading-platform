package at.ikic.tradingPlatform.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import at.ikic.tradingPlatform.entity.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository <User, UUID>{
    User findByMail(String mail);

}
