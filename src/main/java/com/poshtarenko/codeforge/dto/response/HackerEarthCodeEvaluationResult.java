package com.poshtarenko.codeforge.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HackerEarthCodeEvaluationResult
        (@JsonProperty("he_id") String heId,
         @JsonProperty("request_status") RequestStatus requestStatus,
         @JsonProperty("status_update_url") String statusUpdateUrl,
         Result result) {

    public record RequestStatus
            (String code,
             String message) {
    }

    public record Result
            (@JsonProperty("compile_status") String compileStatus,
             @JsonProperty("run_status") RunStatus runStatus) {

        public record RunStatus
                (String output,
                 String status,
                 @JsonProperty("status_detail") String statusDetail,
                 @JsonProperty("time_used") Double timeUsed,
                 @JsonProperty("memory_used") Double memoryUsed) {
        }
    }

}