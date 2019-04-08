package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;
import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Integer> wordDic = new ChainedHashDictionary<>();
        int size = pages.size();
        for (Webpage page : pages) {
            IList<String> words = page.getWords();
            ISet<String> wordCollector = new ChainedHashSet<>();
            for (String word : words) {
                wordCollector.add(word);
            }
            for (String word : wordCollector) {
                wordDic.put(word, wordDic.getOrDefault(word, 0) + 1);
            }
        }
        IDictionary<String, Double> wordDic2 = new ChainedHashDictionary<>();
        for (KVPair<String, Integer> pair : wordDic) {
            double idf = Math.log((double) size / pair.getValue());
            wordDic2.put(pair.getKey(), idf);
        }
        return wordDic2;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Integer> frequenDic = new ChainedHashDictionary<>();
        int size = words.size();
        for (String word : words) {
            frequenDic.put(word, frequenDic.getOrDefault(word, 0) + 1);
        }
        IDictionary<String, Double> tfDic = new ChainedHashDictionary<>();
        for (KVPair<String, Integer> pair : frequenDic) {
            tfDic.put(pair.getKey(), (double) pair.getValue() / size);
        }
        return tfDic;
    }


    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        IDictionary<URI, IDictionary<String, Double>> tfIdfDic = new ChainedHashDictionary<>();
        for (Webpage page : pages) {
            IDictionary<String, Double> tfDic = computeTfScores(page.getWords());
            IDictionary<String, Double> newTfIdfDic = combineScores(tfDic);
            tfIdfDic.put(page.getUri(), newTfIdfDic);
        }
        return tfIdfDic;
    }

    private IDictionary<String, Double> combineScores(IDictionary<String, Double> tfScores) {
        IDictionary<String, Double> tfIdfDic = new ChainedHashDictionary<>();
        for (KVPair<String, Double> tfScore : tfScores) {
            String key = tfScore.getKey();
            Double tf = tfScore.getValue();
            tfIdfDic.put(key, tf * this.idfScores.getOrDefault(key, 0.0));
        }
        return tfIdfDic;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        IDictionary<String, Double> dScores = this.documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> qTfScores = computeTfScores(query);
        IDictionary<String, Double> qScores = combineScores(qTfScores);
        double numerator = 0.0;
        for (String atom : query) {
            Double docWordScore = dScores.getOrDefault(atom, 0.0);
            Double queryWordScore = qScores.get(atom);
            numerator += docWordScore * queryWordScore;
        }
        double dominator = norm(dScores) * norm(qScores);
        if (dominator != 0) {
            return numerator / dominator;
        } else {
            return 0.0;
        }
    }

    private Double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        for (KVPair<String, Double> pair : vector) {
            double score = pair.getValue();
            output += score * score;
        }
        return Math.sqrt(output);
    }
}
