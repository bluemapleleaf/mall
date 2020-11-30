package com.macro.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.cms.model.CmsPrefrenceAreaProductRelation;
import com.macro.mall.cms.model.CmsSubjectProductRelation;
import com.macro.mall.dto.PmsProductQueryParam;
import com.macro.mall.pms.dto.PmsProductParam;
import com.macro.mall.pms.dto.PmsProductResult;
import com.macro.mall.pms.model.PmsProduct;
import com.macro.mall.pms.service.PmsProductRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品管理Service
 * Created by macro on 2018/4/26.
 */
public interface PmsProductService extends PmsProductRepository {
    /**
     * 创建商品
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED)
    int create(PmsProductParam productParam);

    /**
     * 更新商品
     */
    @Transactional
    int update(Long id, PmsProductParam productParam);

    /**
     * 分页查询商品
     */
    Page<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum);

    /**
     * 批量修改审核状态
     * @param ids 产品id
     * @param verifyStatus 审核状态
     * @param detail 审核详情
     */
    @Transactional
    int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail);

    /**
     * 批量修改商品上架状态
     */
    int updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * 批量修改商品推荐状态
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量修改新品状态
     */
    int updateNewStatus(List<Long> ids, Integer newStatus);

    /**
     * 批量删除商品
     */
    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);

    /**
     * 根据商品名称或者货号模糊查询
     */
    List<PmsProduct> list(String keyword);

    /**
     * 关联专题
     * @param productId 商品标识
     * @param relations 关联专题
     * @return 关联数
     */
    int relateSubjects(Long productId, List<CmsSubjectProductRelation> relations);

    /**
     * 关联优选
     * @param productId 商品标识
     * @param relations 关联优选
     * @return 关联数
     */
    int relatePrefrenceAreas(Long productId, List<CmsPrefrenceAreaProductRelation> relations);
}
