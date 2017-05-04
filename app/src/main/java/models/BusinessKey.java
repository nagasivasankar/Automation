package models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 11/29/2016.
 */
public class BusinessKey implements Serializable{

    String externalId;
    String enterpriseName;
    String enterpriseId;
    String name;

    public ArrayList<String>  getPhotoUrlsList() {
        return photoUrlsList;
    }

    public void setPhotoUrls(ArrayList<String> photoUrlsList) {
        this.photoUrlsList = photoUrlsList;
    }

    ArrayList<String> photoUrlsList;
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getGooglePlacesId() {
        return googlePlacesId;
    }

    public void setGooglePlacesId(String googlePlacesId) {
        this.googlePlacesId = googlePlacesId;
    }

    String address;
    String businessId;
    String googlePlacesId;
}
