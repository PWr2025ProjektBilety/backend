package com.example.backend.ticketInspection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InspectTicketResponseDTO {

    public static InspectTicketResponseDTO valid() {
        return new InspectTicketResponseDTO("valid", "");
    }

    public static InspectTicketResponseDTO invalid(String reason) {
        return new InspectTicketResponseDTO("invalid", reason);
    }

    private String status;
    private String reason;
}
