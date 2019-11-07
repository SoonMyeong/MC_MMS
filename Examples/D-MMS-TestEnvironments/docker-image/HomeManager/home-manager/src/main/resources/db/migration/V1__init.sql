CREATE TABLE T_TEST
(
    id          int(11)     NOT NULL AUTO_INCREMENT,
    name        varchar(20) NOT NULL,
    department  varchar(20) NOT NULL,
    age         int(11),
    create_date datetime    NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE MMS
(
    MRN         VARCHAR(255) NOT NULL,
    IP          VARCHAR(39)  NOT NULL,
    PORT        int(6)       NOT NULL,
    CREATE_DATE DATETIME     NOT NULL,
    UPDATE_DATE DATETIME,
    PRIMARY KEY (MRN)
);

CREATE TABLE ENTITY_HOME_MMS
(
    MRN         VARCHAR(255) NOT NULL,
    MRN_MMS     VARCHAR(255) NOT NULL,
    CREATE_DATE DATETIME     NOT NULL,
    UPDATE_DATE DATETIME,
    PRIMARY KEY (MRN),
    FOREIGN KEY (MRN_MMS) REFERENCES MMS (MRN)
);
