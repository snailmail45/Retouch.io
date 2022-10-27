package com.evan.scanenhancer.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.evan.scanenhancer.Constants;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Entity(tableName = "recent_actions")
public class Result {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "status")
    @SerializedName("status")
    private String status;

    @ColumnInfo(name = "date_created")
    private long dateCreated;

    @ColumnInfo(name = "action_type")
    private String actionType;

    @Embedded
    private Data data;

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public String getFormattedDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN, Locale.US);
        return dateFormat.format(dateCreated);
    }


}
