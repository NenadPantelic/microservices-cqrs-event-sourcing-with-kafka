package com.techbank.account.cmd.dto;

import com.techbank.account.common.dto.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenAccountResponse extends BaseResponse {

    private String id;

    public OpenAccountResponse(String id, String message) {
        super(message);
        this.id = id;
    }
}
