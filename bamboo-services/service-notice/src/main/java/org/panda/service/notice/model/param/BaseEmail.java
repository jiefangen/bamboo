package org.panda.service.notice.model.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BaseEmail {
    /**
     * 收件人地址
     */
    @NotNull
    private List<String> addressees;
}
