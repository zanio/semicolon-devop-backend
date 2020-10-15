package com.semicolondevop.suite.model.activity;

import org.apache.http.client.utils.DateUtils;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 12/10/2020 - 4:55 AM
 * @project com.semicolondevop.suite.model.activity in ds-suite
 */
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    @PrePersist
    public void setCreated() {
        this.created = new Date();
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastUpdated() {
        if (lastUpdated == null)
            return created;
        return lastUpdated;
    }

    @PreUpdate
    public void setLastUpdated() {
        this.lastUpdated = new Date();
    }

    public Date getTime() {
        if (this.lastUpdated != null)
            return this.lastUpdated;
        return this.created;
    }

//    public String getReadableDateTime(Date date) {
//        if (date == null) return "";
//        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
//        String dateBeforeString = "31 01 2020";
//        String dateAfterString = "02 02 2020";
//        Date dateBefore = myFormat.parse(dateBeforeString);myFormat
//        Date dateAfter = myFormat.parse(dateAfterString);
//        DateFormat dateFormat = DateUtils.formatDate(date);
//        return dateFormat.format(date);
//    }

    public String getReadableDayMonth(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("dd MMMM").format(date);
    }

    public String getReadableDateWithoutTime(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, dd yyyy");
        return sdf.format(date);
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", created=" + created +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}

