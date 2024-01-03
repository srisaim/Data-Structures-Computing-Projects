import stdlib.StdIn;
import stdlib.StdOut;

public class Outcast {
    private WordNet wordnet; // Create variable WordNet wordnet.

    // Constructs an Outcast object given the WordNet semantic lexicon.
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet; // Initialize variable wordnet.
    }

    // Create and initialize variables.
    // Compute the sum of the distance, using wordnet, between each noun in nouns.
    // Returns the outcast noun from nouns, the noun with the largest distance.
    public String outcast(String[] nouns) {
        int dist = 0; // Create int distance = 0.
        String oca = nouns[0]; // Create String oca, outcast noun.

        for (int x = 0; x < nouns.length; x++) {
            int sum = 0; // Create int sum = 0.

            for (int y = 0; y < nouns.length; y++) {
                if (x == y) {
                    continue;
                }

                int d = wordnet.distance(nouns[x], nouns[y]);
                if (d != -1) {
                    sum += d;
                }
            }
            if (sum > dist) {
                dist = sum;
                oca = nouns[x];
            }
        }

        // Return the outcast noun.
        return oca;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        String[] nouns = StdIn.readAllStrings();
        String outcastNoun = outcast.outcast(nouns);
        for (String noun : nouns) {
            StdOut.print(noun.equals(outcastNoun) ? "*" + noun + "* " : noun + " ");
        }
        StdOut.println();
    }
}
