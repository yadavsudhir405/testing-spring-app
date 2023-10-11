package com.example.testingspringapp.config.realDataBase.global;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;

@Data
public class DbLoc {

    @EqualsAndHashCode.Exclude
    private Integer id;

    private String dbName;

    private String serverName;

    private int portNumber;

    private String instance;

    @EqualsAndHashCode.Exclude
    private int status;

    @EqualsAndHashCode.Exclude
    private Boolean isPilot;

    @EqualsAndHashCode.Exclude
    private ZonedDateTime pilotEndDate;

}
