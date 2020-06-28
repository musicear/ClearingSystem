package resoft.tips.web.action;

import java.util.Collection;

import javax.sql.DataSource;

import resoft.common.web.AbstractAction;
import resoft.tips.web.action.util.QueryUtil;

/**
 * <p/>
 * 获取征收机关
 * </p>
 * Author: chenjianping Date: 2007-7-15 Time: 15:19:58
 */
public class QueryTaxOrg extends AbstractAction {


    public String execute() throws Exception {
        QueryUtil queryUtil = new QueryUtil();
        queryUtil.setDataSource(dataSource);
        taxOrgList = queryUtil.findAll("SELECT TAXORGCODE,TAXORGNAME from TAXORGMNG");
        if(maptype==null||"".equals(maptype))
        	maptype="success";
        return maptype;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Collection getTaxOrgList() {
        return taxOrgList;
    }

    public String getMaptype() {
        return maptype;
    }

    public void setMaptype(String maptype) {
        this.maptype = maptype;
    }

    private Collection taxOrgList;
    private String maptype;
    private DataSource dataSource;

}
