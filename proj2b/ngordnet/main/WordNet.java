package ngordnet.main;

import edu.princeton.cs.algs4.In;

import java.util.*;
import java.util.stream.Collectors;

public class WordNet {
    private Map<Integer, List<Integer>> synIdNodes;
    private Map<String, Set<Integer>> wordToSynIds;
    private Map<Integer, Set<String>> synIdToSynset;

    public WordNet(String synsetsFileName, String hyponymsFileName) {
        // Graph Object
        synIdNodes = new HashMap<Integer, List<Integer>>();
        // Mapping of each word to which Synset Id it corresponds to
        wordToSynIds = new HashMap<String, Set<Integer>>();
        // Mapping of synset Id to actual Synset
        synIdToSynset = new HashMap<Integer, Set<String>>();

        In synsetsFile = new In(synsetsFileName);
        while (!synsetsFile.isEmpty()) {
            String nextLine = synsetsFile.readLine();
            String[] synsetData = nextLine.split(",");
            // get Id and words from synset file
            int synId = Integer.parseInt(synsetData[0]);
            List<String> synsetWords = Arrays.asList(synsetData[1].split(" "));
            // adding synset Ids to graph, pointing to empty list
            synIdNodes.put(synId, new ArrayList<Integer>());
            // create mapping from synset ID to words within that synset
            synIdToSynset.putIfAbsent(synId, new HashSet<String>(synsetWords));

            // create mapping of word to which synsetId it is in
            for (String word : synsetWords) {
                if (!wordToSynIds.containsKey(word)) {
                    wordToSynIds.put(word, new HashSet<Integer>());
                }
                wordToSynIds.get(word).add(synId);
            }
        }

        In hyponymsFile = new In(hyponymsFileName);
        while (!hyponymsFile.isEmpty()) {
            String nextLine = hyponymsFile.readLine();
            String[] synIdStrings = nextLine.split(",");
            List<Integer> hyponymChain = Arrays.stream(synIdStrings)    // stream of String
                    .map(Integer::valueOf)  // stream of Integer
                    .collect(Collectors.toList());
            Integer parentSynId = hyponymChain.remove(0);
            synIdNodes.get(parentSynId).addAll(hyponymChain);
        }
    }

    private Set<Integer> dfsTraversal(Integer parentSynId) {
        Set<Integer> visited = new TreeSet<Integer>();
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(parentSynId);
        while (!stack.isEmpty()) {
            Integer synId = stack.pop();
            if (!visited.contains(synId)) {
                visited.add(synId);
                for (Integer id : synIdNodes.get(synId)) {
                    stack.push(id);
                }
            }
        }
        return visited;
    }

    private Set<String> hyponymsOf(String word) {
        Set<Integer> parentSynIds = wordToSynIds.get(word);
        Set<String> hyponymsResult = new HashSet<>();
        if (parentSynIds != null) {
            for (Integer parentSynId : parentSynIds) {
                Set<Integer> childrenSynIds = dfsTraversal(parentSynId);
                for (Integer synId : childrenSynIds) {
                    hyponymsResult.addAll(synIdToSynset.get(synId));
                }
            }
        }
        return hyponymsResult;
    }

    public List<String> getHyponyms(List<String> words) {
        List<Set<String>> allHyponyms = new ArrayList<>();
        for (String word : words) {
            Set<String> wordHyponyms = hyponymsOf(word);
            allHyponyms.add(wordHyponyms);
        }
        Set<String> intersection = allHyponyms.get(0);
        for (int i = 1; i < allHyponyms.size(); i++) {
            intersection.retainAll(allHyponyms.get(i));
        }
        List<String> hyponymsList = new ArrayList<String>(intersection);  //convert set to list
        Collections.sort(hyponymsList);
        return hyponymsList;
    }

    public String printNodes() {
        String output = "";
        for (Integer synId : synIdNodes.keySet()) {
            Set<String> parentSynSet = synIdToSynset.get(synId);
            output += parentSynSet.toString() + " -> ";
            for (Integer childId : synIdNodes.get(synId)) {
                output += synIdToSynset.get(childId).toString() + " , ";
            }
            output += "\n";
        }
        return output;
    }

}
