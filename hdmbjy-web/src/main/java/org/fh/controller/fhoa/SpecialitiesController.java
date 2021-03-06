package org.fh.controller.fhoa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.fh.controller.base.BaseController;
import org.fh.entity.Page;
import org.fh.util.DateUtil;
import org.fh.util.Jurisdiction;
import org.fh.util.ObjectExcelView;
import org.fh.util.Tools;
import org.fh.entity.PageData;
import org.fh.service.fhoa.SpecialitiesService;
import org.fh.service.organization.OrganizationService;

/** 
 * 说明：开办专业
 * 作者：FH Admin QQ313596790
 * 时间：2019-09-27
 * 官网：www.fhadmin.org
 */
@Controller
@RequestMapping("/specialities")
public class SpecialitiesController extends BaseController {
	
	@Autowired
	private SpecialitiesService specialitiesService;
	//机构管理
	@Autowired
	private OrganizationService organizationService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("specialities:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SPECIALITIES_ID", this.get32UUID());	//主键
		pd.put("CREATION_DATE", DateUtil.date2Str(new Date()));	//创建时间
		List<PageData> listPd = organizationService.findByUserId(Jurisdiction.getUser().getUSER_ID());
		if (listPd.size()>0) {
			pd.put("ORGANIZATION_ID", listPd.get(0).getString("ORGANIZATION_ID"));	//机构ID
			pd.put("ORGANIZATION_NAME", listPd.get(0).getString("NAME"));	//机构名称
		}
		pd.put("BY_B", "");	//备用字段
		pd.put("BY_C", "");	//备用字段
		specialitiesService.save(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("specialities:del")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		specialitiesService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("specialities:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("CREATION_DATE", DateUtil.date2Str(new Date()));	//创建时间
		specialitiesService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("specialities:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		String ORGANIZATION_NAME = pd.getString("ORGANIZATION_NAME");						//机构名称检索条件
		if(Tools.notEmpty(ORGANIZATION_NAME))pd.put("ORGANIZATION_NAME", ORGANIZATION_NAME.trim());
		
		String TYPE = pd.getString("TYPE");
		if(Tools.notEmpty(TYPE))pd.put("TYPE", TYPE.trim());		//专业类型检索
		List<PageData> listPd = organizationService.findByUserId(Jurisdiction.getUser().getUSER_ID());
		if (listPd.size()>0) {
			pd.put("ORGANIZATION_ID", listPd.get(0).getString("ORGANIZATION_ID"));	//备用知道
			pd.put("ORGANIZATION_NAME", listPd.get(0).getString("NAME"));
		}
		
		page.setPd(pd);
		List<PageData>	varList = specialitiesService.list(page);	//列出Specialities列表
		
		if(varList != null && varList.size() > 0) {
			//同步数据
//			PageData opd = new PageData();
//			for (int i = 0; i < varList.size(); i++) {
//				opd.put("ORGANIZATION_ID", varList.get(i).getString("ORGANIZATION_ID"));
//				PageData org = organizationService.findById(opd);
//				varList.get(i).put("ORGANIZATION_NAME", org.getString("NAME"));
//				specialitiesService.edit(varList.get(i));
//			}
			
			PageData spd = new PageData();
			if(varList != null && varList.size() > 0) {
				for (int i = 0; i < varList.size(); i++) {
					spd.put("ORGANIZATION_ID", varList.get(i).getString("ORGANIZATION_ID"));
					PageData org = organizationService.findById(spd); //当前开办专业机构
					if(org != null) {
						varList.get(i).put("BY_B", org.getString("NAME"));
					}else {
						varList.get(i).put("BY_B", "找不到学校");
					}
				}
			}
		}
		
		map.put("TYPE", TYPE);
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@RequiresPermissions("specialities:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = specialitiesService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("specialities:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			specialitiesService.deleteAll(ArrayDATA_IDS);
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("专业名称");	//1
		titles.add("备注");	//2
		titles.add("创建时间");	//3
		titles.add("备用知道");	//4
		titles.add("备用字段");	//5
		titles.add("备用字段");	//6
		dataMap.put("titles", titles);
		List<PageData> varOList = specialitiesService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("NAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("BZ"));	    //2
			vpd.put("var3", varOList.get(i).getString("CREATION_DATE"));	    //3
			vpd.put("var4", varOList.get(i).getString("BY_A"));	    //4
			vpd.put("var5", varOList.get(i).getString("BY_B"));	    //5
			vpd.put("var6", varOList.get(i).getString("BY_C"));	    //6
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	/**
	 * 查询所有数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/listAll")
	@RequiresPermissions("specialities:list")
	@ResponseBody
	public Object listAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> listPd = organizationService.findByUserId(Jurisdiction.getUser().getUSER_ID());
		if (listPd.size()>0) {
			pd.put("ORGANIZATION_ID", listPd.get(0).getString("ORGANIZATION_ID"));	//备用知道
		}
		List<PageData>	varList = specialitiesService.listAll(pd);	//列出Post列表
		map.put("varList", varList);
		map.put("result", errInfo);
		return map;
	}
	
}
