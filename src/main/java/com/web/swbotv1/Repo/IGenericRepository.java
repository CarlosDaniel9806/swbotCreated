package com.web.swbotv1.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IGenericRepository <T,ID> extends JpaRepository <T,ID>{ /* hereda todos los metodos de JpaRepository como save findById FindAll DeleteById etc   */
    
}
