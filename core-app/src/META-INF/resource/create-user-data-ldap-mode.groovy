import java.io.*;
import java.lang.*;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import org.qifu.base.*;
import org.qifu.util.*;
import org.qifu.core.util.*;
import org.qifu.hillfog.entity.*;
import org.qifu.hillfog.logic.*;

try {
	UserUtils.setUserInfoForUserLocalUtilsBackgroundMode();
	IEmployeeLogicService employeeLogicService = (IEmployeeLogicService) AppContext.context.getBean(IEmployeeLogicService.class);
	List<String> orgList = new ArrayList<String>();
	orgList.add("SD"); // no rule for current, modify it.
	HfEmployee employee = new HfEmployee();
	employee.setAccount( user.username );
	employee.setEmpId( SimpleUtils.createRandomString(5).toUpperCase() ); // no rule for current, modify it.
	employee.setName( user.username );
	employee.setUploadOid("");
	employee.setJobTitle( "normal" );
	employee.setDescription( "create account data by LDAP login : " + user.username );
	employeeLogicService.create(employee, user.password, orgList);
	System.out.println("create account data by LDAP login : " + user.username);
} catch (Exception e) {
	throw e;
} finally {
	UserUtils.removeForUserLocalUtils();
}
