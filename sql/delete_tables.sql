CREATE OR REPLACE PROCEDURE destroy_tables
AS
v_count  INT;
v_name VARCHAR2(20);
TYPE namesarray IS VARRAY(15) OF VARCHAR2(20);
names    namesarray;
BEGIN
names := namesarray('lekarze', 'pacjenci', 'terminy', 'adresy', 'recepty', 'specjalizacje', 'statusy_uzytkownika', 'uzytkownicy', 'zrealizowane_wizyty', 'kategorie_pensji', 'pracownicy', 'statusy_pracownika');

FOR i IN 1..names.count LOOP
        v_name := names(i);

SELECT COUNT(*) INTO v_count FROM user_tables WHERE table_name = upper(v_name);
IF v_count = 1 THEN
            DBMS_OUTPUT.PUT_LINE('Dropping table: ' || v_name);
EXECUTE IMMEDIATE 'DROP TABLE '|| v_name || ' CASCADE CONSTRAINTS';
END IF;
END LOOP;
END;
/
