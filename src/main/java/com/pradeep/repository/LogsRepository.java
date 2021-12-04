package com.pradeep.repository;

import com.pradeep.domain.Logs;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface LogsRepository extends CrudRepository<Logs, String>  {

    void deleteByUserId(String userId);

    List<Logs> findByUserId(String userId);

}
