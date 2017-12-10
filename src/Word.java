import java.io.*;
import java.util.*;


//Word class
public class Word {

    private String english;
    private String spanish;
    private String type;
    private boolean plural = false;
    private boolean masculine = true;

    public Word() {
        //empty constructor
    }

    //each word has english and spanish forms and a type
    public Word(String english, String spanish, String type) {

        this.english = english;
        this.spanish = spanish;
        this.type = type;

    }

    //word also has a boolean value for plurality
    public Word(String english, String spanish, String type, boolean plural) {

        this.english = english;
        this.spanish = spanish;
        this.type = type;
        this.plural = plural;

    }

    //setters
    public void setMasculine(boolean masculine) {
        this.masculine = masculine;
    }

    public void setPlural(boolean plural) {
        this.plural = plural;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSpanish(String spanish) {
        this.spanish = spanish;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    //getters
    public boolean getPlural() {
        return plural;
    }

    public String getEnglish() {
        return english;
    }

    public String getSpanish() {
        return spanish;
    }

    public String getType() {
        return type;
    }

    public boolean getMasculine() {
        return masculine;
    }

    public int englishLength() {
        return getEnglish().length();
    }

    public int spanishLength() {
        return getSpanish().length();
    }

}
