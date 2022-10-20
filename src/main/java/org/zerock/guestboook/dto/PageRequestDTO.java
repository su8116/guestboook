package org.zerock.guestboook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO { //페이지 요청 처리 DTO
    private int page;
    private int size;

    private String type;
    private String keyword; // 검색 기능을 위해 type, keyword 추가

    public PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    } //파라미터 수집용 코드

    public Pageable getPageable(Sort sort){

        return PageRequest.of(page -1, size, sort);
    }//위의 파라미터를 이용하여 페이징에 숫자를 결정하고 sort는 전발받아 처리
}
