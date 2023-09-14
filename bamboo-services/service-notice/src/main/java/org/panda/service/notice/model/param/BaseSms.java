package org.panda.service.notice.model.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BaseSms {
    /**
     * 手机号清单
     */
    @NotNull
    private List<String> mobilePhones;
}
