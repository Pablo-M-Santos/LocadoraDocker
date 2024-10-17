package com.locadora.locadoraLivro.Renters.repositories;

import com.locadora.locadoraLivro.Renters.models.RenterModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    RenterModel findByEmail(String email);
    RenterModel findByCpf(String cpf);
    Page<RenterModel> findAllByIsDeletedFalse(Pageable pageable);
    List<RenterModel> findAllByIsDeletedFalse(Sort sort);
    List<RenterModel> findAllByEmail(String email);

    @Query("SELECT u FROM RenterModel u WHERE LOWER(REPLACE(u.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:name, ' ', ''), '%')) AND u.isDeleted = false")
    List<RenterModel> findAllByName(@Param("name") String name, Sort sort);

    @Query("SELECT u FROM RenterModel u WHERE LOWER(REPLACE(u.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:name, ' ', ''), '%')) AND u.isDeleted = false")
    Page<RenterModel> findAllByName(@Param("name") String name, Pageable pageable);
}