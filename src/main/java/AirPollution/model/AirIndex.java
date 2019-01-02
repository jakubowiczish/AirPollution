package AirPollution.model;

/**
 * Class used to store information about air index
 */
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

    public class IndexLevel {
        private int id;
        private String indexLevelName;

        @Override
        public String toString() {
            return "{" + "\n" +
                    "       id:             " + id + "\n" +
                    "       indexLevelName:'" + indexLevelName + '\'' + "\n" +
                    "   }";
        }

        public int getId() {
            return id;
        }

        public String getIndexLevelName() {
            return indexLevelName;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public IndexLevel getStIndexLevel() {
        return stIndexLevel;
    }

    public void setStIndexLevel(IndexLevel stIndexLevel) {
        this.stIndexLevel = stIndexLevel;
    }

    public String getStCalcDate() {
        return stCalcDate;
    }

    public void setStCalcDate(String stCalcDate) {
        this.stCalcDate = stCalcDate;
    }

    public String getStSourceDataDate() {
        return stSourceDataDate;
    }

    public void setStSourceDataDate(String stSourceDataDate) {
        this.stSourceDataDate = stSourceDataDate;
    }

    public IndexLevel getSo2IndexLevel() {
        return so2IndexLevel;
    }

    public void setSo2IndexLevel(IndexLevel so2IndexLevel) {
        this.so2IndexLevel = so2IndexLevel;
    }

    public String getSo2CalcDate() {
        return so2CalcDate;
    }

    public void setSo2CalcDate(String so2CalcDate) {
        this.so2CalcDate = so2CalcDate;
    }

    public String getSo2SourceDataDate() {
        return so2SourceDataDate;
    }

    public void setSo2SourceDataDate(String so2SourceDataDate) {
        this.so2SourceDataDate = so2SourceDataDate;
    }

    public IndexLevel getNo2IndexLevel() {
        return no2IndexLevel;
    }

    public void setNo2IndexLevel(IndexLevel no2IndexLevel) {
        this.no2IndexLevel = no2IndexLevel;
    }

    public String getNo2CalcDate() {
        return no2CalcDate;
    }

    public void setNo2CalcDate(String no2CalcDate) {
        this.no2CalcDate = no2CalcDate;
    }

    public String getNo2SourceDataDate() {
        return no2SourceDataDate;
    }

    public void setNo2SourceDataDate(String no2SourceDataDate) {
        this.no2SourceDataDate = no2SourceDataDate;
    }

    public IndexLevel getCoIndexLevel() {
        return coIndexLevel;
    }

    public void setCoIndexLevel(IndexLevel coIndexLevel) {
        this.coIndexLevel = coIndexLevel;
    }

    public String getCoCalcDate() {
        return coCalcDate;
    }

    public void setCoCalcDate(String coCalcDate) {
        this.coCalcDate = coCalcDate;
    }

    public String getCoSourceDataDate() {
        return coSourceDataDate;
    }

    public void setCoSourceDataDate(String coSourceDataDate) {
        this.coSourceDataDate = coSourceDataDate;
    }

    public IndexLevel getPm10IndexLevel() {
        return pm10IndexLevel;
    }

    public void setPm10IndexLevel(IndexLevel pm10IndexLevel) {
        this.pm10IndexLevel = pm10IndexLevel;
    }

    public String getPm10CalcDate() {
        return pm10CalcDate;
    }

    public void setPm10CalcDate(String pm10CalcDate) {
        this.pm10CalcDate = pm10CalcDate;
    }

    public String getPm10SourceDataDate() {
        return pm10SourceDataDate;
    }

    public void setPm10SourceDataDate(String pm10SourceDataDate) {
        this.pm10SourceDataDate = pm10SourceDataDate;
    }

    public IndexLevel getPm25IndexLevel() {
        return pm25IndexLevel;
    }

    public void setPm25IndexLevel(IndexLevel pm25IndexLevel) {
        this.pm25IndexLevel = pm25IndexLevel;
    }

    public String getPm25CalcDate() {
        return pm25CalcDate;
    }

    public void setPm25CalcDate(String pm25CalcDate) {
        this.pm25CalcDate = pm25CalcDate;
    }

    public String getPm25SourceDataDate() {
        return pm25SourceDataDate;
    }

    public void setPm25SourceDataDate(String pm25SourceDataDate) {
        this.pm25SourceDataDate = pm25SourceDataDate;
    }

    public IndexLevel getO3IndexLevel() {
        return o3IndexLevel;
    }

    public void setO3IndexLevel(IndexLevel o3IndexLevel) {
        this.o3IndexLevel = o3IndexLevel;
    }

    public String getO3CalcDate() {
        return o3CalcDate;
    }

    public void setO3CalcDate(String o3CalcDate) {
        this.o3CalcDate = o3CalcDate;
    }

    public String getO3SourceDataDate() {
        return o3SourceDataDate;
    }

    public void setO3SourceDataDate(String o3SourceDataDate) {
        this.o3SourceDataDate = o3SourceDataDate;
    }

    public IndexLevel getC6h6IndexLevel() {
        return c6h6IndexLevel;
    }

    public void setC6h6IndexLevel(IndexLevel c6h6IndexLevel) {
        this.c6h6IndexLevel = c6h6IndexLevel;
    }

    public String getC6h6CalcDate() {
        return c6h6CalcDate;
    }

    public void setC6h6CalcDate(String c6h6CalcDate) {
        this.c6h6CalcDate = c6h6CalcDate;
    }

    public String getC6h6SourceDataDate() {
        return c6h6SourceDataDate;
    }

    public void setC6h6SourceDataDate(String c6h6SourceDataDate) {
        this.c6h6SourceDataDate = c6h6SourceDataDate;
    }
}
