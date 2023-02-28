create or replace TRIGGER tg_data_terminu
BEFORE INSERT or UPDATE OF DATA_GODZINA ON TERMINY
FOR EACH ROW
DECLARE
    DZISIEJSZA_DATA DATE;
BEGIN
    DZISIEJSZA_DATA := SYSDATE;
    IF :NEW.DATA_GODZINA < DZISIEJSZA_DATA THEN
        RAISE_APPLICATION_ERROR(-20001, 'CANNOT INSERT OR UPDATE PAST DATE');
    END IF;
END;
/
create or replace TRIGGER tg_nowa_pensja
BEFORE INSERT or UPDATE of pensja ON pracownicy FOR EACH ROW
DECLARE
    v_min_sal number(10, 2);
    v_max_sal number(10, 2);
BEGIN
    SELECT min_pensja, max_pensja INTO v_min_sal, v_max_sal
    FROM kategorie_pensji k WHERE k.id_kategorii = :new.id_kategorii;
IF :new.pensja NOT BETWEEN v_min_sal AND v_max_sal THEN
    raise_application_error(-20002, 'Przekroczony zakres płacy');
END IF;
END;