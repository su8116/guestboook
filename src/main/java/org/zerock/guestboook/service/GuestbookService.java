package org.zerock.guestboook.service;

import org.zerock.guestboook.dto.GuestbookDTO;
import org.zerock.guestboook.dto.PageRequestDTO;
import org.zerock.guestboook.dto.PageResultDTO;
import org.zerock.guestboook.entity.Guestbook;

public interface GuestbookService {
    Long register(GuestbookDTO dto);

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    default Guestbook dtoToEntity(GuestbookDTO dto){
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity; //getter를 builder로 받아서 entity에 넣음
    }

    default GuestbookDTO entityToDto(Guestbook entity){
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto; //getter를 builder로 받아서 dto에 넣음
    }

    GuestbookDTO read(Long gno);

    void remove(Long gno);
    void modify(GuestbookDTO dto);
}
