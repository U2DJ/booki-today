package booki_today.dto.auth;

public record MemberLoginRequest(
        String email,
        String password
) {
}
