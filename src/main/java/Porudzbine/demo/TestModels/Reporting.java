package Porudzbine.demo.TestModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Setter
@Getter
public class Reporting implements Serializable {
    private String orderName;
    private String orderNumber;
    private String customerNumber;
    private String customerFullName;
    private OffsetDateTime initialDate;
    private OffsetDateTime startDate;
    private OffsetDateTime expectedDate;
    private String status;

}