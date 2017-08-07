package com.lianyitech.modules.peri.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.Encodes;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.peri.entity.Order;
import com.lianyitech.modules.peri.entity.OrderDetail;
import com.lianyitech.modules.peri.service.OrderDetailService;
import com.lianyitech.modules.peri.service.OrderService;
import com.lianyitech.modules.sys.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by zcx on 2017/3/10.
 * OrderController
 */
@Api(value="期刊登记", description ="有关于期刊订购登记与过刊入库修改删除的操作")
@RestController
@RequestMapping(value = "/api/peri/orders")
public class OrderController extends ApiController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;


    /**
     * 订单列表
     * @param id 订单对象
     * @return list
     */
    //@RequestMapping(value = "list", method = RequestMethod.GET)
    @GetMapping("/{id}")
    @ApiOperation(value="订单列表", notes = "根据数目id查询订单信息", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> find (
            @ApiParam(required =true, name ="订单信息", value="订单信息json数据")
            @PathVariable String id,
            Order order ,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            order.setPeriDirectoryId(id);
            Page<Order> page = orderService.findPage(new Page<>(request, response), order);
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 订购期数
     * @param order 订单对象
     * @return 订购期数
     */
    //@RequestMapping(value = "order/peri", method = RequestMethod.GET)
    @GetMapping("/periods")
    @ApiOperation(value="订购期数", notes = "根据用户输入的日期范围以及订购书目的出版频率查询订购期数", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> orderPeri(
            @ApiParam(required =true, name ="订购期数", value="订购期数")
                    Order order
    ) {
        try {
            int orderPeri = orderService.orderPeri(order);
            if (orderPeri<=0){
                return new ResponseEntity<>(fail("日期间隔过小，请调整日期范围"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(success(orderPeri), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 订购期数
     * @param order 书目ID
     * @return 订购期数
     */
    //@RequestMapping(value = "order/peri", method = RequestMethod.GET)
    @GetMapping("/oederperi")
    @ApiOperation(value="订购期数", notes = "根据期刊目录id查询订购期数", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> orderPeriNum(
            @ApiParam(required =true, name ="订购期数", value="订购期数")
                    Order order
    ) {
        try {
                order = orderService.orderPeriNum(order);
            return new ResponseEntity<>(success(order), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 订购（登记）
     * @param order 订单对象
     * @return code
     */
    //@RequestMapping(value = "save/order", method = RequestMethod.POST)
    @PostMapping("")
    @ApiOperation(value="订购（登记）/新增", notes = "根据用户输入的信息生成一条订单并根据订购期数生成相关的订单明细", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> saveOrder(
            @ApiParam(required =true, name ="订单对象", value="订单对象的相关参数")
            Order order
    ) {
        try {
            order.setOrgId(UserUtils.getOrgId());
            if (orderService.get(order) != null){
                return new ResponseEntity<>(fail("已存在重复的订购信息"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            orderService.saveOrder(order);
            return new ResponseEntity<>(success("添加成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("添加失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 修改订单
     * @param order 订单对象
     * @return code
     */
    //@RequestMapping(value = "update/order", method = RequestMethod.POST)
    //@PostMapping("/edt")
    @PutMapping("")
    @ApiOperation(value="修改订单", notes = "根据用户输入的信息修改订单并删除订单明细中的相关期刊再根据该订单的期数生成相关订单明细", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> updateOrder(
            @ApiParam(required =true, name ="订单对象", value="订单对象的相关参数")
            @RequestBody
            Order order
    ) {
        try {
            order.setOrgId(UserUtils.getOrgId());
            if (orderService.get(order) != null){
                return new ResponseEntity<>(fail("已存在重复的订购信息"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getId());
            List<OrderDetail> list = orderDetailService.checkDelete(orderDetail);
            if (list.size()!=0){
                return new ResponseEntity<>(fail("该期刊已经记到验收，修改失败"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            orderService.updateOrder(order);
            return new ResponseEntity<>(success("修改成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("修改失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 删除订单
     * @param ids 订单ids
     * @return code
     */
    //@RequestMapping(value = "", method = RequestMethod.DELETE)
    @DeleteMapping("")
    @ApiOperation(value="删除订单", notes = "根据订单id删除订单并删除订单明细中的相关期刊，如订单已存在记到期刊则不能删除", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> deleteOrder(
            @ApiParam(required =true, name ="订单对象", value="订单对象的相关参数")
            Order order,
            String ids
    ) {
        try {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getId());
            List<OrderDetail> list = orderDetailService.checkDelete(orderDetail);
            if (list.size()!=0){
                return new ResponseEntity<>(fail("该期刊已经记到验收，删除失败"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            orderService.deleteOrder(ids);
            return new ResponseEntity<>(success("删除成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("删除失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查询机构下所有的订单（点击期刊记到）
     * @param order 订单对象
     * @param request request
     * @param response response
     * @return page
     */
    //@RequestMapping(value = "find/order", method = RequestMethod.GET)
    @GetMapping("")
    @ApiOperation(value="查询机构下所有的订单（点击期刊记到）", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> findOrder (
            @ApiParam(required =true, name ="订单信息", value="订单信息json数据")
                    Order order ,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            if(StringUtils.isNotBlank(order.getTitle())){
                order.setTitle(URLDecoder.decode(order.getTitle(), "utf-8"));
                order.setPublishingName(URLDecoder.decode(order.getPublishingName(), "utf-8"));
            }
            Page<Order> page = orderService.findOrder(new Page<>(request, response), order);
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 记到
     * @param orderDetail 订单明细
     * @return code
     */
    //@RequestMapping(value = "remember", method = RequestMethod.POST)
    @PutMapping("/detail")
    public ResponseEntity<ResponseData> remember(@RequestBody OrderDetail orderDetail) {
        try {
            if(StringUtils.isNotBlank(orderDetail.getPeriNum())){
                orderDetail.setPeriNum(URLDecoder.decode(orderDetail.getPeriNum(), "utf-8"));
            }
            orderDetailService.save(orderDetail);
            return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 合并记到
     * @param orderDetail orderDetail
     * @return code
     */
    @PostMapping("/merge")
    public ResponseEntity<ResponseData> mergeRemember(OrderDetail orderDetail) {
        orderDetailService.deleteDetail(orderDetail.getIds());
        try {
            if(StringUtils.isNotBlank(orderDetail.getPeriNum())){
                orderDetail.setPeriNum(URLDecoder.decode(orderDetail.getPeriNum(), "utf-8"));
            }
            orderService.addRemember(orderDetail);
            return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查询已记到期刊书目（点击期刊合订菜单）
     * @param order 订单实体
     * @param request request
     * @param response response
     * @return page
     */
    //@RequestMapping(value = "find/arrive/directory", method = RequestMethod.GET)
    @GetMapping("/directors")
    @ApiOperation(value="查询已记到期刊书目（点击期刊合订菜单）", notes = "根据订单id查询已记到的书刊记录", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> findArriveDirectory (
            @ApiParam(required =true, name ="订单实体", value="订单的相关参数")
                    Order order,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            if(StringUtils.isNotEmpty(order.getTitle())){
                order.setTitle(Encodes.urlDecode(order.getTitle()));
            }
            if(StringUtils.isNotEmpty(order.getPublishingName())){
                order.setPublishingName(Encodes.urlDecode(order.getPublishingName()));
            }
            Page<Order> page = orderService.findArriveDirectory(new Page<>(request,response),order);
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 未合订期刊
     * @param orderDetail 合订期刊实体
     * @param request request
     * @param response response
     * @return page
     */
    //@RequestMapping(value = "find/arrive/order/detail", method = RequestMethod.GET)
    @GetMapping("/{id}/detail")
    @ApiOperation(value="未合订期刊", notes = "根据书目id查询该书目下所有已记到的书刊", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> findArriveOrderDetail (
            @ApiParam(required =true, name ="合订期刊实体", value="合订期刊的相关参数") OrderDetail orderDetail,
            @PathVariable String id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            orderDetail.setOrderId(id);
            Page<OrderDetail> page = orderDetailService.findArriveOrderDetail(new Page<>(request,response),orderDetail);
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 增刊记到
     * @param orderDetail orderDetail
     * @return code
     */
    @PostMapping("/add/detail")
    @ApiOperation(value="增刊记到", notes = "增刊记到", response = ResponseEntity.class)
    public  ResponseEntity<ResponseData> addRemember (OrderDetail orderDetail) {
        try {
            orderService.addRemember(orderDetail);
            return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
        }catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
