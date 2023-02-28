INSERT INTO statusy_uzytkownika (id_statusu, nazwa) VALUES (1, 'lekarze');
INSERT INTO statusy_uzytkownika (id_statusu, nazwa) VALUES (2, 'recepcjonisci');
INSERT INTO statusy_uzytkownika (id_statusu, nazwa) VALUES (3, 'administratorzy');

INSERT INTO specjalizacje (id_specjalizacji, nazwa) VALUES (spec_id.nextval, 'POZ');
INSERT INTO specjalizacje (id_specjalizacji, nazwa) VALUES (spec_id.nextval, 'Kardiolog');
INSERT INTO specjalizacje (id_specjalizacji, nazwa) VALUES (spec_id.nextval, 'Pediatra');
INSERT INTO specjalizacje (id_specjalizacji, nazwa) VALUES (spec_id.nextval, 'Dermatolog');

INSERT INTO kategorie_pensji (id_kategorii, opis, min_pensja, max_pensja) VALUES (1, 'prezes przychodni', 10000, 40000);
INSERT INTO kategorie_pensji (id_kategorii, opis, min_pensja, max_pensja) VALUES (2, 'administrator bazy danych', 10000, 30000);
INSERT INTO kategorie_pensji (id_kategorii, opis, min_pensja, max_pensja) VALUES (3, 'starszy lekarz specjalista', 8000, 30000);
INSERT INTO kategorie_pensji (id_kategorii, opis, min_pensja, max_pensja) VALUES (4, 'lekarz spcejalista', 8000, 20000);
INSERT INTO kategorie_pensji (id_kategorii, opis, min_pensja, max_pensja) VALUES (5, 'lekarz', 6000, 15000);
INSERT INTO kategorie_pensji (id_kategorii, opis, min_pensja, max_pensja) VALUES (6, 'lekarz stażysta', 4000, 8000);
INSERT INTO kategorie_pensji (id_kategorii, opis, min_pensja, max_pensja) VALUES (7, 'recepcjonista', 3000, 6000);
INSERT INTO kategorie_pensji (id_kategorii, opis, min_pensja, max_pensja) VALUES (8, 'student', 2000, 5000);

INSERT INTO statusy_pracownika (id_statusu, opis) VALUES (1, 'obecnie zatrudniony');
INSERT INTO statusy_pracownika (id_statusu, opis) VALUES (2, 'urlop');
INSERT INTO statusy_pracownika (id_statusu, opis) VALUES (3, 'zawieszony');
INSERT INTO statusy_pracownika (id_statusu, opis) VALUES (4, 'zwolniony');

--haslo - admin
exec Dodaj_uzytkownika_pracownika('Piotr', 'Gryf', '111222333', 'admin', '21232f297a57a5a743894a0e4a801fc3', 38000, u_id.nextval, 3, 1);
-- haslo - recepcjonista
exec Dodaj_uzytkownika_pracownika('Pracownik', 'Przychodni', '111442333', 'recepcjonista', '3385062316a5d5f2666ac7ad11bb6e59', 5000, u_id.nextval, 2, 7);

--haslo - asd1
exec dodaj_nowego_lekarza('Joanna', 'Makowska', '124567891', 'asd1', 'f5b3b9b303f5a0594272f99d191bbf45', 13000, u_id.nextval, 4, 3);
--haslo - lekarz2
exec dodaj_nowego_lekarza('Krzysztof', 'Lewandowski', '121909891', 'krzysl', 'fa2c55070bc02b50cdce4b4d50c405d0', 11000, u_id.nextval, 2, 3);

exec dodaj_nowego_pacjenta('92100907826', 'Anna', 'Nowak', '879527982', 174, 59, add_id.nextval, 'Puławska', '45-099', 'Warszawa');
exec dodaj_nowego_pacjenta('92081205822', 'Jan', 'Kowalski', '809512382', 184, 89, add_id.nextval, 'Polna', '25-119', 'Warszawa');
exec dodaj_nowego_pacjenta('05211953873', 'Ryszard', 'Kwiatkowski', '790512381', 190, 105, add_id.nextval, 'Przemysłowa', '78-122', 'Warszawa');
exec dodaj_nowego_pacjenta('54051638136', 'Eliza', 'Laskowska', '336512382', 180, 70, add_id.nextval, 'Robotnicza', '21-339', 'Warszawa');

alter session set nls_date_format='YYYY-MM-DD HH24:MI';
INSERT INTO terminy (id_terminu, data_godzina, id_lekarza, pesel_pacjenta) VALUES (term_id.nextval, '2023-02-02 13:30', 1, '92100907826');
INSERT INTO terminy (id_terminu, data_godzina, id_lekarza, pesel_pacjenta) VALUES (term_id.nextval, '2023-02-02 12:30', 1, '92081205822');
INSERT INTO terminy (id_terminu, data_godzina, id_lekarza, pesel_pacjenta) VALUES (term_id.nextval, '2023-02-02 14:30', 2, '5211953873');
INSERT INTO terminy (id_terminu, data_godzina, id_lekarza, pesel_pacjenta) VALUES (term_id.nextval, '2023-02-02 14:30', 2, null);

INSERT INTO zrealizowane_wizyty (id_wizyt, opis, data, pesel_pacjenta, id_lekarza) VALUES (term_id.nextval, 'Badanie ekg', '2023-01-22 14:30', '92100907826', 2);
INSERT INTO zrealizowane_wizyty (id_wizyt, opis, data, pesel_pacjenta, id_lekarza) VALUES (term_id.nextval, 'Wykrycie choroby zakaźnej', '2023-01-03 11:30', '92100907826', 1);
INSERT INTO zrealizowane_wizyty (id_wizyt, opis, data, pesel_pacjenta, id_lekarza) VALUES (term_id.nextval, 'Badania rutynowe', '2023-01-11 07:15', '05211953873', 1);

INSERT INTO recepty (id_recepty, data_wystawienia, leki, opis, id_wizyty) VALUES (rec_id.nextval, '2023-01-03 11:30', 'krem z antybiotykiem', 'trzy razy dziennie', 6);
INSERT INTO recepty (id_recepty, data_wystawienia, leki, opis, id_wizyty) VALUES (rec_id.nextval, '2023-01-11 07:15', 'specjalistyczny żel do mycia ciała', 'raz dziennie', 7);
