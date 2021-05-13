package cg.wbd.grandemonstration.service.impl;

import cg.wbd.grandemonstration.model.Province;
import cg.wbd.grandemonstration.repository.ProvinceRepository;
import cg.wbd.grandemonstration.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ProvinceServiceImplWithSpringData extends ProvinceService {
    @Autowired
    private ProvinceRepository repository;

    @Override
    protected CrudRepository<Province, Long> repository() {
        return repository;
    }
}
