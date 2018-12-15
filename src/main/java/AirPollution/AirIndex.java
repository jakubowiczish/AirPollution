package AirPollution;

import java.util.Date;

public class AirIndex {

    JsonAirIndex.IndexLevel stIndexLevel;
    Date stCalcDate;
    Date stSourceDataDate;

    JsonAirIndex.IndexLevel so2IndexLevel;
    Date so2CalcDate;
    Date so2SourceDataDate;

    JsonAirIndex.IndexLevel no2IndexLevel;
    Date no2CalcDate;
    Date no2SourceDataDate;

    JsonAirIndex.IndexLevel coIndexLevel;
    Date coCalcDate;
    Date coSourceDataDate;

    JsonAirIndex.IndexLevel pm10IndexLevel;
    Date pm10CalcDate;
    Date pm10SourceDataDate;

    JsonAirIndex.IndexLevel pm25IndexLevel;
    Date pm25CalcDate;
    Date pm25SourceDataDate;

    JsonAirIndex.IndexLevel o3IndexLevel;
    Date o3CalcDate;
    Date o3SourceDataDate;

    JsonAirIndex.IndexLevel c6h6IndexLevel;
    Date c6h6CalcDate;
    Date c6h6SourceDataDate;


    @Override
    public String toString() {
        return "AirIndex{" +
                "stIndexLevel=" + stIndexLevel +
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
