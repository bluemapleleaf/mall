package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.domain.PmsBrandParam;
import com.macro.mall.pms.model.PmsBrand;
import com.macro.mall.pms.model.PmsProduct;
import com.macro.mall.pms.service.impl.PmsBrandRepositoryImpl;
import com.macro.mall.service.PmsBrandService;
import com.macro.mall.service.PmsProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 商品品牌Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class PmsBrandServiceImpl extends PmsBrandRepositoryImpl implements PmsBrandService {
    @Autowired
    private PmsProductService productService;

    @Override
    public List<PmsBrand> listAllBrand() {
        return list();
    }

    @Override
    public boolean createBrand(PmsBrandParam pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        //如果创建时首字母为空，取名称的第一个为首字母
        if (StringUtils.isEmpty(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        return save(pmsBrand);
    }

    @Override
    public boolean updateBrand(Long id, PmsBrandParam pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        pmsBrand.setId(id);
        //如果创建时首字母为空，取名称的第一个为首字母
        if (StringUtils.isEmpty(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        //更新品牌时要更新商品中的品牌名称
        LambdaUpdateWrapper<PmsProduct> lambda = new LambdaUpdateWrapper<>();
        lambda.eq(PmsProduct::getBrandId, id);
        lambda.set(PmsProduct::getName, pmsBrand.getName());
        productService.update(lambda);
        return updateById(pmsBrand);
    }

    @Override
    public boolean deleteBrand(Long id) {
        return removeById(id);
    }

    @Override
    public boolean deleteBrand(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public Page<PmsBrand> listBrand(String keyword, int pageNum, int pageSize) {
        Page<PmsBrand> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsBrand> lambda = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(keyword)){
            lambda.like(PmsBrand::getName, keyword);
        }
        return page(page, lambda);
    }

    @Override
    public PmsBrand getBrand(Long id) {
        return getById(id);
    }

    @Override
    public boolean updateShowStatus(List<Long> ids, Integer showStatus) {
        LambdaUpdateWrapper<PmsBrand> lambda = new LambdaUpdateWrapper<>();
        lambda.in(PmsBrand::getId, ids);
        lambda.set(PmsBrand::getShowStatus, showStatus);
        return update(lambda);
    }

    @Override
    public boolean updateFactoryStatus(List<Long> ids, Integer factoryStatus) {
        LambdaUpdateWrapper<PmsBrand> lambda = new LambdaUpdateWrapper<>();
        lambda.in(PmsBrand::getId, ids);
        lambda.set(PmsBrand::getFactoryStatus, factoryStatus);
        return update(lambda);
    }
}
