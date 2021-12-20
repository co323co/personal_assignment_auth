
package com.assignment.dao;

import com.assignment.dto.admin.selectuser.SelectUserInput;
import com.assignment.dto.admin.selectuser.SelectUserOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<SelectUserOutput> findByDynamicQuery(SelectUserInput selectUserInput, Pageable pageable);
}
