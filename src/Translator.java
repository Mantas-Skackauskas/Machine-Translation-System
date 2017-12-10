
import java.io.*;
import java.util.*;


public class Translator {

    //defines dictionaries
    private Map<String, Word> dictionaryENtoSP = ParseDictionary("../dictionary.csv", "EN-SP");

    private Map<String, Word> dictionarySPtoEN = ParseDictionary("../dictionary.csv", "SP-EN");

    private String typeOf = "EN-SP";


    //parses dictionary to the program
    public static Map<String, Word> ParseDictionary(String fileLocation, String typeOf) {
        Map<String, Word> dictionary = new LinkedHashMap<String, Word>();

        //string variable equals to each line of input
        String line;

        try {

            //reader object is created
            BufferedReader br = new BufferedReader(new FileReader(fileLocation));

            //reads each line of an input file till the end
            while ((line = br.readLine()) != null) {

                //words are added to a hashmap
                String[] string = line.split(",");

                //element with index 0 represents english word
                //element with index 1 represents spanish word
                //element with index 2 represents type of the word
                Word Word = new Word(string[0], string[1], string[2]);

                //checks which direction translation will be so the key is of correct type
                if (typeOf.equals("EN-SP")) {
                    dictionary.put(string[0], Word);
                } else if (typeOf.equals("SP-EN")) {
                    dictionary.put(string[1], Word);
                }

            }

            //sentence characters are added to dictionary as words
            dictionary.put(",", new Word(",", ",", "comma"));
            dictionary.put(".", new Word(".", ".", "end"));
            dictionary.put("!", new Word("!", "!", "end"));
            dictionary.put("?", new Word("?", "?", "question"));
            dictionary.put("parties", new Word("parties", "fiesta", "noun", true));

            if (typeOf.equals("EN-SP")) {

                //people is an exception of plurality
                dictionary.get("people").setPlural(true);

            } else if (typeOf.equals("SP-EN")) {

                //other forms of the same words are added to the dictionary
                dictionary.put("conmigo", new Word("with", "conmigo", "pronoun"));
                dictionary.put("contigo", new Word("with", "contigo", "pronoun"));
                dictionary.put("estoy", new Word("am", "estoy", "verb"));
                dictionary.put("estás", new Word("are", "estás", "verb"));
                dictionary.put("está", new Word("is", "está", "verb"));
                dictionary.put("estamos", new Word("are", "estamos", "verb"));
                dictionary.put("están", new Word("are", "están", "verb"));
                dictionary.put("eres", new Word("are", "eres", "verb"));
                dictionary.put("somos", new Word("are", "somos", "verb"));
                dictionary.put("me", new Word("me", "me", "pronoun"));
                dictionary.put("lo", new Word("it", "lo", "pronoun"));
                dictionary.put("la", new Word("her", "la", "pronoun"));
                dictionary.put("te", new Word("you", "te", "pronoun"));
                dictionary.put("los", new Word("those", "los", "pronoun"));
                dictionary.put("nos", new Word("us", "nos", "pronoun"));
                dictionary.put("tú", new Word("you", "tú", "pronoun"));
            }

        } catch (IOException e) {
        }

        return dictionary;

    }

    //prints whole dictionary
    public static void printDictionary(Map<String, Word> dictionary) {

        int count = 0;

        for (String name : dictionary.keySet()) {

            String spanish = dictionary.get(name).getSpanish();
            String english = dictionary.get(name).getEnglish();
            String type = dictionary.get(name).getType();
            boolean plural = dictionary.get(name).getPlural();

            count++;

            //displays whole dictionary to terminal
            System.out.println("\n\nEnglish: " + english + " \nSpanish: " + spanish + " \nType: " + type + "\nPlural: " + plural);

        }
        System.out.println("\nTotal words: " + count);
    }

    // in the output, first letter of a sentence is a capital letter
    public String setFirstLettertoUpperCase(String string) {

        //first letter is taken
        char lowToUpper = Character.toUpperCase(string.charAt(0));

        StringBuilder newString = new StringBuilder(string);
        newString.setCharAt(0, lowToUpper);

        return newString.toString();
    }

    //fixes 'I' if it is entered as 'i'
    public String setCaseI(String string) {

        if (string.length() > 2 && string.charAt(0) == 'i' && string.charAt(1) == ' ') {
            //replaces 'i' to 'I'
            string = string.replaceFirst("i ", "I ");
        }

        string = string.replaceAll("\\s+i ", " I ");
        string = string.replaceAll(",\\s+i ", ", I ");
        string = string.replaceAll(".\\s+i ", "." + ". I ");

        return string;
    }

    //removes unnecessary characters and spaces
    public String fixString(String string) {

        string = string.replaceAll("\\s+(?=[(),.?])", "");
        string = string.replaceAll("(?=[(),.?])+\\s+", " ");

        string = string.trim().replaceAll(" +", " ");

        //all the letters are lower case
        string = string.toLowerCase();

        string = setCaseI(string);

        if (Character.isLetterOrDigit(string.charAt(string.length() - 1))) {

            string = string + '.';

        }

        return string;
    }


    //gets the translated sentence
    public String getTranslatedText(String newInputText, String typeOf) {

        String answerString = "";

        //a list of sentences
        List<Sentence> sentences = new ArrayList<>();

        int sentenceIndex = 0;

        String stringRegex = ".*[a-zA-Z]+.*";
        String temporaryString = "";

        newInputText = fixString(newInputText);

        //reading sentences
        for (int i = 0; i < newInputText.length(); i++) {

            if (newInputText.charAt(i) == '.' || newInputText.charAt(i) == '!' || newInputText.charAt(i) == '?') {

                temporaryString = newInputText.substring(sentenceIndex, i) + newInputText.charAt(i);

                if (temporaryString.matches(stringRegex)) {

                    sentences.add(new Sentence(temporaryString, getTypeOf()));
                    sentenceIndex = i + 2;
                }

            }

        }

        sentenceIndex = 0;

        String temporaryAnswer = "";
        //transforming sentences
        for (Sentence s : sentences) {
            if (typeOf.equals("EN-SP")) {
                temporaryAnswer = fixString(s.TranslateSentence(dictionaryENtoSP));
            } else if (typeOf.equals("SP-EN")) {
                temporaryAnswer = fixString(s.TranslateSentence(dictionarySPtoEN));
            }

            answerString += setFirstLettertoUpperCase(temporaryAnswer) + " ";

        }

        return answerString;

    }

    //to set the way of translating (languages1 to language2)
    public void setTypeOf(String typeOf) {
        this.typeOf = typeOf;
    }

    //to get the way of translating
    public String getTypeOf() {
        return typeOf;
    }

}
