package words.g2;

import words.core.Letter;
import words.core.Player;
import words.core.PlayerBids;
import words.core.SecretState;
import words.core.ScrabbleValues;

import java.util.*;

public class albertPlayer2 extends Player {

    private Map<Character, Integer> letterFrequency;
    private Set<String> sevenLetterWords;


    @Override
    public int bid(Letter bidLetter, List<PlayerBids> playerBidList,
                   int totalRounds, ArrayList<String> playerList,
                   SecretState secretState, int playerID) {
        char letter = bidLetter.getCharacter();
        int letterValue = bidLetter.getValue();
        int currentScore = secretState.getScore();
        int roundsLeft = totalRounds - playerBidList.size() / (playerList.size() * 8);

        // Start with a base bid calculation
        int baseBid = 100 / (ScrabbleValues.letterScore(letter) * ScrabbleValues.getLetterFrequency(letter));

        // Increase bid for high-value or rare letters
        if ("BCMPKJQXZ".indexOf(letter) != -1) {
            baseBid += 3;
        }

        // Adjust bid for vowels based on current hand
        if ("AEIOU".indexOf(letter) != -1) {
            int vowelCount = countVowels();
            if (vowelCount < 2) {
                baseBid += 1;
            } else if (vowelCount > 3) {
                baseBid /= 2;
            }
        }

        // Check for potential word improvement
        String currentBestWord = returnWord();
        myLetters.add(letter);
        String potentialBestWord = returnWord();
        myLetters.remove(myLetters.size() - 1);  // Remove the added letter

        if (potentialBestWord.length() >= 7) {
            baseBid = Math.max(baseBid, letterValue * 2);  // Bid more aggressively for 7-letter words
        } else if ((ScrabbleValues.getWordScore(potentialBestWord) - ScrabbleValues.getWordScore(currentBestWord)) > baseBid / 2) {
            baseBid *= 1.5;  // Increase bid if it significantly improves our word
        } else {
            baseBid /= 2;  // Decrease bid if it doesn't improve our word much
        }

        // Be more aggressive in final rounds
        if (roundsLeft <= 2) {
            baseBid *= 1.5;
        }

        if (baseBid >= 10) {
            baseBid = 7;
        }

        // Ensure bid is within valid range
        return Math.max(1, Math.min(baseBid, currentScore - 1));
    }

    private int countVowels() {
        int count = 0;
        for (Character letter : myLetters) {
            if ("AEIOU".indexOf(letter) != -1) {
                count++;
            }
        }
        return count;
    }
}
