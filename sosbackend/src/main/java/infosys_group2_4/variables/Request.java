package infosys_group2_4.variables;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by Oon Tong on 11/19/2017.
 */
public class Request {
    int rqId;
    int requesterId;
    String priority;
    String title;
    String description;
    String location;
    Timestamp bestBy;
    String bestByString;

    public String getBestByString() {
        return bestByString;
    }

    public Request setBestByString(String bestByString) {
        this.bestByString = bestByString;
        return this;
    }

    int acceptId;
    int isDeleted;

    public int getRqId() {
        return rqId;
    }

    public Request setRqId(int rqId) {
        this.rqId = rqId;
        return this;
    }

    public int getRequesterId() {
        return requesterId;
    }

    public Request setRequesterId(int requesterId) {
        this.requesterId = requesterId;
        return this;
    }

    public String getPriority() {
        return priority;
    }

    public Request setPriority(String priority) {
        this.priority = priority;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Request setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Request setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Request setLocation(String location) {
        this.location = location;
        return this;
    }

    public Timestamp getBestBy() {
        return bestBy;
    }

    public Request setBestBy(Timestamp bestBy) {
        this.bestBy = bestBy;
        return this;
    }

    public int getAcceptId() {
        return acceptId;
    }

    public Request setAcceptId(int acceptId) {
        this.acceptId = acceptId;
        return this;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public Request setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }
}
