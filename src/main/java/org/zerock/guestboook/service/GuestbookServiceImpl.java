package org.zerock.guestboook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestboook.dto.GuestbookDTO;
import org.zerock.guestboook.dto.PageRequestDTO;
import org.zerock.guestboook.dto.PageResultDTO;
import org.zerock.guestboook.entity.Guestbook;
import org.zerock.guestboook.entity.QGuestbook;
import org.zerock.guestboook.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor //의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository repository; //의존성에 대한 설정 추가. 반드시 final로 설정

    @Override
    public Long register(GuestbookDTO dto) {

        log.info("DTO--------------------");
        log.info(dto);

        Guestbook entity = dtoToEntity(dto);

        log.info(entity);

        repository.save(entity); //저장

        return entity.getGno(); //저장 후 gno값을 반환
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO); //검색 조건 처리

        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable); //Querydsl 사용

        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public GuestbookDTO read(Long gno) {

        Optional<Guestbook> result = repository.findById(gno);

        return result.isPresent()? entityToDto(result.get()): null;
    }

    @Override
    public void remove(Long gno) {

        repository.deleteById(gno);

    }

    @Override
    public void modify(GuestbookDTO dto) {

        //업데이트 하는 항목은 '제목', '내용'

        Optional<Guestbook> result = repository.findById(dto.getGno());

        if(result.isPresent()){

            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity);
        }
    }

    private BooleanBuilder getSearch(PageRequestDTO requestDTO){ //Querydsl 처리

        String type = requestDTO.getType();
        //타입 객체를 dto와 연결
        String keyword = requestDTO.getKeyword();
        //키워드 객체를 dto와 연결

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        //동적 쿼리를 생성하는 객체 생성. where 조건문을 생성하기 위함

        QGuestbook qGuestbook = QGuestbook.guestbook;
        //Querydsl이 자동으로 만든 객체 연결

        BooleanExpression expression = qGuestbook.gno.gt(0L);
        //gno > 0 조건만 생성

        booleanBuilder.and(expression);
        //and 조건으로 연결

        if(type == null || type.trim().length() == 0){ //검색 조건이 없는 경우 (공백을 제외한 글자수가 0)
            return booleanBuilder;
        }

        //검색 조건을 작성하기
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        //where문을 생성하기 위한 conditionBuilder 변수 선언

        if(type.contains("t")){
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        } //title인 경우 "t"를 사용
        if(type.contains("c")){
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        } //content 경우 "c"를 사용
        if(type.contains("w")){
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        } //writer 경우 "w"를 사용
        //or 조건

        //모든 조건 통합
        booleanBuilder.and(conditionBuilder);
        //and 조건으로 conditionBuilder를 사용한다.

        return booleanBuilder;
    }
}
