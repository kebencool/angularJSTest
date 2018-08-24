package com.example.demo.mapper;


import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;

//@Mapper
public interface UserMappler {

    String TABLE_NAEM = " user ";
    String INSERT_FIELDS = " account, password, salt, head_url ,name ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAEM,"(",INSERT_FIELDS,") values (#{account},#{password},#{salt},#{headUrl},#{name})"})
    public void insertUser(User user);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAEM,"where id=#{id}"})
    public User seletById(int id);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAEM,"where account=#{account}"})
    public User seletByAccount(@Param("account") String account);

    @Delete({"delete from",TABLE_NAEM,"where id=#{id}"})
    public void deleteById(int id);

}
