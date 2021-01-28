package com.macro.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.domain.PmsBrandParam;
import com.macro.mall.pms.model.PmsBrand;
import com.macro.mall.pms.service.PmsBrandRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品品牌Service
 * Created by macro on 2018/4/26.
 */
public interface PmsBrandService extends PmsBrandRepository {
    /**
     * 获取所有品牌
     */
    List<PmsBrand> listAllBrand();

    /**
     * 创建品牌
     */
    boolean createBrand(PmsBrandParam pmsBrandParam);

    /**
     * 修改品牌
     */
    @Transactional
    boolean updateBrand(Long id, PmsBrandParam pmsBrandParam);

    /**
     * 删除品牌
     */
    boolean deleteBrand(Long id);

    /**
     * 批量删除品牌
     */
    boolean deleteBrand(List<Long> ids);

    /**
     * 分页查询品牌
     */
    Page<PmsBrand> listBrand(String keyword, int pageNum, int pageSize);

    /**
     * 获取品牌
     */
    PmsBrand getBrand(Long id);

    /**
     * 修改显示状态
     */
    boolean updateShowStatus(List<Long> ids, Integer showStatus);

    /**
     * 修改厂家制造商状态
     */
    boolean updateFactoryStatus(List<Long> ids, Integer factoryStatus);
}
