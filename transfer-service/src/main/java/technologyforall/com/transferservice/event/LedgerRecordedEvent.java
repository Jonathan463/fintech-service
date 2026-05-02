package technologyforall.com.transferservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LedgerRecordedEvent {
    private Long transferId;
    private String referenceNumber;
    private Long businessId;
    private int entriesCreated;
    private LocalDateTime recordedAt;
}
