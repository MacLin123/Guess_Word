package ru.ncedu.kurakin.guessword;

import java.util.List;

/**
 * This interface provides the ability to play in guess word game
 *
 * @author Mikhail Kurakin
 */
public interface IGuessWord {
    /**
     * This method sets player's nickName
     *
     * @param nickName - player's nickname
     */
    void setNickName(String nickName);

    /**
     * This method returns player's nickName
     *
     * @return player's nickName
     */
    String getNickName();

    /**
     * This method sets word for guessing
     *
     * @param guessWord - word for guessing
     */
    void setGuessWord(String guessWord);

    /**
     * This method returns word for guessing
     *
     * @return word for guessing
     */
    String getGuessWord();

    /**
     * This method sets bank of words for a game
     *
     * @param bankWords - bank of words
     */
    void setBankWords(List<String> bankWords);

    /**
     * This method sets bank of words from a file
     *
     * @param filepath - file path
     */
    void setBankWords(String filepath);

    /**
     * This method runs game
     */
    void runGame();

    /**
     * This method returns random word from a bank of words
     *
     * @return random word from a bank of words
     */
    String getRandomWord();

    /**
     * This method writes achievements of players to a file
     * @param trialCount - number of a trial when player wins
     */
    void writeAchievementsToFile(int trialCount);

}
