package com.macro.mall.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.macro.mall.demo.dto.PmsBrandDto;
import com.macro.mall.demo.service.DemoService;
import com.macro.mall.pms.model.PmsBrand;
import com.macro.mall.pms.service.PmsBrandRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DemoService实现类
 */
@Service
public class DemoServiceImpl implements DemoService {
//    @Autowired
    private PmsBrandRepository brandMapper;

    @Override
    public List<PmsBrand> listAllBrand() {
        return brandMapper.list();
    }

    @Override
    public boolean createBrand(PmsBrandDto pmsBrandDto) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandDto,pmsBrand);
        return brandMapper.save(pmsBrand);
    }

    @Override
    public boolean updateBrand(Long id, PmsBrandDto pmsBrandDto) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandDto,pmsBrand);
        pmsBrand.setId(id);
        return brandMapper.updateById(pmsBrand);
    }

    @Override
    public boolean deleteBrand(Long id) {
        return brandMapper.removeById(id);
    }

    @Override
    public Page<PmsBrand> listBrand(int pageNum, int pageSize) {
        Page<PmsBrand> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsBrand> lambda = new LambdaQueryWrapper<>();
        lambda.ge(PmsBrand::getId, 0);
        return brandMapper.page(page, lambda);
    }

    @Override
    public PmsBrand getBrand(Long id) {
        return brandMapper.getById(id);
    }
}
