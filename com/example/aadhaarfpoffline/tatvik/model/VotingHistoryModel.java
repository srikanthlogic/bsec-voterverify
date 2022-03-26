package com.example.aadhaarfpoffline.tatvik.model;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes2.dex */
public class VotingHistoryModel {
    @SerializedName("MATCHED_ID_DOCUMENT_IMAGE")
    private String MATCHED_ID_DOCUMENT_IMAGE = "";
    @SerializedName("MATCHED_USER_ID")
    private String MATCHED_USER_ID = "";
    @SerializedName("SlNoInWard")
    private String SlNoInWard;
    @SerializedName("GENDER")
    private String gender;
    @SerializedName("SYNCED")
    private int synced;
    @SerializedName("VOTED")
    private String voted;
    @SerializedName("VOTING_DATE")
    private String votingDate;

    public void setSynced(int synced) {
        this.synced = synced;
    }

    public void setSlNoInWard(String slNoInWard) {
        this.SlNoInWard = slNoInWard;
    }

    public void setVotingDate(String votingDate) {
        this.votingDate = votingDate;
    }

    public void setVoted(String voted) {
        this.voted = voted;
    }

    public String getMATCHED_ID_DOCUMENT_IMAGE() {
        return this.MATCHED_ID_DOCUMENT_IMAGE;
    }

    public String getMATCHED_USER_ID() {
        return this.MATCHED_USER_ID;
    }

    public String getSlNoInWard() {
        return this.SlNoInWard;
    }

    public String getVoted() {
        return this.voted;
    }

    public String getVotingDate() {
        return this.votingDate;
    }

    public int getSynced() {
        return this.synced;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMATCHED_ID_DOCUMENT_IMAGE(String image) {
        this.MATCHED_ID_DOCUMENT_IMAGE = image;
    }

    public void setMATCHED_USER_ID(String matched_user_id) {
        this.MATCHED_USER_ID = matched_user_id;
    }
}
