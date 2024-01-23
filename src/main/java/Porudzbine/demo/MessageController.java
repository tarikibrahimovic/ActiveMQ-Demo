package Porudzbine.demo;

import Porudzbine.demo.TestModels.Reporting;
import Porudzbine.demo.TestModels.ReportingResponse;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class MessageController {
    private final MessageProducer messageProducer;

    private List<Reporting> reports;

    public MessageController(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
        reports = new ArrayList<>();
        for(int i = 0; i < 30; i++) {
            reports.add(createDummyReporting("John Doe " + i));
        }
    }

//    @PostMapping("/message")
//    public ResponseEntity<String> sendMessage(@RequestBody String message) {
//        Message message1 = new Message(message);
//        try {
//            messageProducer.sendMessage(message1);
//            return ResponseEntity.ok("Message sent");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Message not sent");
//        }
//    }


    @GetMapping("/reporting/reports/active")
    public ResponseEntity<ReportingResponse> getReports(@RequestParam(required = false) String filter,
                                                        @RequestParam(required = false) String type,
                                                        @RequestParam(required = false) String sort,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Sort.Direction direction = Sort.Direction.DESC;
        if ("asc".equalsIgnoreCase(sort)) {
            direction = Sort.Direction.ASC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "customerFullName"));
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Reporting> list;

        if (reports.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, reports.size());
            list = reports.subList(startItem, toIndex);
        }

        Page<Reporting> reportPage
                = new PageImpl<Reporting>(list, PageRequest.of(currentPage, pageSize), reports.size());

        if (pageable.getSort().isSorted()) {
            list = list.stream().sorted((r1, r2) -> {
                if (pageable.getSort().getOrderFor("customerFullName").isAscending()) {
                    return r1.getOrderName().compareTo(r2.getOrderName());
                } else {
                    return r2.getOrderName().compareTo(r1.getOrderName());
                }
            }).collect(Collectors.toList());
        }

        ReportingResponse reportingResponse = ReportingResponse.builder()
                .numberOfPages((long) Math.ceil((double) reports.size()/pageSize))
                .reportingList(list)
                .build();

        return new ResponseEntity<>(reportingResponse, HttpStatus.OK);
    }

    private Reporting createDummyReporting(String s) {
        Reporting reporting = new Reporting();
        reporting.setOrderName(UUID.randomUUID().toString());
        reporting.setOrderNumber(UUID.randomUUID().toString());
        reporting.setCustomerNumber(UUID.randomUUID().toString());
        reporting.setCustomerFullName(s);
        reporting.setInitialDate(OffsetDateTime.now());
        reporting.setStartDate(OffsetDateTime.now());
        reporting.setExpectedDate(OffsetDateTime.now().plusDays(10));
        reporting.setStatus("Processing");
        return reporting;
    }

}
