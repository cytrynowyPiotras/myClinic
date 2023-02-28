CREATE OR REPLACE PROCEDURE dodaj_uzytkownika_pracownika (
  v_imie VARCHAR2,
  v_nazwisko  VARCHAR2,
  v_nr_telefonu  VARCHAR2,
  v_login VARCHAR2,
  v_haslo VARCHAR2,
  v_pensja NUMBER,
  v_id NUMBER,
  v_id_statusu_u NUMBER,
  v_id_kategorii NUMBER
) AS
BEGIN
INSERT INTO uzytkownicy (id_uzytkownika, login, haslo, id_statusu)
VALUES (v_id,v_login, v_haslo, v_id_statusu_u);

INSERT INTO pracownicy (id_pracownika, imie, nazwisko, nr_telefonu, pensja, data_zatrudnienia, id_uzytkownika, id_statusu, id_kategorii)
VALUES (v_id, v_imie, v_nazwisko, v_nr_telefonu, v_pensja, SYSDATE, v_id, 1, v_id_kategorii);
END;
/

CREATE OR REPLACE PROCEDURE dodaj_nowego_lekarza (
  v_imie VARCHAR2,
  v_nazwisko  VARCHAR2,
  v_nr_telefonu  VARCHAR2,
  v_login VARCHAR2,
  v_haslo VARCHAR2,
  v_pensja NUMBER,
  v_id NUMBER,
  v_id_specjalizacji NUMBER,
  v_id_kategorii NUMBER
) AS
BEGIN
INSERT INTO uzytkownicy (id_uzytkownika, login, haslo, id_statusu)
VALUES (v_id, v_login, v_haslo, 1);

INSERT INTO pracownicy (id_pracownika, imie, nazwisko, nr_telefonu, pensja, data_zatrudnienia, id_uzytkownika, id_statusu, id_kategorii)
VALUES (v_id, v_imie, v_nazwisko, v_nr_telefonu, v_pensja, SYSDATE, v_id, 1, v_id_kategorii);

INSERT INTO lekarze (id_lekarza, imie, nazwisko, nr_telefonu, id_uzytkownika, id_specjalizacji)
VALUES (lek_id.nextval, v_imie, v_nazwisko, v_nr_telefonu, v_id, v_id_specjalizacji);
END;
/

CREATE OR REPLACE PROCEDURE update_salary_bonus(e_id NUMBER, v_bon NUMBER)
AS
    v_new_salary NUMERIC;
    v_max_salary NUMERIC;
    v_category_id NUMBER;
    v_b NUMBER(10, 2) := v_bon/100;
BEGIN
    IF v_bon >= 100 THEN RAISE_APPLICATION_ERROR(-20001, 'Invalid bonus'); END IF;
    SELECT emp_bonus(e_id, v_b) INTO v_new_salary FROM dual;
    SELECT id_kategorii INTO v_category_id FROM pracownicy WHERE id_pracownika = e_id;
    SELECT max_pensja INTO v_max_salary FROM kategorie_pensji WHERE id_kategorii = v_category_id;
    IF v_new_salary > v_max_salary THEN v_new_salary := v_max_salary; END IF;
    UPDATE pracownicy SET pensja = v_new_salary WHERE id_pracownika = e_id;
EXCEPTION
    WHEN no_data_found THEN dbms_output.put_line('no employee found');
    RAISE no_data_found;
END;
/

CREATE OR REPLACE PROCEDURE update_bonus_all_employees(v_bon NUMBER)
AS
    CURSOR cr IS
        SELECT id_pracownika FROM pracownicy;
    v_emp_id NUMBER;
BEGIN
    OPEN cr;
    LOOP
        EXIT WHEN cr%NOTFOUND;
        FETCH cr INTO v_emp_id;
        update_salary_bonus (v_emp_id, v_bon);
    END LOOP;
    CLOSE cr;
END;
/


create or replace PROCEDURE dodaj_nowego_pacjenta (
  v_pesel VARCHAR2,
  v_imie VARCHAR2,
  v_nazwisko  VARCHAR2,
  v_nr_telefonu  VARCHAR2,
  v_wzrost NUMBER,
  v_waga NUMBER,
  v_id_adresu NUMBER,
  v_ulica VARCHAR2,
  v_kod VARCHAR2,
  v_miasto VARCHAR2
) AS
BEGIN
INSERT INTO adresy (id_adresu, ulica, kod_pocztowy, miasto)
VALUES (v_id_adresu, v_ulica, v_kod, v_miasto);

INSERT INTO pacjenci (pesel, imie, nazwisko, nr_telefonu, wzrost, waga, id_adresu)
VALUES (v_pesel, v_imie, v_nazwisko, v_nr_telefonu, v_wzrost, v_waga, v_id_adresu);

END;
