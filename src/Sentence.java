import java.util.*;

//Class to translate sentences
public class Sentence {

    private String string;

    private String languageType;

    //sentence constructor
    public Sentence(String string, String languageType) {

        //unnecessary characters are removed
        string = fixSentence(string);

        this.string = string;
        //language type is known
        this.languageType = languageType;

    }

    //gets language type
    public String getLanguageType() {
        return languageType;
    }

    //getter
    public String getString() {
        return string;
    }


    //translation part
    //hashmap containing the words is taken
    public String TranslateSentence(Map<String, Word> dictionary) {

        //checks the translation type and uses the correct translation
        if (getLanguageType().equals("EN-SP")) {

            String answerString = "";

            //string line is split to an array of words
            String[] words = getString().split("\\s+");

            //list of words is made
            List<Word> labeledWords = new ArrayList<>();

            //string for getting a matching word
            String match;

            boolean lastNounMasculine = true;
            boolean lastNounPlural = false;

            //every word is found in the dictionary
            //the right word is found if spelling mistakes are done
            for (int i = 0; i < words.length; i++) {

                match = distance(words[i], dictionary);

                //new Word object is created including one more attribute (plurality)
                Word matchedWord = new Word(

                        dictionary.get(match).getEnglish(),
                        dictionary.get(match).getSpanish(),
                        dictionary.get(match).getType(),
                        dictionary.get(match).getPlural()

                );

                //correct (matched) words are added to the list
                labeledWords.add(matchedWord);

                int mLength = match.length();
                int wLength = words[i].length();


                //sets a boolean value for plurality of each word
                if (wLength > 1 && ((wLength > mLength && words[i].charAt(wLength - 1) == 's') ||
                        (dictionary.get(match).getEnglish().charAt(mLength - 1) != 's' &&
                                words[i].charAt(wLength - 1) == 's'))) {

                    //changing Word type values
                    labeledWords.get(i).setPlural(true);

                }

            }


            //changes adjective to plural
            //article change
            lastNounMasculine = true;
            lastNounPlural = false;

            //methods of each rule are made
            fixWord_Her(labeledWords);

            ruleENtoSP3(labeledWords);

            ruleENtoSP1(labeledWords);

            ruleENtoSP2(labeledWords);

            ruleENtoSP4(labeledWords);

            ruleENtoSP5(labeledWords);

            ruleENtoSP6(labeledWords);

            ruleENtoSP7(labeledWords);

            ruleENtoSP8(labeledWords);

            ruleENtoSP9(labeledWords);

            ruleENtoSP10(labeledWords);

            ruleENtoSP11(labeledWords);

            //final output is made
            //correct words are put in a sentence
            for (Word x : labeledWords) {
                answerString += x.getSpanish() + " ";
            }

            return answerString;

        } else {
            //goes here if translating Spanish - English

            //translation is done analogically

            String answerString = "";

            //string line is split to an array of words
            String[] words = getString().split("\\s+");

            //list of words is made
            List<Word> labeledWords = new ArrayList<>();

            String match;

            boolean lastNounMasculine = true;
            boolean lastNounPlural = false;


            //every word is found in the dictionary
            //the right word is found if spelling mistakes are done
            for (int i = 0; i < words.length; i++) {

                match = distance(words[i], dictionary);

                //new Word object is created including one more attribute (plurality)
                Word matchedWord = new Word(

                        dictionary.get(match).getEnglish(),
                        dictionary.get(match).getSpanish(),
                        dictionary.get(match).getType(),
                        dictionary.get(match).getPlural()

                );

                labeledWords.add(matchedWord);

                int mLength = match.length();
                int wLength = words[i].length();

                if (wLength > 1 && ((wLength > mLength && words[i].charAt(wLength - 1) == 's') ||
                        (dictionary.get(match).getSpanish().charAt(mLength - 1) != 's' &&
                                words[i].charAt(wLength - 1) == 's')) && labeledWords.get(i).getType().equals("noun")) {

                    //changing Word type values
                    labeledWords.get(i).setEnglish(labeledWords.get(i).getEnglish() + "s");

                }

            }

            //methods of each rule are made
            fixWord_Nosotros(labeledWords);

            ruleSPtoEN10(labeledWords);

            ruleSPtoEN7_6(labeledWords);

            ruleSPtoEN8(labeledWords);

            ruleSPtoEN9(labeledWords);

            fixWord_Ellos_La(labeledWords);

            fixWord_article(labeledWords);

            ruleSPtoEN5_4_3_2_1(labeledWords);

            //  spanish to english
            //final output is made
            //correct words are put in a sentence
            for (Word x : labeledWords) {
                answerString += x.getEnglish() + " ";
            }
            return answerString;
        }
    }

    //word 'her' has two types
    //the right type of 'her' is set
    public static List<Word> fixWord_Her(List<Word> labeledWords) {

        for (int i = 0; i < labeledWords.size() - 1; i++) {

            if (labeledWords.get(i).getEnglish().equals("her") &&
                    (!labeledWords.get(i + 1).getType().equals("adjective") &&
                            !labeledWords.get(i + 1).getType().equals("noun"))) {

                labeledWords.get(i).setType("pronoun");
                labeledWords.get(i).setSpanish("ella");

            }

        }

        return labeledWords;

    }


    //an article 'the' is set when right words are found before the noun
    public static List<Word> fixWord_article(List<Word> labeledWords) {

        for (int i = 0; i < labeledWords.size() - 1; i++) {
            if ((labeledWords.get(i).getSpanish().equals("las") ||
                    labeledWords.get(i).getSpanish().equals("los") ||
                    labeledWords.get(i).getSpanish().equals("la") ||
                    labeledWords.get(i).getSpanish().equals("lo")) &&
                    labeledWords.get(i + 1).getType().equals("noun")) {

                labeledWords.get(i).setEnglish("the");
            }
        }

        return labeledWords;
    }


    //word 'nosotros' has two meanings
    //a right word is chosen when checking the next word in the sentence
    public static List<Word> fixWord_Nosotros(List<Word> labeledWords) {

        for (int i = 0; i < labeledWords.size() - 1; i++) {
            if (labeledWords.get(i).getSpanish().equals("nosotros") && labeledWords.get(i + 1).getType().equals("verb")) {
                labeledWords.get(i).setEnglish("we");
            }
        }

        return labeledWords;
    }

    //if 'them' goes in front of a verb, it is changed to 'they'
    public static List<Word> fixWord_Ellos_La(List<Word> labeledWords) {

        for (int i = 0; i < labeledWords.size() - 1; i++) {

            if (labeledWords.get(i).getEnglish().equals("them") && labeledWords.get(i + 1).getType().equals("verb")) {
                labeledWords.get(i).setEnglish("they");
            }

        }

        return labeledWords;
    }


    //the question character is added  in fron of a sentence
    public static List<Word> ruleENtoSP11(List<Word> labeledWords) {

        if (labeledWords.get((labeledWords.size() - 1)).getEnglish().equals("?")) {

            labeledWords.get(0).setSpanish("¿" + labeledWords.get(0).getSpanish());

        }

        return labeledWords;

    }

    //rule10 SP to EN
    public static List<Word> ruleSPtoEN10(List<Word> labeledWords) {

        for (int i = 0; i < labeledWords.size(); i++) {

            if (i > 0 && labeledWords.get(i).getSpanish().equals("ti") &&
                    labeledWords.get(i - 1).getType().equals("preposition")) {

                labeledWords.get(i).setSpanish("tú");

            } else if (labeledWords.get(i).getSpanish().equals("conmigo")) {

                labeledWords.get(i).setSpanish("con");
                labeledWords.add(i + 1, new Word("me", "mi", "adjective", false));

            } else if (labeledWords.get(i).getSpanish().equals("contigo")) {

                labeledWords.get(i).setSpanish("con");
                labeledWords.add(i + 1, new Word("you", "tú", "adjective", false));

            }

        }

        return labeledWords;
    }


    //rule 10 EN to SP
    public static List<Word> ruleENtoSP10(List<Word> labeledWords) {

        for (int i = labeledWords.size() - 1; i > 0; i--) {

            if (labeledWords.get(i).getSpanish().equals("tú") &&
                    labeledWords.get(i - 1).getType().equals("preposition")) {

                labeledWords.get(i).setSpanish("ti");

            } else if (labeledWords.get(i - 1).getEnglish().equals("with")) {

                if (labeledWords.get(i).getEnglish().equals("me")) {

                    labeledWords.get(i).setSpanish("conmigo");
                    labeledWords.remove(i - 1);

                } else if (labeledWords.get(i).getEnglish().equals("you")) {

                    labeledWords.get(i).setSpanish("contigo");
                    labeledWords.remove(i - 1);
                }

            }

        }

        return labeledWords;

    }

    //rule 9 SP to EN
    public static List<Word> ruleSPtoEN9(List<Word> labeledWords) {

        for (int i = 0; i < labeledWords.size() - 1; i++) {

            if (i < labeledWords.size() - 3 && labeledWords.get(i).getType().equals("negation") &&
                    labeledWords.get(i + 1).getType().equals("pronoun") &&
                    labeledWords.get(i + 2).getType().equals("verb")) {

                labeledWords.add(i + 3, labeledWords.get(i));
                labeledWords.remove(i);


            } else if (i < labeledWords.size() - 4 && labeledWords.get(i).getType().equals("negation") &&
                    labeledWords.get(i + 1).getType().equals("possessive") &&
                    labeledWords.get(i + 2).getType().equals("pronoun") &&
                    labeledWords.get(i + 3).getType().equals("verb")) {
                labeledWords.add(i + 4, labeledWords.get(i));
                labeledWords.remove(i);


            }


        }

        return labeledWords;
    }


    //rule 9 EN to SP
    public static List<Word> ruleENtoSP9(List<Word> labeledWords) {

        for (int i = labeledWords.size() - 1; i > 0; i--) {
            if (i > 1 && i < (labeledWords.size() - 1) &&
                    labeledWords.get(i).getType().equals("negation") &&
                    labeledWords.get(i - 1).getType().equals("verb") &&
                    labeledWords.get(i - 2).getType().equals("pronoun") &&
                    labeledWords.get(i + 1).getType().equals("verb")) {

                labeledWords.add(i - 2, labeledWords.get(i));
                labeledWords.remove(i + 1);

            } else if (labeledWords.get(i).getType().equals("negation") && labeledWords.get(i - 1).getType().equals("verb")) {

                Collections.swap(labeledWords, i, i - 1);
            }
        }
        return labeledWords;

    }


    //rule 8 SP to EN
    public static List<Word> ruleSPtoEN8(List<Word> labeledWords) {

        for (int i = 0; i < labeledWords.size() - 2; i++) {
            if (labeledWords.get(i).getSpanish().equals("lo") ||
                    labeledWords.get(i).getSpanish().equals("la") ||
                    labeledWords.get(i).getSpanish().equals("te") ||
                    labeledWords.get(i).getSpanish().equals("los") ||
                    labeledWords.get(i).getSpanish().equals("nos") ||
                    labeledWords.get(i).getSpanish().equals("me")

                    ) {

                if (labeledWords.get(i + 1).getType().equals("pronoun") &&
                        labeledWords.get(i + 2).getType().equals("verb")) {

                    if (i < labeledWords.size() - 3 && labeledWords.get(i + 3).getType().equals("verb")) {

                        labeledWords.add(i + 4, labeledWords.get(i));
                        labeledWords.remove(i);

                    } else {

                        labeledWords.add(i + 3, labeledWords.get(i));
                        labeledWords.remove(i);

                    }

                }

            }

        }

        return labeledWords;

    }


    //rule 8 EN to SP
    public static List<Word> ruleENtoSP8(List<Word> labeledWords) {

        for (int i = labeledWords.size() - 1; i > 0; i--) {

            if (labeledWords.get(i).getType().equals("pronoun") &&
                    labeledWords.get(i - 1).getType().equals("verb") ||

                    (i > 1 && negation(labeledWords.get(i - 2)) &&
                            labeledWords.get(i).getType().equals("pronoun") &&
                            labeledWords.get(i - 1).getType().equals("verb"))) {

                if (i > 1 && negation(labeledWords.get(i - 2))) {
                    i--;
                }

                if (labeledWords.get(i).getEnglish().equals("him") ||
                        labeledWords.get(i).getEnglish().equals("it")) {

                    labeledWords.get(i).setSpanish("lo");

                } else if (labeledWords.get(i).getEnglish().equals("her")) {
                    labeledWords.get(i).setSpanish("la");
                } else if (labeledWords.get(i).getEnglish().equals("you")) {
                    labeledWords.get(i).setSpanish("te");
                } else if (labeledWords.get(i).getEnglish().equals("me")) {
                    labeledWords.get(i).setSpanish("me");
                } else if (labeledWords.get(i).getEnglish().equals("them") ||
                        labeledWords.get(i).getEnglish().equals("those")) {

                    labeledWords.get(i).setSpanish("los");

                } else if (labeledWords.get(i).getEnglish().equals("us")) {
                    labeledWords.get(i).setSpanish("nos");
                }

                if (negation(labeledWords.get(i - 1))) {

                    labeledWords.add(i - 1, labeledWords.get(i + 1));
                    labeledWords.remove(i + 2);

                } else {
                    labeledWords.add(i - 1, labeledWords.get(i));
                    labeledWords.remove(i + 1);
                }

            }

        }

        return labeledWords;

    }


    //SP to EN rule 6 and 7
    public static List<Word> ruleSPtoEN7_6(List<Word> labeledWords) {

        for (int i = 0; i < labeledWords.size(); i++) {

            if (labeledWords.get(i).getSpanish().equals("estoy")) {

                if (i == 0 || (i > 0 && !labeledWords.get(i - 1).getType().equals("noun") && !labeledWords.get(i - 1).getSpanish().equals("yo"))) {

                    labeledWords.get(i).setSpanish("soy");
                    labeledWords.add(i, new Word("I", "yo", "pronoun", false));
                    i++;

                } else if (i > 0) {

                    labeledWords.get(i - 1).setEnglish("I");

                }

            } else if (labeledWords.get(i).getSpanish().equals("es")) {

                if (i == 0 || (i > 0 && !labeledWords.get(i - 1).getType().equals("noun") && !labeledWords.get(i - 1).getType().equals("pronoun"))) {

                    labeledWords.add(i, new Word("it", "eso", "pronoun", false));
                    i++;

                }

            } else if (labeledWords.get(i).getSpanish().equals("estás")) {

                if (i == 0 || (i > 0 && !labeledWords.get(i - 1).getType().equals("noun") && !labeledWords.get(i - 1).getSpanish().equals("tú"))) {

                    labeledWords.get(i).setSpanish("eres");

                    labeledWords.add(i, new Word("you", "tú", "pronoun", false));
                    i++;

                }
            } else if (labeledWords.get(i).getSpanish().equals("está")) {

                if (i == 0 || (i > 0 && !labeledWords.get(i - 1).getType().equals("noun") && (
                        !labeledWords.get(i - 1).getSpanish().equals("el") ||
                                !labeledWords.get(i - 1).getSpanish().equals("ella") ||
                                !labeledWords.get(i - 1).getSpanish().equals("eso")))) {

                    labeledWords.get(i).setSpanish("es");
                    labeledWords.add(i, new Word("it", "eso", "pronoun", false));
                    i++;

                }

            } else if (labeledWords.get(i).getSpanish().equals("estamos")) {

                if (i == 0 || (i > 0 && !labeledWords.get(i - 1).getType().equals("noun") && !labeledWords.get(i - 1).getSpanish().equals("nosotros"))) {
                    labeledWords.get(i).setSpanish("son");
                    labeledWords.add(i, new Word("we", "nosotros", "pronoun", false));
                    i++;
                }

            } else if (labeledWords.get(i).getSpanish().equals("están")) {

                if (i == 0 || (i > 0 && !labeledWords.get(i - 1).getType().equals("noun") && !labeledWords.get(i - 1).getSpanish().equals("ellos"))) {

                    labeledWords.get(i).setSpanish("son");
                    labeledWords.add(i, new Word("they", "ellos", "pronoun", false));
                    i++;

                }

            } else if (i > 0 && labeledWords.get(i).getSpanish().equals("soy") && !labeledWords.get(i - 1).getSpanish().equals("yo")) {

                labeledWords.add(i, new Word("I", "yo", "pronoun"));

            } else if (i > 0 && labeledWords.get(i).getSpanish().equals("eres") && !labeledWords.get(i - 1).getSpanish().equals("tú")) {

                labeledWords.add(i, new Word("you", "tú", "pronoun"));

            } else if (i > 0 && labeledWords.get(i).getSpanish().equals("somos") && !labeledWords.get(i - 1).getSpanish().equals("nosotros")) {

                labeledWords.add(i, new Word("we", "nosotros", "pronoun"));

            } else if (i > 0 && labeledWords.get(i).getSpanish().equals("son") && !(labeledWords.get(i - 1).getType().equals("noun") || labeledWords.get(i - 1).getType().equals("pronoun"))) {

                labeledWords.add(i, new Word("they", "ellos", "pronoun"));
            }
        }

        return labeledWords;
    }


    //rule 9 EN to SP
    public static List<Word> ruleENtoSP7(List<Word> labeledWords) {

        for (int i = 0; i < labeledWords.size() - 2; i++) {

            if (labeledWords.get(i).getType().equals("pronoun") &&
                    labeledWords.get(i + 1).getType().equals("verb")
                    ) {

                if (labeledWords.get(i + 1).getSpanish().equals("es") ||
                        labeledWords.get(i + 1).getSpanish().equals("soy") ||
                        labeledWords.get(i + 1).getSpanish().equals("eres") ||
                        labeledWords.get(i + 1).getSpanish().equals("somos") ||
                        labeledWords.get(i + 1).getSpanish().equals("son")
                        ) {

                    labeledWords.remove(i);

                } else if (labeledWords.get(i + 1).getSpanish().length() > 3) {

                    if (labeledWords.get(i + 1).getSpanish().substring(0, 3).equals("est")) {

                        labeledWords.remove(i);
                    }

                }

            }

        }

        return labeledWords;
    }


    //rule 6 EN to SP
    public static List<Word> ruleENtoSP6(List<Word> labeledWords) {

        for (int i = labeledWords.size() - 1; i > 0; i--) {


            if ((labeledWords.get(i).getType().equals("verb") ||
                    labeledWords.get(i).getEnglish().equals("with"))
                    ) {

                if (i > 0 && negation(labeledWords.get(i - 1))) {
                    i--;
                }

                if (labeledWords.get(i - 1).getSpanish().equals("soy")) {
                    labeledWords.get(i - 1).setSpanish("estoy");
                } else if (labeledWords.get(i - 1).getSpanish().equals("eres")) {

                    labeledWords.get(i - 1).setSpanish("estás");

                } else if (labeledWords.get(i - 1).getSpanish().equals("es")) {

                    labeledWords.get(i - 1).setSpanish("está");

                } else if (labeledWords.get(i - 1).getSpanish().equals("somos")) {

                    labeledWords.get(i - 1).setSpanish("estamos");

                } else if (labeledWords.get(i - 1).getSpanish().equals("son")) {

                    labeledWords.get(i - 1).setSpanish("están");

                }
            }

        }
        return labeledWords;

    }


    //rule 5 EN to SP
    public static List<Word> ruleENtoSP5(List<Word> labeledWords) {

        for (int i = labeledWords.size() - 1; i > 0; i--) {
            if (labeledWords.get(i).getEnglish().equals("are")) {
                if (labeledWords.get(i - 1).getEnglish().equals("you")) {
                    labeledWords.get(i).setSpanish("eres");
                } else if (labeledWords.get(i - 1).getEnglish().equals("we")) {
                    labeledWords.get(i).setSpanish("somos");
                } else if (labeledWords.get(i - 1).getEnglish().equals("they")) {
                    labeledWords.get(i).setSpanish("son");
                }
            }
        }

        return labeledWords;
    }


    //rule 4 EN to SP
    public static List<Word> ruleENtoSP4(List<Word> labeledWords) {

        for (int i = labeledWords.size() - 1; i > 0; i--) {

            if (labeledWords.get(i).getType().equals("noun") &&
                    labeledWords.get(i).getPlural() &&
                    labeledWords.get(i - 1).getType().equals("possessive")
                    ) {

                labeledWords.get(i - 1).setSpanish(labeledWords.get(i - 1).getSpanish() + "s");

            }

        }

        return labeledWords;
    }


    //rule 2 EN to SP
    public static List<Word> ruleENtoSP2(List<Word> labeledWords) {

        boolean lastNounMasculine = true;
        boolean lastNounPlural = false;


        for (int i = 0; i < labeledWords.size(); i++) {

            //Rule 2 part 1
            if (labeledWords.get(i).getType().equals("noun")) {

                if (labeledWords.get(i).getPlural()) {

                    if (isVowel(labeledWords.get(i).getSpanish().charAt(labeledWords.get(i).spanishLength() - 1))) {

                        labeledWords.get(i).setSpanish(labeledWords.get(i).getSpanish() + "s");

                    } else {

                        labeledWords.get(i).setSpanish(labeledWords.get(i).getSpanish() + "es");

                    }

                }

                lastNounMasculine = labeledWords.get(i).getMasculine();
                lastNounPlural = labeledWords.get(i).getPlural();

            }


            if (labeledWords.get(i).getType().equals("pronoun")) {

                if (labeledWords.get(i).getEnglish().equals("I") ||
                        labeledWords.get(i).getEnglish().equals("he") ||
                        labeledWords.get(i).getEnglish().equals("it")) {

                    lastNounMasculine = true;
                    lastNounPlural = false;

                } else if (labeledWords.get(i).getEnglish().equals("she")) {

                    lastNounMasculine = false;

                } else if (labeledWords.get(i).getEnglish().equals("they") ||
                        labeledWords.get(i).getEnglish().equals("we") ||
                        labeledWords.get(i).getEnglish().equals("you")) {

                    lastNounPlural = true;

                }

            }


            //Rule 2 part 2
            if (lastNounPlural && labeledWords.get(i).getType().equals("adjective")) {

                if (isVowel(labeledWords.get(i).getSpanish().charAt(labeledWords.get(i).spanishLength() - 1))) {

                    labeledWords.get(i).setSpanish(labeledWords.get(i).getSpanish() + "s");

                } else {

                    labeledWords.get(i).setSpanish(labeledWords.get(i).getSpanish() + "es");

                }

            }

        }

        return labeledWords;

    }


    //rule 1 EN to SP
    public static List<Word> ruleENtoSP1(List<Word> labeledWords) {

        boolean lastNounMasculine = true;
        boolean lastNounPlural = false;

        for (int i = 0; i < labeledWords.size(); i++) {

            //Rule 1 part 1
            //changes masculine or feminine type of nouns
            if (labeledWords.get(i).getType().equals("noun")) {

                if (labeledWords.get(i).getSpanish().charAt(labeledWords.get(i).getSpanish().length() - 1) == 'a' ||
                        labeledWords.get(i).getSpanish().charAt(labeledWords.get(i).spanishLength() - 1) == 'd' ||
                        labeledWords.get(i).getSpanish().charAt(labeledWords.get(i).spanishLength() - 1) == 'z' ||
                        (labeledWords.get(i).getSpanish().length() > 3 && labeledWords.get(i).getSpanish().substring(labeledWords.get(i).spanishLength() - 3).equals("ión"))
                        ) {

                    labeledWords.get(i).setMasculine(false);
                }

                lastNounMasculine = labeledWords.get(i).getMasculine();
                lastNounPlural = labeledWords.get(i).getPlural();

            } else if (labeledWords.get(i).getType().equals("pronoun")) {

                if (labeledWords.get(i).getEnglish().equals("I") ||
                        labeledWords.get(i).getEnglish().equals("he") ||
                        labeledWords.get(i).getEnglish().equals("it") ||
                        labeledWords.get(i).getEnglish().equals("you")) {

                    lastNounMasculine = true;
                    lastNounPlural = false;

                } else if (labeledWords.get(i).getEnglish().equals("she")) {

                    lastNounMasculine = false;

                } else if (labeledWords.get(i).getEnglish().equals("they") ||
                        labeledWords.get(i).getEnglish().equals("we")) {

                    lastNounPlural = true;

                }

            }

            //Rule 1 part 2
            //changes adjective last letter to o if the noun is feminine
            if (!lastNounMasculine) {

                if (labeledWords.get(i).getType().equals("adjective") &&
                        labeledWords.get(i).getSpanish().charAt(labeledWords.get(i).spanishLength() - 1) == 'o') {

                    labeledWords.get(i).setSpanish(changeLastLetter(labeledWords.get(i).getSpanish(), "a"));
                }

            }


        }

        for (int i = labeledWords.size() - 1; i >= 0; i--) {

            if (labeledWords.get(i).getType().equals("noun")) {

                lastNounMasculine = labeledWords.get(i).getMasculine();
                lastNounPlural = labeledWords.get(i).getPlural();

            }

            if (labeledWords.get(i).getType().equals("article") && !lastNounMasculine) {

                if (labeledWords.get(i).getEnglish().equals("the")) {

                    labeledWords.get(i).setSpanish("la");

                } else if (labeledWords.get(i).getEnglish().equals("a") ||
                        labeledWords.get(i).getEnglish().equals("an")
                        ) {

                    labeledWords.get(i).setSpanish("una");
                }

            }

            if (labeledWords.get(i).getType().equals("article") && lastNounPlural) {

                if (lastNounMasculine) {

                    labeledWords.get(i).setSpanish("los");

                } else {

                    labeledWords.get(i).setSpanish(labeledWords.get(i).getSpanish() + "s");

                }

            }

        }

        return labeledWords;

    }


    //rule 3 EN to SP
    public static List<Word> ruleENtoSP3(List<Word> labeledWords) {

        int nounIndex = -1;
        int adjectiveIndex = -1;

        boolean firstNoun = true;
        boolean firstAdjective = true;


        for (int i = labeledWords.size() - 1; i > 0; i--) {

            if (labeledWords.get(i).getType().equals("noun")) {

                if (labeledWords.get(i - 1).getType().equals("adjective") || labeledWords.get(i - 1).getEnglish().equals("and")) {

                    Collections.swap(labeledWords, i, i - 1);

                }

            }

        }

        return labeledWords;
    }


    //SP to EN rule 5, 4, 3, 2, 1
    public static List<Word> ruleSPtoEN5_4_3_2_1(List<Word> labeledWords) {

        int counter = 0;

        for (int i = 0; i < labeledWords.size(); i++) {

            if (labeledWords.get(i).getType().equals("noun")) {

                while (i < (labeledWords.size() - 1) &&
                        (labeledWords.get(i + 1).getType().equals("adjective") || labeledWords.get(i + 1).getEnglish().equals("and"))
                        ) {

                    Collections.swap(labeledWords, i, (i + 1));

                }

            }

        }

        return labeledWords;
    }

    //checks the vowels
    //returns true if vowel
    public static boolean isVowel(char c) {

        return "aeiou".indexOf(c) != -1;

    }


    //last letter is changed
    public static String changeLastLetter(String string, String x) {

        return string.substring(0, string.length() - 1) + x;

    }


    //fixes sentence separate words and symbols
    public static String fixSentence(String string) {

        string.toLowerCase();

        int lastIndex = 0;

        String whiteSpace = " ";

        for (int i = 0; i < string.length(); i++) {

            if (string.charAt(i) == ',' ||
                    string.charAt(i) == '.' ||
                    string.charAt(i) == '?' ||
                    string.charAt(i) == '!') {

                string = string.substring(0, i) + whiteSpace + string.substring(i);
                i++;

            }

        }

        return string;
    }


    //returns the word with the lower levensthein value
    //used for find a matching word when spelling mistakes are done
    public static String distance(String word, Map<String, Word> dictionary) {

        String lastWord = "";

        int size = -1;

        for (String name : dictionary.keySet()) {

            String key = name;

            int[] costs = new int[key.length() + 1];


            for (int j = 0; j < costs.length; j++)
                costs[j] = j;


            for (int i = 1; i <= word.length(); i++) {

                costs[0] = i;
                int nw = i - 1;

                for (int j = 1; j <= key.length(); j++) {

                    int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), word.charAt(i - 1) == key.charAt(j - 1) ? nw : nw + 1);
                    nw = costs[j];
                    costs[j] = cj;

                }

            }

            //first loop
            if (size == -1) {

                size = costs[key.length()];
                lastWord = key;

            } else if (costs[key.length()] < size) {

                size = costs[key.length()];
                lastWord = key;

            }

        }
        return lastWord;
    }


    //checks if a word is nogation
    public static boolean negation(Word word) {

        if (word.getType().equals("negation")) {

            return true;

        } else {

            return false;
        }

    }

}
