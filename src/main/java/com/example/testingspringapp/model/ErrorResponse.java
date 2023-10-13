package com.example.testingspringapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
        private String message;
        private HttpStatusCode httpStatus;
        private Integer errorCode;
        private Object data;

        public ErrorResponse(String message, HttpStatusCode httpStatus) {
            this.message = message;
            this.httpStatus = httpStatus;
        }

        public ErrorResponse(String message, HttpStatusCode httpStatus, Integer errorCode) {
            this.message = message;
            this.httpStatus = httpStatus;
            this.errorCode = errorCode;
        }

}
