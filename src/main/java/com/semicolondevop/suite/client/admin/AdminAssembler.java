package com.semicolondevop.suite.client.admin;

/* Aniefiok
 *created on 5/16/2020
 *inside the package */

import com.semicolondevop.suite.model.admin.Admin;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class AdminAssembler implements
        RepresentationModelAssembler<Admin, EntityModel<Admin>> {


    @Override
    public EntityModel<Admin> toModel(Admin admin) {

        return null;


    }

    @Override
    public CollectionModel<EntityModel<Admin>> toCollectionModel(Iterable<? extends Admin> entities) {
        return null;
    }

}
