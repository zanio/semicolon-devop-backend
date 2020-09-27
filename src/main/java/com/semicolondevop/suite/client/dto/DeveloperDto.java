package com.semicolondevop.suite.client.dto;


import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

import java.sql.Date;

@Data
public class DeveloperDto {

    private JsonNullable<String> firstname = JsonNullable.undefined();

    private JsonNullable<String> lastname = JsonNullable.undefined();

    private JsonNullable<String> phoneNumber = JsonNullable.undefined();

    private JsonNullable<Date> dateJoined = JsonNullable.undefined();

    private JsonNullable<String> bvn  = JsonNullable.undefined();


}
