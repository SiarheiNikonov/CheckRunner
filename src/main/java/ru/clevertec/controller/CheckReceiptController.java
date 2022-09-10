package ru.clevertec.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.service.checkreceipt.CheckReceiptService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/receipt")
@RequiredArgsConstructor
public class CheckReceiptController {

    private final CheckReceiptService service;

    @GetMapping
    public void getCheckReceipt(@RequestParam Map<String, String> params, HttpServletResponse resp) throws IOException {
        StringBuilder query = new StringBuilder();
            params.forEach((key, value) -> query.append(key + "=" + value + "&"));
            service.handleRequest(
                    query.deleteCharAt(query.length()-1).toString(),
                    resp.getOutputStream()
            );
    }
}
