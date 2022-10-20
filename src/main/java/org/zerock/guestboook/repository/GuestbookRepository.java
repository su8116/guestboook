package org.zerock.guestboook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.zerock.guestboook.entity.Guestbook;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long>, QuerydslPredicateExecutor<Guestbook> {
} //JPA 저장소로 사용하기 위해서 인터페이스에 상속하는 것 만으로도 가능하다.
//Guestbook 클래스에 Long 타입으로 구성한다.