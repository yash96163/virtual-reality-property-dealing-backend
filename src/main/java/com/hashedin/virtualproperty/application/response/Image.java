package com.hashedin.virtualproperty.application.response;

import lombok.Data;

@Data
public class Image {
    public String publicId;
    public String url;

    public Image(String publicId, String url) {
        this.publicId = publicId;
        this.url = url;
    }
}
