package com.poshtarenko.codeforge.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HackerEarthQueueingResponse(
        Result result,
        @JsonProperty("he_id") String heId,
        @JsonProperty("status_update_url") String statusUpdateUrl,
        @JsonProperty("request_status") RequestStatus requestStatus) {

    public record Result(
            @JsonProperty("run_status") RunStatus runStatus,
            @JsonProperty("compile_status") String compileStatus) {
        public record RunStatus(String status) {
        }
    }

    public record RequestStatus(
            String code,
            String message) {
    }

}