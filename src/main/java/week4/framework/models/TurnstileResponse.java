package week4.framework.models;

import java.util.List;

public class TurnstileResponse {
    private boolean success;
    private List<String> errorCodes;
    private String hostname;

    public boolean isSuccess() { return success; }
}