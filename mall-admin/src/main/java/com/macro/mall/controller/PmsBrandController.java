package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.PmsBrandParam;
import com.macro.mall.model.PmsBrand;
import com.macro.mall.service.PmsBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品品牌管理Controller
 * Created by macro on 2018/4/26.
 */
@Controller
@Api(tags = "PmsBrandController")
@Tag(name = "PmsBrandController", description = "商品品牌管理")
@RequestMapping("/brand")
public class PmsBrandController {
    @Autowired
    private PmsBrandService brandService;

    @ApiOperation(value = "获取全部品牌列表")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsBrand>> getList() {
        return CommonResult.success(brandService.listAllBrand());
    }

    /**
     * Creates a new brand entry.
     * @example
     * const result = createBrand({ name: "BrandName", category: "Category" });
     * console.log(result); // Expected output: { success: true, data: 1 }
     * @param {PmsBrandParam} pmsBrand - An object containing the brand details to be added.
     * @return {CommonResult} - Returns a CommonResult indicating success or failure of the operation.
     * @description
     *   - Validates the incoming request body using the @Validated annotation.
     *   - Utilizes brandService to interact with the data persistence layer.
     *   - Relies on the brandService's createBrand method to return an integer count indicating the result.
     *   - Constructs a response based on the success or failure of the brand creation.
     */
    @ApiOperation(value = "添加品牌")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@Validated @RequestBody PmsBrandParam pmsBrand) {
        CommonResult commonResult;
        int count = brandService.createBrand(pmsBrand);
        if (count == 1) {
            commonResult = CommonResult.success(count);
        } else {
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }

    /**
     * Updates the brand information for a given brand ID.
     * @param {Long} id - The ID of the brand to update.
     * @param {PmsBrandParam} pmsBrandParam - The parameter object containing updated brand information.
     * @return {CommonResult} - A result object indicating success or failure of the update operation.
     * @example
     * CommonResult result = PmsBrandController.update(1L, pmsBrandParam);
     * System.out.println(result); // Expected output: CommonResult instance indicating update success or failure
     * @description
     *   - The method responds to POST requests sent to the "/update/{id}" endpoint.
     *   - Utilizes `brandService` to apply updates to the brand data.
     *   - Returns a success result if one record is updated, otherwise returns a failed result.
     */
    @ApiOperation(value = "更新品牌")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable("id") Long id,
                               @Validated @RequestBody PmsBrandParam pmsBrandParam) {
        CommonResult commonResult;
        int count = brandService.updateBrand(id, pmsBrandParam);
        if (count == 1) {
            commonResult = CommonResult.success(count);
        } else {
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }

    @ApiOperation(value = "删除品牌")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult delete(@PathVariable("id") Long id) {
        int count = brandService.deleteBrand(id);
        if (count == 1) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "根据品牌名称分页获取品牌列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<PmsBrand>> getList(@RequestParam(value = "keyword", required = false) String keyword,
                                                      @RequestParam(value = "showStatus",required = false) Integer showStatus,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<PmsBrand> brandList = brandService.listBrand(keyword,showStatus,pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(brandList));
    }

    @ApiOperation(value = "根据编号查询品牌信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsBrand> getItem(@PathVariable("id") Long id) {
        return CommonResult.success(brandService.getBrand(id));
    }

    @ApiOperation(value = "批量删除品牌")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteBatch(@RequestParam("ids") List<Long> ids) {
        int count = brandService.deleteBrand(ids);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * Updates the visibility status of brands in bulk.
     * @example
     * let result = updateShowStatus([1, 2, 3], 1);
     * console.log(result); // Expected output: { success: true, data: 3 }
     * @param {Array<Long>} ids - List of brand IDs to update visibility status.
     * @param {Integer} showStatus - Visibility status to be set for the given IDs.
     * @return {CommonResult} - Returns a success message with the count of updated records or a failure message.
     * @description
     *   - The method handles both successful and failed update scenarios.
     *   - Updates are performed through a service rather than directly accessing the database.
     *   - Visibility status is represented as an integer where specific values denote different states.
     *   - Ensures the atomic update of the visibility status for all specified IDs.
     */
    @ApiOperation(value = "批量更新显示状态")
    @RequestMapping(value = "/update/showStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateShowStatus(@RequestParam("ids") List<Long> ids,
                                   @RequestParam("showStatus") Integer showStatus) {
        int count = brandService.updateShowStatus(ids, showStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * Updates the factory status for a batch of manufacturers.
     * @example
     * const result = updateFactoryStatus([1, 2, 3], 1);
     * console.log(result); // Expected output: Number of updated records, or an error message
     * @param {Array<number>} ids - List of manufacturer IDs to be updated.
     * @param {number} factoryStatus - New status to be set for the manufacturers.
     * @return {Object} - Response object indicating the success or failure of the operation.
     * @description
     *   - This function handles a request to update the status for multiple manufacturers.
     *   - Internally uses a service to perform the update in the database.
     *   - Returns a success message with a count of updated records if successful.
     *   - Returns a failure message if no records were updated.
     */
    @ApiOperation(value = "批量更新厂家制造商状态")
    @RequestMapping(value = "/update/factoryStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateFactoryStatus(@RequestParam("ids") List<Long> ids,
                                      @RequestParam("factoryStatus") Integer factoryStatus) {
        int count = brandService.updateFactoryStatus(ids, factoryStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }
}
