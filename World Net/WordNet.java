import dsa.DiGraph;
import dsa.SeparateChainingHashST;
import dsa.Set;
import stdlib.In;
import stdlib.StdOut;

// 1. Construct a symbol table that maps a synset noun to a set of synset IDs.
//    (SeparateChainingHashST<String, Set<Integer>> st).
// 2. Construct a symbol table that maps a synset ID to the corresponding.
//    synset string (SeparateChainingHashST<Integer, String> rst).
// 3. Construct shortest common ancestor computations (ShortestCommonAncestor sca).
// 4. Construct DiGraph G.
public class WordNet {
    private SeparateChainingHashST<String, Set<Integer>> st; // 1. Construct
    private SeparateChainingHashST<Integer, String> rst; // 2. Construct
    private ShortestCommonAncestor sca; // 3. Construct
    private DiGraph G; // 4. Construct

    // Constructs a WordNet object given the names of the input (synset and hypernym) files.
    // Initialize instance variables st and rst appropriately.
    // If synsets or hypernyms equal null, throw a new NullPointerException() with a message.
    // Using DiGraph object G, representing a rooted DAG, with the vertices V,
    // add edges and read in from the hypernyms file.
    // Initialize sca, shortest common ancestor, using G.
    public WordNet(String synsets, String hypernyms) {
        this.rst = new SeparateChainingHashST<Integer, String>(); // Initialize
        this.st = new SeparateChainingHashST<String, Set<Integer>>(); // Initialize

        if (synsets == null) {
            throw new NullPointerException("synsets is null");
        }
        if (hypernyms == null) {
            throw new NullPointerException("hypernyms is null");
        }

        int v = 0;
        In q = new In(synsets);

        while (q.hasNextLine()) {
            v++;
            String[] line = q.readLine().split(",");
            String[] words = line[1].split(" ");

            int n = Integer.valueOf(line[0]);
            rst.put(Integer.valueOf(line[0]), line[1]);

            for (int x = 0; x < words.length; x++) {
                Set<Integer> stSet = st.get(words[x]);

                if (stSet == null) {
                    stSet = new Set<>();
                    stSet.add(n);
                    st.put(words[x], stSet);
                } else {
                    if (!stSet.contains(n)) {
                        stSet.add(n);
                    }
                }
            }
        }

        G = new DiGraph(v);
        q = new In(hypernyms);

        while (q.hasNextLine()) {
            String[] line = q.readLine().split(",");

            for (int x = 1; x < line.length; x++) {
                G.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[x]));
            }
        }

        sca = new ShortestCommonAncestor(G);
    }

    // Returns all WordNet nouns.
    public Iterable<String> nouns() {
        return st.keys();
    }

    // Returns true if the given word is a WordNet noun, and false otherwise.
    // If word is equal to null, throw a new NullPointerException() with a message.
    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException("word is null");
        }
        return st.contains(word);
    }

    // Using sca to compute.
    // Returns a synset that is a shortest common ancestor of noun1 and noun2.
    // If noun1 & noun2 equal null, throw a new NullPointerException() with a message.
    // If noun1 & noun2 are not nouns, throw a new IllegalArgumentException() with a message.
    public String sca(String noun1, String noun2) {
        if (noun1 == null) {
            throw new NullPointerException("noun1 is null");
        }
        if (noun2 == null) {
            throw new NullPointerException("noun2 is null");
        }
        if (!isNoun(noun1)) {
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        if (!isNoun(noun2)) {
            throw new IllegalArgumentException("noun2 is not a noun");
        }

        Set<Integer> a = st.get(noun1);
        Set<Integer> b = st.get(noun2);
        int SCA = sca.ancestor(a, b);

        return rst.get(SCA);
    }

    // Using sca to compute.
    // Returns the length of the shortest ancestral path between noun1 and noun2.
    // If noun1 & noun2 equal null, throw a new NullPointerException() with a message.
    // If noun1 & noun2 are not nouns, throw a new IllegalArgumentException() with a message.
    public int distance(String noun1, String noun2) {
        if (noun1 == null) {
            throw new NullPointerException("noun1 is null");
        }
        if (noun2 == null) {
            throw new NullPointerException("noun2 is null");
        }
        if (!isNoun(noun1)) {
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        if (!isNoun(noun2)) {
            throw new IllegalArgumentException("noun2 is not a noun");
        }

        Set<Integer> a = st.get(noun1);
        Set<Integer> b = st.get(noun2);

        return sca.length(a, b);
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        String word1 = args[2];
        String word2 = args[3];
        int nouns = 0;
        for (String noun : wordnet.nouns()) {
            nouns++;
        }
        StdOut.printf("# of nouns = %d\n", nouns);
        StdOut.printf("isNoun(%s)? %s\n", word1, wordnet.isNoun(word1));
        StdOut.printf("isNoun(%s)? %s\n", word2, wordnet.isNoun(word2));
        StdOut.printf("isNoun(%s %s)? %s\n", word1, word2, wordnet.isNoun(word1 + " " + word2));
        StdOut.printf("sca(%s, %s) = %s\n", word1, word2, wordnet.sca(word1, word2));
        StdOut.printf("distance(%s, %s) = %s\n", word1, word2, wordnet.distance(word1, word2));
    }
}
