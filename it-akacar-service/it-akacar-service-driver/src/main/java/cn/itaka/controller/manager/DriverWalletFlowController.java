package cn.itaka.controller.manager;

import cn.itaka.service.IDriverWalletFlowService;
import cn.itaka.pojo.domain.DriverWalletFlow;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.itaka.pojo.query.PageQueryWrapper;
import cn.itaka.result.R;
import cn.itaka.result.PageResult;

import java.util.List;

@Tag(name = "钱包流水",description = "钱包流水")
@RestController
@RequestMapping("/manager/driverWalletFlow")
public class DriverWalletFlowController{

    @Autowired
    public IDriverWalletFlowService driverWalletFlowService;

    @Operation( summary= "保存DriverWalletFlow",description = "基础对象保存接口")
    @Parameter(name = "driverWalletFlow",description = "保存的对象",required = true)
    @PostMapping
    public R<Void> save(@RequestBody @Valid DriverWalletFlow driverWalletFlow){
        return R.success(driverWalletFlowService.save(driverWalletFlow));
    }

    @Operation( summary= "修改DriverWalletFlow",description = "基础对象修改接口")
    @Parameter(name = "driverWalletFlow",description = "修改的对象",required = true)
    @PutMapping
    public R<Void> update(@RequestBody  @Valid DriverWalletFlow driverWalletFlow){
        return R.success(driverWalletFlowService.updateById(driverWalletFlow));
    }

    //删除对象
    @Operation( summary= "删除DriverWalletFlow",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Void> delete(@PathVariable("id") Long id){
        return R.success(driverWalletFlowService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取DriverWalletFlow",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<DriverWalletFlow> get(@PathVariable("id")Long id){
        return R.success(driverWalletFlowService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询DriverWalletFlow列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<DriverWalletFlow>> list(@RequestBody PageQueryWrapper<DriverWalletFlow> query){
        QueryWrapper<DriverWalletFlow> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(driverWalletFlowService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询DriverWalletFlow分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<DriverWalletFlow>> page(@RequestBody PageQueryWrapper<DriverWalletFlow> query){
        //分页查询
        Page<DriverWalletFlow> page = new Page<DriverWalletFlow>(query.getPage(),query.getRows());
        QueryWrapper<DriverWalletFlow> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = driverWalletFlowService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<DriverWalletFlow>(page.getTotal(),page.getRecords()));
    }

}
