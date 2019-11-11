package me.guojiang.blogbackend.Repositories;

import me.guojiang.blogbackend.Models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User getUserByUsername(String name);

    User getUserById(Long id);

    Integer countUserByUsername(String username);

    boolean existsByUsername(String username);

    //
//    @Query("")
//    User addUser(User user);
}
