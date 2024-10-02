package com.locadora.locadoraLivro.Users.Validation;

import com.locadora.locadoraLivro.Exceptions.CustomValidationException;
import com.locadora.locadoraLivro.Users.DTOs.CreateUserRequestDTO;
import com.locadora.locadoraLivro.Users.DTOs.UpdateUserRequestDTO;
import com.locadora.locadoraLivro.Users.models.UserModel;
import com.locadora.locadoraLivro.Users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@AllArgsConstructor
@Component
public class UserValidation {

    private final UserRepository userRepository;

    public void validateName(CreateUserRequestDTO data){
        if (userRepository.findByName(data.name()) != null){
            throw new CustomValidationException("Nome de usuário já em uso");
        }
    }

    public void validateNameUpdate(UpdateUserRequestDTO data, int id){
        UserModel userModel = userRepository.findById(id).get();

        if (!Objects.equals(userModel.getName(), data.name())){
            if (userRepository.findByName(data.name()) != null){
                throw new CustomValidationException("Nome de usuário já em uso");
            }
        }
    }

    public void validateEmail(CreateUserRequestDTO data) {
        if (userRepository.findByEmail(data.email()) != null) {
            throw new CustomValidationException("E-mail já em uso");
        }
    }

    public void validateUpdateEmail(UpdateUserRequestDTO data, int id) {
        UserModel userModel = userRepository.findById(id).get();

        if (!Objects.equals(userModel.getEmail(), data.email())){
            if (userRepository.findByEmail(data.email()) != null) {
                throw new CustomValidationException("E-mail já em uso");
            }
        }
    }
}