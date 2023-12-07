package com.example.zhm;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.swabunga.spell.event.SpellChecker;


@Component
public class MyMain implements CommandLineRunner {
    public static Scanner input = new Scanner(System.in);
    private static SpellChecker spellChecker;
    private static final SpellDictionary dictionary;

    static {
        try {
            dictionary = new SpellDictionaryHashMap();
        } catch (IOException e) {
            throw new RuntimeException(e + "blev fel med spelldictionary");
        }
    }

    @Autowired
    private PersonsRepo pRepo;
    @Autowired
    private WordsRepo wRepo;
    @Autowired
    private CityRepo cRepo;

    @Override
    public void run(String[] args) throws Exception {

        String[] menuChoices = {
                "\n0. Avsluta programmet.",
                "1. Visa allt i databasen.",
                "2. Räkna antal ord.",
                "3. Antal tecken.",
                "4. Antal rader.",
                "5. Stavningskontroll.",
                "6. Kolla mot databasen City."
        };
        int userInput;
        do {
            for (String menu : menuChoices) {
                System.out.println(menu);
            }
            System.out.print("Ditt val: ");
            userInput = readIntOnly();

            switch (userInput) {
                case 1 -> findAll();
                case 2 -> countWords();
                case 3 -> countCharacters();
                case 4 -> countRows();
                case 5 -> checkSpellings();
                case 6 -> findCity();
            }
        } while (userInput != 0);
        System.out.println("Programmet avslutas!");

    }
    // 1. Visa allt
    public void findAll() {
        List<Person> pList = pRepo.findAll();
        List<Words> wList = wRepo.findAll();
        List<City> cList = cRepo.findAll();
        System.out.println(pList);
        System.out.println(wList);
        System.out.println(cList);
    }
    // 2. Räkna ord
    public static void countWords() {
        System.out.print("Skriv eller klistra in text: ");
        int countWords = 0;
        while (input.hasNextLine()) {
            String line = input.nextLine().trim();
            if (line.isEmpty()) {
                break; // Bryt loopen om en tom rad matas in
            }
            String[] words = line.split("[^\\p{L}]+"); // Använd regex som matchar icke-bokstäver
            countWords += words.length;
        }
        System.out.println("Totalt antal ord: " + countWords);
    }
    // 3. Räkna tecken
    public static void countCharacters() {
        System.out.print("Skriv eller klistra in text: ");
        int characterCount = 0;

        while (input.hasNextLine()) {
            String line = input.nextLine();
            if (line.isEmpty()) {
                break; // Bryt loopen om en tom rad matas in
            }
            characterCount += line.length(); // Lägg till antalet tecken i raden till totala räkningen
        }
        System.out.println("Totalt antal tecken: " + characterCount);
    }
    // 4. Räkna rader
    public static void countRows() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int rowCount = 0;
        System.out.println("Skriv in text (tom rad för att avsluta):");
        while (true) {
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                System.out.println("Läsfel: " + e.getMessage());
                break;
            }
            if (line == null || line.isEmpty()) {
                break; // Avsluta om en tom rad matas in
            }
            rowCount++;
        }
        System.out.println(rowCount + " rader");
    }
    // 5. Kolla stavning
    private void checkSpellings() throws IOException {
            ArrayList<String> foundWordsWrapped = new ArrayList<>();
            List<Words> wordsfoundWords = wRepo.findAll();
            List<City> cityFoundWords = cRepo.findAll();
            List<Person> personFoundWords = pRepo.findAll();
            for (Words word : wordsfoundWords) {
                foundWordsWrapped.add(word.getWordAsString());
            }
            for (City word : cityFoundWords) {
                foundWordsWrapped.add(word.getWordAsString());
            }
            for (Person word : personFoundWords) {
                foundWordsWrapped.add(word.getWordAsString());
            }
            System.out.println(foundWordsWrapped);
            System.out.print("Skriv ord som du vill kolla stavning på: ");
            String wordToCheck = validInput();

            // Skapa en SpellChecker med SpellDictionary


            for (String word : foundWordsWrapped){
                dictionary.addWord(word);
            }

            spellChecker = new SpellChecker(dictionary);

            // Kontrollera om ett ord är korrekt stavat
            boolean isSpelledCorrectly = isWordSpelledCorrectly(wordToCheck);

            if (isSpelledCorrectly) {
                System.out.println(wordToCheck + " är stavat korrekt.");
            } else {

                System.out.println(wordToCheck + " är inte stavat korrekt.");

                // Om ordet inte är stavat korrekt, kan du också få förslag på liknande ord
                String suggestions = dictionary.getSuggestions(wordToCheck,10).toString();
                System.out.println("Förslag: " + suggestions);
            }





    }

    private static boolean isWordSpelledCorrectly(String word) {
        String a = dictionary.getSuggestions(word, 10).toString();
        return a.isEmpty();
    }
    /*
    private static List<Words> getSuggestions(String word) {
        List<Words> suggestions = new ArrayList<>();
        List suggestionsArray = spellChecker.getSuggestions(word, 10);

        if (suggestionsArray != null) {
            for (Object suggestion : suggestionsArray) {
                suggestions.add(suggestion);
            }
        }



        return suggestions;
    }



    public void checkSpelling() {
        System.out.print("Skriv ett ord för att kontrollera stavningen: ");
        String wordToCheck = validInput();
        List<Words> foundWords = wRepo.findByNameContaining(wordToCheck);

        if (!foundWords.isEmpty()) {
            System.out.println("Möjliga rätta stavningar:");
            for (Words word : foundWords) {
                System.out.println(word.getName());
            }
        } else {
            System.out.println("Inga liknande ord hittades i databasen.");
        }
    }

     */
    // 6. Hitta staden
    public void findCity() {
        String inputWord = validInput();
        List<City> matchingCities = cRepo.findByName(inputWord);
        if (!matchingCities.isEmpty()) {
            System.out.println("Staden hittades i databasen: ");
            for (City city : matchingCities) {
                System.out.println("City ID: " + city.getId() + "City Namn: " + city.getName());
            }
        } else {
            System.out.println("Staden hittades inte i databasen.");
        }
    }

    public static int readIntOnly() {
        while (true) {
            try {
                return Integer.parseInt(validInput());
            } catch (NumberFormatException e) {
                System.err.println("Ogiltig inmatning. Vänligen skriv in ett heltal: ");
            }
        }
    }

    public static String validInput() {
        String inputString = "";
        while (true) {
            inputString = input.nextLine().trim();
            if (inputString.isEmpty()) {
                System.out.println("Du måste ange något!");
            } else {
                return inputString;
            }
        }
    }

}

