package ru.ncedu.kurakin.guessword;

/**
 * @author Mikhail Kurakin
 */
public class Main {
    public static void main(String[] args) {
        IGuessWord gW = new GuessWord("./bankWords.txt");
        gW.runGame();
    }
}
