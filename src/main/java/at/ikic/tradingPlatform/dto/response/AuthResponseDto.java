package at.ikic.tradingPlatform.dto.response;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String jwt;
    private Boolean status;
    private String message;
    private Boolean isTwoFactorAuthEnabled;
    private String session;
}
