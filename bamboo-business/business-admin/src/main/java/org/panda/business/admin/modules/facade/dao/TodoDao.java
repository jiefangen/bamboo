package org.panda.business.admin.modules.facade.dao;

import org.apache.ibatis.annotations.Param;
import org.panda.business.admin.modules.facade.model.Todo;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface TodoDao {

    List<Todo> findTodo(@Param("userId") BigInteger userId, @Param("workStatus")  Integer workStatus);

    int sortTodo(@Param("userId") BigInteger userId, @Param("todo") Todo todo);

    void insertTodo(@Param("todo") Todo todo);

    int deleteTodo(@Param("todo") Todo todo);

    int updateTodo(@Param("todo") Todo todo);

    int deleteTodoByTime(@Param("intervalDay") int intervalDay);
}
