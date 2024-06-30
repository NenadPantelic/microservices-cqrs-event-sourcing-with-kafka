package com.techbank.account.cmd.api.controller;

import com.techbank.account.cmd.api.command.RestoreReadDbCommand;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infra.CommandDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/restore-db")
public class RestoreReadDbController {

    private final CommandDispatcher commandDispatcher;

    public RestoreReadDbController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> restoreReadDb() {
        log.info("Received a request to restore read database.");
        try {
            commandDispatcher.send(new RestoreReadDbCommand());
            return new ResponseEntity<>(
                    new BaseResponse("Read database restore request completed successfully."),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            var safeErrorResponse = "Error while processing request to restore read database";
            log.error(safeErrorResponse, e);
            return new ResponseEntity<>(new BaseResponse(safeErrorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
