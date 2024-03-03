package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.*;
import java.util.stream.Collectors;

public class HyponymsHandler extends NgordnetQueryHandler {
    private NGramMap ngm;
    private WordNet wordnet;

    public HyponymsHandler(NGramMap ngmCurr, WordNet wordnetCurr) {
        ngm = ngmCurr;
        wordnet = wordnetCurr;
    }

    private class HyponymWordCountComparator implements Comparator<String> {
        int _startYear;
        int _endYear;

        private HyponymWordCountComparator(int startYear, int endYear) {
            super();
            _startYear = startYear;
            _endYear = endYear;
        }

        @Override
        public int compare(String word1, String word2) {
            TimeSeries ts1 = ngm.countHistory(word1, _startYear, _endYear);
            TimeSeries ts2 = ngm.countHistory(word2, _startYear, _endYear);
            Double totalWordCount1 = ts1.data().stream().mapToDouble(f -> f.doubleValue()).sum();
            Double totalWordCount2 = ts2.data().stream().mapToDouble(f -> f.doubleValue()).sum();
            if (totalWordCount2 > totalWordCount1) {
                return 1;
            } else if (totalWordCount2 < totalWordCount1) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private List<String> sortHyponymsOnNGM(List<String> hyponyms, int startYear, int endYear) {
        List<String> sortedHyponyms = hyponyms.stream().collect(Collectors.toList());
        Collections.sort(sortedHyponyms, new HyponymWordCountComparator(startYear, endYear));
        return sortedHyponyms;
    }

    private static HashMap<String, Double> sortByValue(HashMap<String, Double> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double>> list =
                new LinkedList<Map.Entry<String, Double>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        Collections.reverse(list);

        // put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        List<String> hyponyms = wordnet.getHyponyms((words));
        if (k != 0) {
            HashMap<String, Double> hyponymMap = new HashMap<>();
            for (String hyponym : hyponyms) {
                TimeSeries ts = ngm.countHistory(hyponym, startYear, endYear);
                Double totalWordCount = ts.data().stream().mapToDouble(f -> f.doubleValue()).sum();
                if (totalWordCount > 0) {
                    hyponymMap.put(hyponym, totalWordCount);
                }
            }
            Map<String, Double> sortedHyponymMap = sortByValue(hyponymMap);
            List<String> limitedSortedHyponyms = sortedHyponymMap.keySet().stream().limit(k).
                    collect(Collectors.toList());
            Collections.sort(limitedSortedHyponyms);
            return "[" + String.join(", ", limitedSortedHyponyms) + "]";
        }
        return "[" + String.join(", ", hyponyms) + "]";
    }
}
