//package com.example.demo;
//
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@NoArgsConstructor
//@Data
//public class GeocoderMessage {
//
//
//    private Integer status;
//
//    private ResultBean result;
//
//    @NoArgsConstructor
//    @Data
//    public static class ResultBean {
//
//        private LocationBean location;
//
//        private String formatted_address;
//
//        private String business;
//
//        private AddressComponentBean addressComponent;
//
//
//        private List<PoiRegionsBean> poiRegions;
//
//        private String sematic_description;
//
//        private Integer cityCode;
//
//        @NoArgsConstructor
//        @Data
//        public static class LocationBean {
//            private Double lng;
//
//            private Double lat;
//        }
//
//        @NoArgsConstructor
//        @Data
//        public static class AddressComponentBean {
//            private String country;
//
//            private Integer country_code;
//
//            private String country_code_iso;
//
//            private String country_code_iso2;
//
//            private String province;
//
//            private String city;
//
//            private Integer city_level;
//
//            private String district;
//
//            private String town;
//
//            private String town_code;
//
//            private String adcode;
//
//            private String street;
//
//            private String street_number;
//
//            private String direction;
//
//            private String distance;
//        }
//
//        @NoArgsConstructor
//        @Data
//        public static class PoiRegionsBean {
//            private String direction_desc;
//
//            private String name;
//
//            private String tag;
//
//            private String uid;
//
//            private String distance;
//        }
//    }
//
//
//}
