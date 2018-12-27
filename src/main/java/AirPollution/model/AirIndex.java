package AirPollution.model;

public class AirIndex {
    int id;

    IndexLevel stIndexLevel;
    String stCalcDate;
    String stSourceDataDate;

    IndexLevel so2IndexLevel;
    String so2CalcDate;
    String so2SourceDataDate;

    IndexLevel no2IndexLevel;
    String no2CalcDate;
    String no2SourceDataDate;

    IndexLevel coIndexLevel;
    String coCalcDate;
    String coSourceDataDate;

    IndexLevel pm10IndexLevel;
    String pm10CalcDate;
    String pm10SourceDataDate;

    IndexLevel pm25IndexLevel;
    String pm25CalcDate;
    String pm25SourceDataDate;

    IndexLevel o3IndexLevel;
    String o3CalcDate;
    String o3SourceDataDate;

    IndexLevel c6h6IndexLevel;
    String c6h6CalcDate;
    String c6h6SourceDataDate;

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

    class IndexLevel {
        int id;
        String indexLevelName;

        @Override
        public String toString() {
            return "{" + "\n" +
                    "       id:             " + id + "\n" +
                    "       indexLevelName:'" + indexLevelName + '\'' + "\n" +
                    "   }";
        }
    }
}
