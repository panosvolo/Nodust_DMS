package com.mts.mea.nodust_app.Evalution;

/**
 * Created by Mahmoud on 1/22/2018.
 */

public class KPI {
    private String KPI_ID;
    private String KPI_name;
    private String ID;
    private String KPI_VALUE;
    private String Comment;
    private String KPI_Type;

    public String getKPI_Type() {
        return KPI_Type;
    }

    public void setKPI_Type(String KPI_Type) {
        this.KPI_Type = KPI_Type;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public void setKPI_ID(String KPI_ID) {
        this.KPI_ID = KPI_ID;
    }

    public void setKPI_name(String KPI_name) {
        this.KPI_name = KPI_name;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setKPI_VALUE(String KPI_VALUE) {
        this.KPI_VALUE = KPI_VALUE;
    }

    public String getKPI_ID() {
        return KPI_ID;
    }

    public String getKPI_name() {
        return KPI_name;
    }

    public String getID() {
        return ID;
    }

    public String getKPI_VALUE() {
        return KPI_VALUE;
    }
}
