package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less than or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        IDictionary<URI, ISet<URI>> graph = new ChainedHashDictionary<>();
        ISet<URI> linkList = new ChainedHashSet<>();
        for (Webpage web : webpages) {
            linkList.add(web.getUri());
        }
        for (Webpage web2 : webpages) {
            ISet<URI> containedLink = new ChainedHashSet<>();
            URI uri = web2.getUri();
            for (URI link : web2.getLinks()) {
                if (linkList.contains(link)) {
                    containedLink.add(link);
                }
            }
            graph.put(uri, containedLink);
        }
        return graph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less than or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
        // Step 1: The initialize step should go here
        IDictionary<URI, Double> oldRanks = new ChainedHashDictionary<>();
        for (KVPair pair : graph) {
            oldRanks.put((URI) pair.getKey(), 1.0 / graph.size());
        }
        int n = graph.size();
        for (int i = 0; i < limit; i++) {
            // Step 2: The update step should go here
            boolean converge = true;
            IDictionary<URI, Double> newRanks = new ChainedHashDictionary<>();
            for (KVPair<URI, ISet<URI>> pair : graph) {
                newRanks.put(pair.getKey(), 0.0);
            }
            for (KVPair<URI, ISet<URI>> pair : graph) {
                double rank = oldRanks.get(pair.getKey());
                ISet<URI> incrementSet = pair.getValue();
                URI now = pair.getKey();
                if (incrementSet.size() > 0) {
                    for (URI uri : incrementSet) {
                        double newScore = newRanks.get(uri) + decay * (rank / incrementSet.size());
                        newRanks.put(uri, newScore);
                    }
                } else {
                    for (KVPair<URI, ISet<URI>> pair2 : graph) {
                        URI uri2 = pair2.getKey();
                        newRanks.put(uri2, (newRanks.get(uri2) + (decay * rank / n)));
                    }
                }
                newRanks.put(now, newRanks.get(now) + (1.0 - decay)/n);
            }
            // Step 3: the convergence step should go here.
            for (KVPair<URI, ISet<URI>> pair : graph) {
                URI uri = pair.getKey();
                if (Math.abs(newRanks.get(uri) - oldRanks.get(uri)) >= epsilon) {
                    converge = false;
                    break;
                }
            }
            // Return early if we've converged.
            if (converge) {
                return oldRanks;
            } else {
                oldRanks = newRanks;
            }
        }
        return oldRanks;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return pageRanks.get(pageUri);
    }
}
