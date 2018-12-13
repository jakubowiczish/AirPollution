package AirPollution;

import java.util.Date;

public class AirIndex {
    int id;

    Date stCalcDate;
    IndexLevel stIndexLevel;
    Date stSourceDataDate;

    Date so2CalcDate;
    IndexLevel so2IndexLevel;
    Date so2SourceDataDate;

    Date no2CalcDate;
    IndexLevel no2IndexLevel;
    Date no2SourceDataDate;

    Date coCalcDate;
    IndexLevel coIndexLevel;
    Date coSourceDataDate;

    Date pm10CalcDate;
    IndexLevel pm10IndexLevel;
    Date pm10SourceDataDate;

    Date pm25CalcDate;
    IndexLevel pm25IndexLevel;
    Date pm25SourceDataDate;

    Date o3CalcDate;
    IndexLevel o3IndexLevel;
    Date o3SourceDataDate;

    Date c6h6CalcDate;
    IndexLevel c6h6IndexLevel;
    Date c6h6SourceDataDate;

    class IndexLevel {
        int id;
        String indexLevelName;
    }

    @Override
    public String toString() {
        return "AirIndex{" +
                "id=" + id +
                ", stIndexLevel=" + stIndexLevel +
                ", stCalcDate=" + stCalcDate +
                ", stSourceDataDate=" + stSourceDataDate +
                ", so2IndexLevel=" + so2IndexLevel +
                ", so2CalcDate=" + so2CalcDate +
                ", so2SourceDataDate=" + so2SourceDataDate +
                ", no2IndexLevel=" + no2IndexLevel +
                ", no2CalcDate=" + no2CalcDate +
                ", no2SourceDataDate=" + no2SourceDataDate +
                ", coIndexLevel=" + coIndexLevel +
                ", coCalcDate=" + coCalcDate +
                ", coSourceDataDate=" + coSourceDataDate +
                ", pm10IndexLevel=" + pm10IndexLevel +
                ", pm10CalcDate=" + pm10CalcDate +
                ", pm10SourceDataDate=" + pm10SourceDataDate +
                ", pm25IndexLevel=" + pm25IndexLevel +
                ", pm25CalcDate=" + pm25CalcDate +
                ", pm25SourceDataDate=" + pm25SourceDataDate +
                ", o3IndexLevel=" + o3IndexLevel +
                ", o3CalcDate=" + o3CalcDate +
                ", o3SourceDataDate=" + o3SourceDataDate +
                ", c6h6IndexLevel=" + c6h6IndexLevel +
                ", c6h6CalcDate=" + c6h6CalcDate +
                ", c6h6SourceDataDate=" + c6h6SourceDataDate +
                '}';
    }
}
