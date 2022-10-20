package org.zerock.guestboook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestboook.entity.Guestbook;
import org.zerock.guestboook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies(){ //더미 데이터 생성
        IntStream.rangeClosed(1, 300).forEach(i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("Title....." + i)
                    .content("Content....." + i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test
    public void updateTest(){ //수정 시간 테스트
        Optional<Guestbook> result = guestbookRepository.findById(300L);
        //존재하는 번호로 테스트

        if(result.isPresent()){
            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title.....");
            guestbook.changeContent("Changed Content.....");

            guestbookRepository.save(guestbook);
        }
    }

    @Test
    public void testQuery1(){ //단일 항목 검색 테스트
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        //페이지 처리를 위한 코드 10개씩 내림차순 정렬

        QGuestbook qGuestbook = QGuestbook.guestbook; //1
        //Q도메인을 활용한 클래스를 얻어온다. (title, content와 같은 필드를 변수로 사용)

        String keyword = "1";
        //r검색 조건을 1값으로 적용

        BooleanBuilder builder = new BooleanBuilder(); //2
        //where에 들어가는 조건을 넣어주는 컨테이너

        BooleanExpression expression = qGuestbook.title.contains(keyword); //3
        //원하는 조건을 필드값과 결합하여 생성

        builder.and(expression); //4
        //조건을 결합하는 코드 and, or 조건을 이용할 수 있다.

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable); //5
        //조건과 페이징 처리를 result에 담는다.

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    @Test
    public void testQuery2(){ //다중 항목 검색 테스트
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        BooleanExpression exAll = exTitle.or(exContent); //1----------
        //(exTitle or exContent)

        builder.and(exAll); //2----------

        builder.and(qGuestbook.gno.gt(0L)); //3---------- 
        //or 조건 뒤에 and로 조건을 추가

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }
}
