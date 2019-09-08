package geektime.spring.data.declarativetransactiondemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FooServiceImpl implements FooService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

//    事务的本质
//• Spring 的声明式事务本质上是通过 AOP 来增强了了类的功能
//• Spring 的 AOP 本质上就是为类做了了⼀一个代理理
//• 看似在调⽤用⾃自⼰己写的类，实际⽤用的是增强后的代理理类
//• 问题的解法
//• 访问增强后的代理理类的⽅方法，⽽而⾮非直接访问⾃自身的⽅方法

    /**
     * 注入FooService
     */
    @Autowired
    FooService fooService;

    @Override
    @Transactional
    public void insertRecord() {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('AAA')");
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public void insertThenRollback() throws RollbackException {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('BBB')");
        throw new RollbackException();
    }

    @Override
    public void invokeInsertThenRollback() throws RollbackException {
        // 调用了带事物的方法insertThenRollback，但是该方法没有带事物，因此对于该方法来说，
        // @Transactional(rollbackFor = RollbackException.class)事物不生效
//        insertThenRollback();

//        使用声明式事物来解决上述问题
        fooService.insertThenRollback();
    }
}
