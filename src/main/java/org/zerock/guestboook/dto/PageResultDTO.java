package org.zerock.guestboook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> { //페이지 결과 처리 DTO

    private List<DTO> dtoList; //DTO 리스트

    private int totalPage; //총 페이지 번호

    private int page; //현재 페이지 번호

    private int size; //목록 사이즈

    private int start, end; //시작 페이지 번호, 끝 페이지 번호

    private boolean prev, next; //이전, 다음 유무

    private List<Integer> pageList; //페이지 번호 목록

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){
        //Page<EN> result : 람다식으로 결과를 처리
        //Function<EN, DTO> fn : 로직을 전달하는 목적 처리 EN 엔티티, DTO 객체

        dtoList = result.stream().map(fn).collect(Collectors.toList());
        //result.stream().map(fn) 엔티티
        //collect(Collectors.toList()) 컬렉팅 리스트 처리

        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){

        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        //
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;

        prev = start > 1;

        end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
