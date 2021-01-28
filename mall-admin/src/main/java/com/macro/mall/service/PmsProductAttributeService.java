package com.macro.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.domain.PmsProductAttributeParam;
import com.macro.mall.pms.dto.ProductAttrInfo;
import com.macro.mall.pms.model.PmsProductAttribute;
import com.macro.mall.pms.service.PmsProductAttributeRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品属性Service
 * Created by macro on 2018/4/26.
 */
public interface PmsProductAttributeService extends PmsProductAttributeRepository {
    /**
     * 根据分类分页获取商品属性
     * @param cid 分类id
     * @param type 0->属性；2->参数
     */
    Page<PmsProductAttribute> getList(Long cid, Integer type, Integer pageSize, Integer pageNum);

    /**
     * 添加商品属性
     */
    @Transactional
    boolean create(PmsProductAttributeParam pmsProductAttributeParam);

    /**
     * 修改商品属性
     */
    boolean update(Long id, PmsProductAttributeParam productAttributeParam);

    /**
     * 获取单个商品属性信息
     */
    PmsProductAttribute getItem(Long id);

    @Transactional
    int delete(List<Long> ids);

    List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId);
}
