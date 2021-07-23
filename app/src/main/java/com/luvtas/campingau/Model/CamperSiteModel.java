package com.luvtas.campingau.Model;

import java.util.List;

public class CamperSiteModel {
    String CamperSiteID;
    String CamperSiteName;
    List<String> CamperSiteImages;
    String CamperSiteType;
    String CamperSiteDistance;
    String CamperSiteInfo;
    List<Integer> CamperSiteSummary;
    String CamperSiteAddress;
    Double CamperSiteLatitude;
    Double CamperSiteLongitude;
    String CamperSiteLatLng;
    String CamperSitePrice1;
    String CamperSitePrice2;
    String CamperSiteEmail;
    String CamperSiteSub;
    String CamperSiteDescription;
    Long ServerTimeStamp;

    public String getCamperSiteLatLng() {
        return CamperSiteLatLng;
    }

    public void setCamperSiteLatLng(String camperSiteLatLng) {
        CamperSiteLatLng = camperSiteLatLng;
    }

    public CamperSiteModel() {
    }

    public CamperSiteModel(String camperSiteID, String camperSiteName, List<String> camperSiteImages, String camperSiteType, String camperSiteDistance, String camperSiteInfo, List<Integer> camperSiteSummary, String camperSiteAddress, Double camperSiteLatitude, Double camperSiteLongitude, String camperSitePrice1, String camperSitePrice2, String camperSiteEmail, String camperSiteSub, String camperSiteDescription, Long serverTimeStamp, String camperSiteLatLng) {
        CamperSiteID = camperSiteID;
        CamperSiteName = camperSiteName;
        CamperSiteImages = camperSiteImages;
        CamperSiteType = camperSiteType;
        CamperSiteDistance = camperSiteDistance;
        CamperSiteInfo = camperSiteInfo;
        CamperSiteSummary = camperSiteSummary;
        CamperSiteAddress = camperSiteAddress;
        CamperSiteLatitude = camperSiteLatitude;
        CamperSiteLongitude = camperSiteLongitude;
        CamperSitePrice1 = camperSitePrice1;
        CamperSitePrice2 = camperSitePrice2;
        CamperSiteEmail = camperSiteEmail;
        CamperSiteSub = camperSiteSub;
        CamperSiteDescription = camperSiteDescription;
        ServerTimeStamp = serverTimeStamp;
        CamperSiteLatLng = camperSiteLatLng;
    }

    public Long getServerTimeStamp() {
        return ServerTimeStamp;
    }

    public void setServerTimeStamp(Long serverTimeStamp) {
        ServerTimeStamp = serverTimeStamp;
    }

    public String getCamperSiteID() {
        return CamperSiteID;
    }

    public void setCamperSiteID(String camperSiteID) {
        CamperSiteID = camperSiteID;
    }

    public String getCamperSiteName() {
        return CamperSiteName;
    }

    public void setCamperSiteName(String camperSiteName) {
        CamperSiteName = camperSiteName;
    }

    public List<String> getCamperSiteImages() {
        return CamperSiteImages;
    }

    public void setCamperSiteImages(List<String> camperSiteImages) {
        CamperSiteImages = camperSiteImages;
    }

    public String getCamperSiteType() {
        return CamperSiteType;
    }

    public void setCamperSiteType(String camperSiteType) {
        CamperSiteType = camperSiteType;
    }

    public String getCamperSiteDistance() {
        return CamperSiteDistance;
    }

    public void setCamperSiteDistance(String camperSiteDistance) {
        CamperSiteDistance = camperSiteDistance;
    }

    public String getCamperSiteInfo() {
        return CamperSiteInfo;
    }

    public void setCamperSiteInfo(String camperSiteInfo) {
        CamperSiteInfo = camperSiteInfo;
    }

    public List<Integer> getCamperSiteSummary() {
        return CamperSiteSummary;
    }

    public void setCamperSiteSummary(List<Integer> camperSiteSummary) {
        CamperSiteSummary = camperSiteSummary;
    }

    public String getCamperSiteAddress() {
        return CamperSiteAddress;
    }

    public void setCamperSiteAddress(String camperSiteAddress) {
        CamperSiteAddress = camperSiteAddress;
    }

    public Double getCamperSiteLatitude() {
        return CamperSiteLatitude;
    }

    public void setCamperSiteLatitude(Double camperSiteLatitude) {
        CamperSiteLatitude = camperSiteLatitude;
    }

    public Double getCamperSiteLongitude() {
        return CamperSiteLongitude;
    }

    public void setCamperSiteLongitude(Double camperSiteLongitude) {
        CamperSiteLongitude = camperSiteLongitude;
    }

    public String getCamperSitePrice1() {
        return CamperSitePrice1;
    }

    public void setCamperSitePrice1(String camperSitePrice1) {
        CamperSitePrice1 = camperSitePrice1;
    }

    public String getCamperSitePrice2() {
        return CamperSitePrice2;
    }

    public void setCamperSitePrice2(String camperSitePrice2) {
        CamperSitePrice2 = camperSitePrice2;
    }

    public String getCamperSiteEmail() {
        return CamperSiteEmail;
    }

    public void setCamperSiteEmail(String camperSiteEmail) {
        CamperSiteEmail = camperSiteEmail;
    }

    public String getCamperSiteSub() {
        return CamperSiteSub;
    }

    public void setCamperSiteSub(String camperSiteSub) {
        CamperSiteSub = camperSiteSub;
    }

    public String getCamperSiteDescription() {
        return CamperSiteDescription;
    }

    public void setCamperSiteDescription(String camperSiteDescription) {
        CamperSiteDescription = camperSiteDescription;
    }
}
