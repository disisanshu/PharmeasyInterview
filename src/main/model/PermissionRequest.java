package model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class PermissionRequest {

    private Integer permissionRequestId;
    private Integer medicalReportId;
    private PermissionStatus permissionStatus;
    private Integer reportOwnerId;
    private Integer requesterId;
}
