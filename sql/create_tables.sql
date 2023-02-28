CREATE TABLE adresy (
                        id_adresu    INTEGER NOT NULL,
                        ulica        VARCHAR2(40 CHAR) NOT NULL,
                        kod_pocztowy VARCHAR2(40 CHAR) NOT NULL,
                        miasto       VARCHAR2(40 CHAR) NOT NULL
);

ALTER TABLE adresy ADD CONSTRAINT adresy_pk PRIMARY KEY ( id_adresu );

CREATE TABLE kategorie_pensji (
                                  id_kategorii INTEGER NOT NULL,
                                  opis         VARCHAR2(100 CHAR),
                                  min_pensja   NUMBER(10, 2) NOT NULL,
                                  max_pensja   NUMBER(10, 2) NOT NULL
);

ALTER TABLE kategorie_pensji ADD CONSTRAINT kategorie_pensji_pk PRIMARY KEY ( id_kategorii );

CREATE TABLE statusy_pracownika (
                                    id_statusu INTEGER NOT NULL,
                                    opis       VARCHAR2(100 CHAR) NOT NULL
);

ALTER TABLE statusy_pracownika ADD CONSTRAINT statusy_pracownika_pk PRIMARY KEY ( id_statusu );

CREATE TABLE pracownicy (
                            id_pracownika     INTEGER NOT NULL,
                            imie              VARCHAR2(20 CHAR),
                            nazwisko          VARCHAR2(30),
                            nr_telefonu       VARCHAR2(9 CHAR),
                            pensja            NUMBER(10, 2),
                            data_zatrudnienia DATE,
                            id_uzytkownika    INTEGER NOT NULL,
                            id_statusu        INTEGER,
                            id_kategorii      INTEGER NOT NULL
);

ALTER TABLE pracownicy ADD CONSTRAINT pracownicy_pk PRIMARY KEY ( id_pracownika );

CREATE TABLE lekarze (
                         id_lekarza       INTEGER NOT NULL,
                         imie             VARCHAR2(20 CHAR) NOT NULL,
                         nazwisko         VARCHAR2(30 CHAR) NOT NULL,
                         nr_telefonu      VARCHAR2(9 CHAR),
                         id_uzytkownika   INTEGER NOT NULL,
                         id_specjalizacji INTEGER NOT NULL
);

ALTER TABLE lekarze ADD CONSTRAINT lekarze_pk PRIMARY KEY ( id_lekarza );

CREATE TABLE pacjenci (
                          pesel       VARCHAR2(11 CHAR) NOT NULL,
                          imie        VARCHAR2(20 CHAR) NOT NULL,
                          nazwisko    VARCHAR2(30 CHAR) NOT NULL,
                          nr_telefonu VARCHAR2(9 CHAR),
                          wzrost      NUMBER,
                          waga        NUMBER,
                          id_adresu   INTEGER NOT NULL
);

ALTER TABLE pacjenci ADD CONSTRAINT pacjenci_pk PRIMARY KEY ( pesel );

CREATE TABLE recepty (
                         id_recepty       INTEGER NOT NULL,
                         data_wystawienia DATE,
                         leki             VARCHAR2(200 CHAR) NOT NULL,
                         opis             VARCHAR2(1000 CHAR),
                         id_wizyty        INTEGER NOT NULL
);

ALTER TABLE recepty ADD CONSTRAINT recepty_pk PRIMARY KEY ( id_recepty );

CREATE TABLE specjalizacje (
                               id_specjalizacji INTEGER NOT NULL,
                               nazwa            VARCHAR2(40 CHAR) NOT NULL
);

ALTER TABLE specjalizacje ADD CONSTRAINT specjalizacja_pk PRIMARY KEY ( id_specjalizacji );

CREATE TABLE statusy_uzytkownika (
                                     id_statusu INTEGER NOT NULL,
                                     nazwa      VARCHAR2(20 CHAR) NOT NULL
);

ALTER TABLE statusy_uzytkownika ADD CONSTRAINT status_użytkownika_pk PRIMARY KEY ( id_statusu );

CREATE TABLE terminy (
                         id_terminu     INTEGER NOT NULL,
                         data_godzina   DATE NOT NULL,
                         id_lekarza     INTEGER NOT NULL,
                         pesel_pacjenta VARCHAR2(11 CHAR)
);

ALTER TABLE terminy ADD CONSTRAINT terminy_pk PRIMARY KEY ( id_terminu );

CREATE TABLE uzytkownicy (
                             id_uzytkownika INTEGER NOT NULL,
                             login          VARCHAR2(20 CHAR) NOT NULL,
                             haslo          VARCHAR2(40 CHAR) NOT NULL,
                             id_statusu     INTEGER NOT NULL,
                             CONSTRAINT uzytkownicy_login UNIQUE (login)
);

ALTER TABLE uzytkownicy ADD CONSTRAINT użytkownik_pk PRIMARY KEY ( id_uzytkownika );

CREATE TABLE zrealizowane_wizyty (
                                     id_wizyt       INTEGER NOT NULL,
                                     opis           VARCHAR2(1000 CHAR),
                                     data           DATE NOT NULL,
                                     pesel_pacjenta VARCHAR2(11 CHAR) NOT NULL,
                                     id_lekarza     INTEGER NOT NULL
);

ALTER TABLE zrealizowane_wizyty ADD CONSTRAINT zrealizowane_wizyty_pk PRIMARY KEY ( id_wizyt );

ALTER TABLE lekarze
    ADD CONSTRAINT lekarze_specjalizacje_fk FOREIGN KEY ( id_specjalizacji )
        REFERENCES specjalizacje ( id_specjalizacji );

ALTER TABLE lekarze
    ADD CONSTRAINT lekarze_uzytkownicy_fk FOREIGN KEY ( id_uzytkownika )
        REFERENCES uzytkownicy ( id_uzytkownika ) ON DELETE SET NULL;

ALTER TABLE pacjenci
    ADD CONSTRAINT pacjenci_adresy_fk FOREIGN KEY ( id_adresu )
        REFERENCES adresy ( id_adresu ) ON DELETE CASCADE;

ALTER TABLE recepty
    ADD CONSTRAINT recepty_zrealizowane_wizyty_fk FOREIGN KEY ( id_wizyty )
        REFERENCES zrealizowane_wizyty ( id_wizyt ) ON DELETE SET NULL;

ALTER TABLE terminy
    ADD CONSTRAINT terminy_lekarze_fk FOREIGN KEY ( id_lekarza )
        REFERENCES lekarze ( id_lekarza ) ON DELETE CASCADE;

ALTER TABLE terminy
    ADD CONSTRAINT terminy_pacjenci_fk FOREIGN KEY ( pesel_pacjenta )
        REFERENCES pacjenci ( pesel ) ON DELETE SET NULL;

ALTER TABLE uzytkownicy
    ADD CONSTRAINT uzytkownicy_statusy_fk FOREIGN KEY ( id_statusu )
        REFERENCES statusy_uzytkownika ( id_statusu ) ON DELETE SET NULL;

ALTER TABLE zrealizowane_wizyty
    ADD CONSTRAINT wizyty_pacjenci_fk FOREIGN KEY ( pesel_pacjenta )
        REFERENCES pacjenci ( pesel ) ON DELETE SET NULL;

ALTER TABLE zrealizowane_wizyty
    ADD CONSTRAINT zrealizowane_wizyty_lekarze_fk FOREIGN KEY ( id_lekarza )
        REFERENCES lekarze ( id_lekarza ) ON DELETE CASCADE;

ALTER TABLE pracownicy
    ADD CONSTRAINT pracownicy_kategorie_pensji_fk FOREIGN KEY ( id_kategorii )
        REFERENCES kategorie_pensji ( id_kategorii );

ALTER TABLE pracownicy
    ADD CONSTRAINT pracownicy_statusy_fk FOREIGN KEY ( id_statusu )
        REFERENCES statusy_pracownika ( id_statusu );

ALTER TABLE pracownicy
    ADD CONSTRAINT pracownicy_uzytkownicy_fk FOREIGN KEY ( id_uzytkownika )
        REFERENCES uzytkownicy ( id_uzytkownika );

DROP SEQUENCE lek_id;
DROP SEQUENCE term_id;
DROP SEQUENCE add_id;
DROP SEQUENCE spec_id;
DROP SEQUENCE u_id;
DROP SEQUENCE rec_id;

CREATE SEQUENCE lek_id
    MINVALUE 1
    START WITH 1
    INCREMENT BY 1
    CACHE 10;

CREATE SEQUENCE term_id
    MINVALUE 1
    START WITH 1
    INCREMENT BY 1
    CACHE 10;

CREATE SEQUENCE add_id
    MINVALUE 1
    START WITH 1
    INCREMENT BY 1
    CACHE 10;

CREATE SEQUENCE spec_id
    MINVALUE 1
    START WITH 1
    INCREMENT BY 1
    CACHE 10;

CREATE SEQUENCE u_id
    MINVALUE 1
    START WITH 1
    INCREMENT BY 1
    CACHE 10;

CREATE SEQUENCE rec_id
    MINVALUE 1
    START WITH 1
    INCREMENT BY 1
    CACHE 10;
