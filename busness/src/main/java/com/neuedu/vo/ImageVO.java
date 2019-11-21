package com.neuedu.vo;

import lombok.Data;

//lombok
@Data
public class ImageVO {


    private  String uri;
    private String url;


    public ImageVO() {

    }
    public ImageVO(String uri, String url) {
        this.uri = uri;
        this.url = url;
    }



}
