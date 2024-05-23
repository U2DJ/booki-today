package moim_today.implement.todo;

import moim_today.dto.todo.TodoResponse;
import moim_today.global.annotation.Implement;
import moim_today.persistence.entity.todo.TodoJpaEntity;
import moim_today.persistence.repository.todo.TodoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Implement
public class TodoFinder {

    private final TodoRepository todoRepository;

    public TodoFinder(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional(readOnly = true)
    public List<TodoResponse> findAllByDateRange(final long memberId,
                                                 final long moimId,
                                                 final LocalDate startDate,
                                                 final LocalDate endDate) {
        return todoRepository.findAllByDateRange(memberId, moimId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public TodoJpaEntity getById(final long todoId){
        return todoRepository.getById(todoId);
    }
}
