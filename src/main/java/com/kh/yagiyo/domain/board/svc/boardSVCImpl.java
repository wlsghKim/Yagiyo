package com.kh.yagiyo.domain.board.svc;

import com.kh.yagiyo.domain.board.dao.BoardFileDAO;
import com.kh.yagiyo.domain.board.dao.boardDAO;
import com.kh.yagiyo.domain.entity.Board;
import com.kh.yagiyo.domain.entity.BoardFileEntity;
import com.kh.yagiyo.web.form.board.BoardForm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DTO -> Entity (Entity Class)
// Entity -> DTO (DTO Class)

@Service
@RequiredArgsConstructor
public class boardSVCImpl implements boardSVC{
    private final boardDAO boardDAO;
    private final BoardFileDAO boardFileDAO;

    @Override
    public void save(BoardForm boardForm) throws IOException {
        // 파일 첨부 여부에 따라 로직 분리
        if (boardForm.getBoardFile().isEmpty()) {
            // 첨부 파일 없음.
            Board board = Board.toSaveEntity(boardForm);
            boardDAO.save(board);
        } else {
            // 첨부 파일 있음
            Board boardEntity = Board.toSaveFileEntity(boardForm);
            Long savedId = boardDAO.save(boardEntity).getID();
            Board board = boardDAO.findById(savedId).get();

            for (MultipartFile boardFile : boardForm.getBoardFile()) {
                if (boardFile.isEmpty()) {
                    continue; // 파일이 없는 경우 건너뛰기
                }

                String originalFilename = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
                String savePath = "C:/springboot_img/" + storedFileName;

                // 원본 이미지를 저장
                File targetFile = new File(savePath);

                // 원본 이미지를 읽어와서 크기 조정 후 저장
                String thumbnailPath = "C:/springboot_img/" + storedFileName;
                Thumbnails.of(boardFile.getInputStream())
                        .size(200, 200) // 원하는 크기로 조정
                        .toFile(targetFile);

                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
                boardFileDAO.save(boardFileEntity);
            }
        }
    }


    @Override
    @Transactional
    public List<BoardForm> findAll() {
        List<Board> boardEntityList = boardDAO.findAll();
        List<BoardForm> boardFormList = new ArrayList<>();
        for (Board board : boardEntityList){
            boardFormList.add(BoardForm.toBoardForm(board));
        }
        return boardFormList;
    }

    @Override
    @Transactional
    public void updateHits(Long id) {
        boardDAO.updateHits(id);
    }

    @Override
    @Transactional
    public BoardForm findById(Long id) {
        Optional<Board> optionalBoardEntity = boardDAO.findById(id);
        if(optionalBoardEntity.isPresent()){
            Board board = optionalBoardEntity.get();
            BoardForm boardForm = BoardForm.toBoardForm(board);
            return boardForm;
        }else {
            return null;
        }
    }

    @Override
    public BoardForm update(BoardForm boardForm) {
        Board board = Board.toUpdateEntity(boardForm);
        boardDAO.save(board);
        return findById(boardForm.getId());
    }

    @Override
    public void delete(Long id) {
        boardDAO.deleteById(id);
    }

    @Override
    public Page<BoardForm> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10; // 한 페이지에 보여줄 글 갯수
        //한페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작
        Page<Board> board =
            boardDAO.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC,"ID")));

        System.out.println("board.getContent() = " + board.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("board.getTotalElements() = " + board.getTotalElements()); // 전체 글갯수
        System.out.println("board.getNumber() = " + board.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("board.getTotalPages() = " + board.getTotalPages()); // 전체 페이지 갯수
        System.out.println("board.getSize() = " + board.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("board.hasPrevious() = " + board.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("board.isFirst() = " + board.isFirst()); // 첫 페이지 여부
        System.out.println("board.isLast() = " + board.isLast()); // 마지막 페이지 여부

        //목록: id,writer,title,hits,createdTime
        Page<BoardForm> boardForms = board.map(boards -> new BoardForm(boards.getID(), boards.getBOARD_WRITER(), boards.getBOARD_TITLE(), boards.getBOARD_CONTENTS(), boards.getBOARD_HITS(),boards.getCREATE_TIME()));
        return boardForms;
    }
}
