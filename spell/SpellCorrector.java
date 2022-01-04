package spell;

import java.io.*;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
  // Class members
  public Trie wordDict;

  // Class methods
  public SpellCorrector() {
    wordDict = new Trie();
  }

  public void useDictionary(String dictionaryFileName) throws IOException {
    Scanner scanner = null;
    scanner = new Scanner(new File(dictionaryFileName));
    // use the same delimiter as in lab 1
    // scanner.useDelimiter("(\\s+)(#[^\\n]*\\n)?(\\s*)|(#[^\\n]*\\n)(\\s*)");

    // populate dictionary
    while (scanner.hasNext()) {
        String word = scanner.nextLine();
        wordDict.add(word);
    }
    scanner.close();
  }

  public String suggestSimilarWord(String inputWord) {
    if (wordDict.find(inputWord) == null) { // exact word not found
      // match candidates
      Set<String> candidates = new HashSet<String>();

      // place sets of all permittable permutations into level1 set
      Set<String> level1 = new HashSet<String>();
      level1.addAll(deleteSet(inputWord));
      level1.addAll(transposeSet(inputWord));
      level1.addAll(alterSet(inputWord));
      level1.addAll(insertSet(inputWord));

      // find level1 candidates
      for (String word : level1) {
        if (wordDict.find(word) != null)
          candidates.add(word);
      }

      // if no level1 candidates found, process level2
      if (candidates.isEmpty()) {
        Set<String> level2 = new HashSet<String>();
        for (String level1Word : level1) {
          level2.addAll(deleteSet(level1Word));
          level2.addAll(transposeSet(level1Word));
          level2.addAll(alterSet(level1Word));
          level2.addAll(insertSet(level1Word));
        }

        // find level2 candidates
        for (String word : level2) {
          if (wordDict.find(word) != null)
            candidates.add(word);
        }
      }

      // return the best match
      if (candidates.isEmpty())
        return null;
      else { // find the highest-ranking candidate
        String bestCandidate = "";
        int maxFrequency = 0;
        for (String candidate : candidates) {
          int frequency = wordDict.find(candidate).getValue();
          if (frequency > maxFrequency) {
            maxFrequency = frequency;
            bestCandidate = candidate;
          }
          else if (frequency == maxFrequency) {
            if (bestCandidate.compareTo(candidate) > 0)
              bestCandidate = candidate;
          }
        }
        return bestCandidate.toLowerCase();
      }
    }
    else
      return inputWord.toLowerCase();
  }

  Set<String> deleteSet(String word) {
    Set<String> set = new HashSet<String>();
    if (word.length() > 1) {
      for (int i = 0; i < word.length(); i++) {
        StringBuilder deleteWord = new StringBuilder(word);
        deleteWord.deleteCharAt(i);
        set.add(deleteWord.toString());
      }
    }
    return set;
  }

  Set<String> transposeSet(String word) {
    Set<String> set = new HashSet<String>();
    for (int i = 0; i < word.length() - 1; i++) {
      char[] charArray = word.toCharArray();
      char temp = charArray[i];
      charArray[i] = charArray[i+1];
      charArray[i+1] = temp;
      String transposed = new String(charArray);
      set.add(transposed);
    }
    return set;
  }

  Set<String> alterSet(String word) {
    Set<String> set = new HashSet<String>();
    for (int i = 0; i < word.length(); i++) {
      char[] charArray = word.toCharArray();
      for (int j = 0; j < 26; j++) {
        charArray[i] = (char) (j + 'a');
        String altered = new String(charArray);
        set.add(altered);
      }
    }
    return set;
  }

  Set<String> insertSet(String word) {
    Set<String> set = new HashSet<String>();
    for (int i = 0; i <= word.length(); i++) {
      for (int j = 0; j < 26; j++) {
        char c = (char) (j + 'a');
        StringBuilder insertedBuilder = new StringBuilder(word);
        insertedBuilder.insert(i, c);
        String inserted = insertedBuilder.toString();
        set.add(inserted);
      }
    }
    return set;
  }
}
