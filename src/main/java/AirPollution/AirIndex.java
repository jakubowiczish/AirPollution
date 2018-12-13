package AirPollution;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AirIndex {



    JsonAirIndex.IndexLevel stIndexLevel;
    Calendar stCalcDate;
    Calendar stSourceDataDate;

    JsonAirIndex.IndexLevel so2IndexLevel;
    Calendar so2CalcDate;

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "AirIndex{" +
                "stIndexLevel=" + stIndexLevel +
                ", stCalcDate=" + df.format(stCalcDate.getTime()) +
                ", stSourceDataDate=" + df.format(stSourceDataDate.getTime()) +
                ", so2IndexLevel=" + so2IndexLevel +
                ", so2CalcDate=" + df.format(so2CalcDate.getTime()) +
                ", so2SourceDataDate=" + df.format(so2SourceDataDate.getTime()) +
                ", no2IndexLevel=" + no2IndexLevel +
                ", no2CalcDate=" + df.format(no2CalcDate.getTime()) +
                ", no2SourceDataDate=" + df.format(no2SourceDataDate.getTime()) +
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

    Calendar so2SourceDataDate;

    JsonAirIndex.IndexLevel no2IndexLevel;
    Calendar no2CalcDate;
    Calendar no2SourceDataDate;

    JsonAirIndex.IndexLevel coIndexLevel;
    Calendar coCalcDate;
    Calendar coSourceDataDate;

    JsonAirIndex.IndexLevel pm10IndexLevel;
    Calendar pm10CalcDate;
    Calendar pm10SourceDataDate;

    JsonAirIndex.IndexLevel pm25IndexLevel;
    Calendar pm25CalcDate;
    Calendar pm25SourceDataDate;

    JsonAirIndex.IndexLevel o3IndexLevel;
    Calendar o3CalcDate;
    Calendar o3SourceDataDate;

    JsonAirIndex.IndexLevel c6h6IndexLevel;
    Calendar c6h6CalcDate;
    Calendar c6h6SourceDataDate;

//    class IndexLevel {
//        int id;
//        String indexLevelName;
//    }
}
