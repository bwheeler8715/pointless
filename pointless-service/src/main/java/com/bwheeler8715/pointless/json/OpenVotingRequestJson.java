package com.bwheeler8715.pointless.json;

public class OpenVotingRequestJson {
    private String roomId;
    private String currentTickerNumber;
    private String currentTicketSummary;
    private String organizerCode;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getCurrentTickerNumber() {
        return currentTickerNumber;
    }

    public void setCurrentTickerNumber(String currentTickerNumber) {
        this.currentTickerNumber = currentTickerNumber;
    }

    public String getCurrentTicketSummary() {
        return currentTicketSummary;
    }

    public void setCurrentTicketSummary(String currentTicketSummary) {
        this.currentTicketSummary = currentTicketSummary;
    }

    public String getOrganizerCode() {
        return organizerCode;
    }

    public void setOrganizerCode(String organizerCode) {
        this.organizerCode = organizerCode;
    }
}
