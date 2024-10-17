package com.locadora.locadoraLivro.Publishers.Validation;

import com.locadora.locadoraLivro.Books.repositories.BookRepository;
import com.locadora.locadoraLivro.Exceptions.CustomValidationException;
import com.locadora.locadoraLivro.Publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.locadoraLivro.Publishers.DTOs.UpdatePublisherRecordDTO;
import com.locadora.locadoraLivro.Publishers.models.PublisherModel;
import com.locadora.locadoraLivro.Publishers.repositories.PublisherRepository;
import com.locadora.locadoraLivro.Rents.models.RentStatusEnum;
import com.locadora.locadoraLivro.Rents.repositories.RentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@AllArgsConstructor
@Component
public class PublisherValidation {

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private BookRepository bookRepository;

    public void validName(CreatePublisherRequestDTO data){
        if (publisherRepository.findByNameAndIsDeletedFalse(data.name())!= null){
            throw new CustomValidationException("Nome já em uso.");
        }
    }

    public void validNameUpdate(UpdatePublisherRecordDTO data, int id){
        PublisherModel publisher = publisherRepository.findById(id).get();

        if (!Objects.equals(publisher.getName(), data.name())){
            if (publisherRepository.findByName(data.name())!= null){
                throw new CustomValidationException("Nome já em uso.");
            }
        }
    }

    public void validEmail(CreatePublisherRequestDTO data){
        if (publisherRepository.findByEmail(data.email()) != null){
            throw new CustomValidationException("E-mail já em uso.");
        }
    }

    public void validEmailUpdate(UpdatePublisherRecordDTO data, int id){
        PublisherModel publisher = publisherRepository.findById(id).get();

        if (!Objects.equals(publisher.getEmail(), data.email())){
            if (publisherRepository.findByEmail(data.email()) != null){
                throw new CustomValidationException("E-mail já em uso.");
            }
        }
    }

    public void validTelephone(CreatePublisherRequestDTO data){
        if (publisherRepository.findByTelephone(data.telephone())!= null){
            throw new CustomValidationException("Este telefone já está em uso.");
        }
    }

    public void validTelephoneUpdate(UpdatePublisherRecordDTO data, int id){
        PublisherModel publisher = publisherRepository.findById(id).get();

        if (!Objects.equals(publisher.getTelephone(), data.telephone())){
            if (publisherRepository.findByTelephone(data.telephone())!= null){
                throw new CustomValidationException("Este telefone já está em uso.");
            }
        }
    }

    public void validSite(CreatePublisherRequestDTO data){
        if (!Objects.equals(data.site(), "")){
            if (publisherRepository.findBySite(data.site()) != null){
                throw new CustomValidationException("Este site já está em uso.");
            }
        }
    }

    public void validSiteUpdate(UpdatePublisherRecordDTO data, int id){
        PublisherModel publisher = publisherRepository.findById(id).get();

        if (!Objects.equals(data.site(), "")) {
            if (!Objects.equals(publisher.getSite(), data.site())){
                if (publisherRepository.findBySite(data.site()) != null) {
                    throw new CustomValidationException("Este site já está em uso.");
                }
            }
        }
    }

    public void validDeletePublisher(int id) {
        var books = bookRepository.findByPublisherId(id);
        for (var book : books) {
            if (rentRepository.existsByBookIdAndStatus(book.getId(), RentStatusEnum.ALUGADO)) {
                throw new CustomValidationException("Não é possível excluir o editor. Existem livros atualmente alugados.");
            }
        }
    }

}