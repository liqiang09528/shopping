package com.neuedu.pojo;

import com.neuedu.vo.OrderVO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class PageModel {

    private List<OrderVO> list;
}
