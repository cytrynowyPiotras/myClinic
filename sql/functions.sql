CREATE OR REPLACE FUNCTION emp_bonus(e_id NUMBER, v_bon NUMBER)
    RETURN NUMERIC
AS
    v_salary NUMBER;
    v_new_salary NUMBER;
BEGIN
    IF v_bon >= 1 THEN RAISE_APPLICATION_ERROR(-20001, 'Invalid bonus'); END IF;
    SELECT pensja INTO v_salary FROM pracownicy WHERE id_pracownika = e_id;
    v_new_salary := v_salary + v_salary * v_bon;
    RETURN v_new_salary;
END;
/
SELECT emp_bonus(5, 0.1) FROM dual;
/

CREATE OR REPLACE FUNCTION calculate_doctor_bonus(doc_id NUMBER, v_bon NUMBER)
    RETURN NUMERIC
AS
    v_visits_sum INTEGER := 0;
    v_calc_bonus NUMBER := 0;
BEGIN
    IF v_bon < 0 THEN RAISE_APPLICATION_ERROR(-20001, 'Invalid bonus'); END IF;
    SELECT count(id_lekarza) INTO v_visits_sum FROM zrealizowane_wizyty WHERE id_lekarza = doc_id;
    v_calc_bonus := v_visits_sum * v_bon;
    RETURN v_calc_bonus;
EXCEPTION
    WHEN no_data_found THEN dbms_output.put_line('no doctor found');
    RAISE no_data_found;
END;
/
SELECT calculate_doctor_bonus(1, 10) FROM dual;