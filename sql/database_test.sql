--Zapytania, z kt�rych korzysta aplikacja

alter session set nls_date_format='YYYY-MM-DD HH24:MI';

--getAddressByPesel
SELECT a.* FROM adresy a JOIN pacjenci p on (p.id_adresu = a.id_adresu) WHERE p.pesel = 92100907826;

--AssignAppointment
UPDATE terminy SET pesel_pacjenta = '"+ patientPesel +"' WHERE id_terminu = 4;

--DeleteExpiredAppointments
DELETE FROM terminy WHERE data_godzina < to_char(sysdate, 'yyyy-MM-dd') AND pesel_pacjenta IS NULL;

--getVisitsByDateSpec
SELECT t.* FROM terminy t join lekarze l on (t.id_lekarza = l.id_lekarza) join specjalizacje s on (l.id_specjalizacji = s.id_specjalizacji)
WHERE s.nazwa = 'Kardiolog'
AND DATA_GODZINA BETWEEN '2023/02/02 00:00'
AND '2023/02/02 23:59'  AND data_godzina > SYSDATE
AND t.pesel_pacjenta is null ORDER BY data_godzina;

--GetEarliestAppointments
SELECT t.* FROM terminy t join lekarze l on (t.id_lekarza = l.id_lekarza) join specjalizacje s on (l.id_specjalizacji = s.id_specjalizacji)
WHERE s.nazwa = 'Kardiolog' AND data_godzina > SYSDATE
AND t.pesel_pacjenta is null
ORDER BY data_godzina
fetch first 10 rows only;

--getPatientsAppointment
select * from terminy where pesel_pacjenta = 92100907826 and data_godzina >= SYSDATE;

--RemovePatientFromVisit
UPDATE terminy SET pesel_pacjenta = null WHERE id_terminu=2;

--getDocById
SELECT * FROM Lekarze WHERE Id_lekarza=2;

--RemoveDoctor
DELETE FROM lekarze WHERE id_lekarza = 67856;

--GetPatientsPastAppointment
select * from zrealizowane_wizyty where pesel_pacjenta like '92100907826';

--Perscription
SELECT * FROM recepty WHERE id_wizyty = 6;

--AddSpecialization
INSERT INTO specjalizacje (id_specjalizacji, nazwa) VALUES (spec_id.nextval, 'Laryngolog');

-- wy�wietlenie ile pacjenci maj� zrealizowanych wizyt
SELECT p.pesel, p.imie, p.nazwisko, count(z.id_wizyt) as "ZREALIZOWANE WIZYTY" FROM pacjenci p
LEFT JOIN zrealizowane_wizyty z ON (p.pesel = z.pesel_pacjenta)
GROUP BY p.pesel, p.imie, p.nazwisko
ORDER BY count(z.id_wizyt) DESC;

-- wy�wietlenie ile lekarze maj� zrealizowanych wizyt
SELECT l.imie, l.nazwisko, s.nazwa as SPECJALIZACJA, count(z.id_wizyt) as "ZREALIZOWANE WIZYTY" FROM lekarze l
JOIN specjalizacje s USING(id_specjalizacji)
LEFT JOIN zrealizowane_wizyty z ON (l.id_lekarza = z.id_lekarza)
GROUP BY l.imie, l.nazwisko, s.nazwa
ORDER BY count(z.id_wizyt) DESC;


--triggery
--brak mo�liwo�ci dodania terminu ze przeszla data
insert into terminy values( term_id.NEXTVAL, '2021/02/02 00:00', 2, 92100907826);

--brak mozliwosci ustawienia pensji spoza zakresu
update pracownicy set pensja = 9999999 where id_pracownika = 1;

--funkcje
--funkcja liczaca bonus lekarza wedlug zrealizowanych wizyt.
--funkcja wyrzuci wyjatek gdy bonus bedzie mniejszy niz 0
--Argumenty: id lekarza, wartosc bonusu za 1 wizyte
SELECT calculate_doctor_bonus(1, 100) FROM dual;
SELECT calculate_donor_bonus(1, -10) FROM dual;

--funkcja liczaca nowa pensje pracownika wedlug podanego bonusu
--zwroci maksymalna pensje dla stanowiska jesli nowa pensja przekroczy
--okreslony limit dla stanowiska
--jesli bonus bedzie wiekszy od 1 lub mniejszy od 0 funkcja wyrzuci wyjatek
--Argumenty: id pracownika, procent pensji
SELECT emp_bonus(3, 0.1) FROM dual;
SELECT emp_bonus(3, -0.1) FROM dual;
SELECT emp_bonus(3, 1.1) FROM dual;

--procedura dodaj�ca nowego pracownika i dodaj�ca go r�wnie� do tabeli u�ytkownicy
exec Dodaj_uzytkownika_pracownika('Piotr', 'Gryf', '111222333', 'admin1', '21232f297a57a5a743894a0e4a801fc3', 38000, u_id.nextval, 3, 1);
--procedura dodaj�ca nowego pracownika b�d�cego lekarzem i dodaj�ca go r�wnie� do tabeli u�ytkownicy i lekarze
exec dodaj_nowego_lekarza('Joanna', 'Makowska', '124567891', 'asd11', 'f5b3b9b303f5a0594272f99d191bbf45', 13000, u_id.nextval, 4, 3);
--procedura dodaj�ca nowego pacjenta, kt�ra zapisuje r�wnie� jego adres do tabeli adresy
exec dodaj_nowego_pacjenta('92100907823', 'Anna', 'Nowak', '879527982', 174, 59, add_id.nextval, 'Pu�awska', '45-099', 'Warszawa');

--procedura aktualizujaca pensje pracownika wedlug podanego bonusu
--jesli nowa pensja przekroczy limit, zostanie ustawiona na maksymalna pensje w kategorii
--Argumenty: id pracownika, bonus w procentach
exec update_salary_bonus(2, 10);

--procedura aktualizujaca pensje wszystkich pracownikow wedlug podanego bonusu
--jesli nowa pensja przekroczy limit, zostanie ustawiona na maksymalna pensje w kategorii
--Argumenty: bonus w procentach
exec update_bonus_all_employees(10);
