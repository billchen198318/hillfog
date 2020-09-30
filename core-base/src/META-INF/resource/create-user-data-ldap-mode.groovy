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
import org.qifu.core.model.*;
import org.qifu.base.model.*;
import org.qifu.util.*;
import org.qifu.core.util.*;
import org.qifu.core.entity.*;
import org.qifu.core.mapper.*;
import org.qifu.hillfog.entity.*;
import org.qifu.hillfog.mapper.*;

try {
	TbAccountMapper accountMapper = AppContext.context.getBean(TbAccountMapper.class);
	TbUserRoleMapper userRoleMapper = AppContext.context.getBean(TbUserRoleMapper.class);
	HfEmployeeMapper employeeMapper = AppContext.context.getBean(HfEmployeeMapper.class);
	
	Map<String, Object> accParamMap = new HashMap<String, Object>();
	accParamMap.put("account", user.username);
	if (accountMapper.count(accParamMap) > 0) {
		return null;
	}
	 
	TbAccount accObj = new TbAccount();
	accObj.setOid(SimpleUtils.getUUIDStr());
	accObj.setAccount(user.username);
	accObj.setPassword(user.password);
	accObj.setOnJob(YesNo.YES);
	accObj.setCuserid(Constants.SYSTEM_BACKGROUND_USER);
	accObj.setCdate(new Date());
	accountMapper.insert(accObj);
	
	TbUserRole userRoleObj = new TbUserRole();
	userRoleObj.setOid(SimpleUtils.getUUIDStr());
	userRoleObj.setRole("COMMON01"); // default role for normal user
	userRoleObj.setAccount(accObj.getAccount());
	userRoleObj.setDescription(accObj.getAccount() + " 's role, create by LDAP login!");
	userRoleObj.setCuserid(Constants.SYSTEM_BACKGROUND_USER);
	userRoleObj.setCdate(new Date());
	userRoleMapper.insert(userRoleObj);
	
	user.setOid(accObj.getOid());
	
	HfEmployee employee = new HfEmployee();
	employee.setOid(SimpleUtils.getUUIDStr());
	employee.setAccount(accObj.getAccount());
	employee.setEmpId(SimpleUtils.createRandomString(10));
	employee.setName(accObj.getAccount());
	employee.setDescription(accObj.getAccount() + " , create by LDAP login!");
	employee.setCuserid(Constants.SYSTEM_BACKGROUND_USER);
	employee.setCdate(new Date());
	employeeMapper.insert(employee);
	
	System.out.println("create account data by LDAP login : " + accObj.getAccount());
	
} catch (Exception e) {
	throw e;
}
