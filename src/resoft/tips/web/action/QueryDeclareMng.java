package resoft.tips.web.action;

import java.util.Collection;

import javax.sql.DataSource;

import resoft.common.web.AbstractAction;
import resoft.tips.web.action.util.QueryUtil;

public class QueryDeclareMng extends AbstractAction {


    public String execute() throws Exception {
        QueryUtil queryUtil = new QueryUtil();
        queryUtil.setDataSource(dataSource);
        taxOrgList = queryUtil.findAll("select TaxOrgCode,taxOrgName from TaxOrgMng");
        return SUCCESS;
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
