package org.panda.business.admin.modules.facade.controller;

import io.swagger.annotations.Api;
import org.panda.business.admin.common.domain.ResultVO;
import org.panda.business.admin.modules.facade.model.Todo;
import org.panda.business.admin.modules.facade.model.param.SortParam;
import org.panda.business.admin.modules.facade.model.vo.TodoVO;
import org.panda.business.admin.modules.facade.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * 待办事项控制层
 *
 * @author fangen
 * @since JDK 11 2022/5/10
 */
@Api(tags = "待办事项")
@RestController
@RequestMapping("/facade/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping("/list/{userId}")
    public ResultVO list(@PathVariable BigInteger userId){
        TodoVO todoList = todoService.getTodoList(userId);
        return ResultVO.getSuccess(todoList);
    }

    @PostMapping("/sort")
    public ResultVO sort(@RequestBody SortParam sortParam){
        todoService.sortTodo(sortParam.getUserId(), sortParam.getWorkStatus(), sortParam.getList());
        return ResultVO.getSuccess();
    }

    @PostMapping("/add")
    public ResultVO add(@RequestBody Todo todo){
        todoService.addTodo(todo);
        return ResultVO.getSuccess();
    }

    @DeleteMapping ("/del")
    public ResultVO del(@RequestBody Todo todo){
        int result = todoService.delTodo(todo);
        return ResultVO.getSuccess(result);
    }

    @PutMapping ("/edit")
    public ResultVO edit(@RequestBody Todo todo){
        int result = todoService.editTodo(todo);
        return ResultVO.getSuccess(result);
    }
}
