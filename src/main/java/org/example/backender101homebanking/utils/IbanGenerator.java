package org.example.backender101homebanking.utils;

import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class IbanGenerator {
    private static final String COUNTRY_CODE = "IT";
    private static final String BANK_CODE = "03069";  // Codice ABI (Banca)
    private static final String BRANCH_CODE = "05000"; // Codice CAB (Filiale)

    public String generateIban() {
        String nationalCheckDigit = generateRandomUpperCaseLetter(); // Cin nazionale
        String accountNumber = generateRandomNumericString(12); // Numero di conto (12 cifre)

        Iban iban = new Iban.Builder()
                .countryCode(CountryCode.getByCode(COUNTRY_CODE))
                .bankCode(BANK_CODE)
                .branchCode(BRANCH_CODE)
                .nationalCheckDigit(nationalCheckDigit)
                .accountNumber(accountNumber)
                .build();

        return iban.toString();
    }

    private String generateRandomUpperCaseLetter() {
        Random random = new Random();
        char letter = (char) ('A' + random.nextInt(26));
        return String.valueOf(letter);
    }

    private String generateRandomNumericString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Aggiunge un numero casuale tra 0 e 9
        }
        return sb.toString();
    }
}
