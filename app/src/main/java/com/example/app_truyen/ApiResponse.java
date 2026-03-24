package com.example.app_truyen;

import java.util.List;

public class ApiResponse<T> {
    public boolean success;
    public String message;
    public T data;
    public int code;
}

class SearchResponse {
    public List<StoryResponse> content;
    public int totalElements;
    public int totalPages;
    public int currentPage;

    public SearchResponse() {
    }
}
