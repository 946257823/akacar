package cn.itaka.controller.manager;

import cn.itaka.service.ICustomerWalletService;
import cn.itaka.pojo.domain.CustomerWallet;
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

@Tag(name = "乘客的钱包",description = "乘客的钱包")
@RestController
@RequestMapping("/manager/customerWallet")
public class CustomerWalletController{

    @Autowired
    public ICustomerWalletService customerWalletService;

    @Operation( summary= "保存CustomerWallet",description = "基础对象保存接口")
    @Parameter(name = "customerWallet",description = "保存的对象",required = true)
    @PostMapping
    public R<Void> save(@RequestBody @Valid CustomerWallet customerWallet){
        return R.success(customerWalletService.save(customerWallet));
    }

    @Operation( summary= "修改CustomerWallet",description = "基础对象修改接口")
    @Parameter(name = "customerWallet",description = "修改的对象",required = true)
    @PutMapping
    public R<Void> update(@RequestBody  @Valid CustomerWallet customerWallet){
        return R.success(customerWalletService.updateById(customerWallet));
    }

    //删除对象
    @Operation( summary= "删除CustomerWallet",description = "基础对象删除接口，采用状态删除")
    @Parameter(name = "id",description = "删除的对象ID",required = true)
    @DeleteMapping(value="/{id}")
    public R<Void> delete(@PathVariable("id") Long id){
        return R.success(customerWalletService.removeById(id));
    }

    //获取对象
    @Operation( summary= "获取CustomerWallet",description = "基础对象获取接口")
    @Parameter(name = "id",description = "查询的对象ID",required = true)
    @GetMapping(value = "/{id}")
    public R<CustomerWallet> get(@PathVariable("id")Long id){
        return R.success(customerWalletService.getById(id));
    }

    //获取列表:PageQueryWrapper作为通用的查询对象
    @Operation( summary= "查询CustomerWallet列表",description = "基础对象列表查询，不带分页")
    @Parameter(name = "query",description = "查询条件",required = true)
    @PostMapping(value = "/list")
    public R<List<CustomerWallet>> list(@RequestBody PageQueryWrapper<CustomerWallet> query){
        QueryWrapper<CustomerWallet> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        return R.success(customerWalletService.list(wrapper));
    }

    //分页查询
    @Operation( summary= "查询CustomerWallet分页列表",description = "基础对象列表查询，带分页")
    @Parameter(name = "query",description = "查询条件，需要指定分页条件",required = true)
    @PostMapping(value = "/pagelist")
    public R<PageResult<CustomerWallet>> page(@RequestBody PageQueryWrapper<CustomerWallet> query){
        //分页查询
        Page<CustomerWallet> page = new Page<CustomerWallet>(query.getPage(),query.getRows());
        QueryWrapper<CustomerWallet> wrapper = new QueryWrapper<>();
        //实体类作为查询条件
        wrapper.setEntity(query.getQuery());
        //分页查询
        page = customerWalletService.page(page,wrapper);
        //返回结果
        return R.success(new PageResult<CustomerWallet>(page.getTotal(),page.getRecords()));
    }

}
