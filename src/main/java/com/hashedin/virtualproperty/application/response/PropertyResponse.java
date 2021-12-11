package com.hashedin.virtualproperty.application.response;

import lombok.Data;

import java.util.List;

@Data
public class PropertyResponse
{
    public int currentPage;
    public int totalPage;
    public long totalResults;
    List<PropertyShort> data;

    public PropertyResponse(int currentPage, int totalPage, long totalResults, List<PropertyShort> data) {
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.totalResults = totalResults;
        this.data = data;
    }
}
