package org.zerock.guestboook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass //테이블로 생성되지 않는다. 이 클래스를 상속한 엔티티의 클래스로 데이터베이스 테이블이 생성된다.
@EntityListeners(value = {AuditingEntityListener.class}) //JPA 내부에서 객체가 생성, 변경되는 것을 감시한다.
@Getter
abstract class BaseEntity {

    @CreatedDate //등록일을 DB 테이블에 적용하도록 필드 생성 (엔티티 생성 시간 처리)
    @Column(name="regdate", updatable = false) //수정이 안되게 설정
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "moddate") //수정일은 변경되도록 설정
    private LocalDateTime modDate;
}
