package com.neuedu.pojo;



import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
public class Cart implements Serializable {

    int id;
    int userid;
    int product_id;
    int quantity;
    int checked;
    String create_time;
    String update_time;

    public Cart() {
    }

    public Cart(Integer id, Integer userid, Integer product_id, Integer quantity, Integer checked, String create_time, String update_time) {
        this.id = id;
        this.userid = userid;
        this.product_id = product_id;
        this.quantity = quantity;
        this.checked = checked;
        this.create_time = create_time;
        this.update_time = update_time;
    }
}
