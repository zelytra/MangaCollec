package fr.zelytra.mangacollec.api;

public class RestGuestResponse {
    private boolean success;
    private String guest_session_id;
    private String expires_at;

    public boolean isSuccess() {
        return success;
    }

    public String getGuest_session_id() {
        return guest_session_id;
    }

    public String getExpires_at() {
        return expires_at;
    }
}
