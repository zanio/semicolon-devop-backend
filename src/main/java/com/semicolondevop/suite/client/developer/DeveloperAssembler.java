package com.semicolondevop.suite.client.developer;
/*
 *@author tobi
 * created on 08/05/2020
 *
 */

import com.semicolondevop.suite.model.developer.Developer;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DeveloperAssembler implements
        RepresentationModelAssembler<Developer, EntityModel<Developer>> {


    @Override
    public EntityModel<Developer> toModel(Developer developer) {

        return new EntityModel<>(developer,
                linkTo(methodOn(DeveloperController.class).findOne(developer.getId())).withSelfRel(),
                linkTo(methodOn(DeveloperController.class).findAll()).withRel("savers"));
    }

    @Override
    public CollectionModel<EntityModel<Developer>> toCollectionModel(Iterable<? extends Developer> entities) {
        return null;
    }
}
