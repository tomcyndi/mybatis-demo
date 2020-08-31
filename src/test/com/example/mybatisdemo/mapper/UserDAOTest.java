package com.example.mybatisdemo.mapper;

import com.example.mybatisdemo.AddressDTO;
import com.example.mybatisdemo.BaseTest;
import com.example.mybatisdemo.entity.User;
import com.example.mybatisdemo.entity.UserExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class UserDAOTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(UserDAOTest.class);

    @Autowired
    UserDAO userDAO;
    @Autowired
    UserMapper userMapper;

    /**
     * 新增数据并获取该数据的主键id
     * 注意该insert方法的返回值仅表示成功与否（0-失败 1-成功），而不是指该记录的主键id（要通过user.getId()来获取）
     */
    @Test
    void testAdd(){
        User user = new User();
        user.setAge(31);
        user.setAddress("深圳");
        user.setName("wittkang");
        int rows = userDAO.add(user);
        //注意add方法返回值仅表示成功与否，主键id要通过user.getId()来获取
        logger.info("------rows={},id={}",rows,user.getId());
    }

    @Test
    void testInsertSelective(){
        for(int i=0 ; i<20 ; i++){
            User user = new User();
            user.setAge(i);
            user.setAddress("深圳");
            user.setName("wittkang"+i);
            userMapper.insertSelective(user);
        }
    }

    @Test
    void testUpdateByExample(){
        //创建查询条件对象
        UserExample example = new UserExample();
        example.createCriteria().andAgeBetween(10,13);
        //要更改后的目标数据
        User user = new User();
        user.setAddress("广州");
        int rows = userMapper.updateByExampleSelective(user,example);
        logger.info("{} rows updated",rows);
    }

    @Test
    void testSelectByExample(){
        //创建查询条件对象
        UserExample example = new UserExample();
        example.createCriteria().andAddressLike("%深圳");

        //设置排序字段
        example.setOrderByClause(" age desc ");

        //执行查询
        List<User> list = userMapper.selectByExample(example);
        logger.info("user size ={}",list.size());
    }

    @Test
    void testSelect(){
        //执行查询
        List<User> list = userDAO.getAll();
        logger.info("user size ={}",list.size());
    }

    @Test
    void testDelete(){
        List<User> list = userDAO.getAll();
        logger.info("before delete------user size={}",list.size());
        List<Integer> ids = new ArrayList<>();
        list.forEach(item->userDAO.delete(item.getId()));
        list = userDAO.getAll();
        logger.info("after delete------user size={}",list.size());
    }

    @Test
    void testCountByAddress(){
        List<AddressDTO> list = userDAO.countByAddress();
        for(AddressDTO dto:list){
            logger.info("addresds={},count={}",dto.getAddress(),dto.getCount());
        }
    }

    /**
     * mybatis分页
     */
    @Test
    void testPage(){

        int pageNo = 1 ;
        int pageSize = 11;

        //在要分页的查询之前加上PageHelper即可，注意只对其后第一个查询有效
        PageHelper.startPage(pageNo,pageSize);
        //这里看着是查询全部数据，其实真正执行时，会被PageHelper拦截，最终其实是分页查询
        List<User> list = userDAO.getAll();
        PageInfo result=new PageInfo(list);

        logger.info("listSize={},resultSize={},totalSize={}",list.size(),result.getSize(),result.getTotal());
        logger.info("startRow={},nextPage={},pageNum={},pages={}",result.getStartRow(),result.getNextPage(),result.getPageNum(),result.getPages());
    }

    /**
     * 批量插入
     */
    @Test
    void testBatchInsert(){
        List<User> list = new ArrayList<>();
        for(int i=0 ; i<10 ; i++){
            User user = new User();
            user.setName("康思怡"+i);
            user.setAge(i);
            user.setAddress("香港");
            list.add(user);
        }

        int rows = userDAO.batchInsert(list);
        logger.info("------{} rows inserted",rows);
    }

    /**
     * 批量更新
     */
    @Test
    void testBatchUpdate(){
        List<User> list = userMapper.selectByExample(null);
        list.forEach(item->item.setName("张学友"));
        int rows = userDAO.batchUpdate(list);
        logger.info("------{} rows updated",rows);
    }

}
