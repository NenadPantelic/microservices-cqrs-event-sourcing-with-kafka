package com.techbank.account.cmd.api;

import com.techbank.account.cmd.api.command.CloseAccountCommand;
import com.techbank.account.cmd.api.command.DepositFundsCommand;
import com.techbank.account.cmd.api.command.OpenAccountCommand;
import com.techbank.account.cmd.api.command.WithdrawFundsCommand;
import com.techbank.account.cmd.dto.OpenAccountResponse;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.exception.AggregateNotFoundException;
import com.techbank.cqrs.core.infra.CommandDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/bank-accounts")
public class BankAccountController {

    // will dispatch the command to the right command handler
    private final CommandDispatcher commandDispatcher;

    public BankAccountController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand command) {
        log.info("Received a request to open a bank account");
        var id = UUID.randomUUID().toString();
        command.setId(id);

        try {
            commandDispatcher.send(command);
            return new ResponseEntity<>(
                    new OpenAccountResponse("Bank account creation request completed successfully.", id),
                    HttpStatus.CREATED
            );
        } catch (IllegalStateException e) {
            log.warn("Client made a bad request: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {
            String errMessage = String.format("Error while processing request to open a new bank account for id %s", id);
            log.error(errMessage, e);
            return new ResponseEntity<>(
                    new BaseResponse(errMessage), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PutMapping("{id}/deposit")
    public ResponseEntity<BaseResponse> depositFunds(@PathVariable("id") String id,
                                                     @RequestBody DepositFundsCommand command) {
        log.info("Received a request to deposit funds to a bank account");
        try {
            command.setId(id);
            commandDispatcher.send(command);
            return new ResponseEntity<>(
                    new BaseResponse("Funds deposited successfully."),
                    HttpStatus.OK
            );
        } catch (IllegalStateException | AggregateNotFoundException e) {
            log.warn("Client made a bad request: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {
            String errMessage = String.format("Error while processing request to deposit funds to bank account with id %s", id);
            log.error(errMessage, e);
            return new ResponseEntity<>(
                    new BaseResponse(errMessage), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


    @PutMapping("{id}/withdraw")
    public ResponseEntity<BaseResponse> withdrawFunds(@PathVariable("id") String id,
                                                      @RequestBody WithdrawFundsCommand command) {
        log.info("Received a request to withdraw funds to a bank account");
        try {
            command.setId(id);
            commandDispatcher.send(command);
            return new ResponseEntity<>(
                    new BaseResponse("Funds withdrawn successfully."),
                    HttpStatus.OK
            );
        } catch (IllegalStateException | AggregateNotFoundException e) {
            log.warn("Client made a bad request: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {
            String errMessage = String.format("Error while processing request to withdraw funds to bank account with id %s", id);
            log.error(errMessage, e);
            return new ResponseEntity<>(
                    new BaseResponse(errMessage), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BaseResponse> closeAccount(@PathVariable("id") String id) {
        log.info("Received a request to close bank account");
        try {
            CloseAccountCommand command = new CloseAccountCommand(id);
            commandDispatcher.send(command);
            return new ResponseEntity<>(
                    new BaseResponse("Account closed successfully."),
                    HttpStatus.OK
            );
        } catch (IllegalStateException | AggregateNotFoundException e) {
            log.warn("Client made a bad request: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {
            String errMessage = String.format("Error while processing request to close the bank account with id %s", id);
            log.error(errMessage, e);
            return new ResponseEntity<>(
                    new BaseResponse(errMessage), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
