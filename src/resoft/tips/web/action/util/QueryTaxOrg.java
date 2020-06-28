package resoft.tips.web.action.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.tips.domain.KeyValuePair;

public class QueryTaxOrg {
    public List findAll() {
    	
    	NodeMngList=new ArrayList();
		try {
			List rowSet = QueryUtil.queryRowSet("select TaxOrgCode,taxOrgName from TaxOrgMng");
			 for (Iterator itr = rowSet.iterator(); itr.hasNext();) {
				 Map row = (Map) itr.next();
					String nodeCode = (String)row.get("TAXORGCODE");
					String nodeName = (String)row.get("TAXORGNAME");	
					NodeMngList.add(new KeyValuePair(nodeCode, nodeName)) ;
			 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NodeMngList;
    }
    private List NodeMngList;
}
