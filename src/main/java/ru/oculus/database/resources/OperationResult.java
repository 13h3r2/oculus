package ru.oculus.database.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OperationResult {
    private boolean success;

    private String message;

    public static final OperationResult OK = new OperationResult(true, null);

    public OperationResult() {
    }

    private OperationResult(boolean success, String message) {
        super();
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
