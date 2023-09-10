package dbs.bankingsystem.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@Getter
@Setter
@NoArgsConstructor
public class Response {

    private String status;
    private String message;

    public String getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Response(final String status, final String message) {
        this.status = status;
        this.message = message;
    }


}
