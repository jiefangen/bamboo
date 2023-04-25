package org.panda.business.admin.modules.workflow.dao;

import org.apache.ibatis.annotations.Param;
import org.panda.business.admin.modules.workflow.model.WorkTodo;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface TodoDao {

    List<WorkTodo> findTodo(@Param("userId") BigInteger userId, @Param("workStatus")  Integer workStatus);

    int sortTodo(@Param("userId") BigInteger userId, @Param("todo") WorkTodo todo);

    void insertTodo(@Param("todo") WorkTodo todo);

    int deleteTodo(@Param("todo") WorkTodo todo);

    int updateTodo(@Param("todo") WorkTodo todo);

    int deleteTodoByTime(@Param("intervalDay") int intervalDay);
}
