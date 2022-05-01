package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    /**
     * Metodo criado de acordo com as regras do JPA find By ProtertyName entao a JPA
     * ja sabe que tem que buscar na tabela user pelo campo email
     * @param email
     * @return
     */
    User findByEmail(String email);
}
