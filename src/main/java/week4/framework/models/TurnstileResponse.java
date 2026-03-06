package week4.framework.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record TurnstileResponse(
        boolean success,
        @SerializedName("error-codes")
        List<String> errorCodes,
        String hostname
) {
}