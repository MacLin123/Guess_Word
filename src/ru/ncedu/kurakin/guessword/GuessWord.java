package ru.ncedu.kurakin.guessword;

import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Mikhail Kurakin
 */
public class GuessWord implements IGuessWord {
    private final String[] initialWords = {"plug", "obnoxious", "sprout", "rambunctious", "adjustment", "cloudy", "dapper", "shock", "order", "lick", "instruct", "used",
            "wicked", "team", "true", "fog", "simplistic", "extend", "poor", "tasteful", "explode", "bottle", "mean", "axiomatic", "mailbox",
            "unpack", "afterthought", "caption", "substantial", "handsome", "calendar", "travel", "call", "rare", "jeans", "sophisticated", "untidy", "eager", "tail",
            "dirt", "wreck", "appear", "examine", "alarm", "sleepy", "infamous", "blush", "cloistered", "honey"};
    private final static String ACHIEVEMENTS_FILE = "./achievements.json";
    private String nickName;
    private String guessWord;
    private List<String> bankWords = new ArrayList<>();
    private final StringBuilder curWord = new StringBuilder();

    @Override
    public String getGuessWord() {
        return guessWord;
    }

    @Override
    public void setGuessWord(String guessWord) {
        this.guessWord = guessWord;
    }

    @Override
    public String getNickName() {
        return nickName;
    }

    @Override
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public GuessWord() {
        bankWords.addAll(Arrays.asList(initialWords));

    }

    public GuessWord(List<String> bankWords) {
        setBankWords(bankWords);
    }

    public GuessWord(String filepath) {
        setBankWords(filepath);
    }

    @Override
    public void setBankWords(List<String> bankWords) {
        this.bankWords.addAll(bankWords);
    }

    @Override
    public void setBankWords(String filepath) {
        try {
            bankWords = Files.readAllLines(Paths.get(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runGame() {
        setGuessWord(getRandomWord());
        for (int i = 0; i < guessWord.length(); i++) {
            curWord.append("_");
        }
        boolean isGuessed = false;
        int trialCount = 1;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter your name");
        setNickName(in.nextLine());
        for (; !isGuessed; trialCount++) {
            System.out.printf("Key in one character or your guess word:");
            String inputStr = in.nextLine();
            int resultOfGuess = guessWordOrLetter(inputStr);
            if (resultOfGuess == 1) {
                System.out.println("Congratulation!");
                System.out.println("You got in " + trialCount + " trials");
                writeAchievementsToFile(trialCount);
                isGuessed = true;
            } else if (resultOfGuess == -1) {
                System.out.println("You lose!");
                isGuessed = true;
            } else {
                System.out.println("Trial " + trialCount + ":" + curWord.toString());
            }
        }

    }

    @Override
    public String getRandomWord() {
        Random random = new Random();
        int numWord = random.nextInt(bankWords.size());
        return bankWords.get(numWord);
    }

    /**
     * This method determines if the correct letter or word is entered
     *
     * @param inputStr - input string
     * @return 1 if win, 0 if guess letter, - 1 if lose
     */
    private int guessWordOrLetter(String inputStr) {
        if (inputStr.length() == 1) { //one letter
            char inChar = inputStr.charAt(0);
            if (getGuessWord().contains(inputStr) && !curWord.toString().contains(inputStr)) {
                List<Integer> letterIndexes = new ArrayList<>();
                for (int i = 0; i < getGuessWord().length(); i++) {
                    if (inChar == getGuessWord().charAt(i)) {
                        letterIndexes.add(i);
                    }
                }
                for (int i = 0; i < letterIndexes.size(); i++) {
                    curWord.replace(letterIndexes.get(i), letterIndexes.get(i) + 1, inputStr);
                }
                if (curWord.toString().equals(getGuessWord())) {
                    return 1;
                }
                return 0;
            } else {
                return -1;
            }

        } else {
            if (inputStr.equals(getGuessWord())) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * This method writes JsonElement to achievements file
     *
     * @param obj - json element to write
     */
    private void writeJsonElementToFile(JsonElement obj) {
        try (Writer writer = new FileWriter(ACHIEVEMENTS_FILE)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(obj, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeAchievementsToFile(int trialCount) {
        Gson gson = new Gson();
        if (Files.exists(Paths.get(ACHIEVEMENTS_FILE))) {

            Reader reader = null;
            try {
                reader = new FileReader(ACHIEVEMENTS_FILE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            JsonObject playersMap = gson.fromJson(reader, JsonObject.class);
            if (playersMap.has(getNickName())) {
                JsonObject words = playersMap.getAsJsonObject(getNickName());
                words.addProperty(getGuessWord(), trialCount);
            } else {
                JsonObject words = new JsonObject();
                words.addProperty(getGuessWord(), trialCount);
                playersMap.add(getNickName(), words);
            }

            writeJsonElementToFile(playersMap);
        } else {
            JsonObject playersMap = new JsonObject();
            JsonObject words = new JsonObject();
            words.addProperty(getGuessWord(), trialCount);
            playersMap.add(getNickName(), words);

            writeJsonElementToFile(playersMap);
        }
    }
}
