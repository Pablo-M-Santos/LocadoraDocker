package com.locadora.locadoraLivro.Renters.Validation;

import com.locadora.locadoraLivro.Exceptions.CustomValidationException;
import com.locadora.locadoraLivro.Renters.repositories.RenterRepository;
import org.springframework.stereotype.Component;

@Component
public class RenterValidation {

    private final RenterRepository renterRepository;

    public RenterValidation(RenterRepository renterRepository) {
        this.renterRepository = renterRepository;
    }

    public void validateRenter(String cpf, String email, String telephone) {

        if (cpf != null && renterRepository.findByCpf(cpf) != null) {
            throw new CustomValidationException("Esse CPF já está em uso.");
        }

        if (email != null && !renterRepository.findAllByEmail(email).isEmpty()) {
            throw new CustomValidationException("Esse E-mail já está em uso.");
        }


        if (telephone != null && !renterRepository.findAllByTelephone(telephone).isEmpty()) {
            throw new CustomValidationException("Esse telefone já está em uso.");
        }
    }


}
