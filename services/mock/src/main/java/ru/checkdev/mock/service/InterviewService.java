package ru.checkdev.mock.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.repository.InterviewRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class InterviewService {

    private final InterviewRepository interviewRepository;

    private static final Logger LOG = LoggerFactory.getLogger(InterviewService.class.getName());

    public Optional<Interview> save(Interview interview) {
        Optional<Interview> rsl = Optional.empty();
        interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
        try {
            rsl = Optional.of(interviewRepository.save(interview));
        } catch (DataIntegrityViolationException e) {
            LOG.error("Error!", e);
        }
        return rsl;
    }

    public List<Interview> findAll() {
        return interviewRepository.findAll().stream()
                .peek(interview -> {
                    if (interview.getTopicId() == null) {
                        interview.setTopicId(1);
                    }
                }).collect(Collectors.toList());
    }

    public Page<Interview> findPaging(int page, int size) {
        return interviewRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate")));
    }

    public Optional<Interview> findById(Integer id) {
        return interviewRepository.findById(id);
    }

    public List<Interview> findByMode(int mode) {
        return interviewRepository.findByMode(mode).stream()
                .peek(interview -> {
                    if (interview.getTopicId() == null) {
                        interview.setTopicId(1);
                    }
                })
                .collect(Collectors.toList());
    }

    public Page<Interview> findByTopicId(int topicId, int page, int size) {
        return interviewRepository.findByTopicId(topicId, PageRequest.of(page, size));
    }

    public Page<Interview> findByTopicsIds(List<Integer> topicsIds, int page, int size) {
        return interviewRepository.findByTopicIdIn(topicsIds, PageRequest.of(page, size));
    }

    public boolean update(Interview interview) {
        interviewRepository.save(interview);
        return true;
    }

    public boolean delete(Interview interview) {
        if (findById(interview.getId()).isPresent()) {
            interviewRepository.delete(interview);
            return true;
        }
        return false;
    }

    /**
     * Метод обновляет статус собеседования.
     *
     * @param id     ID Interview.
     * @param status Status
     * @return boolean true / false
     */
    public boolean updateStatus(int id, int status) {
        try {
            interviewRepository.updateStatus(id, status);
            return true;
        } catch (Exception e) {
            log.error("Update status error {}", e.getMessage());
            return false;
        }
    }
}
