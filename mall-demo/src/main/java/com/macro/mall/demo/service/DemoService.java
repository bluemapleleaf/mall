package com.macro.mall.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.demo.dto.PmsBrandDto;
import com.macro.mall.pms.model.PmsBrand;

import java.util.List;

/**
 * DemoService接口
 */
public interface DemoService {
    List<PmsBrand> listAllBrand();

    boolean createBrand(PmsBrandDto pmsBrandDto);

    boolean updateBrand(Long id, PmsBrandDto pmsBrandDto);

    boolean deleteBrand(Long id);

    Page<PmsBrand> listBrand(int pageNum, int pageSize);

    PmsBrand getBrand(Long id);
}
