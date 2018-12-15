package AirPollution;

public class JsonAirIndex {
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


//    public static Map<String, Object> convertJsonAirIndexToAirIndex(Map<String, Object> JsonAirIndexMap) {
//        Map<String, Object> AirIndexMap = new HashMap<>();
//        SimpleDateFormat usedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        for (Map.Entry<String, Object> entry : JsonAirIndexMap.entrySet()) {
//            if (entry.getKey().contains("-")) {
//                AirIndexMap.entrySet().add(entry);
//            } else {
//                entry.getValue();
//                entry.setValue(null);
//                AirIndexMap.entrySet().add(entry);
//            }
//        }
//        return AirIndexMap;
//    }


//    public AirIndex convertJsonAirIndex() throws ParseException {
//        SimpleDateFormat usedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        AirIndex newIndex = new AirIndex();
//
//        newIndex.stIndexLevel = this.stIndexLevel;
//
//        if (this.stCalcDate != null && this.stCalcDate.contains("-")) {
//            newIndex.stCalcDate = Utils.toCalendar(usedDateFormat.parse(this.stCalcDate));
//        } else newIndex.stCalcDate = null;
//
//        if (this.stSourceDataDate != null && this.stSourceDataDate.contains("-")) {
//            newIndex.stSourceDataDate = Utils.toCalendar(usedDateFormat.parse(this.stCalcDate));
//        } else newIndex.stSourceDataDate = null;
//
//        newIndex.so2IndexLevel = this.so2IndexLevel;
//
//        if (this.so2CalcDate != null && this.so2CalcDate.contains("-")) {
//            newIndex.so2CalcDate = Utils.toCalendar(usedDateFormat.parse(this.so2CalcDate));
//        } else newIndex.so2CalcDate = null;
//
//        if (this.so2SourceDataDate != null && this.so2SourceDataDate.contains("-")) {
//            newIndex.so2SourceDataDate = Utils.toCalendar(usedDateFormat.parse(this.so2SourceDataDate));
//        } else newIndex.so2SourceDataDate = null;
//
//
//        newIndex.no2IndexLevel = this.no2IndexLevel;
//
//        if (this.no2CalcDate != null && this.no2CalcDate.contains("-")) {
//            newIndex.no2CalcDate = Utils.toCalendar(usedDateFormat.parse(this.no2CalcDate));
//        } else newIndex.no2CalcDate = null;
//        if (this.no2SourceDataDate != null && this.no2SourceDataDate.contains("-")) {
//            newIndex.no2SourceDataDate = Utils.toCalendar(usedDateFormat.parse(this.no2SourceDataDate));
//        } else newIndex.no2SourceDataDate = null;
//
//
//        newIndex.coIndexLevel = this.coIndexLevel;
//
//        if (this.coCalcDate != null && this.coCalcDate.contains("-")) {
//            newIndex.coCalcDate = Utils.toCalendar(usedDateFormat.parse(this.coCalcDate));
//        } else newIndex.coCalcDate = null;
//        if (this.coSourceDataDate != null && this.coSourceDataDate.contains("-")) {
//            newIndex.coSourceDataDate = Utils.toCalendar(usedDateFormat.parse(this.coSourceDataDate));
//        } else newIndex.coSourceDataDate = null;
//
//
//        newIndex.pm10IndexLevel = this.pm10IndexLevel;
//
//        if (this.pm10CalcDate != null && this.pm10CalcDate.contains("-")) {
//            newIndex.pm10CalcDate = Utils.toCalendar(usedDateFormat.parse(this.pm10CalcDate));
//        } else newIndex.pm10CalcDate = null;
//        if (this.pm10SourceDataDate != null && this.pm10SourceDataDate.contains("-")) {
//            newIndex.pm10SourceDataDate = Utils.toCalendar(usedDateFormat.parse(this.pm10SourceDataDate));
//        } else newIndex.pm10SourceDataDate = null;
//
//
//        newIndex.pm25IndexLevel = this.pm25IndexLevel;
//
//        if (this.pm25CalcDate != null && this.pm25CalcDate.contains("-")) {
//            newIndex.pm25CalcDate = Utils.toCalendar(usedDateFormat.parse(this.pm25CalcDate));
//        } else newIndex.pm25CalcDate = null;
//        if (this.pm25SourceDataDate != null && this.pm25SourceDataDate.contains("-")) {
//            newIndex.pm25SourceDataDate = Utils.toCalendar(usedDateFormat.parse(this.pm25SourceDataDate));
//        } else newIndex.pm25SourceDataDate = null;
//
//        newIndex.o3IndexLevel = this.o3IndexLevel;
//
//        if (this.o3CalcDate != null && this.o3CalcDate.contains("-")) {
//            newIndex.o3CalcDate = Utils.toCalendar(usedDateFormat.parse(this.o3CalcDate));
//        } else newIndex.o3CalcDate = null;
//        if (this.o3SourceDataDate != null && this.o3SourceDataDate.contains("-")) {
//            newIndex.o3SourceDataDate = Utils.toCalendar(usedDateFormat.parse(this.o3SourceDataDate));
//        } else newIndex.o3SourceDataDate = null;
//
//        newIndex.c6h6IndexLevel = this.c6h6IndexLevel;
//
//        if (this.c6h6CalcDate != null && this.c6h6CalcDate.contains("-")) {
//            newIndex.c6h6CalcDate = Utils.toCalendar(usedDateFormat.parse(this.c6h6CalcDate));
//        } else newIndex.c6h6CalcDate = null;
//        if (this.c6h6SourceDataDate != null && this.c6h6SourceDataDate.contains("-")) {
//            newIndex.c6h6SourceDataDate = Utils.toCalendar(usedDateFormat.parse(this.c6h6SourceDataDate));
//        } else newIndex.c6h6SourceDataDate = null;
//
//        return newIndex;
//    }


    @Override
    public String toString() {
        return "JsonAirIndex{" +
                "id=" + id +
                ", stIndexLevel=" + stIndexLevel +
                ", stCalcDate='" + stCalcDate + '\'' +
                ", stSourceDataDate='" + stSourceDataDate + '\'' +
                ", so2IndexLevel=" + so2IndexLevel +
                ", so2CalcDate='" + so2CalcDate + '\'' +
                ", so2SourceDataDate='" + so2SourceDataDate + '\'' +
                ", no2IndexLevel=" + no2IndexLevel +
                ", no2CalcDate='" + no2CalcDate + '\'' +
                ", no2SourceDataDate='" + no2SourceDataDate + '\'' +
                ", coIndexLevel=" + coIndexLevel +
                ", coCalcDate='" + coCalcDate + '\'' +
                ", coSourceDataDate='" + coSourceDataDate + '\'' +
                ", pm10IndexLevel=" + pm10IndexLevel +
                ", pm10CalcDate='" + pm10CalcDate + '\'' +
                ", pm10SourceDataDate='" + pm10SourceDataDate + '\'' +
                ", pm25IndexLevel=" + pm25IndexLevel +
                ", pm25CalcDate='" + pm25CalcDate + '\'' +
                ", pm25SourceDataDate='" + pm25SourceDataDate + '\'' +
                ", o3IndexLevel=" + o3IndexLevel +
                ", o3CalcDate='" + o3CalcDate + '\'' +
                ", o3SourceDataDate='" + o3SourceDataDate + '\'' +
                ", c6h6IndexLevel=" + c6h6IndexLevel +
                ", c6h6CalcDate='" + c6h6CalcDate + '\'' +
                ", c6h6SourceDataDate='" + c6h6SourceDataDate + '\'' +
                '}';
    }

    class IndexLevel {
        int id;
        String indexLevelName;

        @Override
        public String toString() {
            return "IndexLevel{" +
                    "id=" + id +
                    ", indexLevelName='" + indexLevelName + '\'' +
                    '}';
        }
    }
}
