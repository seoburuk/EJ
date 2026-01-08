package com.example.Globalin.dao;

import com.example.Globalin.model.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardDao {
    List<Board> findAllBoards();
    Board findById(Long id);
}
