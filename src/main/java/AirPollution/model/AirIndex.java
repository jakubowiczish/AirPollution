package AirPollution.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Class used to store information about air index
 */
@Getter
@Setter
public class AirIndex {

    private int id;

    private IndexLevel stIndexLevel;
    private String stCalcDate;
    private String stSourceDataDate;

    private IndexLevel so2IndexLevel;
    private String so2CalcDate;
    private String so2SourceDataDate;

    private IndexLevel no2IndexLevel;
    private String no2CalcDate;
    private String no2SourceDataDate;

    private IndexLevel coIndexLevel;
    private String coCalcDate;
    private String coSourceDataDate;

    private IndexLevel pm10IndexLevel;
    private String pm10CalcDate;
    private String pm10SourceDataDate;

    private IndexLevel pm25IndexLevel;
    private String pm25CalcDate;
    private String pm25SourceDataDate;

    private IndexLevel o3IndexLevel;
    private String o3CalcDate;
    private String o3SourceDataDate;

    private IndexLevel c6h6IndexLevel;
    private String c6h6CalcDate;
    private String c6h6SourceDataDate;

    @Override
    public String toString() {
        return "{" + "\n" +
                "   id:                 " + id + "\n" +
                "   stIndexLevel:       " + stIndexLevel + "\n" +
                "   stCalcDate:         " + stCalcDate + "\n" +
                "   stSourceDataDate:   " + stSourceDataDate + "\n" +
                "   so2IndexLevel:      " + so2IndexLevel + "\n" +
                "   so2CalcDate:        " + so2CalcDate + "\n" +
                "   so2SourceDataDate:  " + so2SourceDataDate + "\n" +
                "   no2IndexLevel:      " + no2IndexLevel + "\n" +
                "   no2CalcDate:        " + no2CalcDate + "\n" +
                "   no2SourceDataDate:  " + no2SourceDataDate + "\n" +
                "   coIndexLevel:       " + coIndexLevel + "\n" +
                "   coCalcDate:         " + coCalcDate + "\n" +
                "   coSourceDataDate:   " + coSourceDataDate + "\n" +
                "   pm10IndexLevel:     " + pm10IndexLevel + "\n" +
                "   pm10CalcDate:       " + pm10CalcDate + "\n" +
                "   pm10SourceDataDate: " + pm10SourceDataDate + "\n" +
                "   pm25IndexLevel:     " + pm25IndexLevel + "\n" +
                "   pm25CalcDate:       " + pm25CalcDate + "\n" +
                "   pm25SourceDataDate: " + pm25SourceDataDate + "\n" +
                "   o3IndexLevel:       " + o3IndexLevel + "\n" +
                "   o3CalcDate:         " + o3CalcDate + "\n" +
                "   o3SourceDataDate:   " + o3SourceDataDate + "\n" +
                "   c6h6IndexLevel:     " + c6h6IndexLevel + "\n" +
                "   c6h6CalcDate:       " + c6h6CalcDate + "\n" +
                "   c6h6SourceDataDate: " + c6h6SourceDataDate + "\n" +
                '}';
    }

    @Getter
    @Setter
    public static class IndexLevel {
        private int id;
        private String indexLevelName;

        @Override
        public String toString() {
            return "{" + "\n" +
                    "       id:             " + id + "\n" +
                    "       indexLevelName:'" + indexLevelName + '\'' + "\n" +
                    "   }";
        }
    }
}
