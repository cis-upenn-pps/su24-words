package words.g2;

import words.core.Letter;
import words.core.Player;
import words.core.PlayerBids;
import words.core.SecretState;
import words.core.ScrabbleValues;

import java.util.*;

public class StrategicPlayer extends Player {

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

        // Start with the letter value as base bid
        int baseBid = letterValue;

        // Increase bid for high-value or rare letters
        if ("BCMPKJQXZ".indexOf(letter) != -1) {
            baseBid *= 4;
        }

        // Adjust bid for vowels based on current hand
        if ("AEIOU".indexOf(letter) != -1) {
            int vowelCount = countVowels();
            if (vowelCount < 2) {
                baseBid += 7;
            } else if (vowelCount > 3) {
                baseBid /= 2;
            }
        }

        // Be more aggressive in final rounds
        if (roundsLeft <= 2) {
            baseBid *= 1.5;
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