package com.samplesecurity.dto.Board;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

@Data
public class BoardPageDto {
    private static final int DEFAULT_MIN_SIZE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final int DEFAULT_MAX_SIZE = 50;

    private int page;
    private int size;
    private String boardType;

    public BoardPageDto() {
        this.page = 1;
        this.size = DEFAULT_SIZE;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page < 0 ? 1 : page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size < DEFAULT_MIN_SIZE || size > DEFAULT_MAX_SIZE ? DEFAULT_SIZE : size;
    }

    public Pageable makePageable() {
        return PageRequest.of(this.page - 1, this.size);
    }

    public String getListLink(){
        UriComponentsBuilder builder=UriComponentsBuilder.fromPath("")
                .queryParam("page",this.page)
                .queryParam("boardType",this.getBoardType());

        return builder.toUriString();
    }
}