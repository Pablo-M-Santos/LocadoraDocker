package com.locadora.locadoraLivro.Renters.repositories;

import com.locadora.locadoraLivro.Renters.models.RenterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RenterRepository extends JpaRepository<RenterModel, Integer> {
    UserDetails findByName(String name);
    RenterModel findByCpf(String cpf);
    List<RenterModel> findAllByEmail(String email);
    List<RenterModel> findAllByTelephone(String telephone);

    List<RenterModel> findAllByIsDeletedFalse();
    List<RenterModel> findAllByIsDeletedFalse(Sort id);

    @Query("SELECT u FROM RenterModel u WHERE LOWER(REPLACE(u.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:name, ' ', ''), '%'))")
    List<RenterModel> findAllByName(@Param("name") String name);
}
