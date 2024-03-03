package ngordnet.ngrams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        for (Map.Entry<Integer, Double> entry : ts.entrySet()) {
            if (entry.getKey() >= startYear && entry.getKey() <= endYear) {
                this.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        ArrayList<Integer> justKeys = new ArrayList<>();
        for (Integer key : this.keySet()) {
            justKeys.add(key);
        }
        return justKeys;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        ArrayList<Double> justValues = new ArrayList<>();
        for (Double values : this.values()) {
            justValues.add(values);
        }
        return justValues;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     * <p>
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries sumSeries = new TimeSeries();
        for (Map.Entry<Integer, Double> entry : this.entrySet()) {
            int key = entry.getKey();
            double value = entry.getValue();
            if (ts.containsKey(key)) {
                value += ts.get(key);
            }
            sumSeries.put(key, value);
        }
        for (Map.Entry<Integer, Double> entry : ts.entrySet()) {
            int key2 = entry.getKey();
            double value2 = entry.getValue();
            if (!this.containsKey(key2)) {
                sumSeries.put(key2, value2);
            }
        }
        return sumSeries;
    }


    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     * <p>
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        divideByHelper(ts);

        TimeSeries newDivide = new TimeSeries();
        for (int year : this.years()) {
            if (ts.containsKey(year)) {
                double divideValue = this.get(year) / ts.get(year);
                newDivide.put(year, divideValue);
            }
        }
        return newDivide;
    }

    public TimeSeries divideByHelper(TimeSeries ts) {
        for (int year : this.years()) {
            if (!ts.containsKey(year)) {
                throw new IllegalArgumentException("There isn't key for a that particular year in the TimeSeries");
            }
        }
        return ts;
    }
}
