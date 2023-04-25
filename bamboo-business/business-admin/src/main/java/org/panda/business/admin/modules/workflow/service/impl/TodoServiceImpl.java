package org.panda.business.admin.modules.workflow.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.panda.business.admin.common.constant.enumeration.TodoType;
import org.panda.business.admin.modules.workflow.dao.TodoDao;
import org.panda.business.admin.modules.workflow.model.WorkTodo;
import org.panda.business.admin.modules.workflow.model.vo.TodoVO;
import org.panda.business.admin.modules.workflow.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    private TodoDao todoDao;

    @Override
    public TodoVO getTodoList(BigInteger userId) {
        TodoVO todoList = new TodoVO();
        todoList.setTodoList(todoDao.findTodo(userId, TodoType.TODO.getValue()));
        todoList.setWorkingList(todoDao.findTodo(userId, TodoType.WORKING.getValue()));
        todoList.setDoneList(todoDao.findTodo(userId, TodoType.DONE.getValue()));
        todoList.setDiscardList(todoDao.findTodo(userId, TodoType.DISCARD.getValue()));
        return todoList;
    }

    @Override
    public void sortTodo(BigInteger userId, Integer workStatus, List<WorkTodo> list) {
        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                WorkTodo oldTodo = list.get(i);
                WorkTodo newTodo = new WorkTodo();
                newTodo.setId(oldTodo.getId());
                newTodo.setWorkStatus(workStatus);
                newTodo.setSort(i);
                todoDao.sortTodo(userId, newTodo);
            }
        }
    }

    @Override
    public void addTodo(WorkTodo todo) {
        todo.setCreateTime(todo.getUpdateTime());
        todoDao.insertTodo(todo);
    }

    @Override
    public int delTodo(WorkTodo todo) {
        todo.setWorkStatus(TodoType.DISCARD.getValue());
        return todoDao.deleteTodo(todo);
    }

    @Override
    public int editTodo(WorkTodo todo) {
        return todoDao.updateTodo(todo);
    }
}
