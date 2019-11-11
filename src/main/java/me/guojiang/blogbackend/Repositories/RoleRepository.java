package me.guojiang.blogbackend.Repositories;

import me.guojiang.blogbackend.Models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role findByRole(String role);
}
